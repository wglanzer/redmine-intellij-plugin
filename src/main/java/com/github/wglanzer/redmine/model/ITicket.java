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
   * Returns the title of this ticket
   *
   * @return title, not <tt>null</tt>
   */
  @NotNull
  String getTitle();

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
   * Returns the additional properties
   *
   * @return Map of the additional properties, not <tt>null</tt>
   */
  @NotNull
  Map<String, String> getAdditionalProperties();

}
