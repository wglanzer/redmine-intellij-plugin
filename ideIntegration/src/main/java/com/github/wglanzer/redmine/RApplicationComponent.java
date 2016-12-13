package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.listener.INotifier;
import com.github.wglanzer.redmine.listener.RServerListener;
import com.github.wglanzer.redmine.model.IServer;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

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

  @Override
  public void initComponent()
  {
    RManager manager = RManager.getInstance();

    // Init "background"-tasks
    manager.init(new RManagerPrefsImpl());
    manager.startup();

    // Init listeners which interact with IntelliJ
    for(IServer currServer : manager.getServerManager().getAvailableServers())
      currServer.addServerListener(new RServerListener(currServer, notifier));
  }

  @Override
  public void disposeComponent()
  {
    RManager.getInstance().shutdown();
  }

  @NotNull
  @Override
  public String getComponentName()
  {
    return REDMINE_INTEGRATION_PLUGIN_NAME;
  }

}
