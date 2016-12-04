package com.github.wglanzer.redmine;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author w.glanzer, 27.11.2016.
 */
class RLoggingFacadeImpl implements IRLoggingFacade
{

  private static final String NOTIFICATION_ID = "Redmine Integration";
  private static final boolean _IS_DEBUG = System.getProperty("plugin.redmine.debug") != null;

  static
  {
    Notifications.Bus.register(NOTIFICATION_ID, NotificationDisplayType.BALLOON);
  }

  @Override
  public void error(Exception pEx)
  {
    error("Exception", pEx);
  }

  @Override
  public void error(String pMessage, Exception pCause)
  {
    StringWriter writer = new StringWriter();
    pCause.printStackTrace(new PrintWriter(writer));
    _log(NotificationType.ERROR, pMessage, writer.toString(), false);
    if(_IS_DEBUG)
      pCause.printStackTrace(); // Print it on console -> Debug-Reasons
  }

  @Override
  public void log(String pLogString)
  {
    _log(NotificationType.INFORMATION, "LOG", pLogString, false);
  }

  @Override
  public void debug(String pDebugString)
  {
    // No debug-outputs, when debug is not enabled (Live-System)
    if(!_IS_DEBUG)
      return;

    _log(NotificationType.INFORMATION, "DEBUG", pDebugString, true);
  }

  private void _log(NotificationType pType, @Nullable String pTitle, String pDetails, boolean pOnlyEventLog)
  {
    Notification notification = new Notification(NOTIFICATION_ID, new ImageIcon(), NOTIFICATION_ID, pTitle != null ? pTitle : "", pDetails, pType, null);
    if(pOnlyEventLog)
      notification.expire();
    Notifications.Bus.notify(notification);
  }

}
