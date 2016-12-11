package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.model.ITicket;
import com.github.wglanzer.redmine.util.IntelliJIDEAUtility;
import com.intellij.notification.NotificationType;

/**
 * Listener on each ticket.
 *
 * @author w.glanzer, 11.12.2016.
 */
public class RTicketListener implements ITicket.ITicketListener
{

  private final ITicket ticket;

  public RTicketListener(ITicket pTicket)
  {
    ticket = pTicket;
  }

  @Override
  public void redminePropertyChanged(String pName, Object pOldValue, Object pNewValue)
  {
    IntelliJIDEAUtility.showMessage("Ticket property change", "name: " + pName + ", newValue: " + pNewValue, NotificationType.INFORMATION, false);
  }
}
