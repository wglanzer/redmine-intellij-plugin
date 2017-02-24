package com.github.wglanzer.redmine.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This interface describes a description
 * that can be used to create a ICondition
 *
 * @author w.glanzer, 22.02.2017.
 */
public interface IConditionDescription
{

  /**
   * Name of this conditionDescription; ID
   *
   * @return Name
   */
  @NotNull
  String getName();

  @NotNull
  EConditionAttribute getAttribute();

  @NotNull
  EConditionOperator getOperator();

  /**
   * Returns a list with ALL possible values
   * that this condition can contain
   *
   * @return a list, not <tt>null</tt>
   */
  @NotNull
  List<String> getPossibleValues();

}
