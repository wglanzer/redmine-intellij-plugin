package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Listener on each ticket.
 *
 * @author w.glanzer, 11.12.2016.
 */
public class RTicketListener extends AbstractNotifiableListener implements ITicket.ITicketListener
{

  private final IServer server;
  private final IProject project;
  private final ITicket ticket;

  public RTicketListener(IServer pServer, IProject pProject, ITicket pTicket, @NotNull INotifier pNotifier)
  {
    super(pNotifier);
    server = pServer;
    project = pProject;
    ticket = pTicket;
  }

  @Override
  public void redminePropertiesChanged(String[] pProperties, Object[] pOldValue, Object[] pNewValue)
  {
    for(int i = 0; i < pProperties.length; i++)
    {
      String propName = pProperties[i];

      // Only notify about "updated_on" when it was the only property that has changed
      if(!Objects.equals(propName, PROP_UPDATEDON) || pProperties.length == 1)
        getNotifier().notifyTicketPropertyChanged(server, ticket, propName, pOldValue[i], pNewValue[i]);
    }
  }
}
