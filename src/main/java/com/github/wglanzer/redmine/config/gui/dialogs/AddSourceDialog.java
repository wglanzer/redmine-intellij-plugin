package com.github.wglanzer.redmine.config.gui.dialogs;

import com.google.common.base.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

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

}
