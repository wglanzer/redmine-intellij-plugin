package com.github.wglanzer.redmine.model;

import com.github.wglanzer.redmine.exceptions.NotConnectedException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Represents one redmine server
 *
 * @author w.glanzer, 16.11.2016.
 */
public interface IServer
{

  /**
   * Connects the server to its destination
   */
  void connect();

  /**
   * Disconnects the server
   */
  void disconnect();

  /**
   * <tt>true</tt>, if the serverconnection is healthy (up and running)
   *
   * @return <tt>true</tt> if healthy, <tt>false</tt> otherwise
   */
  boolean isValid();

  /**
   * Returns all projects this server contains
   *
   * @return all projects, not <tt>null</tt>
   * @throws NotConnectedException if the server is not connected yet
   */
  @NotNull
  Collection<IProject> getProjects();

}
