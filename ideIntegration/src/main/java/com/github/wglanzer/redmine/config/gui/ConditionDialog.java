package com.github.wglanzer.redmine.config.gui;

import com.github.wglanzer.redmine.config.WatchDataModel;
import com.github.wglanzer.redmine.util.propertly.BulkModifyHierarchy;
import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.ui.ToolbarDecorator;
import de.adito.propertly.core.common.path.PropertyPath;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * ConfigurationDialog for conditions in a watch
 *
 * @author w.glanzer, 26.02.2017.
 */
class ConditionDialog extends JDialog
{

  private final RAppSettingsModel model;
  private final WatchDataModel watch;
  private BulkModifyHierarchy<?> bulkedHierarchy;

  public ConditionDialog(Component owner, RAppSettingsModel pModel, WatchDataModel pWatch)
  {
    super(SwingUtilities.getWindowAncestor(owner), "Edit Watch '" + pWatch.getDisplayName() + "'");
    model = pModel;
    bulkedHierarchy = new BulkModifyHierarchy<>(pWatch.getPit().getHierarchy());
    watch = (WatchDataModel) new PropertyPath(pWatch).find(bulkedHierarchy).getValue();
    setSize(500, 310);
    setMinimumSize(new Dimension(500, 250));
    setLocationRelativeTo(SwingUtilities.getWindowAncestor(owner));
    setLayout(new BorderLayout(5, 5));
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setModal(true);
    ((JComponent) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));
    _initComponents();
  }

  /**
   * Initializes all components
   */
  private void _initComponents()
  {
    JTextField nameField = new JTextField(watch.getDisplayName());
    nameField.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        watch.setDisplayName(nameField.getText());
      }
    });

    JPanel topPanel = new JPanel(new BorderLayout(5, 0));
    topPanel.add(new JLabel("Name:"), BorderLayout.WEST);
    topPanel.add(nameField, BorderLayout.CENTER);
    add(topPanel, BorderLayout.NORTH);

    ConditionTable conditionTable = new ConditionTable(model, watch);
    JPanel conditionTableContainer = ToolbarDecorator.createDecorator(conditionTable)
        .setToolbarPosition(ActionToolbarPosition.TOP)
        .setAddAction(conditionTable::onAddClick)
        .setRemoveAction(conditionTable::onRemoveClick)
        .disableUpAction()
        .disableDownAction()
        .setPanelBorder(new LineBorder(Color.LIGHT_GRAY))
        .createPanel();
    conditionTableContainer.requestFocusInWindow();
    add(conditionTableContainer, BorderLayout.CENTER);

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> setVisible(false));

    JButton okButton = new JButton("OK");
    okButton.addActionListener(e -> {
      bulkedHierarchy.writeBack();
      setVisible(false);
    });
    okButton.setPreferredSize(cancelButton.getPreferredSize()); //both same size
    getRootPane().setDefaultButton(okButton);

    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    bottom.add(okButton);
    bottom.add(Box.createHorizontalStrut(5));
    bottom.add(cancelButton);
    add(bottom, BorderLayout.SOUTH);
  }

}
