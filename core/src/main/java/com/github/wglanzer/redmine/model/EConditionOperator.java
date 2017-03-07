package com.github.wglanzer.redmine.model;

import java.util.Arrays;

/**
 * Represents an operator for a condition which shows
 * how filter and value are compared.
 *
 * Example: EConditionAttribute "EQUALS" Value
 *
 * @author w.glanzer, 24.02.2017.
 */
public enum EConditionOperator
{

  EQUALS,
  NOT_EQUALS,
  CONTAINS,
  NOT_CONTAINS,

  /*
  * Special-Operators
  */
  ACCEPT_ALL;

  /**
   * Returns all "common" operators, without special operators
   *
   * @return an array of operators
   */
  public static EConditionOperator[] common()
  {
    return Arrays.stream(EConditionOperator.values())
        .filter(pOperator -> !pOperator.equals(ACCEPT_ALL))
        .toArray(EConditionOperator[]::new);
  }

}
