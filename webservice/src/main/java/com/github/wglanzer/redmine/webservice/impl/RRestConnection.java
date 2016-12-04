package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.impl.exceptions.ResultHasErrorException;
import com.github.wglanzer.redmine.webservice.spi.*;
import com.google.common.base.Stopwatch;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * RestConnection to a redmine server
 *
 * @author w.glanzer, 18.11.2016.
 */
class RRestConnection implements IRRestConnection
{
  private String url;
  private String apiKey;
  private IRRestLoggingFacade loggingFacade;

  private AtomicReference<Exception> wasError = new AtomicReference<>(null); //todo does nothing, remove?

  public RRestConnection(String pUrl, String pAPIKey, @Nullable IRRestLoggingFacade pLoggingFacade)
  {
    url = pUrl;
    apiKey = pAPIKey;
    loggingFacade = pLoggingFacade;
  }

  @NotNull
  @Override
  public IRRestResult doGET(IRRestRequest pGETRequest) throws Exception
  {
    JSONObject GETResponse = _doGET(pGETRequest.getSubPage() + ".json", pGETRequest.getArguments()).getObject();
    JSONArray result;

    if(pGETRequest.getResultTopLevel() == null)
      result = new JSONArray(Collections.singleton(GETResponse));
    else
      result = GETResponse.getJSONArray(pGETRequest.getResultTopLevel());

    return new JSONRRestResult(pGETRequest, result);
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
  @NotNull
  private JsonNode _doGET(@NotNull String pPageKey, @Nullable ArrayList<IRRestArgument> pAdditionalArguments) throws Exception
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
      pAdditionalArguments.forEach((pArgument) -> urlBuilder.append("&").append(pArgument.getName()).append("=").append(pArgument.getValue()));

    loggingFacade.debug(getClass().getSimpleName() + ": GET -> " + urlBuilder.toString());

    Stopwatch watch = Stopwatch.createStarted();
    HttpResponse<JsonNode> response = Unirest.get(urlBuilder.toString()).asJson();
    watch.stop();

    loggingFacade.debug(getClass().getSimpleName() + ": GET -> " + urlBuilder.toString() + " -> took " + watch.elapsed(TimeUnit.MILLISECONDS) + " ms");

    JsonNode result = response.getBody();
    if(_isException(result))
      throw new ResultHasErrorException(result, urlBuilder.toString());

    return result;
  }

  /**
   * Returns true, if the result is an exception
   *
   * @param pNode Result
   * @return <tt>true</tt>, if the result is an exception
   */
  private boolean _isException(JsonNode pNode)
  {
    JSONObject obj = pNode.getObject();
    return obj.has("errors");
  }

}
