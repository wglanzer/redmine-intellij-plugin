package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.util.IntelliJIDEAUtility;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

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
    IntelliJIDEAUtility.showMessage(pMessage, _toLogableString(pCause), NotificationType.ERROR, false);
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

  private String _toLogableString(Throwable e)
  {
    Throwable currEx = e;
    StringBuilder builder = new StringBuilder();
    for(int i = 0; currEx != null; i++)
    {
      StringBuilder myExLine = new StringBuilder();
      for(int whitespaces = 0; whitespaces < i; whitespaces++)
        myExLine.append("  ");

      // Log exception
      myExLine.append(currEx.getClass()).append(": ").append(currEx.getLocalizedMessage());
      for(StackTraceElement currElement : currEx.getStackTrace())
        myExLine.append(" [->] ").append(currElement);

      // Shorten it
      if(myExLine.length() > 255)
        myExLine.delete(255, myExLine.length()).append("[...continued...]");

      currEx = currEx.getCause();
      if(currEx != null)
        myExLine.append("\n");
      builder.append(myExLine);
    }
    return builder.toString();
  }

}
