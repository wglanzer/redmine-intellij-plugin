package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.spi.IRRestConnection;
import com.github.wglanzer.redmine.webservice.spi.IRRestLoggingFacade;
import org.jetbrains.annotations.NotNull;

/**
 * Builds RRestConnections
 *
 * @author w.glanzer, 03.12.2016.
 */
public class RRestConnectionBuilder
{

  public static IRRestConnection createConnection(@NotNull IRRestLoggingFacade pLoggingFacade, String pURL, String pAPIKey, Integer pPageSize, boolean pVerifySSL)
  {
    return new RRestConnection(pLoggingFacade, pURL, pAPIKey, pPageSize, pVerifySSL);
  }

}
