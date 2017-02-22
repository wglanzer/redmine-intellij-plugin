package com.github.wglanzer.redmine.config.gui;

import com.github.wglanzer.redmine.config.SettingsDataModel;
import com.github.wglanzer.redmine.config.SourceDataModel;
import com.github.wglanzer.redmine.config.WatchDataModel;
import com.github.wglanzer.redmine.model.ISource;
import com.github.wglanzer.redmine.model.impl.server.PollingServer;
import com.github.wglanzer.redmine.util.WeakListenerList;
import com.github.wglanzer.redmine.util.propertly.BulkModifyHierarchy;
import com.github.wglanzer.redmine.util.propertly.DataModelFactory;
import de.adito.propertly.core.spi.IHierarchy;
import de.adito.propertly.core.spi.IProperty;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Model for global configuration.
 * It won't be saved to disk. All the settings will be "cached" and stored asynchronously
 *
 * @author w.glanzer, 06.10.2016.
 */
public class RAppSettingsModel
{
  public static final String PROP_SOURCES = "sources";
  public static final String PROP_WATCHES = "watches";

  private final AtomicBoolean modified = new AtomicBoolean(false);
  private final WeakListenerList<PropertyChangeListener> listeners = new WeakListenerList<>();
  private BulkModifyHierarchy<SettingsDataModel> bulkedSettingsHierarchy;

  public RAppSettingsModel(@NotNull SettingsDataModel pCurrentSettings)
  {
    IHierarchy<SettingsDataModel> hierarchy = (IHierarchy<SettingsDataModel>) pCurrentSettings.getPit().getHierarchy();
    bulkedSettingsHierarchy = new BulkModifyHierarchy<>(hierarchy);
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
      firePropertyChanged(PROP_SOURCES, null, source);
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
      boolean removed = bulkedSettingsHierarchy.getValue().removeSource(pSourceName);
      if(removed)
        firePropertyChanged(PROP_SOURCES, pSourceName, null);
      return removed;
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
        SourceDataModel.Watches watches = source.getPit().getProperty(SourceDataModel.watches).getValue();
        assert watches != null;
        watches.addProperty(watch);
        firePropertyChanged(PROP_WATCHES, null, watch);
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
      if(sourceContainingWatch != null)
      {
        boolean removed = sourceContainingWatch.removeWatch(pWatchName);
        if(removed)
          firePropertyChanged(PROP_WATCHES, pWatchName, null);
        return removed;
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
      bulkedSettingsHierarchy = new BulkModifyHierarchy<>(bulkedSettingsHierarchy.getSourceHierarchy());
      modified.set(false);
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
    if(!Objects.equals(pOldValue, pNewValue))
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

}
