package com.github.wglanzer.redmine.listener;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Notifies the user that something has changed in redmine
 *
 * @author w.glanzer, 13.12.2016.
 */
public interface IChangeNotifier
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
   * @param pServer     Server
   * @param pTicket     Added ticket
   * @param pProperties Property that has changed
   */
  void notifyTicketPropertyChanged(@NotNull IServer pServer, @NotNull ITicket pTicket, @NotNull Map<String, Map.Entry<Object, Object>> pProperties);

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
   * @param pServer     Server
   * @param pProject    Added project
   * @param pProperties Property that has changed
   */
  void notifyProjectPropertyChanged(@NotNull IServer pServer, @NotNull IProject pProject, @NotNull Map<String, Map.Entry<Object, Object>> pProperties);
}
