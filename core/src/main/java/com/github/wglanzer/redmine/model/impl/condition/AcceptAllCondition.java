package com.github.wglanzer.redmine.model.impl.condition;

import com.github.wglanzer.redmine.model.EConditionOperator;
import com.github.wglanzer.redmine.model.IConditionDescription;

import java.util.List;

/**
 * Condition for Operator ACCEPT_ALL
 *
 * @see EConditionOperator#ACCEPT_ALL
 * @author w.glanzer, 25.02.2017.
 */
@ConditionRegistration(operator = EConditionOperator.ACCEPT_ALL)
class AcceptAllCondition extends AbstractCondition
{

  AcceptAllCondition(IConditionDescription pDescription)
  {
    super(pDescription);
  }

  @Override
  protected boolean testImpl(List<String> pPossibleValues, String pRealValue)
  {
    return true;
  }

}
