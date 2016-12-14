package com.github.wglanzer.redmine.notifiers;

/**
 * Notifies something inside IntelliJ
 *
 * @author w.glanzer, 14.12.2016.
 */
public interface INotifier
{

  /**
   * Shows a string as notify-dialog
   *
   * @param pTitle   Title of this dialog
   * @param pMessage Message to be shown
   */
  void notify(String pTitle, String pMessage);

  /**
   * Logs an exception
   *
   * @param pMessage   Message that should be logged
   * @param pException Exception that caused this message
   */
  void error(String pMessage, Exception pException);

}
