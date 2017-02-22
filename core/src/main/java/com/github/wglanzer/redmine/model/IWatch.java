package com.github.wglanzer.redmine.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a watch that defines when a notification should be shown
 *
 * @author w.glanzer, 22.02.2017.
 */
public interface IWatch
{

  /**
   * Name of this watch; ID
   *
   * @return Name
   */
  @NotNull
  String getName();

  /**
   * Displayname of this watch, can be defined by user
   *
   * @return human readable name
   */
  @Nullable
  String getDisplayName();

  /**
   * Returns all conditions that this watch contains.
   * Only if ALL of this are positive a specific notification is shown (project / ticket added, etc.)
   *
   * @return List of ALL available conditions
   */
  List<ICondition> getConditions();

}
