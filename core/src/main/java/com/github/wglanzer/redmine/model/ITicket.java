package com.github.wglanzer.redmine.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.EventListener;
import java.util.Map;

/**
 * Represents a single Redmine ticket
 *
 * @author w.glanzer, 05.10.2016.
 */
public interface ITicket
{

  /**
   * Returns the unique ticket id
   *
   * @return unique ticket id (#12345)
   */
  long getID();

  /**
   * Returns the subject of this ticket
   *
   * @return title, not <tt>null</tt>
   */
  @NotNull
  String getSubject();

  /**
   * Returns the description of this ticket
   *
   * @return description, not <tt>null</tt>
   */
  @NotNull
  String getDescription();

  /**
   * Date, when this project was created
   * Precision = seconds
   *
   * @return Date
   */
  Instant getCreatedOn();

  /**
   * Date, when this project was updated.
   * Precision = seconds
   *
   * @return Date
   */
  Instant getUpdatedOn();

  /**
   * Returns the current status of this ticket
   *
   * @return status, not <tt>null</tt>
   */
  @NotNull
  String getStatus();

  /**
   * Returns the author of this ticket
   *
   * @return author, not <tt>null</tt>
   */
  @NotNull
  String getAuthor();

  /**
   * Returns the person, which is assigned to this ticket
   *
   * @return the assigned person, or <tt>null</tt> if it is not assigned
   */
  @Nullable
  String getAssignee();

  /**
   * Returns the priority of this ticket
   *
   * @return priority, not <tt>null</tt>
   */
  @NotNull
  String getPriority();

  /**
   * Returns the tracker of this ticket
   *
   * @return tracker, not <tt>null</tt>
   */
  @NotNull
  String getTracker();

  /**
   * Returns the category of this ticket
   *
   * @return category, not <tt>null</tt>
   */
  @NotNull
  String getCategory();

  /**
   * Returns the additional properties
   *
   * @return Map of the additional properties, not <tt>null</tt>
   */
  @NotNull
  Map<String, String> getAdditionalProperties();

  /**
   * Adds an ITicketListener to this ticket
   *
   * @param pListener Listener to add
   */
  void addWeakTicketListener(@NotNull ITicketListener pListener);

  /**
   * Removes an ITicketListener from this ticket
   *
   * @param pListener Listener to remove
   */
  void removeWeakTicketListener(@NotNull ITicketListener pListener);

  /**
   * Listens on one ITicket
   *
   * @see ITicket
   */
  interface ITicketListener extends EventListener
  {

    String PROP_SUBJECT = "subject";
    String PROP_DESCRIPTION = "description";
    String PROP_CREATEDON = "createdOn";
    String PROP_UPDATEDON = "updatedOn";
    String PROP_STATUS = "status";
    String PROP_AUTHOR = "author";
    String PROP_PRIORITY = "priority";
    String PROP_TRACKER = "tracker";
    String PROP_CATEGORY = "category";
    String PROP_ASSIGNEE = "assignee";

    /**
     * Fires, that redmine properties have changed
     *
     * @param pProperties Properties that were changed
     * @param pOldValue   Array of old values
     * @param pNewValue   Array of new values
     */
    default void redminePropertiesChanged(String[] pProperties, Object[] pOldValue, Object[] pNewValue)
    {
    }

  }
}
