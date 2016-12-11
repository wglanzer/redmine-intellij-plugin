package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;

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

  public RTicketListener(IServer pServer, IProject pProject, ITicket pTicket)
  {
    server = pServer;
    project = pProject;
    ticket = pTicket;
  }

  @Override
  public void redminePropertyChanged(String pName, Object pOldValue, Object pNewValue)
  {
    Notifier.notifyTicketPropertyChanged(server, ticket, pName, pOldValue, pNewValue);
  }
}
