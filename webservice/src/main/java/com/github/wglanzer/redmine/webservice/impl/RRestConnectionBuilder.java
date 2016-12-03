package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.spi.IRRestConnection;
import com.github.wglanzer.redmine.webservice.spi.IRRestLoggingFacade;

/**
 * Builds RRestConnections
 *
 * @author w.glanzer, 03.12.2016.
 */
public class RRestConnectionBuilder
{

  public static IRRestConnection createConnection(String pURL, String pAPIKey, IRRestLoggingFacade pLoggingFacade)
  {
    return new RRestConnection(pURL, pAPIKey, pLoggingFacade);
  }

}
