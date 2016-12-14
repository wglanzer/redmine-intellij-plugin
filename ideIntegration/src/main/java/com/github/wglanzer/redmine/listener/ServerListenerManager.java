package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.IRServerManagerListener;
import com.github.wglanzer.redmine.RServerManager;
import com.github.wglanzer.redmine.model.IServer;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages all "global" listeners for redmine
 * What it does:
 * 1. add an RServerListener to all IServers
 * 2. persist these listeners, because they are weak
 *
 * @author w.glanzer, 14.12.2016.
 */
public class ServerListenerManager implements IRServerManagerListener
{
  private static ServerListenerManager _INSTANCE;

  public static ServerListenerManager getInstance()
  {
    if(_INSTANCE == null)
      _INSTANCE = new ServerListenerManager();
    return _INSTANCE;
  }

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private final Map<String, RServerListener> serverManagerListeners = new HashMap<>();
  private IChangeNotifier notifier;
  private boolean inited = false;

  public void init(RServerManager pServerManager, IChangeNotifier pNotifier)
  {
    if(inited)
      shutdown();

    notifier = pNotifier;

    // Add all now available servers to our map
    pServerManager.getAvailableServers().forEach(this::serverCreated);
    pServerManager.addWeakServerManagerListener(this);
  }

  public void shutdown()
  {
    synchronized(serverManagerListeners)
    {
      serverManagerListeners.clear();
    }

    inited = false;
    notifier = null;
  }

  @Override
  public void serverCreated(IServer pServer)
  {
    synchronized(serverManagerListeners)
    {
      RServerListener listener = new RServerListener(pServer, notifier);
      pServer.addWeakServerListener(listener);
      serverManagerListeners.put(pServer.getID(), listener);
    }
  }

  @Override
  public void serverDisconnected(IServer pServer)
  {
    synchronized(serverManagerListeners)
    {
      RServerListener removedListener = serverManagerListeners.remove(pServer.getID());
      if(removedListener != null)
        pServer.removeWeakServerListener(removedListener);
    }
  }
}
