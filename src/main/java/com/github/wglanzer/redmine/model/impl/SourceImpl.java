package com.github.wglanzer.redmine.model.impl;

import com.github.wglanzer.redmine.model.ISource;

/**
 * @author w.glanzer, 06.10.2016.
 */
public class SourceImpl implements ISource
{

  private String url;

  public SourceImpl(String pURL)
  {
    url = pURL;
  }

  @Override
  public String getURL()
  {
    return url;
  }

}
