package com.github.wglanzer.redmine.gui;

import com.github.wglanzer.redmine.RManager;
import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;
import com.intellij.openapi.ui.JBCheckboxMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Displayable component for the statusBarWidget
 *
 * @author w.glanzer, 16.12.2016.
 */
public class RedmineStatusBarComponent extends JPanel
{

  private final JLabel iconLabel;
  private final RManager manager;

  public RedmineStatusBarComponent(RManager pManager)
  {
    manager = pManager;
    setLayout(new BorderLayout());
    ImageIcon icon = new ImageIcon(RManager.class.getResource("redmine_12.png"));
    iconLabel = new JLabel(icon);
    add(iconLabel, BorderLayout.CENTER);
    ToolTipManager.sharedInstance().registerComponent(this);
    addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        _onClick(e);
      }
    });
  }

  @Override
  public String getToolTipText()
  {
    List<IServer> servers = manager.getServerManager().getAvailableServers();
    List<IProject> projects = servers.stream().flatMap(pServer -> pServer.getProjects().stream()).collect(Collectors.toList());
    List<ITicket> tickets = projects.stream().flatMap(pProject -> pProject.getTickets().stream()).collect(Collectors.toList());
    return "Loaded " + servers.size() + " server" + (servers.size() > 1 ? "s" : "") +
        ", containing " + projects.size() + " project" + (projects.size() > 1 ? "s" : "") +
        " and " + tickets.size() + " ticket" + (tickets.size() > 1 ? "s" : "");
  }

  /**
   * Method executed on mouse click
   *
   * @param pMouseEvent event
   */
  public void _onClick(MouseEvent pMouseEvent)
  {
    if(/*pMouseEvent.getButton() == MouseEvent.BUTTON3 && */pMouseEvent.getClickCount() == 1)
      _showContextMenu(pMouseEvent.getPoint());
  }

  /**
   * Shows the context menu
   *
   * @param pPoint point at which the menu should be shown
   */
  private void _showContextMenu(Point pPoint)
  {
    JBPopupMenu menu = new JBPopupMenu();

    JBCheckboxMenuItem enableNotifications = new JBCheckboxMenuItem("Enable notifications");
    enableNotifications.setState(manager.getPreferences().getCurrentSettings().isEnableNotifications());
    enableNotifications.addChangeListener(e -> manager.getPreferences().getCurrentSettings().setEnableNotifications(enableNotifications.getState()));
    menu.add(enableNotifications);

    JBCheckboxMenuItem enableLog = new JBCheckboxMenuItem("Enable logging");
    enableLog.setState(manager.getPreferences().getCurrentSettings().isEnableLog());
    enableLog.addChangeListener(e -> manager.getPreferences().getCurrentSettings().setEnableLog(enableLog.getState()));
    menu.add(enableLog);

    menu.show(this, pPoint.x, pPoint.y);
  }
}
