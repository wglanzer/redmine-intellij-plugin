package com.github.wglanzer.redmine.model;

import com.github.wglanzer.redmine.exceptions.NotConnectedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EventListener;
import java.util.List;

/**
 * Represents one redmine server
 *
 * @author w.glanzer, 16.11.2016.
 */
public interface IServer
{

  /**
   * Returns an unique identifier for this server
   *
   * @return ID, not <tt>null</tt>
   */
  @NotNull
  String getID();

  /**
   * Connects the server to its destination
   */
  void connectAsync();

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
   * Displayable name representing this server
   *
   * @return displayable name, or <tt>null</tt> if no name is specified
   */
  @Nullable
  String getDisplayName();

  /**
   * Returns a list containing all available watches
   *
   * @return a list of all watches
   */
  @NotNull
  List<IWatch> getWatches();

  /**
   * Adds an IServerListener
   *
   * @param pListener Listener that should be added
   */
  void addWeakServerListener(IServerListener pListener);

  /**
   * Removes an IServerListener
   *
   * @param pListener Listener that should be removed
   */
  void removeWeakServerListener(IServerListener pListener);

  /**
   * Listener for server events
   */
  interface IServerListener extends EventListener
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
     * @param pCreated              New created project
     * @param pCreatedDuringPreload <tt>true</tt>, if this project was created during preload-phase
     */
    default void projectCreated(IProject pCreated, boolean pCreatedDuringPreload)
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

    /**
     * Fires, when the busy-State changes
     * (busy = is some work to do)
     *
     * @param pIsBusy  <tt>true</tt> if some work will be done now
     */
    default void busyStateChanged(boolean pIsBusy)
    {
    }
  }

}
