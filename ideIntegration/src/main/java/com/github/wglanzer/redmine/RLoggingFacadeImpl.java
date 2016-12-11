package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.util.IntelliJIDEAUtility;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author w.glanzer, 27.11.2016.
 */
class RLoggingFacadeImpl implements IRLoggingFacade
{

  private static final boolean _IS_DEBUG = System.getProperty("plugin.redmine.debug") != null;

  static
  {
    Notifications.Bus.register(RApplicationComponent.NOTIFICATION_ID, NotificationDisplayType.BALLOON);
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
    IntelliJIDEAUtility.showMessage(pMessage, writer.toString(), NotificationType.ERROR, false);
    if(_IS_DEBUG)
      pCause.printStackTrace(); // Print it on console -> Debug-Reasons
  }

  @Override
  public void log(String pLogString)
  {
    IntelliJIDEAUtility.showMessage("LOG", pLogString, NotificationType.INFORMATION, false);
  }

  @Override
  public void debug(String pDebugString)
  {
    // No debug-outputs, when debug is not enabled (Live-System)
    if(!_IS_DEBUG)
      return;

    IntelliJIDEAUtility.showMessage("DEBUG", pDebugString, NotificationType.INFORMATION, true);
  }

}
