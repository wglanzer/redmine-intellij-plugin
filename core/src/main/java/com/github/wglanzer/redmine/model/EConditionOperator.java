package com.github.wglanzer.redmine.model;

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
  NOT_CONTAINS

}
