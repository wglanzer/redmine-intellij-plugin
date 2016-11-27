package com.github.wglanzer.redmine;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author w.glanzer, 27.11.2016.
 */
class RLoggingFacadeImpl implements IRLoggingFacade
{

  private static final String NOTIFICATION_ID = "Redmine Integration";

  static
  {
    Notifications.Bus.register(NOTIFICATION_ID, NotificationDisplayType.BALLOON);
  }

  @Override
  public void log(Exception pEx, boolean pSilent)
  {
    log(NotificationType.ERROR, pEx.getMessage(), pSilent);
  }

  @Override
  public void log(String pLogString, boolean pSilent)
  {
    log(NotificationType.INFORMATION, pLogString, pSilent);
  }

  @Override
  public void log(NotificationType pType, String pLogString, boolean pSilent)
  {
    Notification notification = new Notification(NOTIFICATION_ID, NOTIFICATION_ID, _toLogString(pLogString), pType);
    if(pSilent)
      notification.expire();
    Notifications.Bus.notify(notification);
  }

  @Override
  public void debug(String pDebugString)
  {
    log(pDebugString, true);
    System.out.println(pDebugString);
  }

  private String _toLogString(String pMessage)
  {
    String now = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
    return now + ": " + pMessage;
  }
}
