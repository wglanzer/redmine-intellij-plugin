package com.github.wglanzer.redmine.model.impl.condition;

import com.github.wglanzer.redmine.model.EConditionOperator;
import com.github.wglanzer.redmine.model.IConditionDescription;

import java.util.List;

/**
 * Condition for Operator EQUALS
 *
 * @see EConditionOperator#EQUALS
 * @author w.glanzer, 25.02.2017.
 */
@ConditionRegistration(operator = EConditionOperator.EQUALS)
class EqualsCondition extends AbstractCondition
{

  EqualsCondition(IConditionDescription pDescription)
  {
    super(pDescription);
  }

  @Override
  protected boolean testImpl(List<String> pPossibleValues, String pRealValue)
  {
    return pPossibleValues.contains(pRealValue);
  }

}
