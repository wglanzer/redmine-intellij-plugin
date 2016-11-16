package com.github.wglanzer.redmine.model;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents a complete Redmine project,
 * with all its tickets, users, etc.
 *
 * @author w.glanzer, 05.10.2016.
 */
public interface IProject
{

  /**
   * All assigned tickets. Opened, closed, ...
   *
   * @return Map, ticketID -> ticket, not <tt>null</tt>
   */
  @NotNull
  Map<String, ITicket> getTickets();

  /**
   * Adds an IProjectListener to this project
   *
   * @param pListener  Listener to add
   */
  void addProjectListener(@NotNull IProjectListener pListener);

  /**
   * Removes an IProjectListener from this project
   *
   * @param pListener  Listener to remove
   */
  void removeProjectListener(@NotNull IProjectListener pListener);

  /**
   * Listens on one IProject
   *
   * @see IProject
   */
  interface IProjectListener
  {
    /**
     * Fires, if one ticket was added
     *
     * @param pTicketAdded Ticket, which was added
     */
    void ticketAdded(@NotNull ITicket pTicketAdded);

    /**
     * Fires, if a ticket was removed
     *
     * @param pTicketRemoved Ticket, which was removed
     */
    void ticketRemoved(@NotNull ITicket pTicketRemoved);
  }

}
