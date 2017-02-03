package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.IRLoggingFacade;
import com.github.wglanzer.redmine.RManager;
import com.github.wglanzer.redmine.config.beans.SettingsDataModel;
import com.github.wglanzer.redmine.util.propertly.DataModelFactory;
import com.google.common.base.Objects;
import com.intellij.openapi.components.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.JDomDriver;
import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.serialization.MapSerializationProvider;
import de.adito.propertly.serialization.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

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
public class RAppSettings implements PersistentStateComponent<RAppSettings.StateContainer>
{

  private static final Serializer<Map<String, Object>> _SERIALIZER = Serializer.create(new MapSerializationProvider());
  private static final XStream _CONVERTER = new XStream(new JDomDriver());

  private SettingsDataModel settingsModel;

  /**
   * Returns the instance of the saved ISettings
   *
   * @return ISettings, not <tt>null</tt>
   */
  @NotNull
  public static SettingsDataModel getSettings()
  {
    RAppSettings settings = ServiceManager.getService(RAppSettings.class);
    if(settings.settingsModel == null)
      settings.settingsModel = DataModelFactory.createModel(SettingsDataModel.class);
    return settings.settingsModel;
  }

  @Nullable
  @Override
  public StateContainer getState()
  {
    StateContainer stateContainer = new StateContainer();
    try
    {
      stateContainer.value = _CONVERTER.toXML(_SERIALIZER.serialize(settingsModel));
    }
    catch(Exception e)
    {
      _getLogginFacade().error("RAppSettings#getState", e);
    }
    return stateContainer;
  }

  @Override
  public void loadState(StateContainer pStateContainer)
  {
    String value = pStateContainer.value;
    if(value != null && !value.isEmpty())
    {
      try
      {
        Map<String, Object> savedSettings = (Map<String, Object>) _CONVERTER.fromXML(value);
        SettingsDataModel model = DataModelFactory.initModel((SettingsDataModel) _SERIALIZER.deserialize(savedSettings));
        settingsModel = new Hierarchy<>("settings", model).getValue();
      }
      catch(Exception e)
      {
        _getLogginFacade().error("Initializing settings with default values...", e);
      }
    }

    if(settingsModel == null)
      settingsModel = DataModelFactory.createModel(SettingsDataModel.class);
  }

  private static IRLoggingFacade _getLogginFacade()
  {
    return RManager.getInstance().getPreferences().getLoggingFacade();
  }

  /**
   * Container to hold a single json-value
   */
  static class StateContainer
  {
    public String value;

    @Override
    public boolean equals(Object pO)
    {
      if(this == pO) return true;
      if(pO == null || getClass() != pO.getClass()) return false;
      StateContainer stateContainer = (StateContainer) pO;
      return Objects.equal(value, stateContainer.value);
    }

    @Override
    public int hashCode()
    {
      return Objects.hashCode(value);
    }
  }
}
