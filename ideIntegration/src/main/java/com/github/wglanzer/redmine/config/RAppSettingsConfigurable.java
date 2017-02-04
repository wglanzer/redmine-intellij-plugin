package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.RApplicationComponent;
import com.github.wglanzer.redmine.RManager;
import com.github.wglanzer.redmine.config.gui.RAppSettingsComponent;
import com.github.wglanzer.redmine.config.gui.RAppSettingsModel;
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
  private RAppSettingsModel myCompModel;

  @Override
  public void apply() throws ConfigurationException
  {
    if(myComp != null)
    {
      myCompModel.apply();
      RManager.getInstance().reloadConfiguration();
    }
  }

  @Override
  public void reset()
  {
    if(myCompModel != null)
      myCompModel.reset();
    myComp.reset();
  }

  @Override
  public boolean isModified()
  {
    return myCompModel != null && myCompModel.isModified();
  }

  @Nullable
  @Override
  public JComponent createComponent()
  {
    if(myComp == null || myCompModel == null)
    {
      myCompModel = new RAppSettingsModel(RAppSettings.getSettings());
      myComp = new RAppSettingsComponent(myCompModel);
    }
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
