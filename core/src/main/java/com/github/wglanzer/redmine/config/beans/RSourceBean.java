package com.github.wglanzer.redmine.config.beans;

import com.github.wglanzer.redmine.model.ISource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * Bean, which will be saved on disk
 *
 * @author w.glanzer, 06.10.2016.
 */
public class RSourceBean implements ISource, Serializable
{

  private static final long serialVersionUID = -5253459215423359704L;

  public String displayName;
  public String url;
  public String apiKey;
  public Integer pollingInterval;
  public Integer pageSize;

  @Nullable
  @Override
  public String getDisplayName()
  {
    return displayName;
  }

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
  public Integer getPollInterval()
  {
    return pollingInterval;
  }

  @Nullable
  @Override
  public Integer getPageSize()
  {
    return pageSize;
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
  public void setPollingInterval(Integer pPollingInterval)
  {
    pollingInterval = pPollingInterval;
  }

  /**
   * Sets the pageSize for this server
   *
   * @param pPageSize Size of the GET-Request-Page, or <tt>null</tt> -> default
   */
  public void setPageSize(Integer pPageSize)
  {
    pageSize = pPageSize;
  }

  /**
   * Sets the display name for this server
   *
   * @param pDisplayName DisplayName that should be set, <tt>null</tt> clears this display name
   */
  public void setDisplayName(String pDisplayName)
  {
    displayName = pDisplayName;
  }
}
