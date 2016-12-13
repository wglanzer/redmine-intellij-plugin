package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.listener.INotifier;
import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;
import com.github.wglanzer.redmine.util.IntelliJIDEAUtility;
import com.github.wglanzer.redmine.util.StringUtility;
import com.intellij.notification.NotificationType;
import org.jetbrains.annotations.NotNull;

/**
 * Notifies the user that something has changed in redmine
 *
 * @author w.glanzer, 11.12.2016.
 */
class IDEANotifier implements INotifier
{

  @Override
  public void notifyNewTicket(@NotNull IServer pServer, @NotNull ITicket pTicket)
  {
    String balloonTitle = "New Ticket: #" + pTicket.getID();
    String balloonContent = "<u>" + StringUtility.abbreviate(StringUtility.toDisplayString(pServer, pTicket, true), 0, 107) + "</u></br>" +
        pTicket.getDescription();
    IntelliJIDEAUtility.showMessage(balloonTitle, balloonContent, NotificationType.INFORMATION, false);
  }

  @Override
  public void notifyTicketPropertyChanged(@NotNull IServer pServer, @NotNull ITicket pTicket, String pChangedProperty, Object pOldValue, Object pNewValue)
  {
    String balloonTitle = "Ticket Changed: #" + pTicket.getID();
    String balloonContent = pChangedProperty + " -> " + pNewValue + " (show " + StringUtility.toURL(pServer, pTicket) + ")";
    IntelliJIDEAUtility.showMessage(balloonTitle, balloonContent, NotificationType.INFORMATION, false);
  }

  @Override
  public void notifyNewProject(@NotNull IServer pServer, @NotNull IProject pProject)
  {
    String balloonTitle = "New Project: #" + pProject.getID();
    String balloonContent = "<u>" + StringUtility.abbreviate(StringUtility.toDisplayString(pServer, pProject, true), 0, 107) + "</u></br>" +
        pProject.getDescription();
    IntelliJIDEAUtility.showMessage(balloonTitle, balloonContent, NotificationType.INFORMATION, false);
  }


  @Override
  public void notifyProjectPropertyChanged(@NotNull IServer pServer, @NotNull IProject pProject, String pChangedProperty, Object pOldValue, Object pNewValue)
  {
    String balloonTitle = "Project Changed: #" + pProject.getID();
    String balloonContent = pChangedProperty + " -> " + pNewValue + " (show " + StringUtility.toURL(pServer, pProject) + ")";
    IntelliJIDEAUtility.showMessage(balloonTitle, balloonContent, NotificationType.INFORMATION, false);
  }

}
