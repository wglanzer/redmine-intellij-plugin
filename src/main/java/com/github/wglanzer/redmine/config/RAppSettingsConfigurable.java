package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.RApplicationComponent;
import com.github.wglanzer.redmine.RManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Configurable for IntelliJ-API
 *
 * @author w.glanzer, 04.10.2016.
 */

public class RAppSettingsConfigurable implements SearchableConfigurable
{

  private RAppSettingsComponent myComp;

  @Override
  public void apply() throws ConfigurationException
  {
    if(myComp != null)
    {
      myComp.applyTo((RMutableSettings) RAppSettings.getSettings());
      RManager.getInstance().reloadConfiguration();
    }
  }

  @Override
  public void reset()
  {
//    ((RMutableSettings) RAppSettings.getSettings()).resetToDefault();
  }

  @Override
  public boolean isModified()
  {
    return myComp != null && myComp.isModified();
  }

  @Nullable
  @Override
  public JComponent createComponent()
  {
    if(myComp == null)
      myComp = new RAppSettingsComponent(RAppSettings.getSettings());
    return myComp;
  }

  @Nls
  @Override
  public String getDisplayName()
  {
    return RApplicationComponent.REDMINE_INTEGRATION_PLUGIN_NAME;
  }

  @NotNull
  @Override
  public String getId()
  {
    return "preferences.RedminePlugin";
  }

  @Nullable
  @Override
  public Runnable enableSearch(String pS)
  {
    return null;
  }

  @Nullable
  @Override
  public String getHelpTopic()
  {
    return null;
  }

  @Override
  public void disposeUIResources()
  {
    if(myComp != null)
      myComp.dispose();
  }
}
