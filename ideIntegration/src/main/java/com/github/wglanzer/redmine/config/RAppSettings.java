package com.github.wglanzer.redmine.config;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * AppSettings-Impl, for persistence with IJ
 *
 * @author w.glanzer, 04.10.2016.
 */
@State(
    name = "preferences.RedminePlugin",
    storages = {
        @Storage(file = StoragePathMacros.APP_CONFIG + "/redmineSettings.xml")
    }
)
public class RAppSettings implements PersistentStateComponent<ISettings>
{

  private RMutableSettings settings = new RMutableSettings();

  /**
   * Returns the instance of the saved RAppSettings
   *
   * @return RAppSettings, not <tt>null</tt>
   */
  @NotNull
  public static ISettings getSettings()
  {
    RAppSettings settings = ServiceManager.getService(RAppSettings.class);
    settings = settings != null ? settings : new RAppSettings();
    ISettings myState = settings.getState();
    assert myState != null;
    return myState;
  }

  @Nullable
  @Override
  public ISettings getState()
  {
    return settings;
  }

  @Override
  public void loadState(ISettings pSettings)
  {
    XmlSerializerUtil.copyBean(pSettings, settings);
  }

}
