package com.github.wglanzer.redmine;

import java.util.function.Consumer;

/**
 * Interface that is able to create background-tasks
 *
 * @author w.glanzer, 11.12.2016.
 */
public interface IRTaskCreator
{

  /**
   * Executes a given task in background
   *
   * @param pTask Task that should be done in background
   * @return the given task
   */
  <T extends ITask> T executeInBackground(T pTask);

  /**
   * Progress, that can be executed
   */
  interface ITask extends Consumer<IProgressIndicator>
  {
    /**
     * @return the name of the current progress
     */
    String getName();
  }

  /**
   * Indicator-Interface to call back to UI
   */
  interface IProgressIndicator
  {
    /**
     * Adds the given number to percentage
     *
     * @param pPercentage 0-100
     */
    void addPercentage(double pPercentage);
  }

}
