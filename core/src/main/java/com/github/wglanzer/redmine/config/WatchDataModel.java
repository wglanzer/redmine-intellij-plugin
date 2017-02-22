package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.model.ICondition;
import com.github.wglanzer.redmine.model.IWatch;
import de.adito.propertly.core.common.PD;
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
public class WatchDataModel extends AbstractPPP<SettingsDataModel, WatchDataModel, Object>
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
    String value = getPit().getValue(displayName);
    assert value != null;
    return value;
  }

  @Override
  public List<ICondition> getConditions()
  {
    Conditions conditions = getPit().getValue(WatchDataModel.conditions);
    assert conditions != null;
    return new ArrayList<>(conditions.getValues());
  }

  /**
   * Container for ConditionDataModels
   */
  public static class Conditions extends AbstractMutablePPP<WatchDataModel, Conditions, ConditionDataModel>
  {
    public Conditions()
    {
      super(ConditionDataModel.class);
    }
  }

}
