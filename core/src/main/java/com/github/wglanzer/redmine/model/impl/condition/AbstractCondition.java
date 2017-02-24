package com.github.wglanzer.redmine.model.impl.condition;

import com.github.wglanzer.redmine.model.ICondition;
import com.github.wglanzer.redmine.model.IConditionDescription;
import com.github.wglanzer.redmine.model.ITicket;

import java.util.List;

/**
 * Abstract Condition which all Conditions must implement
 *
 * @author w.glanzer, 25.02.2017.
 */
public abstract class AbstractCondition implements ICondition
{

  private final IConditionDescription description;

  AbstractCondition(IConditionDescription pDescription)
  {
    description = pDescription;
  }

  @Override
  public boolean test(ITicket pITicket)
  {
    List<String> possibleValues = getDescription().getPossibleValues();
    String realValue = getDescription().getAttribute().getValue(pITicket);
    return testImpl(possibleValues, realValue);
  }

  protected IConditionDescription getDescription()
  {
    return description;
  }

  protected abstract boolean testImpl(List<String> pPossibleValues, String pRealValue);
}
