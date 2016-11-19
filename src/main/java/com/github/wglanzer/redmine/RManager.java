package com.github.wglanzer.redmine;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.wm.StatusBar;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
    loggingFacade = new _LoggingFacade();
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

  /**
   * LoggingFacade-Impl
   */
  private static class _LoggingFacade implements IRLoggingFacade
  {
    public static final NotificationGroup GROUP_BALLOON =
        new NotificationGroup("Redmine Integration", NotificationDisplayType.BALLOON, true);

    @Override
    public void log(Exception pEx, boolean pSilent)
    {
      if(!pSilent)
        Notifications.Bus.notify(GROUP_BALLOON.createNotification("Redmine Integration", _toLogString(pEx.getMessage()), NotificationType.ERROR, null));
      else
        StatusBar.Info.set(pEx.getMessage(), null);
    }

    @Override
    public void log(String pLogString, boolean pSilent)
    {
      if(!pSilent)
        Notifications.Bus.notify(GROUP_BALLOON.createNotification("Redmine Integration", _toLogString(pLogString), NotificationType.INFORMATION, null));
      else
        StatusBar.Info.set(pLogString, null);
    }

    private String _toLogString(String pMessage)
    {
      String now = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
      return now + ": " + pMessage;
    }
  }
}
