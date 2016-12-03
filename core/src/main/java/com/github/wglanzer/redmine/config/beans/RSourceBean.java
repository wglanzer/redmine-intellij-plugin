package com.github.wglanzer.redmine.config.beans;

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

  private static final long serialVersionUID = -5253459219403859704L;

  private String url;
  private String apiKey;
  private int pollingInterval;

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

  @Override
  public int getPollInterval()
  {
    return pollingInterval;
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

  /**
   * Sets the polling interval for this server
   *
   * @param pPollingInterval polling interval
   */
  public void setPollingInterval(int pPollingInterval)
  {
    pollingInterval = pPollingInterval;
  }
}
