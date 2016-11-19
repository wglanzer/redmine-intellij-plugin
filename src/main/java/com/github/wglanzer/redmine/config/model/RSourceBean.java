package com.github.wglanzer.redmine.config.model;

import com.github.wglanzer.redmine.model.ISource;
import org.jetbrains.annotations.NotNull;

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

  private String apiKey;

  @NotNull
  @Override
  public String getURL()
  {
    return url;
  }

  @NotNull
  @Override
  public String getAPIKey()
  {
    return apiKey;
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

  /**
   * Sets the API-Key for this server
   *
   * @param pApiKey Key
   */
  public void setApiKey(@NotNull String pApiKey)
  {
    apiKey = pApiKey;
  }
}
