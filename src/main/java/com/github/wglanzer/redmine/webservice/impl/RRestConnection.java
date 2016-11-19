package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.IRLoggingFacade;
import com.github.wglanzer.redmine.webservice.spi.ERRestRequest;
import com.github.wglanzer.redmine.webservice.spi.IRRestConnection;
import com.google.common.base.Strings;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * RestConnection to a redmine server
 *
 * @author w.glanzer, 18.11.2016.
 */
public class RRestConnection implements IRRestConnection
{
  private String url;
  private String apiKey;
  private IRLoggingFacade loggingFacade;

  private AtomicReference<Exception> wasError = new AtomicReference<>(null);

  public RRestConnection(String pUrl, String pAPIKey, @Nullable IRLoggingFacade pLoggingFacade)
  {
    url = pUrl;
    apiKey = pAPIKey;
    loggingFacade = pLoggingFacade;
  }

  @Override
  public Stream<JSONObject> doGET(ERRestRequest pGETRequest)
  {
    JSONObject GETResponse = _doGET(pGETRequest.getSubPage() + ".json").getObject();

    if(pGETRequest.getResultTopLevel() == null)
      return Stream.of(GETResponse);

    JSONArray arrayReturned = GETResponse.getJSONArray(pGETRequest.getResultTopLevel());
    if(arrayReturned != null)
      return StreamSupport.stream(arrayReturned.spliterator(), true)
          .map(pObject -> (JSONObject) pObject);
    return null;
  }

  @Override
  public boolean isValid()
  {
    return loggingFacade == null || wasError.get() == null;
  }

  /**
   * Builds the get-URL and returns the result body as JsonNode
   *
   * @param pPageKey Subpage ("projects", "issues", etc.)
   * @return Result as JsonNode
   */
  private JsonNode _doGET(@NotNull String pPageKey)
  {
    StringBuilder urlBuilder = new StringBuilder();

    // http://myredmineserver.de/
    urlBuilder.append(url);
    if(!url.endsWith("/"))
      urlBuilder.append("/");

    // http://myredmineserver.de/issues.json
    urlBuilder.append(pPageKey);

    // http://myredmineserver.de/issues.json?key=myapikey
    urlBuilder.append("?key=").append(apiKey);

    if(Strings.nullToEmpty(System.getProperty("plugin.redmine.debug")).equals("true"))
      loggingFacade.log(getClass().getName() + ": GET -> " + urlBuilder.toString(), true);

    try
    {
      HttpResponse<JsonNode> response = Unirest.get(urlBuilder.toString()).asJson();
      return response.getBody();
    }
    catch(UnirestException e)
    {
      _dispatchError(e);
      throw new RuntimeException(e);
    }
  }

  /**
   * Handles an exception
   *
   * @param pEx Exception that should be handled
   */
  private void _dispatchError(Exception pEx)
  {
    if(loggingFacade != null)
      loggingFacade.log(pEx, false);
    else
      wasError.set(pEx);
  }
}
