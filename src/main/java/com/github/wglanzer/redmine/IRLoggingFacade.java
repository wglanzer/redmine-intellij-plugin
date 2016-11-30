package com.github.wglanzer.redmine;

import com.intellij.notification.NotificationType;

/**
 * Contains all logging methods needed
 *
 * @author w.glanzer, 19.11.2016.
 */
public interface IRLoggingFacade
{

  /**
   * Logs an exception
   *
   * @param pEx     Exception that should be logged
   */
  void error(Exception pEx);

  /**
   * Logs a string
   *
   * @param pLogString String that should be logged
   * @param pSilent    <tt>true</tt>, if no balloon should be shown
   */
  void log(String pLogString, boolean pSilent);

  /**
   * Logs a string with a specific notification type
   *
   * @param pType      Type that should be used
   * @param pLogString String that should be logged
   * @param pSilent    <tt>true</tt>, if no balloon should be shown
   */
  void log(NotificationType pType, String pLogString, boolean pSilent);

  /**
   * Logs a debug string
   *
   * @param pDebugString String that should be logged as debuglevel
   */
  void debug(String pDebugString);

}
