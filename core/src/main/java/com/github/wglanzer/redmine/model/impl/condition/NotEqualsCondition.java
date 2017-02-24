package com.github.wglanzer.redmine.model.impl.condition;

import com.github.wglanzer.redmine.model.EConditionOperator;
import com.github.wglanzer.redmine.model.IConditionDescription;

import java.util.List;

/**
 * Condition for Operator NOT_EQUALS
 *
 * @see EConditionOperator#NOT_EQUALS
 * @author w.glanzer, 25.02.2017.
 */
@ConditionRegistration(operator = EConditionOperator.NOT_EQUALS)
class NotEqualsCondition extends AbstractCondition
{

  NotEqualsCondition(IConditionDescription pDescription)
  {
    super(pDescription);
  }

  @Override
  protected boolean testImpl(List<String> pPossibleValues, String pRealValue)
  {
    return !pPossibleValues.contains(pRealValue);
  }

}
