package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.config.beans.RSourceBean;
import com.github.wglanzer.redmine.model.ISource;
import com.google.common.base.Strings;
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

  private static final long serialVersionUID = 7208863115811985902L;

  private List<RSourceBean> sources;

  @NotNull
  @Override
  public List<ISource> getSources()
  {
    ArrayList<ISource> sources = this.sources != null ? new ArrayList<>(this.sources) : new ArrayList<>();
    if(Strings.nullToEmpty(System.getProperty("plugin.redmine.debug.useDummy")).equals("true"))
    {
      RSourceBean tt = new IGNORE_TestingSourcesBean();
      if(sources.stream().noneMatch(pSource -> pSource.getURL().equals(tt.getURL())))
        sources.add(tt);
    }
    return sources;
  }

  public void setSources(@Nullable List<RSourceBean> pSources)
  {
    sources = pSources;
  }

}
