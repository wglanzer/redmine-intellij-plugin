package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.model.EConditionAttribute;
import com.github.wglanzer.redmine.model.EConditionOperator;
import com.github.wglanzer.redmine.model.IConditionDescription;
import com.google.common.base.MoreObjects;
import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.core.spi.extension.AbstractMutablePPP;
import de.adito.propertly.core.spi.extension.AbstractPPP;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single condition that determines
 * if a notification can be shown or not
 *
 * @author w.glanzer, 22.02.2017.
 */
public class ConditionDescriptionDataModel extends AbstractPPP<IPropertyPitProvider, ConditionDescriptionDataModel, Object>
  implements IConditionDescription
{

  private static final IPropertyDescription<ConditionDescriptionDataModel, EConditionAttribute> filter = PD.create(ConditionDescriptionDataModel.class);
  private static final IPropertyDescription<ConditionDescriptionDataModel, EConditionOperator> operator = PD.create(ConditionDescriptionDataModel.class);
  private static final IPropertyDescription<ConditionDescriptionDataModel, Values> values = PD.create(ConditionDescriptionDataModel.class);

  @NotNull
  @Override
  public String getName()
  {
    return getOwnProperty().getName();
  }

  @NotNull
  @Override
  public EConditionAttribute getAttribute()
  {
    EConditionAttribute filter = getValue(ConditionDescriptionDataModel.filter);
    assert filter != null;
    return filter;
  }

  public void setAttribute(EConditionAttribute pFilter)
  {
    setValue(filter, pFilter);
  }

  @NotNull
  @Override
  public EConditionOperator getOperator()
  {
    EConditionOperator operator = getValue(ConditionDescriptionDataModel.operator);
    assert operator != null;
    return operator;
  }

  public void setOperator(EConditionOperator pOperator)
  {
    setValue(operator, pOperator);
  }

  @NotNull
  @Override
  public List<String> getPossibleValues()
  {
    Values values = getValue(ConditionDescriptionDataModel.values);
    assert values != null;
    return MoreObjects.firstNonNull(values.getValues(), Collections.emptyList());
  }

  public void setPossibleValues(List<String> pList)
  {
    Values values = getValue(ConditionDescriptionDataModel.values);
    if(values == null)
      values = setValue(ConditionDescriptionDataModel.values, new Values());
    assert values != null;

    // Property is wished but VALUES does not contain
    for(String value : pList)
      if(values.getProperties().stream().map(IProperty::getValue).noneMatch(pPropVal -> Objects.equals(pPropVal, value)))
        values.addProperty(value);

    // Property is not wished but VALUES does contain
    for(IProperty<Values, String> property : values.getProperties())
      if(!pList.contains(property.getValue()))
        values.removeProperty(property);
  }

  public static class Values extends AbstractMutablePPP<ConditionDescriptionDataModel, Values, String>
  {
    public Values()
    {
      super(String.class);
    }
  }
}
