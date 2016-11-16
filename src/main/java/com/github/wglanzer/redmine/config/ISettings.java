package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.model.ISource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Settings interface
 *
 * @author w.glanzer, 04.10.2016.
 */
public interface ISettings
{

  /**
   * Returns the redmine sources
   *
   * @return List of ALL saved redmine sources
   */
  @NotNull
  List<ISource> getSources();

}
