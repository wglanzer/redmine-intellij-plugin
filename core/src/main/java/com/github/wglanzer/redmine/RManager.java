package com.github.wglanzer.redmine;

/**
 * Manager 4 Redmine
 *
 * @author w.glanzer, 04.10.2016.
 */
public class RManager
{

  private static RManager _INSTANCE;

  public static synchronized RManager getInstance()
  {
    if(_INSTANCE == null)
      _INSTANCE = new RManager();
    return _INSTANCE;
  }

  private RServerManager serverManager;
  private IRManagerPrefs preferences;

  public void init(IRManagerPrefs pPreferences)
  {
    preferences = pPreferences;
    serverManager = new RServerManager(preferences.getLoggingFacade(), preferences.getCurrentSettings());
  }

  /**
   * The Redmine-Manager should be started now
   */
  public void startup()
  {
    _checkInit();
    serverManager.connect();
  }

  /**
   * Reloads the configuration
   */
  public void reloadConfiguration()
  {
    _checkInit();
    serverManager.reloadConfiguration(preferences.getCurrentSettings());
  }

  /**
   * Kills the Redmine-Manager
   */
  public void shutdown()
  {
    _checkInit();
    serverManager.shutdown();
  }

  /**
   * Gets the active serverManager
   *
   * @return the RServerManager
   */
  public RServerManager getServerManager()
  {
    _checkInit();
    return serverManager;
  }

  /**
   * Returns the global preferences, for wich this Manager was initialized
   *
   * @return global preferences
   */
  public IRManagerPrefs getPreferences()
  {
    _checkInit();
    return preferences;
  }

  /**
   * Checks if the Manager was inited
   */
  private void _checkInit()
  {
    if(serverManager == null)
      throw new RuntimeException("RManager not initialized!");
  }

}
