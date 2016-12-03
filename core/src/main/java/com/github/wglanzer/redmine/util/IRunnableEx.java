package com.github.wglanzer.redmine.util;

/**
 * Represents an Runnable, which can throw an Exception
 *
 * @author w.glanzer, 30.11.2016.
 */
@FunctionalInterface
public interface IRunnableEx extends Runnable
{

  void runEx() throws Exception;

  default void run()
  {
    try
    {
      runEx();
    }
    catch(Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

}
