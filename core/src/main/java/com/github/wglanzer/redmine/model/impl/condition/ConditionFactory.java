package com.github.wglanzer.redmine.model.impl.condition;

import com.github.wglanzer.redmine.RPicoRegistry;
import com.github.wglanzer.redmine.model.EConditionOperator;
import com.github.wglanzer.redmine.model.ICondition;
import com.github.wglanzer.redmine.model.IConditionDescription;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory that constructs IConditions
 *
 * @author w.glanzer, 25.02.2017.
 */
public class ConditionFactory
{

  private static final Map<EConditionOperator, Class<? extends AbstractCondition>> _REGISTRY = _createRegistry();

  /**
   * Construct an ICondition that matches the given IConditionDescription
   *
   * @param pDescription Description
   * @return ICondition that matches given description
   */
  public static ICondition create(IConditionDescription pDescription)
  {
    // Find condition-class
    Class<? extends AbstractCondition> myCondition = _REGISTRY.get(pDescription.getOperator());
    if(myCondition == null)
      throw new RuntimeException("Invalid description with operator " + pDescription.getOperator());
    try
    {
      Constructor constructor = myCondition.getDeclaredConstructor(IConditionDescription.class);
      constructor.setAccessible(true);
      return (ICondition) constructor.newInstance(pDescription);
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates the registry which is used to locate all *Conditions
   *
   * @return A map which contains Operator and ConditionClass
   */
  public static Map<EConditionOperator, Class<? extends AbstractCondition>> _createRegistry()
  {
    Map<EConditionOperator, Class<? extends AbstractCondition>> registry = new HashMap<>();
    RPicoRegistry.INSTANCE.find(AbstractCondition.class, ConditionRegistration.class).forEach((key, value) ->
    {
      EConditionOperator operator = value.operator();
      registry.put(operator, key);
    });
    return registry;
  }

}
