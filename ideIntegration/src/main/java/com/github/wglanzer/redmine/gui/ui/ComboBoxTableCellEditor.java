package com.github.wglanzer.redmine.gui.ui;

import com.intellij.openapi.util.Iconable;
import com.intellij.ui.EnumComboBoxModel;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.PopupMenuListenerAdapter;
import com.intellij.util.ListWithSelection;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import java.awt.*;
import java.util.Collections;

/**
 * Supported value type: {@link ListWithSelection} and {@link java.lang.Enum}.
 * Value type can implement {@link com.intellij.openapi.util.Iconable} to display icon.
 */
public class ComboBoxTableCellEditor extends DefaultCellEditor
{
  public static final ComboBoxTableCellEditor INSTANCE = new ComboBoxTableCellEditor();

  private final JComboBox comboBox;

  public ComboBoxTableCellEditor()
  {
    //noinspection unchecked,UndesirableClassUsage
    super(new JComboBox(new ListComboBoxModel(Collections.emptyList())));

    comboBox = (JComboBox) getComponent();

    // problem: pop-up opened - closed by esc - editing is not canceled, but must be
    comboBox.addPopupMenuListener(new PopupMenuListenerAdapter()
    {
      @Override
      public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
      {
        stopCellEditing();
      }

      @Override
      public void popupMenuCanceled(PopupMenuEvent e)
      {
        cancelCellEditing();
      }
    });

    //noinspection unchecked
    comboBox.setRenderer(new ListCellRendererWrapper()
    {
      @Override
      public void customize(JList list, Object value, int index, boolean selected, boolean hasFocus)
      {
        setIcon(value instanceof Iconable ? ((Iconable) value).getIcon(Iconable.ICON_FLAG_VISIBILITY) : null);
      }
    });
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
  {
    if(value instanceof ListWithSelection)
    {
      ListWithSelection options = (ListWithSelection) value;
      //noinspection unchecked
      comboBox.setModel(new ListComboBoxModel(options));

      if(options.getSelection() == null)
      {
        options.selectFirst();
      }
      comboBox.setSelectedItem(options.getSelection());
    }
    else
    {
      Enum enumValue = (Enum) value;
      Class enumClass = enumValue.getDeclaringClass();
      //noinspection unchecked
      ComboBoxModel model = comboBox.getModel();
      if(!(model instanceof EnumComboBoxModel && model.getSize() > 0 && ((Enum) model.getElementAt(0)).getDeclaringClass() == enumClass))
      {
        //noinspection unchecked
        comboBox.setModel(new EnumComboBoxModel(enumClass));
      }
      comboBox.setSelectedItem(value);
    }

    return comboBox;
  }
}