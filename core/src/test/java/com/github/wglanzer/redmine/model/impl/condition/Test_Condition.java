package com.github.wglanzer.redmine.model.impl.condition;

import com.github.wglanzer.redmine.model.EConditionAttribute;
import com.github.wglanzer.redmine.model.EConditionOperator;
import com.github.wglanzer.redmine.model.IConditionDescription;
import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Tests all things for conditions
 *
 * @author w.glanzer, 25.02.2017.
 */
public class Test_Condition
{

  /**
   * Tests if all EOperators are mapped to an AbstractCondition
   */
  @Test
  public void test_allOperatorsBoundToConditions()
  {
    for(EConditionOperator operator : EConditionOperator.values())
      Assert.assertNotNull("operator: " + operator, ConditionFactory.create(new _ConditionDescription(EConditionAttribute.ASSIGNEE, operator, null)));
  }

  /**
   * ConditionDescription impl
   */
  private static class _ConditionDescription implements IConditionDescription
  {
    private final String name = UUID.randomUUID().toString();
    private final EConditionAttribute filter;
    private final EConditionOperator operator;
    private final List<String> filterValues;

    public _ConditionDescription(EConditionAttribute pFilter, EConditionOperator pOperator, @Nullable List<String> pFilterValues)
    {
      filter = pFilter;
      operator = pOperator;
      filterValues = MoreObjects.firstNonNull(pFilterValues, Collections.emptyList());
    }

    @NotNull
    @Override
    public String getName()
    {
      return name;
    }

    @NotNull
    @Override
    public EConditionAttribute getAttribute()
    {
      return filter;
    }

    @NotNull
    @Override
    public EConditionOperator getOperator()
    {
      return operator;
    }

    @NotNull
    @Override
    public List<String> getPossibleValues()
    {
      return filterValues;
    }
  }

}
