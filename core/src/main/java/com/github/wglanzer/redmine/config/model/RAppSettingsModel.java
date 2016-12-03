package com.github.wglanzer.redmine.config.model;

import com.github.wglanzer.redmine.config.ISettings;
import com.github.wglanzer.redmine.config.RMutableSettings;
import com.github.wglanzer.redmine.model.ISource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Model for global configuration.
 * It won't be saved to disk. All the settings will be "cached" and stored
 * to RMutableSettings when calling {@link RAppSettingsModel#applyTo(RMutableSettings)}
 *
 * @author w.glanzer, 06.10.2016.
 */
public class RAppSettingsModel
{
  public static final String PROP_SOURCES = "sources";

  private final AtomicBoolean modified = new AtomicBoolean(false);
  private final List<PropertyChangeListener> listeners = new ArrayList<>();

  // All model fields
  private List<RSourceBean> sources = new ArrayList<>();

  public RAppSettingsModel(@Nullable ISettings pCurrentSettings)
  {
    if(pCurrentSettings != null)
      resetTo(pCurrentSettings);
  }

  /**
   * Adds a redmine source
   *
   * @param pURL  redmine server url
   */
  public void addSource(String pURL, String pAPIKey, int pPollInterval) throws MalformedURLException
  {
    synchronized(modified)
    {
      if(!_checkURL(pURL))
        throw new MalformedURLException("URL invalid (url: " + pURL + ")");

      RSourceBean sourceBean = new RSourceBean();
      sourceBean.setUrl(pURL);
      sourceBean.setApiKey(pAPIKey);
      sourceBean.setPollingInterval(pPollInterval);
      sources.add(sourceBean);
      _modified();
      _firePropertyChanged(PROP_SOURCES, null, sourceBean);
    }
  }

  /**
   * Removes one source from the list
   *
   * @param pSource  Source, which will be removed
   * @return <tt>true</tt> if something was changed
   */
  public boolean removeSource(ISource pSource)
  {
    synchronized(modified)
    {
      RSourceBean deletedBean = sources.stream()
          .filter(pStreamSource -> pStreamSource.equals(pSource))
          .findAny().orElse(null);

      if(deletedBean != null)
      {
        boolean remove = sources.remove(deletedBean);
        _firePropertyChanged(PROP_SOURCES, deletedBean, null);
        return remove;
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
      return Collections.unmodifiableList(sources);
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
   * Applies this visualization to an settings instance
   *
   * @param pSettings  instance
   */
  public void applyTo(RMutableSettings pSettings)
  {
    pSettings.setSources(sources);
  }

  /**
   * Resets this complete model to the settings instance
   *
   * @param pSettings container for all settings
   */
  public synchronized void resetTo(ISettings pSettings)
  {
    synchronized(modified)
    {
      sources = pSettings.getSources().stream()
          .map(pSource -> (RSourceBean) pSource)
          .collect(Collectors.toList());
      modified.set(false);
    }
  }

  /**
   * Adds a PropertyChangeListener to this model which will be called,
   * when a model property has changed
   *
   * @param pListener Listener which will be added
   */
  public void addPropertyChangeListener(PropertyChangeListener pListener)
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
  public void removePropertyChangeListener(PropertyChangeListener pListener)
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
  private void _firePropertyChanged(String pProperty, Object pOldValue, Object pNewValue)
  {
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
   * Checks the URL, if it can be set as Redmine-URL
   *
   * @param pURL  URL, which is wanted to be set
   * @return <tt>true</tt>, if it can be set
   */
  private boolean _checkURL(String pURL)
  {
    try
    {
      new URL(pURL);
      return true;
    }
    catch(Exception e)
    {
      return false;
    }
  }

}
