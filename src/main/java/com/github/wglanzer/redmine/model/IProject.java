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

}
