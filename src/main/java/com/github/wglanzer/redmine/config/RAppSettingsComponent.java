package com.github.wglanzer.redmine.config;

import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Visualization for Redmine configuration
 *
 * @author w.glanzer, 04.10.2016.
 */
class RAppSettingsComponent extends JPanel
{

  private final ISettings currentSettings;

  private AtomicBoolean modified = new AtomicBoolean(false);

  public RAppSettingsComponent(ISettings pCurrentSettings)
  {
    currentSettings = pCurrentSettings;
    _initComponents();
  }

  /**
   * Applies this visualization to an settings instance
   *
   * @param pSettings  instance
   */
  public void applyTo(RMutableSettings pSettings)
  {
    pSettings.setExample(UUID.randomUUID().toString());
  }

  /**
   * Returns the modified state of the configuration
   *
   * @return <tt>true</tt>, if the configuration was modified
   */
  public boolean isModified()
  {
    return modified.get();
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
    add(ToolbarDecorator.createDecorator(_createSourceList())
        .setAddAction(new _AddSourceAction())
        .setRemoveAction(new _RemoveSourceAction())
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
    add(ToolbarDecorator.createDecorator(_createWatchList())
        .setAddAction(new _AddWatchAction())
        .setRemoveAction(new _RemoveWatchAction())
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
  private JList _createSourceList()
  {
    return new JBList(new CollectionListModel<>("meinElement1", "meinElement2", "meinElement3"));
  }

  /**
   * Constructs the list of all defined watches
   *
   * @return JList, not <tt>null</tt>
   */
  @NotNull
  private JList _createWatchList()
  {
    return new JBList(new CollectionListModel<>("meineWatch1", "meineWatch2", "meineWatch3"));
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

  /**
   * Implementates an action which is called when the "remove" button
   * from the sources list was pressed
   */
  private static class _RemoveSourceAction implements AnActionButtonRunnable
  {
    @Override
    public void run(AnActionButton pAnActionButton)
    {

    }
  }

  /**
   * Implementates an action which is called when the "add" button
   * from the sources list was pressed
   */
  private static class _AddSourceAction implements AnActionButtonRunnable
  {
    @Override
    public void run(AnActionButton pAnActionButton)
    {

    }
  }

  /**
   * Implementates an action which is called when the "remove" button
   * from the watches list was pressed
   */
  private static class _RemoveWatchAction implements AnActionButtonRunnable
  {
    @Override
    public void run(AnActionButton pAnActionButton)
    {

    }
  }

  /**
   * Implementates an action which is called when the "add" button
   * from the watches list was pressed
   */
  private static class _AddWatchAction implements AnActionButtonRunnable
  {
    @Override
    public void run(AnActionButton pAnActionButton)
    {

    }
  }
}
