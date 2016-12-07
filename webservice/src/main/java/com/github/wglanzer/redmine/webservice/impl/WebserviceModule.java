package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.spi.IRRestConnection;
import com.google.inject.AbstractModule;

/**
 * Guice-Module for Webservice-Bundle
 *
 * @author w.glanzer, 06.12.2016.
 */
public class WebserviceModule extends AbstractModule
{

  @Override
  protected void configure()
  {
    bind(IRRestConnection.class).to(RRestConnection.class);
  }

}
