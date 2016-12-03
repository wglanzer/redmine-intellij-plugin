package com.github.wglanzer.redmine;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

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
  public void error(Exception pEx)
  {
    log(NotificationType.ERROR, pEx.getMessage(), false);
    pEx.printStackTrace(); // Print it on console -> Debug-Reasons
  }

  @Override
  public void log(String pLogString, boolean pSilent)
  {
    log(NotificationType.INFORMATION, pLogString, pSilent);
  }

  @Override
  public void log(NotificationType pType, String pLogString, boolean pSilent)
  {
    Notification notification = new Notification(NOTIFICATION_ID, NOTIFICATION_ID, pLogString, pType);
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

}
