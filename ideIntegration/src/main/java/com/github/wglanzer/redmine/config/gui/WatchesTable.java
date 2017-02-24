package com.github.wglanzer.redmine.config.gui;

import com.github.wglanzer.redmine.config.SourceDataModel;
import com.github.wglanzer.redmine.config.WatchDataModel;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Component inside settings.
 * Watches
 *
 * @author w.glanzer, 06.10.2016.
 */
class WatchesTable extends JBTable
{
  private final RAppSettingsModel model;
  private final Supplier<SourceDataModel> selectedSourceSupplier;

  public WatchesTable(RAppSettingsModel pModel, Supplier<SourceDataModel> pSelectedSourceSupplier)
  {
    setModel(new _Model(pModel, pSelectedSourceSupplier));
    setDefaultRenderer(Object.class, new _Renderer());
    model = pModel;
    selectedSourceSupplier = pSelectedSourceSupplier;

    getColumnModel().getColumn(0).setHeaderValue("Name");
    getColumnModel().getColumn(1).setHeaderValue("");
    setAutoResizeMode(AUTO_RESIZE_LAST_COLUMN);
    addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        int row = rowAtPoint(e.getPoint());
        if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
          _onDoubleClick((WatchDataModel) getModel().getValueAt(row, 0));
        else if(row == -1)
          getSelectionModel().clearSelection();
      }
    });
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
    List<WatchDataModel> toDelete = Arrays.stream(getSelectedRows())
        .mapToObj(pSelectedRow -> (WatchDataModel) getModel().getValueAt(pSelectedRow, 0))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    int reallyDeleteResult = JOptionPane.showConfirmDialog(this, "Do you really want to delete the selected watches?", "Delete Watches", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if(reallyDeleteResult == JOptionPane.YES_OPTION)
      toDelete.forEach(pWatch -> model.removeWatch(pWatch.getName()));
  }

  /**
   * Updates the GUI
   */
  public void update()
  {
    SwingUtilities.invokeLater(() -> {
      revalidate();
      repaint();
    });
  }

  /**
   * Will be called if the table was doubleclicked
   *
   * @param pSelectedWatch Watch which was doublelcicked
   */
  private void _onDoubleClick(@NotNull WatchDataModel pSelectedWatch)
  {
    ConditionDialog dialog = new ConditionDialog(this, model, pSelectedWatch);
    dialog.setVisible(true);
  }

  /**
   * Renderer-Impl
   */
  private static class _Renderer extends DefaultTableCellRenderer
  {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      WatchDataModel watch = (WatchDataModel) value;
      if(column == 0)
        setText(watch.getDisplayName());
      else
        setText(watch.toString());
      return this;
    }
  }

  /**
   * ListModel-Impl for WatchesTable
   */
  private class _Model extends AbstractTableModel
  {
    private final PropertyChangeListener propertyChangeListener;
    private final RAppSettingsModel model;
    private final Supplier<SourceDataModel> selectedSourceSupplier;

    public _Model(RAppSettingsModel pModel, Supplier<SourceDataModel> pSelectedSourceSupplier)
    {
      model = pModel;
      selectedSourceSupplier = pSelectedSourceSupplier;
      propertyChangeListener = evt -> {
        fireTableChanged(new TableModelEvent(this, 0, Math.max(getRowCount() - 1, 0)));
        revalidate();
        repaint();
      };
      model.addWeakPropertyChangeListener(propertyChangeListener);
    }

    @Override
    public int getRowCount()
    {
      SourceDataModel selectedSource = selectedSourceSupplier.get();
      if(selectedSource == null)
        return 0;
      else
        return selectedSource.getWatches().size();
    }

    @Override
    public int getColumnCount()
    {
      return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
      if(rowIndex < 0) //onClose
        return null;

      SourceDataModel selectedSource = selectedSourceSupplier.get();
      if(selectedSource == null)
        return null;
      else
        return selectedSource.getWatches().get(rowIndex);
    }
  }
}
