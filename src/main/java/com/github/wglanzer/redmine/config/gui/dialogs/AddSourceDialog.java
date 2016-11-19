package com.github.wglanzer.redmine.config.gui.dialogs;

import com.github.wglanzer.redmine.model.impl.server.PollingServer;
import com.google.common.base.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

/**
 * Dialog to enter redmine url and api key
 *
 * @author w.glanzer, 19.11.2016.
 */
public class AddSourceDialog extends JDialog
{
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextField urlField;
  private JTextField apiKeyField;
  private JSpinner pollIntervall;

  public AddSourceDialog()
  {
    setTitle("Add Source");
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(e -> dispose());
    buttonCancel.addActionListener(e -> dispose());

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        dispose();
      }
    });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    // Default-pollintervall = 5min
    pollIntervall.setValue(PollingServer.DEFAULT_POLLINTERVAL);
  }

  /**
   * @return Returns the entered URL, or
   * <tt>null</tt> if the entered URL was ''
   */
  public String getURL()
  {
    return Strings.emptyToNull(urlField.getText());
  }

  /**
   * @return Returns the entered API-Key, or
   * <tt>null</tt> if the entered API-Key was ''
   */
  public String getAPIKey()
  {
    return Strings.emptyToNull(apiKeyField.getText());
  }

  /**
   * Returns the entered polling interval
   *
   * @return poll interval as integer, min=0
   */
  public int getPollInterval()
  {
    try
    {
      return Math.max(0, Integer.parseInt(Objects.toString(pollIntervall.getValue())));
    }
    catch(NumberFormatException e)
    {
      return PollingServer.DEFAULT_POLLINTERVAL;
    }
  }

  {
    // GUI initializer generated by IntelliJ IDEA GUI Designer
    // >>> IMPORTANT!! <<<
    // DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
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
    contentPane = new JPanel();
    contentPane.setLayout(new GridBagLayout());
    contentPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
    urlField = new JTextField();
    GridBagConstraints gbc;
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.weightx = 100.0;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    contentPane.add(urlField, gbc);
    final JLabel label1 = new JLabel();
    label1.setText("URL:");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(0, 0, 0, 5);
    contentPane.add(label1, gbc);
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.BOTH;
    contentPane.add(panel1, gbc);
    buttonOK = new JButton();
    buttonOK.setText("OK");
    panel1.add(buttonOK);
    buttonCancel = new JButton();
    buttonCancel.setText("Cancel");
    panel1.add(buttonCancel);
    apiKeyField = new JTextField();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    contentPane.add(apiKeyField, gbc);
    final JLabel label2 = new JLabel();
    label2.setText("API-Key:");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(0, 0, 0, 5);
    contentPane.add(label2, gbc);
    pollIntervall = new JSpinner();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    contentPane.add(pollIntervall, gbc);
    final JLabel label3 = new JLabel();
    label3.setText("Poll-Interval:");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new Insets(0, 0, 0, 5);
    contentPane.add(label3, gbc);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$()
  {
    return contentPane;
  }
}
