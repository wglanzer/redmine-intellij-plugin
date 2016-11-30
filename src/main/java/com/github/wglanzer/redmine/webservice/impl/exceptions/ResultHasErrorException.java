package com.github.wglanzer.redmine.webservice.impl.exceptions;

import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;

import java.util.Objects;

/**
 * Exception thrown, if the result of the http-request
 * has no valid response or it contains errors
 *
 * @author w.glanzer, 30.11.2016.
 */
public class ResultHasErrorException extends Exception
{

  public ResultHasErrorException(JsonNode pResult, String pExecutedURL)
  {
    super(_toMessage(pResult) + " - " + pExecutedURL);
  }

  private static String _toMessage(JsonNode pNode)
  {
    try
    {
      JSONArray myErrors = pNode.getObject().getJSONArray("errors");
      return Objects.toString(myErrors);
    }
    catch(Exception e)
    {
      return e.getMessage();
    }
  }

}
