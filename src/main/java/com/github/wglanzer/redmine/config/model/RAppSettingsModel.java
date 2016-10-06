package com.github.wglanzer.redmine.config.model;

import com.github.wglanzer.redmine.config.ISettings;
import com.github.wglanzer.redmine.config.RMutableSettings;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Model for global configuration
 *
 * @author w.glanzer, 06.10.2016.
 */
public class RAppSettingsModel
{

  private final AtomicBoolean modified = new AtomicBoolean(false);

  public RAppSettingsModel(@Nullable ISettings pCurrentSettings)
  {
    if(pCurrentSettings != null)
      resetTo(pCurrentSettings);
  }

  /**
   * Applies this visualization to an settings instance
   *
   * @param pSettings  instance
   */
  public void applyTo(RMutableSettings pSettings)
  {
    pSettings.setExample(UUID.randomUUID().toString());
  }

  /**
   * Returns <tt>true</tt>, if this instance was modified
   *
   * @return <tt>true</tt>, if modified
   */
  public boolean isModified()
  {
    return modified.get();
  }

  /**
   * Resets this complete model to the settings instance
   *
   * @param pSettings container for all settings
   */
  public synchronized void resetTo(ISettings pSettings)
  {
    //todo set all settings
    modified.set(false);
  }

  /**
   * Sets the modified state
   */
  private void _modified()
  {
    modified.set(true);
  }

}
