package com.github.wglanzer.redmine.config.gui;

import com.github.wglanzer.redmine.config.model.RAppSettingsModel;
import com.intellij.ui.ToolbarDecorator;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Visualization for Redmine configuration
 *
 * @author w.glanzer, 04.10.2016.
 */
public class RAppSettingsComponent extends JPanel
{

  private RAppSettingsModel model;

  public RAppSettingsComponent(RAppSettingsModel pModel)
  {
    model = pModel;
    _initComponents();
  }

  /**
   * Disposes this component
   */
  public void dispose()
  {
  }

  /**
   * Initializes the GUI
   */
  private void _initComponents()
  {
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    int inset = 7;

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 2;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.anchor = GridBagConstraints.NORTHWEST;
    gbc.weightx = 10;
    gbc.weighty = 100;
    gbc.insets = new Insets(0, 0, 0, inset);
    SourcesList sourceList = _createSourceList();
    add(ToolbarDecorator.createDecorator(sourceList)
        .setAddAction(sourceList)
        .setRemoveAction(sourceList)
        .disableUpAction()
        .disableDownAction()
        .setAsUsualTopToolbar()
        .setLineBorder(1, 1, 1, 1)
        .createPanel(), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 90;
    gbc.weighty = 60;
    gbc.insets = new Insets(0, 0, inset, 0);
    WatchesList watchesList = _createWatchList();
    add(ToolbarDecorator.createDecorator(watchesList)
        .setAddAction(watchesList)
        .setRemoveAction(watchesList)
        .disableUpAction()
        .disableDownAction()
        .setAsUsualTopToolbar()
        .setLineBorder(1, 1, 1, 1)
        .createPanel(), gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 90;
    gbc.weighty = 40;
    gbc.insets = new Insets(0, 0, 0, 0);
    add(_createOptionsPanel(), gbc);
  }

  /**
   * Constructs the list of all source-URLs
   *
   * @return JList, not <tt>null</tt>
   */
  @NotNull
  private SourcesList _createSourceList()
  {
    return new SourcesList(model);
  }

  /**
   * Constructs the list of all defined watches
   *
   * @return JList, not <tt>null</tt>
   */
  @NotNull
  private WatchesList _createWatchList()
  {
    return new WatchesList(model);
  }

  /**
   * Constructs the options panel
   *
   * @return JPanel, not <tt>null</tt>
   */
  @NotNull
  private JPanel _createOptionsPanel()
  {
    JPanel panel = new JPanel();
    panel.setOpaque(true);
    panel.setBackground(Color.RED);
    return panel;
  }

}
