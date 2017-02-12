package com.github.wglanzer.redmine.notifiers;

import com.github.wglanzer.redmine.config.SettingsDataModel;
import com.github.wglanzer.redmine.gui.RedmineStatusBarWidget;
import com.github.wglanzer.redmine.listener.IChangeNotifier;
import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;
import com.github.wglanzer.redmine.notifiers.balloons.BalloonComponentFactory;
import com.github.wglanzer.redmine.util.StringUtility;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupAdapter;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 1. Notifies the user that something has changed in redmine
 * 2. Global notifier for IntelliJ-Interaction
 *
 * @author w.glanzer, 11.12.2016.
 */
public class SimpleNotifier implements IChangeNotifier, INotifier
{

  public static final String NOTIFICATION_ID = "Redmine";
  private final _BalloonDispatcher balloonDispatcher = new _BalloonDispatcher();
  private final Supplier<SettingsDataModel> preferences;

  public SimpleNotifier(Supplier<SettingsDataModel> pSettings)
  {
    preferences = pSettings;
  }

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
    _showCustomBalloon(BalloonComponentFactory.createExceptionBalloon(pMessage, pException));
    _logInEventLog(pMessage.trim().isEmpty() ? "Exception" : pMessage, StringUtility.toLogString(pException), NotificationType.ERROR);
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
      balloonDispatcher.next(RelativePoint.getCenterOf(myWidget.getComponent()), pBalloon);
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
    if(preferences.get().isEnableLog())
    {
      Notification notification = new Notification(NOTIFICATION_ID, new ImageIcon(), NOTIFICATION_ID, pTitle != null ? pTitle : "", pDetails, pType, null);
      notification.expire();
      for(Project project : ProjectManager.getInstance().getOpenProjects())
        Notifications.Bus.notify(notification, project);
    }
  }

  /**
   * Dispatcher to show only one Balloon at a time
   */
  private class _BalloonDispatcher
  {
    private final List<_Entry> queue = new ArrayList<>();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    /**
     * Adds a new Ballon to this queue
     *
     * @param pPoint   Point, at which the balloon should be shown
     * @param pBalloon Balloon which should be shown
     */
    public void next(RelativePoint pPoint, Balloon pBalloon)
    {
      synchronized(queue)
      {
        queue.add(new _Entry(pPoint, pBalloon));
        if(!isRunning.get())
          _dispatchNext();
      }
    }

    private void _dispatchNext()
    {
      synchronized(queue)
      {
        isRunning.set(true);

        if(!queue.isEmpty())
        {
          _Entry next = queue.remove(0);
          _showBallon(next, event -> {
            if(event == null || event.asBalloon().wasFadedOut()) //todo notice if balloon was "killed" or normally disposed
              _dispatchNext();
            else
            {
              // Balloon was killed
              int lostMessages = queue.size();
              queue.clear();
              SimpleNotifier.this.notify(null, lostMessages + " remaining messages");
            }
          });
        }
        else
          isRunning.set(false);
      }
    }

    /**
     * Shows a balloon instantly
     *
     * @param pNextEntry Entry that should be shown
     * @param pOnClose   Consumer which will be executed on balloon closed
     */
    private void _showBallon(_Entry pNextEntry, Consumer<LightweightWindowEvent> pOnClose)
    {
      if(preferences.get().isEnableNotifications() && !pNextEntry.balloon.isDisposed())
      {
        pNextEntry.balloon.show(pNextEntry.point, Balloon.Position.above);
        pNextEntry.balloon.addListener(new JBPopupAdapter()
        {
          @Override
          public void onClosed(LightweightWindowEvent event)
          {
            pOnClose.accept(event);
          }
        });
      }
      else
        pOnClose.accept(null);
    }

    /**
     * Holder Balloon + Point
     */
    private class _Entry
    {
      private RelativePoint point;
      private Balloon balloon;

      public _Entry(RelativePoint pPoint, Balloon pBalloon)
      {
        point = pPoint;
        balloon = pBalloon;
      }
    }
  }

}
