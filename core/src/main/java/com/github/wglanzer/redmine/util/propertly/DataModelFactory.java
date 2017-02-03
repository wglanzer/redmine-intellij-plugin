package com.github.wglanzer.redmine.util.propertly;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.common.PropertlyUtility;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyPitProvider;

import java.util.UUID;

/**
 * A Factory to create and initialize Propertly-DataModels
 *
 * @author w.glanzer, 03.02.2017.
 */
public class DataModelFactory
{

  /**
   * Creates and inits the datamodel with the given class
   *
   * @param pClass Class of the model that should be created
   * @return DataModel ready to be used
   */
  public static <T extends IPropertyPitProvider<?, ?, ?>> T createModel(Class<T> pClass)
  {
    T creation = new Hierarchy<>(UUID.randomUUID().toString(), PropertlyUtility.create(pClass)).getValue();
    return initModel(creation);
  }

  /**
   * Initializes all empty PPPs with default values
   *
   * @param ppp PropertyPitProvider which should be initialized
   * @return the initialized ppp
   */
  public static <T extends IPropertyPitProvider<?, ?, ?>> T initModel(T ppp)
  {
    ppp.getPit().getProperties().forEach(DataModelFactory::_initProperty);
    return ppp;
  }

  private static void _initProperty(IProperty<?, ?> pProp)
  {
    if(IPropertyPitProvider.class.isAssignableFrom(pProp.getType()))
    {
      if(pProp.getValue() == null)
      {
        IPropertyPitProvider<?, ?, ?> val = PropertlyUtility.create((Class<? extends IPropertyPitProvider>) pProp.getType());
        ((IProperty<?, IPropertyPitProvider>) pProp).setValue(val);
      }

      IPropertyPitProvider<?, ?, ?> ppp = ((IProperty<?, IPropertyPitProvider>) pProp).getValue();
      assert ppp != null;
      ppp.getPit().getProperties().forEach(DataModelFactory::_initProperty);
    }
  }

}
