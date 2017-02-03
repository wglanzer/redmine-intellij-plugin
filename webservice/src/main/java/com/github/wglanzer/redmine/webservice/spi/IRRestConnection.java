package com.github.wglanzer.redmine.webservice.spi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author w.glanzer, 18.11.2016.
 */
public interface IRRestConnection
{

  /**
   * Performs a <b>not cached</b> GET-Request
   * to the specified redmine server
   *
   * @param pGETRequest Request that should be done
   * @return Result, wrapped in IRRestResult, <tt>null</tt> if the request was cancelled
   */
  @NotNull
  IRRestResult doGET(IRRestRequest pGETRequest) throws Exception;

  /**
   * Performs a <b>not cached</b> GET-Request
   * to the specified redmine server
   *
   * @param pGETRequest        Request that should be done
   * @param pProgressIndicator Progress Indicator to show progress
   * @return Result, wrapped in IRRestResult, <tt>null</tt> if the request was cancelled
   */
  @Nullable
  IRRestResult doGET(IRRestRequest pGETRequest, IRRestProgressIndicator pProgressIndicator) throws Exception;

  /**
   * Returns true, if the connection can be used
   *
   * @return <tt>true</tt>, if it's all ok
   */
  boolean isValid();

}
