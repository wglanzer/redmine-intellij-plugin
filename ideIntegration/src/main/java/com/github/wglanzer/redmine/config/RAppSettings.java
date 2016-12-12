package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.config.beans.RSourceBean;
import com.github.wglanzer.redmine.model.ISource;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
public class RAppSettings implements PersistentStateComponent<RAppSettings>, IMutableSettings
{

  public List<RSourceBean> sources = new ArrayList<>();

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
    RAppSettings myState = settings.getState();
    assert myState != null;
    return myState;
  }

  @Override
  public void setSources(List<RSourceBean> pSources)
  {
    if(pSources == null)
      pSources = Collections.emptyList();
    pSources.removeIf(Objects::isNull);
    sources = new ArrayList<>(pSources);
  }

  @NotNull
  @Override
  public List<ISource> getSources()
  {
    if(sources == null)
      return Collections.emptyList();

    sources.removeIf(Objects::isNull);
    return new ArrayList<>(sources);
  }

  @Nullable
  @Override
  public RAppSettings getState()
  {
    return this;
  }

  @Override
  public void loadState(RAppSettings pSettings)
  {
    XmlSerializerUtil.copyBean(pSettings, this);
  }
}
