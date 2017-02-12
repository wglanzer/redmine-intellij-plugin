package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.impl.exceptions.ResultHasErrorException;
import com.github.wglanzer.redmine.webservice.spi.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.ssl.SSLContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * RestConnection to a redmine server
 *
 * @author w.glanzer, 18.11.2016.
 */
class RRestConnection implements IRRestConnection
{
  private static final int _DEFAULT_PAGESIZE = 25;
  private static final int _MAX_PAGESIZE = 100; // Maximum elements are limited to 100 per page (Redmine-REST-API)

  static
  {
    try
    {
      // Set Custom SSL Factory to allow only specific hostnames permanently
      Unirest.setAsyncHttpClient(HttpAsyncClients.custom()
          .setSSLHostnameVerifier(RHostnameVerifier.getInstance())
          .setSSLContext(SSLContexts.custom()
              .loadTrustMaterial(null, RHostnameVerifier.getInstance())
              .build())
          .build());
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  private final int pagesize;
  private final String url;
  private final String apiKey;
  private final IRRestLoggingFacade loggingFacade;
  private final ExecutorService executor = Executors.newFixedThreadPool(8); //todo why 8?
  private final AtomicReference<Exception> wasError = new AtomicReference<>(null); //todo does nothing, remove?

  public RRestConnection(@NotNull IRRestLoggingFacade pLoggingFacade, String pUrl, String pAPIKey, Integer pPageSize)
  {
    url = pUrl;
    apiKey = pAPIKey;
    loggingFacade = pLoggingFacade;
    pagesize = pPageSize == null ? _DEFAULT_PAGESIZE : (pPageSize > _MAX_PAGESIZE ? _MAX_PAGESIZE : pPageSize);
  }

  @NotNull
  @Override
  public IRRestResult doGET(IRRestRequest pGETRequest) throws Exception
  {
    IRRestResult result = doGET(pGETRequest, new _AlwaysAliveIndicator());
    assert result != null; // DummyIndicator -> alive -> true
    return result;
  }

  @Nullable
  @Override
  public IRRestResult doGET(IRRestRequest pGETRequest, @NotNull IRRestProgressIndicator pProgressIndicator) throws Exception
  {
    if(!(pGETRequest instanceof RRestRequestImpl))
      throw new UnsupportedOperationException(pGETRequest.getClass() + " not supported as request-type");

    RRestRequestImpl request = (RRestRequestImpl) pGETRequest;
    JsonNode node = _doGET(pGETRequest, pProgressIndicator, request.getSubPage() + ".json", request.getArguments());
    if(node == null) // cancelled request
      return null;

    JSONObject GETResponse = node.getObject();
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
  @Nullable
  private JsonNode _doGET(@NotNull IRRestRequest pRequest, @NotNull IRRestProgressIndicator pProgressIndicator, @NotNull String pPageKey, @Nullable ArrayList<IRRestArgument> pAdditionalArguments) throws Exception
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
      response = _executePaged(pRequest, urlBuilder.toString(), pProgressIndicator, arguments);
    else
    {
      try
      {
        HttpResponse<JsonNode> resp = _executePlain(urlBuilder.toString(), pProgressIndicator, arguments).get();
        if(resp == null)
          return null;

        response = resp.getBody();
      }
      catch(InterruptedException e)
      {
        response = null;
      }
    }

    // Cancelled request
    if(response == null)
      return null;

    if(_isException(response))
      throw new ResultHasErrorException(response, urlBuilder.toString());

    return response;
  }

  /**
   * Executes the REST-URL, does pageing
   *
   * @param pURL       URL that should be executed
   * @param pArguments Additional arguments, not <tt>null</tt>
   * @return result, not wrapped, <tt>null</tt> if it was cancelled
   * @throws Exception if an error occured
   */
  @Nullable
  private JsonNode _executePaged(IRRestRequest pRequest, @NotNull String pURL, IRRestProgressIndicator pProgressIndicator, @NotNull ArrayList<IRRestArgument> pArguments) throws Exception
  {
    synchronized(executor)
    {
      try
      {
        int total_count = _getTotalCount(pURL, pProgressIndicator, pArguments);

        // Add pagelimit and sort, always the same during request
        pArguments.add(IRRestArgument.PAGE_LIMIT.value(String.valueOf(pagesize)));
        pArguments.add(IRRestArgument.SORT.value(":desc"));
        List<Future<HttpResponse<JsonNode>>> resultFutures = new ArrayList<>();

        for(int i = 0; i < total_count; i += pagesize)
        {
          final ArrayList<IRRestArgument> currentArguments = new ArrayList<>(pArguments);
          currentArguments.add(IRRestArgument.PAGE_OFFSET.value(String.valueOf(i)));
          resultFutures.add(_executePlain(pURL, pProgressIndicator, currentArguments));
        }

        // Wait to finish all tasks
        List<JsonNode> results = resultFutures.stream()
            .map(pFuture -> {
              try
              {
                if(pProgressIndicator.alive().get())
                  return pFuture.get();
              }
              catch(Exception ignored)
              {
              }

              return null;
            })
            .filter(Objects::nonNull)
            .map(HttpResponse::getBody)
            .collect(Collectors.toList());

        // It was cancelled -> NULL
        if(!pProgressIndicator.alive().get())
          return null;

        // Merge to one jsonnode
        return _merge(results, ((RRestRequestImpl) pRequest).getResultTopLevel());
      }
      catch(Exception e)
      {
        if(pProgressIndicator.alive().get()) //only throw if it is still alive
          throw new Exception("Error executing request (url: " + pURL + ")", e);
        return null;
      }
    }
  }

  /**
   * Returns the total_count-Attribute from Rest-API
   *
   * @param pURL       URL
   * @param pArguments Arguments
   * @return total_count-Attribute
   * @throws Exception if an error occured
   */
  private int _getTotalCount(String pURL, @NotNull IRRestProgressIndicator pProgressIndicator, @NotNull ArrayList<IRRestArgument> pArguments) throws Exception
  {
    try
    {
      ArrayList<IRRestArgument> arguments = new ArrayList<>(pArguments);
      arguments.add(IRRestArgument.PAGE_LIMIT.value(String.valueOf(0)));
      JsonNode node = _executePlain(pURL, pProgressIndicator, arguments).get().getBody();
      return node.getObject().getInt(IRRestArgument.PAGE_TOTALCOUNT.getRequestName());
    }
    catch(Exception e)
    {
      throw new Exception("total_count could not be determined", e);
    }
  }

  /**
   * Executes the REST-URL, NO pageing
   *
   * @param pURL       URL that should be executed
   * @param pArguments Additional arguments, not <tt>null</tt>  @return result, not wrapped
   * @throws Exception if an error occured
   */
  private Future<HttpResponse<JsonNode>> _executePlain(@NotNull String pURL, @NotNull IRRestProgressIndicator pProgressIndicator, @NotNull ArrayList<IRRestArgument> pArguments) throws Exception
  {
    StringBuilder urlBuilder = new StringBuilder(pURL);
    ArrayList<IRRestArgument> arguments = new ArrayList<>(pArguments);

    // Add arguments to urlBuilder
    if(arguments.size() > 0)
    {
      IRRestArgument firstArg = arguments.get(0);
      urlBuilder.append("?").append(firstArg.getRequestName()).append("=").append(firstArg.getValue());
      arguments.remove(0);
    }
    arguments.forEach((pArgument) -> urlBuilder.append("&").append(pArgument.getRequestName()).append("=").append(pArgument.getValue()));

    Future<HttpResponse<JsonNode>> future = Unirest.get(urlBuilder.toString()).asJsonAsync();

    // Instant cancel
    if(!pProgressIndicator.alive().get())
      future.cancel(true);

    // Wait for cancel
    pProgressIndicator.alive().addListener(new ChangeListener<Boolean>()
    {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
      {
        if(oldValue && !newValue)
        {
          pProgressIndicator.alive().removeListener(this);
          if(!future.isDone() && !future.isCancelled())
            future.cancel(true);
        }
      }
    });
    return future;
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

  /**
   * IRRestProgressIndicator-Impl for dummy use
   */
  private static class _AlwaysAliveIndicator implements IRRestProgressIndicator
  {
    @Override
    public ObservableBooleanValue alive()
    {
      return new SimpleBooleanProperty(true);
    }
  }

}
