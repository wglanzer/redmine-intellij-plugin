package com.github.wglanzer.redmine.model.impl.cache;

import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a ticket cache, which can
 * store tickets permanently
 *
 * @author w.glanzer, 27.11.2016.
 */
public interface ITicketCache
{

  /**
   * Puts a ticket to this cache
   *
   * @param pTicket Ticket which should be stored.
   *                It does not have to be serializable
   */
  void put(ITicket pTicket);

  /**
   * Removes a ticket from this cache
   *
   * @param pID ID of a ticket stored in this cache
   */
  void remove(Long pID);

  /**
   * Returns a ticket stored in this cache
   *
   * @param pID ID of the searched ticket
   * @return ticket, or <tt>null</tt> if nothing is found
   */
  @Nullable
  ITicket get(Long pID);

  /**
   * Returns all tickets stored in this cache.
   * Important: It can be very slow!
   *
   * @return a list of all tickets
   */
  @NotNull
  List<ITicket> getAllTickets();

  /**
   * Returns the last modified ticket
   *
   * @return the last modified ticket, or <tt>null</tt> if no ticket was registered
   */
  @Nullable
  ITicket getLastUpdatedTicket();

  /**
   * Destroys this ticket cache completely!
   */
  void destroy();
}
