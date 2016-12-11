package com.github.wglanzer.redmine.model;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Collection;

/**
 * Represents a complete Redmine project,
 * with all its tickets, users, etc.
 *
 * @author w.glanzer, 05.10.2016.
 */
public interface IProject
{

  /**
   * Returns the unique redmine-ID for this project
   *
   * @return ID as unique string
   */
  String getID();

  /**
   * Returns the name of this project
   *
   * @return Readable name
   */
  String getName();

  /**
   * Description for this project
   *
   * @return Description as string
   */
  String getDescription();

  /**
   * Date, when this project was created.
   * Precision = seconds
   *
   * @return Date
   */
  Instant getCreatedOn();

  /**
   * Date, when this project was updated
   * Precision = seconds
   *
   * @return Date
   */
  Instant getUpdatedOn();

  /**
   * All assigned tickets. Opened, closed, ...
   *
   * @return Map, ticketID -> ticket, not <tt>null</tt>
   */
  @NotNull
  Collection<ITicket> getTickets();

  /**
   * Returns the valid state of this project instance
   *
   * @return <tt>true</tt> if this project instance is valid
   */
  boolean isValid();

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
     * Fires, if a redmine property was updated (id, name, description, createddate, updateddate, ispublic)
     *
     * @param pName     Name of the changed property
     * @param pOldValue Old value for this property
     * @param pNewValue New value for this property
     */
    void redminePropertyChanged(String pName, Object pOldValue, Object pNewValue);

    /**
     * Fires, if one ticket was added
     *
     * @param pTicketAdded Ticket, which was added
     */
    void ticketAdded(@NotNull ITicket pTicketAdded);

  }

}
