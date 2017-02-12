package com.github.wglanzer.redmine.webservice.impl.exceptions;

import com.mashape.unirest.http.HttpResponse;

/**
 * Exception that shows up when the API-Key is invalid
 *
 * @author w.glanzer, 12.02.2017.
 */
public class AuthorizationException extends Exception
{

  private final HttpResponse response;
  private final String url;
  private final String apiKey;

  public AuthorizationException(HttpResponse pResponse, String pUrl, String pAPIKey)
  {
    response = pResponse;
    url = pUrl;
    apiKey = pAPIKey;
  }

  public String getUrl()
  {
    return url;
  }

  public HttpResponse getResponse()
  {
    return response;
  }

  public String getApiKey()
  {
    return apiKey;
  }
}
