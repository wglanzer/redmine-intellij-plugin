package com.github.wglanzer.redmine.util.propertly;

import de.adito.propertly.core.api.Hierarchy;
import de.adito.propertly.core.spi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author w.glanzer, 03.02.2017
 */
public class BulkModifyHierarchy<T extends IPropertyPitProvider> extends Hierarchy<T>
{

  @NotNull
  private final IHierarchy<T> sourceHierarchy;

  public BulkModifyHierarchy(@NotNull IHierarchy<T> pSourceHierarchy)
  {
    super(pSourceHierarchy.getProperty().getName(), pSourceHierarchy.getValue());
    sourceHierarchy = pSourceHierarchy;
  }

  @NotNull
  public IHierarchy<T> getSourceHierarchy()
  {
    return sourceHierarchy;
  }

  public synchronized void writeBack()
  {
    Collection<_Change> writeables = _prepareWrite(getValue().getPit(), sourceHierarchy.getValue().getPit());
    writeables.forEach(_Change::_doWrite);
  }

  private <P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, V>, V> Collection<_Change<P, S, V>> _prepareWrite(IPropertyPit<P, S, V> pSource, IPropertyPit<P, S, V> pTarget)
  {
    List<_Change<P, S, V>> result = new ArrayList<>();
    List<IProperty<S, V>> myProperties = pSource.getPit().getProperties();
    for(IProperty<S, V> currProp : myProperties)
    {
      V currVal = currProp.getValue();
      IProperty<S, V> targetProp = pTarget.findProperty(currProp.getDescription());

      if(targetProp == null)
      {
        if(!(pTarget instanceof IMutablePropertyPit))
          continue;

        targetProp = ((IMutablePropertyPit) pTarget).addProperty(currProp.getDescription(), new Object[0]);
      }

      boolean sameValues = Objects.equals(currVal, targetProp.getValue());
      boolean isPPP = currVal instanceof IPropertyPitProvider;
      boolean sameDescriptions = isPPP && Objects.equals(targetProp.getDescription(), ((IPropertyPitProvider) currVal).getPit().getOwnProperty().getDescription());
      boolean sameChildType = isPPP && Objects.equals(targetProp.getType(), ((IPropertyPitProvider) currVal).getPit().getOwnProperty().getType());

      if((!isPPP && !sameValues) || (isPPP && (!sameDescriptions || !sameChildType || (currVal == null ^ targetProp.getValue() == null))))
        result.add(new _Change<>(targetProp, currVal));
      else
      {
        V tarVal = targetProp.getValue();
        if(currVal instanceof IPropertyPitProvider && tarVal instanceof IPropertyPitProvider)
          result.addAll(_prepareWrite(((IPropertyPitProvider) currVal).getPit(), ((IPropertyPitProvider) tarVal).getPit()));
      }
    }

    if(pTarget instanceof IMutablePropertyPit)
      pTarget.getProperties().stream()
          .filter(pProp -> pSource.findProperty(pProp.getDescription()) == null)
          .forEach(pProp -> ((IMutablePropertyPit) pTarget.getPit()).removeProperty(pProp));

    return result;
  }

  private static class _Change<P extends IPropertyPitProvider, S extends IPropertyPitProvider<P, S, V>, V>
  {
    private IProperty<S, V> property;
    private V newValue;

    public _Change(IProperty<S, V> pProperty, V pNewValue)
    {
      property = pProperty;
      newValue = pNewValue;
    }

    private void _doWrite()
    {
      property.setValue(newValue);
    }
  }

}
