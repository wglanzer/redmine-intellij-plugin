package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;

/**
 * Notifies the user that something has changed in redmine
 *
 * @author w.glanzer, 13.12.2016.
 */
public interface INotifier
{
  /**
   * Notifies the user, that a ticket was added
   *
   * @param pServer Server
   * @param pTicket Added ticket
   */
  void notifyNewTicket(@NotNull IServer pServer, @NotNull ITicket pTicket);

  /**
   * Notifies the user, that a ticket was changed
   *
   * @param pServer          Server
   * @param pTicket          Added ticket
   * @param pChangedProperty Property that has changed
   * @param pOldValue        Old value for the given property
   * @param pNewValue        New value
   */
  void notifyTicketPropertyChanged(@NotNull IServer pServer, @NotNull ITicket pTicket, String pChangedProperty, Object pOldValue, Object pNewValue);

  /**
   * Notifies the user, that a project was added
   *
   * @param pServer  Server
   * @param pProject Added project
   */
  void notifyNewProject(@NotNull IServer pServer, @NotNull IProject pProject);

  /**
   * Notifies the user, that a project was changed
   *
   * @param pServer          Server
   * @param pProject         Added project
   * @param pChangedProperty Property that has changed
   * @param pOldValue        Old value for the given property
   * @param pNewValue        New value
   */
  void notifyProjectPropertyChanged(@NotNull IServer pServer, @NotNull IProject pProject, String pChangedProperty, Object pOldValue, Object pNewValue);
}
