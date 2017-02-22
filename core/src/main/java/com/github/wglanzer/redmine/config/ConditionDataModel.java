package com.github.wglanzer.redmine.config;

import com.github.wglanzer.redmine.model.ICondition;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import de.adito.propertly.core.spi.extension.AbstractPPP;

/**
 * Represents a single condition that determines
 * if a notification can be shown or not
 *
 * @author w.glanzer, 22.02.2017.
 */
public class ConditionDataModel extends AbstractPPP<IPropertyPitProvider, ConditionDataModel, Object>
  implements ICondition
{


}
