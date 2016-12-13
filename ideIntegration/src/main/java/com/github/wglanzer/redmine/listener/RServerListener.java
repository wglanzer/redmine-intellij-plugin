package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import org.jetbrains.annotations.NotNull;

/**
 * Listener that does things, when a project is created / deleted
 *
 * @author w.glanzer, 11.12.2016.
 */
public class RServerListener extends AbstractNotifiableListener implements IServer.IServerListener
{

  private final IServer server;

  public RServerListener(IServer pServer, @NotNull INotifier pNotifier)
  {
    super(pNotifier);
    server = pServer;
  }

  @Override
  public void projectCreated(IProject pCreated, boolean pCreatedDuringPreload)
  {
    // All already loaded tickets can be got here -> listen on it
    pCreated.getTickets().forEach(pTicket -> pTicket.addWeakTicketListener(strongRef(new RTicketListener(server, pCreated, pTicket, getNotifier()))));
    pCreated.addWeakProjectListener(strongRef(new RProjectListener(server, pCreated, getNotifier())));
    if(!pCreatedDuringPreload)
      getNotifier().notifyNewProject(server, pCreated);
  }
}
