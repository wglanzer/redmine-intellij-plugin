package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.config.model.RSourceBean;
import com.github.wglanzer.redmine.model.ISource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Settings-Impl, mutable.
 * This bean can be saved via serialization to disk
 *
 * @author w.glanzer, 04.10.2016.
 */
public class RMutableSettings implements ISettings, Serializable
{

  private static final long serialVersionUID = 7208863115811988902L;

  private List<RSourceBean> sources;

  @NotNull
  @Override
  public List<ISource> getSources()
  {
    return sources != null ? new ArrayList<>(sources) : new ArrayList<>();
  }

  public void setSources(@Nullable List<RSourceBean> pSources)
  {
    sources = pSources;
  }

}
