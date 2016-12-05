package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.spi.IRRestArgument;
import org.jetbrains.annotations.NotNull;

/**
 * IRRestArgument-Impl
 *
 * @author w.glanzer, 04.12.2016.
 */
public class RRestArgumentImpl implements IRRestArgument
{

  private final String name;
  private final String value;

  public RRestArgumentImpl()
  {
    this(null, null);
  }

  private RRestArgumentImpl(String pName, String pValue)
  {
    name = pName;
    value = pValue;
  }

  @Override
  public RRestArgumentImpl value(String pValue)
  {
    return new RRestArgumentImpl(name, pValue);
  }

  @Override
  public String getName()
  {
    return name;
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
  public RRestArgumentImpl name(@NotNull String pName)
  {
    return new RRestArgumentImpl(pName, value);
  }

}