package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.config.RAppSettings;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ISource;
import com.github.wglanzer.redmine.model.impl.server.PollingServer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Manages all server instances
 *
 * @author w.glanzer, 16.11.2016.
 */
public class RServerManager
{

  private final List<IServer> availableServers = new ArrayList<>();
  private final IRLoggingFacade loggingFacade;
  private AtomicBoolean isRunning = new AtomicBoolean(false);

  public RServerManager(IRLoggingFacade pLoggingFacade)
  {
    loggingFacade = pLoggingFacade;
    reloadConfiguration();
  }

  /**
   * Reloads the configuration from AppSettings
   */
  public void reloadConfiguration()
  {
    synchronized(availableServers)
    {
      availableServers.stream()
          .filter(IServer::isValid)
          .forEach(IServer::disconnect);
      availableServers.clear();
      availableServers.addAll(RAppSettings.getSettings().getSources().stream()
          .map(this::_toServer)
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
      availableServers.forEach(IServer::connect);
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
          .forEach(IServer::disconnect);
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
   * Converts a source to a server
   *
   * @param pSource  source which should be converted
   * @return the server instance
   */
  private IServer _toServer(ISource pSource)
  {
    return new PollingServer(pSource, loggingFacade);
  }

}
