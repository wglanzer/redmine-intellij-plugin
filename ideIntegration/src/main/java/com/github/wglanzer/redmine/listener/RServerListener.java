package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;

/**
 * Listener that does things, when a project is created / deleted
 *
 * @author w.glanzer, 11.12.2016.
 */
public class RServerListener implements IServer.IServerListener
{

  private final IServer server;

  public RServerListener(IServer pServer)
  {
    server = pServer;
  }

  @Override
  public void projectCreated(IProject pCreated, boolean pCreatedDuringPreload)
  {
    if(pCreatedDuringPreload)
    {
      pCreated.addProjectListener(new RProjectListener(pCreated));

      // All already loaded tickets can be got here -> listen on it
      pCreated.getTickets().forEach(pTicket -> pTicket.addTicketListener(new RTicketListener(pTicket)));
    }
  }
}
