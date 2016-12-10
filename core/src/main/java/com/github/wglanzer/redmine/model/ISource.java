package com.github.wglanzer.redmine.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Describes a source for a server to connect.
 * It contains all information that a server
 * needs to connect to a specific redmine-server
 *
 * @author w.glanzer, 06.10.2016.
 */
public interface ISource
{

  /**
   * URL as string, not <tt>null</tt>
   * For example: "https://redmine.mydomain.com"
   *
   * @return URL as string, not <tt>null</tt>
   */
  @NotNull
  String getURL();

  /**
   * Returns the API-Key to login on redmine
   *
   * @return API-Key for using redmine with authentication
   */
  @NotNull
  String getAPIKey();

  /**
   * Returns the polling interval in seconds
   *
   * @return polling interval in seconds
   */
  int getPollInterval();

  /**
   * Returns the wished element-count per GET-Request-Page
   *
   * @return page (<=100) or <tt>null</tt> if no specific size should be set
   */
  @Nullable
  Integer getPageSize();

}
