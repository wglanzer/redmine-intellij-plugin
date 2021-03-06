package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.webservice.spi.IRRestLoggingFacade;

/**
 * Contains all logging methods needed
 *
 * @author w.glanzer, 19.11.2016.
 */
public interface IRLoggingFacade extends IRRestLoggingFacade
{

  /**
   * Logs an exception
   *
   * @param pEx Exception that should be logged
   */
  void error(Exception pEx);

  /**
   * Logs an exception
   *
   * @param pMessage Message that should be logged
   * @param pCause   Exception that caused this message
   */
  void error(String pMessage, Exception pCause);

  /**
   * Logs a debug string
   *
   * @param pDebugString String that should be logged as debuglevel
   */
  void debug(String pDebugString);

}
