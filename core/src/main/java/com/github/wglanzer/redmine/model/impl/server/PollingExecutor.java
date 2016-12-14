package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.IRLoggingFacade;
import com.github.wglanzer.redmine.IRTaskCreator;
import com.github.wglanzer.redmine.util.IRunnableEx;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Executes the polling-task given in constructor
 *
 * @author w.glanzer, 19.11.2016.
 */
class PollingExecutor
{
  private final IRLoggingFacade loggingFacade;
  private final IRTaskCreator taskCreator;
  private final IRunnableEx pollRunnable;
  private final int interval;
  private final boolean showAsBackgroundTask;
  private final AtomicBoolean interruped = new AtomicBoolean(false);

  public PollingExecutor(IRLoggingFacade pLoggingFacade, IRTaskCreator pTaskCreator, IRunnableEx pPollRunnable, int pInterval, boolean pShowAsBackgroundTask)
  {
    loggingFacade = pLoggingFacade;
    taskCreator = pTaskCreator;
    pollRunnable = pPollRunnable;
    interval = pInterval;
    showAsBackgroundTask = pShowAsBackgroundTask;
  }

  /**
   * Starts the task
   */
  public void start()
  {
    Executors.newSingleThreadExecutor().execute(() ->
    {
      try
      {
        Thread.currentThread().setName("PollingExecutor-Thread (background: " + showAsBackgroundTask + ")");

        while(!interruped.get())
        {
          _Task task = new _Task(pollRunnable);

          try
          {
            if(showAsBackgroundTask)
              taskCreator.executeInBackground(task).waitUntilFinished();
            else
              task.accept(new _DummyIndicator());
          }
          catch(Throwable ex)
          {
            loggingFacade.error("Error executing task", new Exception(ex));
          }

          if(!interruped.get())
          {
            try
            {
              Thread.sleep(interval * 1000);
            }
            catch(Exception ignored)
            {
            }
          }
        }
      }
      catch(Exception e)
      {
        loggingFacade.error(e);
      }
    });
  }

  /**
   * Ends the task
   */
  public void stop()
  {
    interruped.set(true);
  }

  /**
   * Dummy for progressIndicator
   */
  private static class _DummyIndicator implements IRTaskCreator.IProgressIndicator
  {
    @Override
    public void addPercentage(double pPercentage)
    {
      // dont do anything, because you are a dummy!
    }
  }

  /**
   * Polling-Tickets runnable
   */
  private static class _Task implements IRTaskCreator.ITask
  {
    private final IRunnableEx pollRunnable;
    private final Object _LOCK = new Object();

    public _Task(IRunnableEx pRunnableEx)
    {
      pollRunnable = pRunnableEx;
    }

    @Override
    public String getName()
    {
      return "polling tickets...";
    }

    @Override
    public void accept(IRTaskCreator.IProgressIndicator pIProgressIndicator)
    {
      pollRunnable.run();

      synchronized(_LOCK)
      {
        _LOCK.notifyAll();
      }
    }

    public void waitUntilFinished()
    {
      synchronized(_LOCK)
      {
        try
        {
          _LOCK.wait();
        }
        catch(Exception ignored)
        {
        }
      }
    }
  }

}
