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
public class RProjectListener extends AbstractNotifiableListener implements IProject.IProjectListener
{

  private final IServer server;
  private final IProject project;

  public RProjectListener(IServer pServer, IProject pProject, @NotNull INotifier pNotifier)
  {
    super(pNotifier);
    server = pServer;
    project = pProject;
  }

  @Override
  public void redminePropertiesChanged(String[] pProperties, Object[] pOldValue, Object[] pNewValue)
  {
    for(int i = 0; i < pProperties.length; i++)
      getNotifier().notifyProjectPropertyChanged(server, project, pProperties[i], pOldValue[i], pNewValue[i]);
  }

  @Override
  public void ticketAdded(@NotNull ITicket pTicketAdded, boolean pCreatedDuringPreload)
  {
    pTicketAdded.addWeakTicketListener(strongRef(new RTicketListener(server, project, pTicketAdded, getNotifier()))); // New ticket -> listen on it
    if(!pCreatedDuringPreload)
      getNotifier().notifyNewTicket(server, pTicketAdded);
  }

}
