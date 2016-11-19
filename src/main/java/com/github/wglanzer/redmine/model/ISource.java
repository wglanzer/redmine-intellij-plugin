package com.github.wglanzer.redmine.model;

import org.jetbrains.annotations.NotNull;

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

}