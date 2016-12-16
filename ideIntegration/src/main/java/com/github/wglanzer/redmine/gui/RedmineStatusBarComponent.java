package com.github.wglanzer.redmine.gui;

import com.github.wglanzer.redmine.RManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Displayable component for the statusBarWidget
 *
 * @author w.glanzer, 16.12.2016.
 */
public class RedmineStatusBarComponent extends JPanel
{

  private final JLabel iconLabel;

  public RedmineStatusBarComponent()
  {
    setLayout(new BorderLayout());
    ImageIcon icon = new ImageIcon(RManager.class.getResource("redmine_12.png"));
    iconLabel = new JLabel(icon);
    add(iconLabel, BorderLayout.CENTER);
  }

  public void onClick(MouseEvent pMouseEvent)
  {

  }

}
