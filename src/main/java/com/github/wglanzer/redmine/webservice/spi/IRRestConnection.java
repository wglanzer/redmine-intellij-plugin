package com.github.wglanzer.redmine.webservice.spi;

import org.json.JSONObject;

import java.util.stream.Stream;

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
   * @return JSON-Result as Stream
   */
  Stream<JSONObject> doGET(ERRestRequest pGETRequest);

  /**
   * Returns true, if the connection can be used
   *
   * @return <tt>true</tt>, if it's all ok
   */
  boolean isValid();

}
