package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.ITicket;
import com.github.wglanzer.redmine.util.IntelliJIDEAUtility;
import com.intellij.notification.NotificationType;
import org.jetbrains.annotations.NotNull;

/**
 * Listeners, that does things when a ticket was added / changed
 *
 * @author w.glanzer, 11.12.2016.
 */
public class RProjectListener implements IProject.IProjectListener
{

  private final IProject project;

  public RProjectListener(IProject pProject)
  {
    project = pProject;
  }

  @Override
  public void redminePropertyChanged(String pName, Object pOldValue, Object pNewValue)
  {
    // property from project changed
  }

  @Override
  public void ticketAdded(@NotNull ITicket pTicketAdded)
  {
    IntelliJIDEAUtility.showMessage("Ticket created", pTicketAdded.getSubject(), NotificationType.ERROR, false);
  }

}
