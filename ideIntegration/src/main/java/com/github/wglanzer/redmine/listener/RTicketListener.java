package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Listener on each ticket.
 *
 * @author w.glanzer, 11.12.2016.
 */
class RTicketListener extends AbstractNotifiableListener implements ITicket.ITicketListener
{

  private final IServer server;
  private final IProject project;
  private final ITicket ticket;

  RTicketListener(IServer pServer, IProject pProject, ITicket pTicket, @NotNull IChangeNotifier pNotifier)
  {
    super(pNotifier);
    server = pServer;
    project = pProject;
    ticket = pTicket;
  }

  @Override
  public void redminePropertiesChanged(Map<String, Map.Entry<Object, Object>> pProperties)
  {
    HashMap<String, Map.Entry<Object, Object>> copy = new HashMap<>(pProperties);
    if(copy.size() > 1)
      copy.remove(PROP_UPDATEDON);

    getNotifier().notifyTicketPropertyChanged(server, ticket, copy);
  }
}
