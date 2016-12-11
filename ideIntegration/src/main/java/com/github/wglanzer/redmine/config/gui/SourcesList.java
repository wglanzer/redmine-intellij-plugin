package com.github.wglanzer.redmine.config.gui;

import com.github.wglanzer.redmine.model.ISource;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.*;

/**
 * Component inside settings.
 * Redmine-Sources
 *
 * @author w.glanzer, 06.10.2016.
 */
class SourcesList extends JBList
{
  private RAppSettingsModel model;

  public SourcesList(RAppSettingsModel pModel)
  {
    super(new _Model(pModel));
    model = pModel;
    setCellRenderer(new _Renderer());
  }

  /**
   * Method will be called, if "+"-Button is pressed
   *
   * @param pButton  Button, which was pressed
   */
  public void onAddClick(AnActionButton pButton)
  {
    model.addEmptySource();
  }

  /**
   * Method will be called, if "-"-Button is pressed
   *
   * @param pButton  Button, which was pressed
   */
  public void onRemoveClick(AnActionButton pButton)
  {
    ISource selectedValue = (ISource) getSelectedValue();
    if(selectedValue != null)
      model.removeSource(selectedValue);
  }

  /**
   * Renderer-Impl for rendering ISource-objects
   */
  private static class _Renderer extends DefaultListCellRenderer
  {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      setText(((ISource) value).getURL());
      return this;
    }
  }

  /**
   * ListModel-Impl for SourcesList
   */
  private static class _Model extends AbstractListModel<ISource>
  {
    private RAppSettingsModel model;

    public _Model(RAppSettingsModel pModel)
    {
      model = pModel;
      model.addPropertyChangeListener(evt -> fireContentsChanged(this, 0, Math.max(getSize() - 1, 0)));
    }

    @Override
    public int getSize()
    {
      return model.getSources().size();
    }

    @Override
    public ISource getElementAt(int index)
    {
      return model.getSources().get(index);
    }
  }
}
