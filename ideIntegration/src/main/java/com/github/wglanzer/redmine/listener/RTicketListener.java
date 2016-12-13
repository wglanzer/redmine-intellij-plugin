package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;

/**
 * Listener on each ticket.
 *
 * @author w.glanzer, 11.12.2016.
 */
public class RTicketListener implements ITicket.ITicketListener
{

  private final IServer server;
  private final IProject project;
  private final ITicket ticket;
  private final INotifier notifier;

  public RTicketListener(IServer pServer, IProject pProject, ITicket pTicket, @NotNull INotifier pNotifier)
  {
    server = pServer;
    project = pProject;
    ticket = pTicket;
    notifier = pNotifier;
  }

  @Override
  public void redminePropertyChanged(String pName, Object pOldValue, Object pNewValue)
  {
    notifier.notifyTicketPropertyChanged(server, ticket, pName, pOldValue, pNewValue);
  }
}
