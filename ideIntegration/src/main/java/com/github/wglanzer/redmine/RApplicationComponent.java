package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.gui.RedmineStatusBarWidget;
import com.github.wglanzer.redmine.listener.ServerListenerManager;
import com.github.wglanzer.redmine.notifiers.FilteredNotifier;
import com.github.wglanzer.redmine.notifiers.SimpleNotifier;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Main implementation of the redmine integration component
 *
 * @author w.glanzer, 04.10.2016.
 */
public class RApplicationComponent implements ApplicationComponent
{
  public static final String REDMINE_INTEGRATION_PLUGIN_NAME = "Redmine Integration";

  @Override
  public void initComponent()
  {
    RManager manager = RManager.getInstance();

    // Notifier
    SimpleNotifier notifier = new FilteredNotifier(() -> manager.getPreferences().getCurrentSettings());

    // Init custom widget to ALL projects
    RedmineStatusBarWidget.init();

    // Init "background"-tasks
    manager.init(new RManagerPrefsImpl(notifier));
    manager.startup();

    // Init weak listeners which interact with IntelliJ
    ServerListenerManager.getInstance().init(manager.getServerManager(), notifier);
  }

  @Override
  public void disposeComponent()
  {
    // Disconnect listeners
    ServerListenerManager.getInstance().shutdown();

    // Shutdown manager
    RManager.getInstance().shutdown();
  }

  @NotNull
  @Override
  public String getComponentName()
  {
    return REDMINE_INTEGRATION_PLUGIN_NAME;
  }

}
