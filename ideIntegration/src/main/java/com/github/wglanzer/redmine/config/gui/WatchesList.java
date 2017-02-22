package com.github.wglanzer.redmine.config.gui;

import com.github.wglanzer.redmine.config.SourceDataModel;
import com.github.wglanzer.redmine.config.WatchDataModel;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Component inside settings.
 * Watches
 *
 * @author w.glanzer, 06.10.2016.
 */
class WatchesList extends JBList
{
  private final RAppSettingsModel model;
  private final Supplier<SourceDataModel> selectedSourceSupplier;
  private final Consumer<PropertyChangeEvent> eventConsumer;

  public WatchesList(RAppSettingsModel pModel, Supplier<SourceDataModel> pSelectedSourceSupplier, Consumer<PropertyChangeEvent> pEventConsumer)
  {
    super(new _Model(pModel, pSelectedSourceSupplier));
    model = pModel;
    selectedSourceSupplier = pSelectedSourceSupplier;
    eventConsumer = pEventConsumer;
  }

  /**
   * Method will be called, if "+"-Button is pressed
   *
   * @param pButton  Button, which was pressed
   */
  public void onAddClick(AnActionButton pButton)
  {
    SourceDataModel selectedSource = selectedSourceSupplier.get();
    if(selectedSource != null)
      model.addEmptyWatch(selectedSource.getName());
  }

  /**
   * Method will be called, if "-"-Button is pressed
   *
   * @param pButton  Button, which was pressed
   */
  public void onRemoveClick(AnActionButton pButton)
  {
    List<WatchDataModel> selectedWatches = getSelectedValuesList();
    if(selectedWatches.size() > 0)
      selectedWatches.forEach(pWatch -> model.removeWatch(pWatch.getName()));
  }

  /**
   * ListModel-Impl for WatchesList
   */
  private static class _Model extends AbstractListModel<WatchDataModel>
  {
    private final PropertyChangeListener propertyChangeListener;
    private final RAppSettingsModel model;
    private final Supplier<SourceDataModel> selectedSourceSupplier;

    public _Model(RAppSettingsModel pModel, Supplier<SourceDataModel> pSelectedSourceSupplier)
    {
      model = pModel;
      selectedSourceSupplier = pSelectedSourceSupplier;
      propertyChangeListener = evt -> fireContentsChanged(this, 0, Math.max(getSize() - 1, 0));
      model.addWeakPropertyChangeListener(propertyChangeListener);
    }

    @Override
    public int getSize()
    {
      SourceDataModel selectedSource = selectedSourceSupplier.get();
      if(selectedSource == null)
        return 0;
      else
        return selectedSource.getWatches().size();
    }

    @Override
    public WatchDataModel getElementAt(int index)
    {
      SourceDataModel selectedSource = selectedSourceSupplier.get();
      if(selectedSource == null)
        return null;
      else
        return (WatchDataModel) selectedSource.getWatches().get(index);
    }
  }
}
