package com.github.wglanzer.redmine.config.gui;

import com.github.wglanzer.redmine.config.ConditionDescriptionDataModel;
import com.github.wglanzer.redmine.config.SettingsDataModel;
import com.github.wglanzer.redmine.config.SourceDataModel;
import com.github.wglanzer.redmine.config.WatchDataModel;
import com.github.wglanzer.redmine.model.EConditionAttribute;
import com.github.wglanzer.redmine.model.EConditionOperator;
import com.github.wglanzer.redmine.model.ISource;
import com.github.wglanzer.redmine.model.impl.server.PollingServer;
import com.github.wglanzer.redmine.util.WeakListenerList;
import com.github.wglanzer.redmine.util.propertly.BulkModifyHierarchy;
import com.github.wglanzer.redmine.util.propertly.DataModelFactory;
import de.adito.propertly.core.common.PropertyPitEventAdapter;
import de.adito.propertly.core.spi.IHierarchy;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Model for global configuration.
 * It won't be saved to disk. All the settings will be "cached" and stored asynchronously
 *
 * @author w.glanzer, 06.10.2016.
 */
public class RAppSettingsModel
{
  private final AtomicBoolean modified = new AtomicBoolean(false);
  private final WeakListenerList<PropertyChangeListener> listeners = new WeakListenerList<>();
  private final _BulkedHierarchyListener bulkedHierarchyListener = new _BulkedHierarchyListener();
  private BulkModifyHierarchy<SettingsDataModel> bulkedSettingsHierarchy;

  public RAppSettingsModel(@NotNull SettingsDataModel pCurrentSettings)
  {
    IHierarchy<SettingsDataModel> hierarchy = (IHierarchy<SettingsDataModel>) pCurrentSettings.getPit().getHierarchy();
    bulkedSettingsHierarchy = new BulkModifyHierarchy<>(hierarchy);
    bulkedSettingsHierarchy.addWeakListener(bulkedHierarchyListener);
  }

  /**
   * Adds an empty redmine source
   */
  public void addEmptySource()
  {
    synchronized(modified)
    {
      SourceDataModel source = DataModelFactory.createModel(SourceDataModel.class);
      source.setDisplayName("Redmine Server");
      source.setUrl("https://example.mydomain.com/");
      source.setApiKey("mySecureAPIKeyGeneratedByRedmine");
      source.setPollingInterval(PollingServer.DEFAULT_POLLINTERVAL);
      source.setPageSize(25);
      source.setCheckCertificate(true);
      SettingsDataModel.Sources sources = bulkedSettingsHierarchy.getValue().getPit().getProperty(SettingsDataModel.sources).getValue();
      assert sources != null;
      sources.addProperty(source);
    }
  }

  /**
   * Removes one source from the list
   *
   * @param pSourceName  Source, which will be removed
   * @return <tt>true</tt> if something was changed
   */
  public boolean removeSource(@NotNull String pSourceName)
  {
    synchronized(modified)
    {
      return bulkedSettingsHierarchy.getValue().removeSource(pSourceName);
    }
  }

  /**
   * Adds an empty redmine watch
   */
  public void addEmptyWatch(String pSourceName)
  {
    synchronized(modified)
    {
      IProperty<SettingsDataModel.Sources, SourceDataModel> propSource =
          bulkedSettingsHierarchy.getValue().getValue(SettingsDataModel.sources).findProperty(pSourceName);
      if(propSource != null)
      {
        SourceDataModel source = propSource.getValue();
        assert source != null;
        WatchDataModel watch = DataModelFactory.createModel(WatchDataModel.class);
        watch.setDisplayName("New Watch");
        SourceDataModel.Watches watches = source.getPit().getProperty(SourceDataModel.watches).getValue();
        assert watches != null;
        watches.addProperty(watch);
      }
    }
  }

  /**
   * Removes one watch from the list
   *
   * @param pWatchName  Watch, which will be removed
   * @return <tt>true</tt> if something was changed
   */
  public boolean removeWatch(@NotNull String pWatchName)
  {
    synchronized(modified)
    {
      SourceDataModel sourceContainingWatch = bulkedSettingsHierarchy.getValue().getValue(SettingsDataModel.sources).getValues().stream()
          .filter(pSourceModel -> pSourceModel.getWatches().stream()
              .anyMatch(pWatch -> pWatch.getName().equals(pWatchName)))
          .findFirst().orElse(null);
      return sourceContainingWatch != null && sourceContainingWatch.removeWatch(pWatchName);
    }
  }

