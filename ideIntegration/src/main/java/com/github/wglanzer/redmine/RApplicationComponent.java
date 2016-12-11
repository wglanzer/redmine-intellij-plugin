package com.github.wglanzer.redmine;

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
    RManager.getInstance().init(new RManagerPrefsImpl());
    RManager.getInstance().startup();
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
