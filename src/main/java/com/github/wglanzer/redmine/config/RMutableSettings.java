package com.github.wglanzer.redmine.config;

import java.io.Serializable;

/**
 * Settings-Impl, mutable
 *
 * @author w.glanzer, 04.10.2016.
 */
public class RMutableSettings implements ISettings, Serializable
{

  private String example;

  @Override
  public String getExample()
  {
    return example;
  }

  public void setExample(String pExample)
  {
    example = pExample;
  }

}
