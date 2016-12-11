package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.spi.IRRestArgument;
import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

/**
 * IRRestArgument-Impl
 *
 * @author w.glanzer, 04.12.2016.
 */
public class RRestArgumentImpl implements IRRestArgument
{

  private final String name;
  private final String requestName;
  private final String value;

  public RRestArgumentImpl()
  {
    this(null, null, null);
  }

  private RRestArgumentImpl(String pName, String pRequestName, String pValue)
  {
    name = pName;
    requestName = pRequestName;
    value = pValue;
  }

  @Override
  public RRestArgumentImpl value(String pValue)
  {
    return new RRestArgumentImpl(name, requestName, pValue);
  }

  @Override
  public String getResultName()
  {
    return name;
  }

  @Override
  public String getRequestName()
  {
    return requestName;
  }

  @Override
  public String getValue()
  {
    return value;
  }

  /**
   * Sets the name of this argument
   *
   * @param pName new Name
   * @return a copy of this argument, name set to pName
   */
  public RRestArgumentImpl resultName(@NotNull String pName)
  {
    return new RRestArgumentImpl(pName, MoreObjects.firstNonNull(requestName, pName), value);
  }

  /**
   * Specify an additional request-Name. It describes the name used
   * in GET-Requests. The normal "name" is used to identify something in the result-Object
   *
   * @param pRequestName Name
   * @return a copy of this argument, name set to pName
   */
  public RRestArgumentImpl requestName(@NotNull String pRequestName)
  {
    return new RRestArgumentImpl(name, pRequestName, value);
  }

}
