package com.github.wglanzer.redmine.notifiers;

import com.github.wglanzer.redmine.gui.RedmineStatusBarWidget;
import com.github.wglanzer.redmine.listener.IChangeNotifier;
import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;
import com.github.wglanzer.redmine.notifiers.balloons.BalloonComponentFactory;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

/**
 * 1. Notifies the user that something has changed in redmine
 * 2. Global notifier for IntelliJ-Interaction
 *
 * @author w.glanzer, 11.12.2016.
 */
public class SimpleNotifier implements IChangeNotifier, INotifier
{

  public static final String NOTIFICATION_ID = "Redmine";

  @Override
  public void notifyNewTicket(@NotNull IServer pServer, @NotNull ITicket pTicket)
  {
    _showCustomBalloon(BalloonComponentFactory.createTicketChangedBalloon(pServer, pTicket, null));
    _logInEventLog(NOTIFICATION_ID, "ticket created: " + pTicket.getID(), NotificationType.INFORMATION);
  }

  @Override
  public void notifyTicketPropertyChanged(@NotNull IServer pServer, @NotNull ITicket pTicket, @NotNull Map<String, Map.Entry<Object, Object>> pProperties)
  {
    _showCustomBalloon(BalloonComponentFactory.createTicketChangedBalloon(pServer, pTicket, pProperties));
    _logInEventLog(NOTIFICATION_ID, "ticket changed: " + pTicket.getID(), NotificationType.INFORMATION);
  }

  @Override
  public void notifyNewProject(@NotNull IServer pServer, @NotNull IProject pProject)
  {
    _showCustomBalloon(BalloonComponentFactory.createProjectChangedBalloon(pServer, pProject, null));
    _logInEventLog(NOTIFICATION_ID, "project created: " + pProject.getID(), NotificationType.INFORMATION);
  }

  @Override
  public void notifyProjectPropertyChanged(@NotNull IServer pServer, @NotNull IProject pProject, @NotNull Map<String, Map.Entry<Object, Object>> pProperties)
  {
    _showCustomBalloon(BalloonComponentFactory.createProjectChangedBalloon(pServer, pProject, pProperties));
    _logInEventLog(NOTIFICATION_ID, "project changed: " + pProject.getID(), NotificationType.INFORMATION);
  }

  @Override
  public void notify(String pTitle, String pMessage)
  {
    String html = "<html><b>" + pTitle + "</b></br>" + pMessage + "</html>";
    _showCustomBalloon(BalloonComponentFactory.createHTMLBalloon(html, MessageType.INFO));
    _logInEventLog(pTitle, pMessage, NotificationType.INFORMATION);
  }

  @Override
  public void error(String pMessage, Exception pException)
  {
    String logString = _toLogableString(pException);
    String html = "<html>" + pMessage + "</br>" + logString + "</html>";
    _showCustomBalloon(BalloonComponentFactory.createHTMLBalloon(html, MessageType.ERROR));
    _logInEventLog(pMessage.trim().isEmpty() ? "Exception" : pMessage, logString, NotificationType.ERROR);
  }

  /**
   * Shows a message on all opened projects with a custom balloon
   *
   * @param pBalloon      Custom Balloon, not <tt>null</tt>
   */
  private void _showCustomBalloon(@NotNull Balloon pBalloon)
  {
    for(Project project : ProjectManager.getInstance().getOpenProjects())
    {
      StatusBar statusbar = WindowManager.getInstance().getStatusBar(project);
      RedmineStatusBarWidget myWidget = (RedmineStatusBarWidget) statusbar.getWidget(RedmineStatusBarWidget.REDMINE_STATUSBAR_ID);
      assert myWidget != null;
      pBalloon.show(RelativePoint.getCenterOf(myWidget.getComponent()), Balloon.Position.above);
    }
  }

  /**
   * Shows a message on all opened projects
   *
   * @param pTitle        Title that should be shown
   * @param pDetails      Details of the message
   * @param pType         MessageType (Error, Info, ...)
   */
  private void _logInEventLog(@Nullable String pTitle, @Nullable String pDetails, @NotNull NotificationType pType)
  {
    Notification notification = new Notification(NOTIFICATION_ID, new ImageIcon(), NOTIFICATION_ID, pTitle != null ? pTitle : "", pDetails, pType, null);
    notification.expire();
    for(Project project : ProjectManager.getInstance().getOpenProjects())
      Notifications.Bus.notify(notification, project);
  }

  /**
   * Converts an exception to a readable string
   *
   * @param e Exception which is about to be shown
   * @return String
   */
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