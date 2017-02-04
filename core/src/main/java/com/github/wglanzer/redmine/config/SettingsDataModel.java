package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.model.ISource;
import com.google.common.base.MoreObjects;
import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.core.spi.extension.AbstractMutablePPP;
import de.adito.propertly.core.spi.extension.AbstractPPP;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This SettingsDataModel describes the WHOLE user-setable things
 * inside the WHOLE plugin.
 *
 * @author w.glanzer, 02.02.2017.
 */
public class SettingsDataModel extends AbstractPPP<IPropertyPitProvider, SettingsDataModel, Object>
{

  public static final IPropertyDescription<SettingsDataModel, Sources> sources = PD.create(SettingsDataModel.class);
  public static final IPropertyDescription<SettingsDataModel, Boolean> enableNotifications = PD.create(SettingsDataModel.class);
  public static final IPropertyDescription<SettingsDataModel, Boolean> enableLog = PD.create(SettingsDataModel.class);

  @NotNull
  public List<ISource> getSources()
  {
    Sources sources = getPit().getValue(SettingsDataModel.sources);
    assert sources != null;
    return new ArrayList<>(sources.getValues());
  }

  /**
   * Removes one source from the list
   *
   * @param pSourceName Source, which will be removed
   * @return <tt>true</tt> if something was changed
   */
  public boolean removeSource(@NotNull String pSourceName)
  {
    SettingsDataModel.Sources sources = getPit().getProperty(SettingsDataModel.sources).getValue();
    assert sources != null;
    IProperty<Sources, SourceDataModel> deletedProp = sources.getProperties().stream()
        .filter(pSourceProp -> pSourceProp.getName().equals(pSourceName))
        .findAny().orElse(null);

    return deletedProp != null && sources.removeProperty(deletedProp);
  }

  /**
   * Returns <tt>true</tt> if notifications can be shown
   *
   * @return <tt>true</tt> = show notifications
   */
  public boolean isEnableNotifications()
  {
    return MoreObjects.firstNonNull(getPit().getValue(enableNotifications), true);
  }

  /**
   * Sets, if notifications should be shown
   *
   * @param pEnable <tt>true</tt> if they should be shown
   */
  public void setEnableNotifications(boolean pEnable)
  {
    getPit().setValue(enableNotifications, pEnable);
  }

  /**
   * Returns <tt>true</tt> if log-messages can be shown
   *
   * @return <tt>true</tt> = show log-messages
   */
  public boolean isEnableLog()
  {
    return MoreObjects.firstNonNull(getPit().getValue(enableLog), true);
  }

  /**
   * Sets, if log-messages should be shown
   *
   * @param pEnable <tt>true</tt> if they should be shown
   */
  public void setEnableLog(boolean pEnable)
  {
    getPit().setValue(enableLog, pEnable);
  }

  /**
   * PPP to hold a set of Sources
   */
  public static class Sources extends AbstractMutablePPP<SettingsDataModel, Sources, SourceDataModel>
  {
    public Sources()
    {
      super(SourceDataModel.class);
    }
  }

}
