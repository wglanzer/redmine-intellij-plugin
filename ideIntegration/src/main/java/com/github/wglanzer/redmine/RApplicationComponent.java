package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.listener.INotifier;
import com.github.wglanzer.redmine.listener.RServerListener;
import com.github.wglanzer.redmine.model.IServer;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Main implementation of the redmine integration component
 *
 * @author w.glanzer, 04.10.2016.
 */
public class RApplicationComponent implements ApplicationComponent
{
  public static final String NOTIFICATION_ID = "Redmine Integration";
  public static final String REDMINE_INTEGRATION_PLUGIN_NAME = "Redmine Integration";

  private final INotifier notifier = new IDEANotifier();
  private _ServerManagerListener serverManagerListenerStrongRef;

  @Override
  public void initComponent()
  {
    RManager manager = RManager.getInstance();

    // Init "background"-tasks
    manager.init(new RManagerPrefsImpl());
    manager.startup();

    // Init weak listeners which interact with IntelliJ
    if(serverManagerListenerStrongRef != null)
      serverManagerListenerStrongRef.destroyAll();
    serverManagerListenerStrongRef = new _ServerManagerListener(manager.getServerManager(), notifier);
    manager.getServerManager().addWeakServerManagerListener(serverManagerListenerStrongRef);
  }

  @Override
  public void disposeComponent()
  {
    // Disconnect listeners
    serverManagerListenerStrongRef.destroyAll();

    // Shutdown manager
    RManager.getInstance().shutdown();
  }

  @NotNull
  @Override
  public String getComponentName()
  {
    return REDMINE_INTEGRATION_PLUGIN_NAME;
  }

  /**
   * IRServerManagerListener-Impl to:
   * 1. add an RServerListener to all IServers
   * 2. persist these listeners, because they are weak
   */
  private static class _ServerManagerListener implements IRServerManagerListener
  {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final Map<String, RServerListener> serverManagerListeners = new HashMap<>();
    private final INotifier notifier;

    public _ServerManagerListener(RServerManager pServerManager, INotifier pNotifier)
    {
      notifier = pNotifier;

      // Add all now available servers to our map
      pServerManager.getAvailableServers().forEach(this::serverCreated);
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

    public void destroyAll()
    {
      synchronized(serverManagerListeners)
      {
        serverManagerListeners.clear();
      }
    }
  }

}
