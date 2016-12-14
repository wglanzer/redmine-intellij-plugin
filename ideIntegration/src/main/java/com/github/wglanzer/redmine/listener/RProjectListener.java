package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Listeners, that does things when a ticket was added / changed
 *
 * @author w.glanzer, 11.12.2016.
 */
class RProjectListener extends AbstractNotifiableListener implements IProject.IProjectListener
{

  private final IServer server;
  private final IProject project;

  RProjectListener(IServer pServer, IProject pProject, @NotNull IChangeNotifier pNotifier)
  {
    super(pNotifier);
    server = pServer;
    project = pProject;
  }

  @Override
  public void redminePropertiesChanged(Map<String, Map.Entry<Object, Object>> pProperties)
  {
    HashMap<String, Map.Entry<Object, Object>> copy = new HashMap<>(pProperties);
    if(copy.size() > 1)
      copy.remove(PROP_UPDATEDON);

    getNotifier().notifyProjectPropertyChanged(server, project, copy);
  }

  @Override
  public void ticketAdded(@NotNull ITicket pTicketAdded, boolean pCreatedDuringPreload)
  {
    pTicketAdded.addWeakTicketListener(strongRef(new RTicketListener(server, project, pTicketAdded, getNotifier()))); // New ticket -> listen on it
    if(!pCreatedDuringPreload)
      getNotifier().notifyNewTicket(server, pTicketAdded);
  }

}
