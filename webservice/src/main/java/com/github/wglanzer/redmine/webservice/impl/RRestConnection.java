package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.impl.exceptions.ResultHasErrorException;
import com.github.wglanzer.redmine.webservice.spi.*;
import com.google.common.base.Stopwatch;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * RestConnection to a redmine server
 *
 * @author w.glanzer, 18.11.2016.
 */
class RRestConnection implements IRRestConnection
{
  private static final int _PAGESIZE = 25;
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
    if(!(pGETRequest instanceof RRestRequestImpl))
      throw new UnsupportedOperationException(pGETRequest.getClass() + " not supported as request-type");

    RRestRequestImpl request = (RRestRequestImpl) pGETRequest;
    JSONObject GETResponse = _doGET(pGETRequest, request.getSubPage() + ".json", request.getArguments()).getObject();
    JSONArray result;

    if(request.getResultTopLevel() == null)
      result = new JSONArray(Collections.singleton(GETResponse));
    else
      result = GETResponse.getJSONArray(request.getResultTopLevel());

    return new JSONRRestResult(request, result);
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
  private JsonNode _doGET(@NotNull IRRestRequest pRequest, @NotNull String pPageKey, @Nullable ArrayList<IRRestArgument> pAdditionalArguments) throws Exception
  {
    StringBuilder urlBuilder = new StringBuilder();

    // http://myredmineserver.de/
    urlBuilder.append(url);
    if(!url.endsWith("/"))
      urlBuilder.append("/");

    // http://myredmineserver.de/issues.json
    urlBuilder.append(pPageKey);

    // http://myredmineserver.de/issues.json?key=myapikey
    ArrayList<IRRestArgument> arguments = pAdditionalArguments != null ? new ArrayList<>(pAdditionalArguments) : new ArrayList<>();
    arguments.add(IRRestArgument.API_KEY.value(apiKey));

    JsonNode response;
    if(pRequest.isPageable())
      response = _executePaged(pRequest, urlBuilder.toString(), arguments);
    else
      response = _executePlain(urlBuilder.toString(), arguments);

    if(_isException(response))
      throw new ResultHasErrorException(response, urlBuilder.toString());

    return response;
  }

  /**
   * Executes the REST-URL, does pageing
   *
   * @param pURL       URL that should be executed
   * @param pArguments Additional arguments, not <tt>null</tt>
   * @return result, not wrapped
   * @throws Exception if an error occured
   */
  private JsonNode _executePaged(IRRestRequest pRequest, @NotNull String pURL, @NotNull ArrayList<IRRestArgument> pArguments) throws Exception
  {
    try
    {
      int currentOffset = 0;
      int total_count = _PAGESIZE;

      // Add pagelimit and sort, always the same during request
      pArguments.add(IRRestArgument.PAGE_LIMIT.value(String.valueOf(_PAGESIZE)));
      pArguments.add(IRRestArgument.SORT.value(":desc"));
      ArrayList<JsonNode> results = new ArrayList<>();

      while(total_count >= currentOffset + _PAGESIZE)
      {
        ArrayList<IRRestArgument> currentArguments = new ArrayList<>(pArguments);
        currentArguments.add(IRRestArgument.PAGE_OFFSET.value(String.valueOf(currentOffset)));
        JsonNode response = _executePlain(pURL, currentArguments);
        JSONObject object = response.getObject();
        currentOffset = object.getInt(IRRestArgument.PAGE_OFFSET.getName()) + _PAGESIZE;
        total_count = object.getInt(IRRestArgument.PAGE_TOTALCOUNT.getName());
        results.add(response);
      }

      // Merge to one jsonnode
      return _merge(results, ((RRestRequestImpl) pRequest).getResultTopLevel());
    }
    catch(Exception e)
    {
      throw new Exception("Error executing request (url: " + pURL + ")", e);
    }
  }

  /**
   * Executes the REST-URL, NO pageing
   *
   * @param pURL       URL that should be executed
   * @param pArguments Additional arguments, not <tt>null</tt>
   * @return result, not wrapped
   * @throws Exception if an error occured
   */
  private JsonNode _executePlain(@NotNull String pURL, @NotNull ArrayList<IRRestArgument> pArguments) throws Exception
  {
    StringBuilder urlBuilder = new StringBuilder(pURL);
    ArrayList<IRRestArgument> arguments = new ArrayList<>(pArguments);

    // Add arguments to urlBuilder
    if(arguments.size() > 0)
    {
      IRRestArgument firstArg = arguments.get(0);
      urlBuilder.append("?").append(firstArg.getName()).append("=").append(firstArg.getValue());
     arguments.remove(0);
    }
    arguments.forEach((pArgument) -> urlBuilder.append("&").append(pArgument.getName()).append("=").append(pArgument.getValue()));

    // Log output
    loggingFacade.debug(getClass().getSimpleName() + ": GET -> " + urlBuilder.toString());

    Stopwatch watch = Stopwatch.createStarted();
    JsonNode result = Unirest.get(urlBuilder.toString()).asJson().getBody();
    watch.stop();

    // Log output + time
    loggingFacade.debug(getClass().getSimpleName() + ": GET -> " + urlBuilder.toString() + " -> took " + watch.elapsed(TimeUnit.MILLISECONDS) + " ms");
    return result;
  }

  /**
   * Merges a lot of JsonNodes to one JsonNode
   *
   * @param pNodes          Nodes that should be merged
   * @param pTopLevelResult TLR
   * @return a Node containing all other nodes content
   */
  @NotNull
  private JsonNode _merge(List<JsonNode> pNodes, String pTopLevelResult)
  {
    JsonNode resultNode = new JsonNode(null);
    JSONObject resultObject = resultNode.getObject();
    JSONArray resultTopLevelArray = new JSONArray();
    resultObject.put(pTopLevelResult, resultTopLevelArray);
    pNodes.stream()
        .map(JsonNode::getObject)
        .map(pJSONObject -> pJSONObject.getJSONArray(pTopLevelResult))
        .forEach(pTopLevelArray -> {
          for(Object entry : pTopLevelArray)
            resultTopLevelArray.put(entry);
        });
    return resultNode;
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
