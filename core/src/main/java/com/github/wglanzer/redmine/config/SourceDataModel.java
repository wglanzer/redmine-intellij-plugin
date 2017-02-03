package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.model.ISource;
import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.core.spi.extension.AbstractPPP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * PPP for one Redmine-Source
 *
 * @author w.glanzer, 30.01.2017.
 */
public class SourceDataModel extends AbstractPPP<IPropertyPitProvider, SourceDataModel, Object>
  implements ISource
{

  public static final IPropertyDescription<SourceDataModel, String> displayName = PD.create(SourceDataModel.class);
  public static final IPropertyDescription<SourceDataModel, String> url = PD.create(SourceDataModel.class);
  public static final IPropertyDescription<SourceDataModel, String> apiKey = PD.create(SourceDataModel.class);
  public static final IPropertyDescription<SourceDataModel, Integer> pollingInterval = PD.create(SourceDataModel.class);
  public static final IPropertyDescription<SourceDataModel, Integer> pageSize = PD.create(SourceDataModel.class);
  public static final IPropertyDescription<SourceDataModel, Boolean> checkCertificate = PD.create(SourceDataModel.class);

  @NotNull
  public String getName()
  {
    return getPit().getOwnProperty().getName();
  }

  @Nullable
  @Override
  public String getDisplayName()
  {
    return getPit().getValue(displayName);
  }

  /**
   * Sets the display name for this server
   *
   * @param pDisplayName DisplayName that should be set, <tt>null</tt> clears this display name
   */
  public void setDisplayName(String pDisplayName)
  {
    getPit().setValue(displayName, pDisplayName);
  }

  @NotNull
  @Override
  public String getURL()
  {
    return getPit().getValue(url);
  }

  /**
   * Sets the URL to the server
   *
   * @param pUrl URL to server
   */
  public void setUrl(String pUrl)
  {
    getPit().setValue(url, pUrl);
  }

  @NotNull
  @Override
  public String getAPIKey()
  {
    return getPit().getValue(apiKey);
  }

  /**
   * Sets the API-Key for this server
   *
   * @param pApiKey Key
   */
  public void setApiKey(@NotNull String pApiKey)
  {
    getPit().setValue(apiKey, pApiKey);
  }

  @Override
  public Integer getPollInterval()
  {
    return getPit().getValue(pollingInterval);
  }

  /**
   * Sets the polling interval for this server
   *
   * @param pPollingInterval polling interval
   */
  public void setPollingInterval(Integer pPollingInterval)
  {
    getPit().setValue(pollingInterval, pPollingInterval);
  }

  @Nullable
  @Override
  public Integer getPageSize()
  {
    return getPit().getValue(pageSize);
  }

  /**
   * Sets the pageSize for this server
   *
   * @param pPageSize Size of the GET-Request-Page, or <tt>null</tt> -> default
   */
  public void setPageSize(Integer pPageSize)
  {
    getPit().setValue(pageSize, pPageSize);
  }

  @Override
  public boolean isCheckCeritifacte()
  {
    return getPit().getValue(checkCertificate);
  }

  /**
   * Sets that the certificate should be checked or not
   *
   * @param pCheck <tt>true</tt> if it should be checked
   */
  public void setCheckCertificate(boolean pCheck)
  {
    getPit().setValue(checkCertificate, pCheck);
  }
}
