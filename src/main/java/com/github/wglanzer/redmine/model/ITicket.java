package com.github.wglanzer.redmine.model;

import org.jetbrains.annotations.NotNull;

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

}
