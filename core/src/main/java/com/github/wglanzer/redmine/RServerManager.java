package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.config.ISettings;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ISource;
import com.github.wglanzer.redmine.model.impl.server.PollingServer;
import com.github.wglanzer.redmine.util.WeakListenerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Manages all server instances
 *
 * @author w.glanzer, 16.11.2016.
 */
public class RServerManager
{

  private final WeakListenerList<IRServerManagerListener> serverManagerListeners = new WeakListenerList<>();
  private final List<IServer> availableServers = new ArrayList<>();
  private final IRLoggingFacade loggingFacade;
  private final IRTaskCreator taskCreator;
  private AtomicBoolean isRunning = new AtomicBoolean(false);

  public RServerManager(IRLoggingFacade pLoggingFacade, IRTaskCreator pTaskCreator, ISettings pSettings)
  {
    loggingFacade = pLoggingFacade;
    taskCreator = pTaskCreator;
    reloadConfiguration(pSettings);
  }

  /**
   * Reloads the configuration from AppSettings
   *
   * @param pNewSettings Newly created settings
   */
  public void reloadConfiguration(ISettings pNewSettings)
  {
    synchronized(availableServers)
    {
      availableServers.stream()
          .filter(IServer::isValid)
          .forEach((pServer) -> {
            serverManagerListeners.forEach(pListener -> pListener.serverWillBeDisconnected(pServer));
            pServer.disconnect();
            serverManagerListeners.forEach(pListener -> pListener.serverDisconnected(pServer));
          });
      availableServers.clear();
      availableServers.addAll(pNewSettings.getSources().stream()
          .peek(pSource -> serverManagerListeners.forEach(pListener -> pListener.serverWillBeCreated(pSource)))
          .map(this::_toServer)
          .filter(Objects::nonNull)
          .peek(pServer -> serverManagerListeners.forEach(pListener -> pListener.serverCreated(pServer)))
          .collect(Collectors.toList()));

      if(isRunning.get())
        connect();
    }
  }

  /**
   * Connects all servers to its destination-URL
   */
  public void connect()
  {
    synchronized(availableServers)
    {
      availableServers.forEach(IServer::connectAsync);
      isRunning.set(true);
    }
  }

  /**
   * Disconnects all servers
   */
  public void shutdown()
  {
    synchronized(availableServers)
    {
      availableServers.stream()
          .filter(IServer::isValid)
          .forEach((pServer) -> {
            serverManagerListeners.forEach(pListener -> pListener.serverWillBeDisconnected(pServer));
            pServer.disconnect();
            serverManagerListeners.forEach(pListener -> pListener.serverDisconnected(pServer));
          });
      availableServers.clear();
      isRunning.set(false);
    }
  }

  /**
   * Returns all currently connected servers
   *
   * @return all servers, unmodifiable
   */
  @NotNull
  public List<IServer> getAvailableServers()
  {
    synchronized(availableServers)
    {
      return Collections.unmodifiableList(availableServers);
    }
  }

  /**
   * Adds a new ServerManagerListener to this ServerManager
   *
   * @param pListener Listener that should be added
   */
  public void addWeakServerManagerListener(IRServerManagerListener pListener)
  {
    synchronized(serverManagerListeners)
    {
      serverManagerListeners.add(pListener);
    }
  }

  /**
   * Removes a specific listener
   *
   * @param pListener Listener that should be removed
   */
  public void removeServerManagerListener(IRServerManagerListener pListener)
  {
    synchronized(serverManagerListeners)
    {
      serverManagerListeners.remove(pListener);
    }
  }

  /**
   * Converts a source to a server
   *
   * @param pSource source which should be converted
   * @return the server instance, <tt>null</tt> if it can't be created
   */
  @Nullable
  private IServer _toServer(ISource pSource)
  {
    try
    {
      return new PollingServer(loggingFacade, taskCreator, pSource);
    }
    catch(Exception e)
    {
      loggingFacade.error(e);
      return null;
    }
  }

}
