package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.spi.IRRestArgument;
import com.github.wglanzer.redmine.webservice.spi.IRRestRequest;
import com.github.wglanzer.redmine.webservice.spi.IRRestResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * IRRestResult-Impl for JSON-Nodes
 *
 * @author w.glanzer, 04.12.2016.
 */
class JSONRRestResult implements IRRestResult
{
  private final IRRestRequest request;
  private final List<Node> nodes;

  public JSONRRestResult(@NotNull IRRestRequest pRequest, @NotNull JSONArray pWebserviceResult)
  {
    request = pRequest;
    nodes = StreamSupport.stream(pWebserviceResult.spliterator(), true)
        .map(pObject -> (JSONObject) pObject)
        .map(_NodeImpl::new)
        .collect(Collectors.toList());
  }

  @Override
  public Stream<Node> getResultNodes()
  {
    return nodes.stream();
  }

  /**
   * Node-Impl for JSON-Objects
   */
  private static class _NodeImpl implements Node
  {
    private Map<String, String> arguments = new HashMap<>();

    public _NodeImpl(JSONObject pMyObject)
    {
      arguments = pMyObject.keySet().stream()
          .map(pArgumentKey -> {
            Object result = pMyObject.get(pArgumentKey);
            if(result instanceof JSONObject)
            {
              JSONObject jsonResult = (JSONObject) result;
              if(jsonResult.has("name"))
                result = jsonResult.get("name");
              else if(jsonResult.has("id"))
                result = jsonResult.get("id");
              else
                throw new RuntimeException("result can not be exactly determined (" + result + ")");
            }

            return new AbstractMap.SimpleImmutableEntry<>(pArgumentKey, Objects.toString(result));
          })
          .collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue));
    }

    @Nullable
    @Override
    public String getValue(IRRestArgument pArgument)
    {
      return arguments.get(pArgument.getName());
    }
  }

}
