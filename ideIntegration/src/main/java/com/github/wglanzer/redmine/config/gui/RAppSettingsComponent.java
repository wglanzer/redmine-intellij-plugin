package com.github.wglanzer.redmine.config.gui;

import com.github.wglanzer.redmine.config.SettingsDataModel;
import com.github.wglanzer.redmine.config.SourceDataModel;
import com.github.wglanzer.redmine.model.ISource;
import com.google.common.base.MoreObjects;
import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.ToolbarDecorator;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

/**
 * Visualization for Redmine configuration
 *
 * @author w.glanzer, 04.10.2016.
 */
public class RAppSettingsComponent extends JPanel
{
  @SuppressWarnings("FieldCanBeLocal")
  private final PropertyChangeListener modelChangeListenerStrongRef;

  private RAppSettingsModel model;
  private SourceDataModel selectedSource;

  private Splitter splitter;
  private SourcesList sourceList;
  private JPanel sourcesListPanel;
  private JPanel watchesTablePanel;
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
  private JTextField displayName;
  private JLabel displayNamePanel;
  private JCheckBox disableCertCheck;
  private JLabel intervalLabelDesc;
  private JLabel pagesizeLabelDesc;
  private JPanel generalTabPanel;
  private WatchesTable watchesTable;

  public RAppSettingsComponent(RAppSettingsModel pModel)
  {
    super(new BorderLayout());
    model = pModel;
    $$$setupUI$$$();
    splitter.setFirstComponent(sourcesListPanel);
    splitter.setSecondComponent(contentPane);
    add(splitter, BorderLayout.CENTER);
    add(new JLabel("*: Mandatory input"), BorderLayout.SOUTH);

    // NO GOD DAMN SCROLLBARS!!
    setPreferredSize(new Dimension(0, 0));

    // init
    _selectedSourceChanged(null);

    // URL-Field
    urlField.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        if(selectedSource != null)
          selectedSource.setUrl(urlField.getText());
      }
    });

    // API-Key
    apiKeyField.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        if(selectedSource != null)
          selectedSource.setApiKey(apiKeyField.getText());
      }
    });

    // DisplayName
    displayName.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        if(selectedSource != null)
          selectedSource.setDisplayName(displayName.getText());
      }
    });

    // PollIntervall
    Runnable renewPollingInterval = () ->
    {
      if(selectedSource != null && pollIntervall.getValue() != null && !Objects.equals(pollIntervall.getValue(), selectedSource.getPollInterval()))
      {
        Integer newVal = Integer.valueOf(String.valueOf(pollIntervall.getValue()));
        if(newVal >= 5)
          selectedSource.setPollingInterval(newVal);
        else
          pollIntervall.setValue(selectedSource.getPollInterval());
      }
    };
    pollIntervall.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        renewPollingInterval.run();
      }
    });
    pollIntervall.addChangeListener(e -> renewPollingInterval.run());

    // PageSize
    Runnable renewPageSize = () ->
    {
      if(selectedSource != null && pageSize.getValue() != null && !Objects.equals(pageSize.getValue(), selectedSource.getPageSize()))
      {
        Integer newVal = Integer.valueOf(String.valueOf(pageSize.getValue()));
        if(newVal >= 1 && newVal <= 100)
          selectedSource.setPageSize(newVal);
        else
          pageSize.setValue(selectedSource.getPageSize());
      }
    };
    pageSize.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        renewPageSize.run();
      }
    });
    pageSize.addChangeListener(e -> renewPageSize.run());

    // If a source was added, select it!
    modelChangeListenerStrongRef = evt ->
    {
      if(Objects.equals(evt.getPropertyName(), SettingsDataModel.sources.getName())) // todo does not work...
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
    };
    model.addWeakPropertyChangeListener(modelChangeListenerStrongRef);

    // Steal focus listeners
    MouseAdapter ma = new MouseAdapter()
    {
      @Override
      public void mousePressed(MouseEvent e)
      {
        generalTabPanel.requestFocusInWindow();
      }
    };
    generalTabPanel.addMouseListener(ma);

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

  public void reset()
  {
    sourceList.clearSelection();
    _selectedSourceChanged(null); //just to go safe
  }

  /**
   * Called, when the selected ISource was changed
   *
   * @param pSelectedSource Currently active SourceBean
   */
  private void _selectedSourceChanged(@Nullable SourceDataModel pSelectedSource)
  {
    selectedSource = pSelectedSource;
    boolean enableSelected = pSelectedSource != null;
    urlPanel.setEnabled(enableSelected);
    intervalPanel.setEnabled(enableSelected);
    pageSizePanel.setEnabled(enableSelected);
    displayNamePanel.setEnabled(enableSelected);
    intervalLabelDesc.setEnabled(enableSelected);
    pagesizeLabelDesc.setEnabled(enableSelected);
    apiKeyPanel.setEnabled(enableSelected);
    urlField.setEnabled(enableSelected);
    apiKeyField.setEnabled(enableSelected);
    pollIntervall.setEnabled(enableSelected);
    pageSize.setEnabled(enableSelected);
    displayName.setEnabled(enableSelected);
    watchesTable.setEnabled(enableSelected);
    watchesTable.update();
    urlField.setText(pSelectedSource != null ? pSelectedSource.getURL() : "");
    apiKeyField.setText(pSelectedSource != null ? pSelectedSource.getAPIKey() : "");
    displayName.setText(pSelectedSource != null ? pSelectedSource.getDisplayName() : "");
    pollIntervall.setValue(MoreObjects.firstNonNull(pSelectedSource != null ? pSelectedSource.getPollInterval() : null, 0));
    pageSize.setValue(MoreObjects.firstNonNull(pSelectedSource != null ? pSelectedSource.getPageSize() : null, 0));
    disableCertCheck.setSelected(pSelectedSource != null && !pSelectedSource.isCheckCeritifacte());
  }

  /**
   * Intellij GUI Designer -> custom UI Components
   */
  private void createUIComponents()
  {
    sourceList = new SourcesList(model);
    sourceList.addListSelectionListener(e ->
    {
      if(sourceList.getSelectedIndices().length == 1) // only 1 selected entry
        _selectedSourceChanged((SourceDataModel) sourceList.getSelectedValue());
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

    watchesTable = new WatchesTable(model, () -> selectedSource);
    watchesTablePanel = ToolbarDecorator.createDecorator(watchesTable)
        .setToolbarPosition(ActionToolbarPosition.TOP)
        .setAddAction(watchesTable::onAddClick)
        .setRemoveAction(watchesTable::onRemoveClick)
        .disableUpAction()
        .disableDownAction()
        .setPanelBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY))
        .createPanel();

    splitter = new Splitter(false, 0.3F);
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
    contentPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-4144960)), null));
    contentPane.add(watchesTablePanel, BorderLayout.CENTER);
    generalTabPanel = new JPanel();
    generalTabPanel.setLayout(new GridBagLayout());
    contentPane.add(generalTabPanel, BorderLayout.NORTH);
    generalTabPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), null));
    urlPanel = new JLabel();
    urlPanel.setEnabled(false);
    urlPanel.setText("URL*:");
    GridBagConstraints gbc;
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(5, 0, 0, 10);
    generalTabPanel.add(urlPanel, gbc);
    urlField = new JTextField();
    urlField.setEnabled(false);
    urlField.setText("");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridwidth = 4;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 0, 0, 0);
    generalTabPanel.add(urlField, gbc);
    apiKeyPanel = new JLabel();
    apiKeyPanel.setEnabled(false);
    apiKeyPanel.setText("API-Key*:");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(5, 0, 0, 10);
    generalTabPanel.add(apiKeyPanel, gbc);
    apiKeyField = new JTextField();
    apiKeyField.setEnabled(false);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 5;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 0, 0, 0);
    generalTabPanel.add(apiKeyField, gbc);
    intervalPanel = new JLabel();
    intervalPanel.setEnabled(false);
    intervalPanel.setText("Interval:");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(5, 0, 0, 10);
    generalTabPanel.add(intervalPanel, gbc);
    pollIntervall = new JSpinner();
    pollIntervall.setEnabled(false);
    pollIntervall.setMaximumSize(new Dimension(75, 32767));
    pollIntervall.setMinimumSize(new Dimension(75, 26));
    pollIntervall.setPreferredSize(new Dimension(75, 26));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 0, 0, 0);
    generalTabPanel.add(pollIntervall, gbc);
    pageSizePanel = new JLabel();
    pageSizePanel.setEnabled(false);
    pageSizePanel.setText("PageSize:");
    pageSizePanel.setToolTipText("Minimum: 1\nMaximum: 100");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(5, 0, 0, 10);
    generalTabPanel.add(pageSizePanel, gbc);
    pageSize = new JSpinner();
    pageSize.setEnabled(false);
    pageSize.setMaximumSize(new Dimension(75, 32767));
    pageSize.setMinimumSize(new Dimension(75, 26));
    pageSize.setPreferredSize(new Dimension(75, 26));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 0, 0, 0);
    generalTabPanel.add(pageSize, gbc);
    displayName = new JTextField();
    displayName.setEnabled(false);
    displayName.setText("");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.gridwidth = 5;
    gbc.weightx = 1.0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    generalTabPanel.add(displayName, gbc);
    displayNamePanel = new JLabel();
    displayNamePanel.setEnabled(false);
    displayNamePanel.setText("Name:");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(0, 0, 0, 10);
    generalTabPanel.add(displayNamePanel, gbc);
    disableCertCheck = new JCheckBox();
    disableCertCheck.setEnabled(false);
    disableCertCheck.setLabel("disable Cert-Check");
    disableCertCheck.setText("disable Cert-Check");
    disableCertCheck.setToolTipText("Certificate-Check is disabled temporarily");
    gbc = new GridBagConstraints();
    gbc.gridx = 5;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(5, 0, 0, 0);
    generalTabPanel.add(disableCertCheck, gbc);
    intervalLabelDesc = new JLabel();
    intervalLabelDesc.setEnabled(false);
    intervalLabelDesc.setText("sec");
    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(5, 5, 0, 0);
    generalTabPanel.add(intervalLabelDesc, gbc);
    pagesizeLabelDesc = new JLabel();
    pagesizeLabelDesc.setEnabled(false);
    pagesizeLabelDesc.setText("min: 1, max: 100");
    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(5, 5, 0, 0);
    generalTabPanel.add(pagesizeLabelDesc, gbc);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$()
  {
    return contentPane;
  }
}
