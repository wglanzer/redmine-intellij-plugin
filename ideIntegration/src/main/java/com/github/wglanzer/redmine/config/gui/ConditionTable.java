package com.github.wglanzer.redmine.config.gui;

import com.github.wglanzer.redmine.config.ConditionDescriptionDataModel;
import com.github.wglanzer.redmine.config.SettingsDataModel;
import com.github.wglanzer.redmine.config.SourceDataModel;
import com.github.wglanzer.redmine.config.WatchDataModel;
import com.github.wglanzer.redmine.gui.ui.ComboBoxTableCellEditor;
import com.github.wglanzer.redmine.gui.ui.ComboBoxTableCellRenderer;
import com.github.wglanzer.redmine.model.EConditionAttribute;
import com.github.wglanzer.redmine.model.EConditionOperator;
import com.github.wglanzer.redmine.model.IConditionDescription;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ListWithSelection;
import de.adito.propertly.core.common.PropertyPitEventAdapter;
import de.adito.propertly.core.spi.IProperty;
import de.adito.propertly.core.spi.IPropertyDescription;
import de.adito.propertly.core.spi.IPropertyPitProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.*;
import java.util.stream.Collectors;

  /**
   * Table for the ConditionDialog which shows all available conditions
   *
   * @see ConditionDialog
   * @author w.glanzer, 26.02.2017.
   */
  class ConditionTable extends JBTable
  {

  private final RAppSettingsModel model;
  private final WatchDataModel watch;

  public ConditionTable(RAppSettingsModel pModel, WatchDataModel pWatch)
  {
    model = pModel;
    watch = pWatch;
    setModel(new _Model());
    setDefaultEditor(ListWithSelection.class, ComboBoxTableCellEditor.INSTANCE);
    setDefaultEditor(String.class, new DefaultCellEditor(new JTextField()));
    setDefaultRenderer(ListWithSelection.class, ComboBoxTableCellRenderer.INSTANCE);
    setDefaultRenderer(String.class, new DefaultTableCellRenderer());
  }

  public void onAddClick(AnActionButton pButton)
  {
    SourceDataModel source = watch.getPit().getParent().getParent();
    assert source != null;
    model.addEmptyCondition((SettingsDataModel) source.getPit().getHierarchy().getValue(), source.getName(), watch.getName());
  }

  public void onRemoveClick(AnActionButton pButton)
  {
    List<ConditionDescriptionDataModel> toDelete = Arrays.stream(getSelectedRows())
        .mapToObj(pSelectedRow -> (ConditionDescriptionDataModel) watch.getConditionDescriptions().get(pSelectedRow))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    int reallyDeleteResult = JOptionPane.showConfirmDialog(this, "Do you really want to delete the selected conditions?", "Delete Conditions",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if(reallyDeleteResult == JOptionPane.YES_OPTION)
      toDelete.forEach(pCondition -> model.removeCondition((SettingsDataModel) watch.getPit().getHierarchy().getValue(), watch.getPit().getParent().getParent().getName(), watch.getName(), pCondition.getName()));
  }

  /**
   * TableModel-Impl for conditionTable
   */
  private class _Model extends AbstractTableModel
  {
    private final PropertyPitEventAdapter<IPropertyPitProvider, Object> propertyPitEventAdapter;

    public _Model()
    {
      propertyPitEventAdapter = new PropertyPitEventAdapter<IPropertyPitProvider, Object>()
      {
        @Override
        public void propertyRemoved(@NotNull IPropertyPitProvider pSource, @NotNull IPropertyDescription<IPropertyPitProvider, Object> pPropertyDescription, @NotNull Set<Object> pAttributes)
        {
          _propChanged();
        }

        @Override
        public void propertyAdded(@NotNull IPropertyPitProvider pSource, @NotNull IPropertyDescription<IPropertyPitProvider, Object> pPropertyDescription, @NotNull Set<Object> pAttributes)
        {
          _propChanged();
        }

        @Override
        public void propertyValueChanged(@NotNull IProperty<IPropertyPitProvider, Object> pProperty, Object pOldValue, Object pNewValue, @NotNull Set<Object> pAttributes)
        {
          _propChanged();
        }

        private void _propChanged()
        {
          fireTableChanged(new TableModelEvent(_Model.this, 0, Math.max(getRowCount() - 1, 0)));
          revalidate();
          repaint();
        }
      };
      watch.getPit().getHierarchy().addWeakListener(propertyPitEventAdapter);
    }

    @Override
    public int getRowCount()
    {
      return watch.getConditionDescriptions().size();
    }

    @Override
    public int getColumnCount()
    {
      return 3;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
      if(columnIndex == 0)
        return ListWithSelection.class;
      else if(columnIndex == 1)
        return ListWithSelection.class;
      else if(columnIndex == 2)
        return String.class;

      return Object.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
      if(rowIndex == -1 ||columnIndex == -1)
        return null;

      IConditionDescription description = watch.getConditionDescriptions().get(rowIndex);

      if(columnIndex == 0)
      {
        EConditionAttribute myAttribute = description.getAttribute();
        ListWithSelection<EConditionAttribute> allAttributes = new ListWithSelection<>(Arrays.asList(EConditionAttribute.values()));
        allAttributes.select(myAttribute);
        return allAttributes;
      }
      else if(columnIndex == 1)
      {
        EConditionAttribute myAttribute = description.getAttribute();
        ListWithSelection<EConditionOperator> allOperators = new ListWithSelection<>(Arrays.asList(myAttribute.getPossibleOperators()));
        allOperators.select(description.getOperator());
        return allOperators;
      }
      else if(columnIndex == 2)
      {
        List<String> possibleValues = description.getPossibleValues();
        return possibleValues.size() == 0 ? "" : possibleValues.get(0);
      }

      return null;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
      if(rowIndex == -1 ||columnIndex == -1)
        return;

      ConditionDescriptionDataModel description = (ConditionDescriptionDataModel) watch.getConditionDescriptions().get(rowIndex);

      if(columnIndex == 0)
        description.setAttribute((EConditionAttribute) value);
      else if(columnIndex == 1)
        description.setOperator((EConditionOperator) value);
      else if(columnIndex == 2)
        description.setPossibleValues(Collections.singletonList((String) value)); //todo more possible values
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
      return true;
    }
  }

}
