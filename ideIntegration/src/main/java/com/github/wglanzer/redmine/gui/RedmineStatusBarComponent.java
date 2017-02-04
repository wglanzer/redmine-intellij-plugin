package com.github.wglanzer.redmine.gui;

import com.github.wglanzer.redmine.RManager;
import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;

import javax.swing.*;
import java.awt.*;
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
  }

  public void onClick(MouseEvent pMouseEvent)
  {

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
}
