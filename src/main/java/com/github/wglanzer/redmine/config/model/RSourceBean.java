package com.github.wglanzer.redmine.config.model;

import com.github.wglanzer.redmine.model.ISource;

import java.io.Serializable;

/**
 * Bean, which will be saved on disk
 *
 * @author w.glanzer, 06.10.2016.
 */
public class RSourceBean implements ISource, Serializable
{

  private static final long serialVersionUID = -5253457219403859704L;

  private String url;

  @Override
  public String getURL()
  {
    return url;
  }

  /**
   * Sets the URL to the server
   *
   * @param pUrl  URL to server
   */
  public void setUrl(String pUrl)
  {
    url = pUrl;
  }
}
