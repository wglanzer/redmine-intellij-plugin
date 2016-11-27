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

  private final RServerManager serverManager;
  private final IRLoggingFacade loggingFacade;

  public RManager()
  {
    loggingFacade = new RLoggingFacadeImpl();
    serverManager = new RServerManager(loggingFacade);
  }

  /**
   * The Redmine-Manager should be started now
   */
  public void startup()
  {
    serverManager.connect();
  }

  /**
   * Reloads the configuration
   */
  public void reloadConfiguration()
  {
    serverManager.reloadConfiguration();
  }

  /**
   * Kills the Redmine-Manager
   */
  public void shutdown()
  {
    serverManager.shutdown();
  }

  /**
   * Gets the active serverManager
   *
   * @return the RServerManager
   */
  public RServerManager getServerManager()
  {
    return serverManager;
  }

}
