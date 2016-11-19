package com.github.wglanzer.redmine.config.gui;

import com.github.wglanzer.redmine.config.gui.dialogs.AddSourceDialog;
import com.github.wglanzer.redmine.config.model.RAppSettingsModel;
import com.github.wglanzer.redmine.model.ISource;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;

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
    try
    {
      AddSourceDialog dialog = new AddSourceDialog();
      dialog.setSize(400, 150);
      dialog.setLocationRelativeTo(SwingUtilities.getRoot(this));
      dialog.setVisible(true);

      String url = dialog.getURL();
      String apiKey = dialog.getAPIKey();
      int pollInterval = dialog.getPollInterval();
      if(url != null && apiKey != null)
        model.addSource(url, apiKey, pollInterval);
    }
    catch(MalformedURLException ignored)
    {
      JOptionPane.showMessageDialog(SwingUtilities.getRoot(this), "Invalid url!", "Exception", JOptionPane.ERROR_MESSAGE);
    }
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
      ISource source = (ISource) value;
      setText(source.getURL());
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
