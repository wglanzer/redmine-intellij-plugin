package com.github.wglanzer.redmine.model.impl.condition;

import com.github.wglanzer.redmine.model.EConditionOperator;
import com.github.wglanzer.redmine.model.IConditionDescription;

import java.util.List;

/**
 * Condition for Operator NOT_CONTAINS
 *
 * @see EConditionOperator#CONTAINS
 * @author w.glanzer, 07.03.2017.
 */
@ConditionRegistration(operator = EConditionOperator.NOT_CONTAINS)
class NotContainsCondition extends AbstractCondition
{

  NotContainsCondition(IConditionDescription pDescription)
  {
    super(pDescription);
  }

  @Override
  protected boolean testImpl(List<String> pPossibleValues, String pRealValue)
  {
    if(pRealValue == null)
      return false;

    for(String value : pPossibleValues)
      if(!pRealValue.contains(value))
        return true;

    return false;
  }

}
