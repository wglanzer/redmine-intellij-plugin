package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.IRLoggingFacade;
import com.github.wglanzer.redmine.webservice.spi.IRRestConnection;
import com.github.wglanzer.redmine.webservice.spi.IRRestRequest;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;
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
  public Stream<JSONObject> doGET(IRRestRequest pGETRequest)
  {
    JSONObject GETResponse = _doGET(pGETRequest.getSubPage() + ".json", pGETRequest.getArguments()).getObject();

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
  private JsonNode _doGET(@NotNull String pPageKey, @Nullable Map<String, String> pAdditionalArguments)
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

    // http://myredmineserver.de/issues.json?key=myapikey&[ARGUMENT]=[VALUE]
    if(pAdditionalArguments != null)
      pAdditionalArguments.forEach((pKey, pValue) -> urlBuilder.append("&").append(pKey).append("=").append(pValue));

    if(Strings.nullToEmpty(System.getProperty("plugin.redmine.debug")).equals("true"))
      loggingFacade.debug(getClass().getSimpleName() + ": GET -> " + urlBuilder.toString());

    try
    {
      Stopwatch watch = Stopwatch.createStarted();
      HttpResponse<JsonNode> response = Unirest.get(urlBuilder.toString()).asJson();
      watch.stop();

      if(Strings.nullToEmpty(System.getProperty("plugin.redmine.debug")).equals("true"))
        loggingFacade.debug(getClass().getSimpleName() + ": GET -> " + urlBuilder.toString() + " -> took " + watch.elapsed(TimeUnit.MILLISECONDS) + " ms");

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
