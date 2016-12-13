package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import org.jetbrains.annotations.NotNull;

/**
 * Listener that does things, when a project is created / deleted
 *
 * @author w.glanzer, 11.12.2016.
 */
public class RServerListener implements IServer.IServerListener
{

  private final IServer server;
  private final INotifier notifier;

  public RServerListener(IServer pServer, @NotNull INotifier pNotifier)
  {
    server = pServer;
    notifier = pNotifier;
  }

  @Override
  public void projectCreated(IProject pCreated, boolean pCreatedDuringPreload)
  {
    // All already loaded tickets can be got here -> listen on it
    pCreated.getTickets().forEach(pTicket -> pTicket.addTicketListener(new RTicketListener(server, pCreated, pTicket, notifier)));
    pCreated.addProjectListener(new RProjectListener(server, pCreated, notifier));

    if(!pCreatedDuringPreload)
      notifier.notifyNewProject(server, pCreated);
  }
}
