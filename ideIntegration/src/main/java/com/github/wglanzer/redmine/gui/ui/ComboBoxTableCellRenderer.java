package com.github.wglanzer.redmine.gui.ui;

import com.intellij.openapi.diagnostic.LogUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.ListWithSelection;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Collections;

public class ComboBoxTableCellRenderer extends JPanel implements TableCellRenderer
{
  public static final TableCellRenderer INSTANCE = new ComboBoxTableCellRenderer();

  /**
   * DefaultTableCellRenderer, that displays JComboBox on selected value.
   */
  public static final TableCellRenderer COMBO_WHEN_SELECTED_RENDERER = new DefaultTableCellRenderer()
  {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
      if(isSelected)
      {
        value = new ListWithSelection<>(Collections.singletonList(value));
        return INSTANCE.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      }
      return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
  };

  private static final Logger LOG = Logger.getInstance("#com.intellij.util.ui.ComboBoxTableCellRenderer");

  private final JComboBox myCombo = new ComboBox();

  private ComboBoxTableCellRenderer()
  {
    super(new GridBagLayout());
    add(myCombo,
        new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, JBUI.emptyInsets(), 0, 0));
  }

  @Override
  public JComponent getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    if(value instanceof ListWithSelection)
    {
      final ListWithSelection tags = (ListWithSelection) value;
      if(tags.getSelection() == null)
      {
        tags.selectFirst();
      }
      myCombo.removeAllItems();
      for(Object tag : tags)
      {
        myCombo.addItem(tag);
      }
      myCombo.setSelectedItem(tags.getSelection());
    }
    else
    {
      if(value != null)
      {
        LOG.error("value " + LogUtil.objectAndClass(value) + ", at " + row + ":" + column + ", in " + table.getModel());
      }
      myCombo.removeAllItems();
      myCombo.setSelectedIndex(-1);
    }

    return this;
  }
}