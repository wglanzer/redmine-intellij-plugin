package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.notifiers.INotifier;

/**
 * @author w.glanzer, 27.11.2016.
 */
class RLoggingFacadeImpl implements IRLoggingFacade
{

  private static final boolean _IS_DEBUG = System.getProperty("plugin.redmine.debug") != null;
  private final INotifier notifier;

  public RLoggingFacadeImpl(INotifier pNotifier)
  {
    notifier = pNotifier;
  }

  @Override
  public void error(Exception pEx)
  {
    error("Exception", pEx);
  }

  @Override
  public void error(String pMessage, Exception pCause)
  {
    notifier.error(pMessage, pCause);
    pCause.printStackTrace(); // Print it on console, better than nothing
  }

  @Override
  public void debug(String pDebugString)
  {
    // No debug-outputs, when debug is not enabled (Live-System)
    if(!_IS_DEBUG)
      return;
    notifier.notify("DEBUG", pDebugString);
  }

}
