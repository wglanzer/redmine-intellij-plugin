package com.github.wglanzer.redmine.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Defines the component for the toolwindow
 *
 * @author w.glanzer, 19.11.2016.
 */
public class RedmineToolComponent extends JPanel
{
  private JPanel rootPanel;

  public RedmineToolComponent()
  {
    setLayout(new BorderLayout());
    add(rootPanel, BorderLayout.CENTER);
  }
}
