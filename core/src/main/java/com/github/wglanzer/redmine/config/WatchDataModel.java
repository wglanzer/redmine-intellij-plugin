package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.model.IConditionDescription;
import com.github.wglanzer.redmine.model.IWatch;
import com.google.common.base.MoreObjects;
import de.adito.propertly.core.common.PD;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.extension.AbstractMutablePPP;
import de.adito.propertly.core.spi.extension.AbstractPPP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * DataModel for a single watch containing a list of conditions
 *
 * @author w.glanzer, 22.02.2017.
 */
public class WatchDataModel extends AbstractPPP<SourceDataModel.Watches, WatchDataModel, Object>
    implements IWatch
{

  public static final IPropertyDescription<WatchDataModel, String> displayName = PD.create(WatchDataModel.class);
  public static final IPropertyDescription<WatchDataModel, Conditions> conditions = PD.create(WatchDataModel.class);

  @NotNull
  @Override
  public String getName()
  {
    return getPit().getOwnProperty().getName();
  }

  @Nullable
  @Override
  public String getDisplayName()
  {
    return getPit().getValue(displayName);
  }

  /**
   * Sets the displayName of this watch
   *
   * @param pDisplayName Name that should be set
   */
  public void setDisplayName(String pDisplayName)
  {
    getPit().setValue(displayName, MoreObjects.firstNonNull(pDisplayName, getName()));
  }

  @NotNull
  @Override
  public List<IConditionDescription> getConditionDescriptions()
  {
    Conditions conditions = getPit().getValue(WatchDataModel.conditions);
    assert conditions != null;
    return new ArrayList<>(conditions.getValues());
  }

  /**
   * Removes a specific condition from this model
   *
   * @param pName name of the condition which should be removed
   * @return <tt>true</tt> if something has changed
   */
  public boolean removeCondition(String pName)
  {
    IProperty<Conditions, ConditionDescriptionDataModel> descToRemove = getValue(conditions).findProperty(pName);
    return descToRemove != null && getValue(conditions).removeProperty(descToRemove);
  }

  /**
   * Container for ConditionDataModels
   */
  public static class Conditions extends AbstractMutablePPP<WatchDataModel, Conditions, ConditionDescriptionDataModel>
  {
    public Conditions()
    {
      super(ConditionDescriptionDataModel.class);
    }
  }

}
