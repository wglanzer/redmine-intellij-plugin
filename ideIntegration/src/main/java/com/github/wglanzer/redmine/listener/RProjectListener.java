package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;

/**
 * Listeners, that does things when a ticket was added / changed
 *
 * @author w.glanzer, 11.12.2016.
 */
public class RProjectListener implements IProject.IProjectListener
{

  private final IServer server;
  private final IProject project;
  private final INotifier notifier;

  public RProjectListener(IServer pServer, IProject pProject, @NotNull INotifier pNotifier)
  {
    server = pServer;
    project = pProject;
    notifier = pNotifier;
  }

  @Override
  public void redminePropertyChanged(String pName, Object pOldValue, Object pNewValue)
  {
    notifier.notifyProjectPropertyChanged(server, project, pName, pOldValue, pNewValue);
  }

  @Override
  public void ticketAdded(@NotNull ITicket pTicketAdded, boolean pCreatedDuringPreload)
  {
    pTicketAdded.addTicketListener(new RTicketListener(server, project, pTicketAdded, notifier)); // New ticket -> listen on it
    if(!pCreatedDuringPreload)
      notifier.notifyNewTicket(server, pTicketAdded);
  }

}
