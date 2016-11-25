package com.github.wglanzer.redmine.model.impl.server;

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
  private final Runnable pollRunnable;
  private final int interval;

  public PollingExecutor(Runnable pPollRunnable, int pInterval)
  {
    pollRunnable = pPollRunnable;
    interval = pInterval;
  }

  /**
   * Starts the task
   */
  public void start()
  {
    executorService.scheduleAtFixedRate(pollRunnable, interval, interval, TimeUnit.SECONDS);
  }

  /**
   * Ends the task
   */
  public void stop()
  {
    executorService.shutdownNow();
  }

}