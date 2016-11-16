package com.github.wglanzer.redmine.exceptions;

/**
 * Exception thrown, if a server should do
 * something but it is not yet connected to its destination
 *
 * @author w.glanzer, 16.11.2016.
 */
public class NotConnectedException extends Exception
{
  public NotConnectedException()
  {
  }

  public NotConnectedException(String message)
  {
    super(message);
  }

  public NotConnectedException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public NotConnectedException(Throwable cause)
  {
    super(cause);
  }
}