  /**
   * Adds an empty condition to a specific watch
   *
   * @param pSettings SettingsDataModel because the conditions have its own bulkModify...
   */
  public void addEmptyCondition(SettingsDataModel pSettings, String pSourceName, String pWatchName)
  {
    synchronized(modified)
    {
      IProperty<SettingsDataModel.Sources, SourceDataModel> propSource =
          pSettings.getValue(SettingsDataModel.sources).findProperty(pSourceName);
      if(propSource != null && propSource.getValue() != null)
      {
        IProperty<SourceDataModel.Watches, WatchDataModel> propWatch =
            propSource.getValue().getPit().getValue(SourceDataModel.watches).findProperty(pWatchName);
        if(propWatch != null && propWatch.getValue() != null)
        {
          ConditionDescriptionDataModel desc = DataModelFactory.createModel(ConditionDescriptionDataModel.class);
          desc.setAttribute(EConditionAttribute.ASSIGNEE);
          desc.setOperator(EConditionOperator.EQUALS);
          desc.setPossibleValues(Collections.emptyList());
          propWatch.getValue().getPit().getProperty(WatchDataModel.conditions).getValue().addProperty(desc);
        }
      }
    }
  }

  /**
   * Removes one condition from the list
   *
   * @param pSettings SettingsDataModel because the conditions have its own bulkModify...
   * @param pConditionName  Condition, which will be removed
   * @return <tt>true</tt> if something was changed
   */
  public boolean removeCondition(SettingsDataModel pSettings, String pSourceName, String pWatchName, String pConditionName)
  {
    synchronized(modified)
    {
      IProperty<SettingsDataModel.Sources, SourceDataModel> propSource =
          pSettings.getValue(SettingsDataModel.sources).findProperty(pSourceName);
      if(propSource != null && propSource.getValue() != null)
      {
        IProperty<SourceDataModel.Watches, WatchDataModel> propWatch =
            propSource.getValue().getValue(SourceDataModel.watches).findProperty(pWatchName);
        if(propWatch != null && propWatch.getValue() != null)
          return propWatch.getValue().removeCondition(pConditionName);
      }

      return false;
    }
  }

  /**
   * Returns all the sources of this settings-model
   *
   * @return all sources, not null
   */
  @NotNull
  public List<ISource> getSources()
  {
    synchronized(modified)
    {
      return bulkedSettingsHierarchy.getValue().getSources();
    }
  }

  /**
   * Returns <tt>true</tt>, if this instance was modified
   *
   * @return <tt>true</tt>, if modified
   */
  public boolean isModified()
  {
    synchronized(modified)
    {
      return modified.get();
    }
  }

  /**
   * Applies this visualization to the
   * underlying source-Hierarchy
   */
  public void apply()
  {
    synchronized(modified)
    {
      bulkedSettingsHierarchy.writeBack();
      modified.set(false);
    }
  }

  /**
   * Resets this complete model to default state
   */
  public synchronized void reset()
  {
    synchronized(modified)
    {
      bulkedSettingsHierarchy.removeListener(bulkedHierarchyListener);
      bulkedSettingsHierarchy = new BulkModifyHierarchy<>(bulkedSettingsHierarchy.getSourceHierarchy());
      bulkedSettingsHierarchy.addWeakListener(bulkedHierarchyListener);
      modified.set(false);
      firePropertyChanged("modified", true, false);
    }
  }

  /**
   * Adds a PropertyChangeListener to this model which will be called,
   * when a model property has changed
   *
   * @param pListener Listener which will be added
   */
  public void addWeakPropertyChangeListener(PropertyChangeListener pListener) //todo PropertyPitEventListener
  {
    synchronized(listeners)
    {
      listeners.add(pListener);
    }
  }

  /**
   * Removes a listener from the list
   *
   * @param pListener  Listener which will be removed
   */
  public void removeWeakPropertyChangeListener(PropertyChangeListener pListener)
  {
    synchronized(listeners)
    {
      listeners.remove(pListener);
    }
  }

  /**
   * Fires, that a property has changed
   *
   * @param pProperty  Property which changed its value
   * @param pOldValue  Old value, before change
   * @param pNewValue  New value, after change
   */
  protected void firePropertyChanged(String pProperty, Object pOldValue, Object pNewValue)
  {
    if(!Objects.equals(pProperty, "modified") && !Objects.equals(pOldValue, pNewValue))
      _modified();

    synchronized(listeners)
    {
      PropertyChangeEvent ev = new PropertyChangeEvent(this, pProperty, pOldValue, pNewValue);
      listeners.forEach(pListener -> pListener.propertyChange(ev));
    }
  }

  /**
   * Sets the modified state
   */
  private void _modified()
  {
    synchronized(modified)
    {
      modified.set(true);
    }
  }

  /**
   * EventAdapter for bulkedHierarchy
   */
  private class _BulkedHierarchyListener extends PropertyPitEventAdapter<IPropertyPitProvider, Object>
  {
    @Override
    public void propertyWillBeRemoved(@NotNull IProperty<IPropertyPitProvider, Object> pProperty, @NotNull Consumer<Runnable> pOnRemoved, @NotNull Set<Object> pAttributes)
    {
      Object oldValue = pProperty.getValue();
      String name = pProperty.getName();
      pOnRemoved.accept(() -> firePropertyChanged(name, oldValue, null));
    }

    @Override
    public void propertyValueChanged(@NotNull IProperty pProperty, Object pOldValue, Object pNewValue, @NotNull Set pAttributes)
    {
      firePropertyChanged(pProperty.getName(), pOldValue, pNewValue);
    }
  }

}
