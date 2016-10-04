package com.github.wglanzer.redmine.config;

import javax.swing.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Visualization for Redmine configuration
 *
 * @author w.glanzer, 04.10.2016.
 */
class RAppSettingsComponent extends JPanel
{

  private final ISettings currentSettings;

  private AtomicBoolean modified = new AtomicBoolean(false);

  public RAppSettingsComponent(ISettings pCurrentSettings)
  {
    currentSettings = pCurrentSettings;
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
   * Returns the modified state of the configuration
   *
   * @return <tt>true</tt>, if the configuration was modified
   */
  public boolean isModified()
  {
    return modified.get();
  }

  /**
   * Disposes this component
   */
  public void dispose()
  {
  }
}
