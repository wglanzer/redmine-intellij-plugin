package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ISource;

/**
 * Change Listener for Server Manager
 *
 * @author w.glanzer, 13.12.2016.
 */
public interface IRServerManagerListener
{

  /**
   * Fired, if a server is about to be disconnected
   *
   * @param pServer Server that will be disconnected
   */
  default void serverWillBeDisconnected(IServer pServer)
  {
  }

  /**
   * Fired, if a server has been disconnected
   *
   * @param pServer Server that has been disconnected
   */
  default void serverDisconnected(IServer pServer)
  {
  }

  /**
   * Fired, if a new IServer-Instance is about to be created
   *
   * @param pSource Sources, for which the server will be created
   */
  default void serverWillBeCreated(ISource pSource)
  {
  }

  /**
   * Fired, if the server was created.
   * Care: It is not connected now!
   *
   * @param pServer Server that has been connected
   */
  default void serverCreated(IServer pServer)
  {
  }
}
