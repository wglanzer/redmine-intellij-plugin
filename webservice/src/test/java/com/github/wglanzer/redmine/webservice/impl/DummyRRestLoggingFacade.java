package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.spi.IRRestLoggingFacade;

/**
 * RRestLoggingFacade for JUnit-Tests
 *
 * @author w.glanzer, 03.02.2017.
 */
public class DummyRRestLoggingFacade implements IRRestLoggingFacade
{
  @Override
  public void debug(String pDebugString)
  {
    System.out.println(pDebugString);
  }
}
