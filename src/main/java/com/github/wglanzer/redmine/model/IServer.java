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
  void connect() throws Exception;

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

  /**
   * Gets the url from the connection
   *
   * @return configured url
   */
  @NotNull
  String getURL();

  /**
   * Adds an IServerListener
   *
   * @param pListener Listener that should be added
   */
  void addServerListener(IServerListener pListener);

  /**
   * Removes an IServerListener
   *
   * @param pListener Listener that should be removed
   */
  void removeServerListener(IServerListener pListener);

  /**
   * Listener for server events
   */
  interface IServerListener
  {
    /**
     * Fired, when the status of the connection changed
     *
     * @param pIsConnectedNow <tt>true</tt>, if the server is running now
     */
    default void connectionStatusChanged(boolean pIsConnectedNow)
    {
    }

    /**
     * Fired, when a project was created
     *
     * @param pCreated New created project
     */
    default void projectCreated(IProject pCreated)
    {
    }

    /**
     * Fired, when a project was removed
     *
     * @param pRemoved Removed project
     */
    default void projectRemoved(IProject pRemoved)
    {
    }
  }

}
