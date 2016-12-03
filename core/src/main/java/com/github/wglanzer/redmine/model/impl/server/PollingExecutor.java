package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.IRLoggingFacade;
import com.github.wglanzer.redmine.util.IRunnableEx;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Executes the polling-task given in constructor
 *
 * @author w.glanzer, 19.11.2016.
 */
class PollingExecutor
{

  private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
  private final IRLoggingFacade loggingFacade;
  private final IRunnableEx pollRunnable;
  private final int interval;

  public PollingExecutor(IRLoggingFacade pLoggingFacade, IRunnableEx pPollRunnable, int pInterval)
  {
    loggingFacade = pLoggingFacade;
    pollRunnable = pPollRunnable;
    interval = pInterval;
  }

  /**
   * Starts the task
   */
  public void start()
  {
    executorService.scheduleAtFixedRate(() -> {
      try
      {
        pollRunnable.runEx();
      }
      catch(Exception e)
      {
        loggingFacade.error(e);
      }
    }, interval, interval, TimeUnit.SECONDS);
  }

  /**
   * Ends the task
   */
  public void stop()
  {
    executorService.shutdownNow();
  }

}
