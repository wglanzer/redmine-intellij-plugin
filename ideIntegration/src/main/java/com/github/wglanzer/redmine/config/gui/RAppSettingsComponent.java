package com.github.wglanzer.redmine.config.gui;

import com.github.wglanzer.redmine.config.beans.RSourceBean;
import com.github.wglanzer.redmine.model.ISource;
import com.google.common.base.MoreObjects;
import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.ToolbarDecorator;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Objects;

/**
 * Visualization for Redmine configuration
 *
 * @author w.glanzer, 04.10.2016.
 */
public class RAppSettingsComponent extends JPanel
{

  private static final Color _MANDATORY_INPUT_COLOR = Color.ORANGE;

  private RAppSettingsModel model;
  private RSourceBean selectedSource;

  private Splitter splitter;
  private SourcesList sourceList;
  private JPanel sourcesListPanel;
  private JPanel watchesListPanel;
  private JPanel contentPane;

  // UI Components
  private JTextField urlField;
  private JTextField apiKeyField;
  private JSpinner pollIntervall;
  private JSpinner pageSize;
  private JLabel urlPanel;
  private JLabel apiKeyPanel;
  private JLabel intervalPanel;
  private JLabel pageSizePanel;
  private JTabbedPane tab;

  public RAppSettingsComponent(RAppSettingsModel pModel)
  {
    super(new BorderLayout());
    model = pModel;
    $$$setupUI$$$();
    splitter.setFirstComponent(sourcesListPanel);
    splitter.setSecondComponent(contentPane);
    add(splitter, BorderLayout.CENTER);

    // init
    _selectedSourceChanged(null);

    // URL-Field
    urlField.setBackground(_MANDATORY_INPUT_COLOR);
    urlField.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        if(selectedSource != null)
        {
          _fireChange("url", selectedSource.getURL(), urlField.getText());
          selectedSource.setUrl(urlField.getText());
        }
      }
    });

    // API-Key
    apiKeyField.setBackground(_MANDATORY_INPUT_COLOR);
    apiKeyField.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        if(selectedSource != null)
        {
          _fireChange("apikey", selectedSource.getAPIKey(), apiKeyField.getText());
          selectedSource.setApiKey(apiKeyField.getText());
        }
      }
    });

    // PollIntervall
    pollIntervall.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        if(selectedSource != null)
        {
          _fireChange("pollinterval", selectedSource.getPollInterval(), pollIntervall.getValue());
          selectedSource.setPollingInterval(Integer.valueOf(String.valueOf(pollIntervall.getValue())));
        }
      }
    });
    pollIntervall.addChangeListener(e ->
    {
      if(selectedSource != null && pollIntervall.getValue() != null)
      {
        _fireChange("pollinterval", selectedSource.getPollInterval(), pollIntervall.getValue());
        selectedSource.setPollingInterval(Integer.valueOf(String.valueOf(pollIntervall.getValue())));
      }
    });

    // PageSize
    pageSize.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        if(selectedSource != null && pageSize.getValue() != null)
        {
          _fireChange("pagesize", selectedSource.getPageSize(), pageSize.getValue());
          selectedSource.setPageSize(Integer.valueOf(String.valueOf(pageSize.getValue())));
        }
      }
    });
    pageSize.addChangeListener(e ->
    {
      if(selectedSource != null && pageSize.getValue() != null)
      {
        _fireChange("pagesize", selectedSource.getPageSize(), pageSize.getValue());
        selectedSource.setPageSize(Integer.valueOf(String.valueOf(pageSize.getValue())));
      }
    });

    // If a source was added, select it!
    model.addPropertyChangeListener(evt ->
    {
      if(Objects.equals(evt.getPropertyName(), RAppSettingsModel.PROP_SOURCES))
      {
        // removed
        if(evt.getOldValue() != null && evt.getNewValue() == null)
        {
          if(sourceList.getSelectedIndex() >= sourceList.getModel().getSize()) // selection is outside of range
            _selectedSourceChanged(null);
        }
        // added
        else if(evt.getOldValue() == null && evt.getNewValue() != null)
          sourceList.setSelectedIndex(sourceList.indexOf((ISource) evt.getNewValue()));
      }
    });

    // Select first entry
    SwingUtilities.invokeLater(() ->
    {
      Object el = sourceList.getModel().getElementAt(0);
      if(el != null)
        sourceList.setSelectedIndex(0);
    });
  }

  /**
   * Disposes this component
   */
  public void dispose()
  {
  }

  /**
   * Called, when the selected ISource was changed
   *
   * @param pSelectedSource Currently active SourceBean
   */
  private void _selectedSourceChanged(@Nullable RSourceBean pSelectedSource)
  {
    selectedSource = pSelectedSource;
    boolean enableSelected = pSelectedSource != null;
    urlPanel.setEnabled(enableSelected);
    intervalPanel.setEnabled(enableSelected);
    pageSizePanel.setEnabled(enableSelected);
    apiKeyPanel.setEnabled(enableSelected);
    urlField.setEnabled(enableSelected);
    apiKeyField.setEnabled(enableSelected);
    pollIntervall.setEnabled(enableSelected);
    pageSize.setEnabled(enableSelected);
    urlField.setText(pSelectedSource != null ? pSelectedSource.getURL() : "");
    apiKeyField.setText(pSelectedSource != null ? pSelectedSource.getAPIKey() : "");
    pollIntervall.setValue(MoreObjects.firstNonNull(pSelectedSource != null ? pSelectedSource.getPollInterval() : null, 0));
    pageSize.setValue(MoreObjects.firstNonNull(pSelectedSource != null ? pSelectedSource.getPageSize() : null, 0));
  }

  /**
   * Intellij GUI Designer -> custom UI Components
   */
  private void createUIComponents()
  {
    sourceList = new SourcesList(model);
    sourceList.addListSelectionListener(e -> {
      if(sourceList.getSelectedIndices().length == 1) // only 1 selected entry
        _selectedSourceChanged((RSourceBean) sourceList.getSelectedValue());
      else
        _selectedSourceChanged(null);
    });
    sourcesListPanel = ToolbarDecorator.createDecorator(sourceList)
        .setToolbarPosition(ActionToolbarPosition.TOP)
        .setAddAction(sourceList::onAddClick)
        .setRemoveAction(sourceList::onRemoveClick)
        .disableUpAction()
        .disableDownAction()
        .createPanel();

    WatchesList watchesList = new WatchesList(model);
    watchesListPanel = ToolbarDecorator.createDecorator(watchesList)
        .setToolbarPosition(ActionToolbarPosition.LEFT)
        .setAddAction(watchesList)
        .setRemoveAction(watchesList)
        .disableUpAction()
        .disableDownAction()
        .createPanel();

    splitter = new Splitter(false, 0.3F);
  }

  /**
   * Fires, that something has changed on gui
   *
   * @param pProperty Property that has changed
   * @param pOldValue Old value
   * @param pNewValue New value
   */
  private void _fireChange(String pProperty, Object pOldValue, Object pNewValue)
  {
    model.firePropertyChanged(pProperty, pOldValue, pNewValue);
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$()
  {
    createUIComponents();
    contentPane = new JPanel();
    contentPane.setLayout(new BorderLayout(0, 0));
    tab = new JTabbedPane();
    contentPane.add(tab, BorderLayout.CENTER);
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridBagLayout());
    tab.addTab("General", panel1);
    panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
    urlPanel = new JLabel();
    urlPanel.setEnabled(false);
    urlPanel.setText("URL:");
    GridBagConstraints gbc;
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(0, 0, 0, 5);
    panel1.add(urlPanel, gbc);
    urlField = new JTextField();
    urlField.setEnabled(false);
    urlField.setText("");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(1, 0, 1, 0);
    panel1.add(urlField, gbc);
    apiKeyPanel = new JLabel();
    apiKeyPanel.setEnabled(false);
    apiKeyPanel.setText("API-Key:");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(5, 0, 0, 5);
    panel1.add(apiKeyPanel, gbc);
    apiKeyField = new JTextField();
    apiKeyField.setEnabled(false);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 0, 0, 0);
    panel1.add(apiKeyField, gbc);
    intervalPanel = new JLabel();
    intervalPanel.setEnabled(false);
    intervalPanel.setText("Interval (sec):");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(5, 0, 0, 5);
    panel1.add(intervalPanel, gbc);
    pollIntervall = new JSpinner();
    pollIntervall.setEnabled(false);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 0, 0, 0);
    panel1.add(pollIntervall, gbc);
    pageSizePanel = new JLabel();
    pageSizePanel.setEnabled(false);
    pageSizePanel.setText("PageSize:");
    pageSizePanel.setToolTipText("Minimum: 1\nMaximum: 100");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(5, 0, 0, 5);
    panel1.add(pageSizePanel, gbc);
    pageSize = new JSpinner();
    pageSize.setEnabled(false);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 0, 0, 0);
    panel1.add(pageSize, gbc);
    final JPanel spacer1 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.weightx = 0.1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(spacer1, gbc);
    final JPanel spacer2 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 4;
    gbc.weightx = 0.15;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(spacer2, gbc);
    final JPanel spacer3 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 4;
    gbc.weightx = 0.35;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(spacer3, gbc);
    final JPanel spacer4 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 5;
    gbc.weighty = 0.1;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer4, gbc);
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridBagLayout());
    panel2.setEnabled(true);
    panel2.setVisible(true);
    tab.addTab("Watches", panel2);
    tab.setEnabledAt(1, false);
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0.9;
    gbc.weighty = 0.8;
    gbc.fill = GridBagConstraints.BOTH;
    panel2.add(watchesListPanel, gbc);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$()
  {
    return contentPane;
  }
}
