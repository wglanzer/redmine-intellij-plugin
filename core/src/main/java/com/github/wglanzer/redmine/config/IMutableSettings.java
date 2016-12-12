package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.config.beans.RSourceBean;

import java.util.List;

/**
 * Mutable settings
 *
 * @author W.Glanzer, 12.12.2016
 */
public interface IMutableSettings extends ISettings
{

  /**
   * Sets new sources to this settings instance
   *
   * @param pSources Sources that should be set
   */
  void setSources(List<RSourceBean> pSources);

}
