package com.github.wglanzer.redmine.webservice.spi;

/**
 * @author w.glanzer, 03.12.2016.
 */
public interface IRRestLoggingFacade
{

  /**
   * Logs a debug string
   *
   * @param pDebugString String that should be logged as debuglevel
   */
  void debug(String pDebugString);

}
