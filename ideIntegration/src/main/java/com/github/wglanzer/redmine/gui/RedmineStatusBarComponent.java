package com.github.wglanzer.redmine.gui;

import com.github.wglanzer.redmine.RManager;
import com.github.wglanzer.redmine.gui.badges.NotificationBadgeFactory;
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
class RedmineStatusBarComponent extends JPanel
{
  private static final int _SIZE = 18;
  private final RManager manager;
  private boolean busyState = false;

  public RedmineStatusBarComponent(RManager pManager)
  {
    manager = pManager;
    setLayout(new BorderLayout());
    setOpaque(true);
    setPreferredSize(new Dimension(_SIZE, _SIZE));
    _buildUI();

    // Tooltips
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
   * Sets, if the loading-state should be shown
   *
   * @param pIsBusyNow <tt>true</tt> if the plugin is currently doing some work
   */
  public void setBusy(boolean pIsBusyNow)
  {
    busyState = pIsBusyNow;
    SwingUtilities.invokeLater(() -> {
      revalidate();
      repaint();
    });
  }

  /**
   * Method executed on mouse click
   *
   * @param pMouseEvent event
   */
  private void _onClick(MouseEvent pMouseEvent)
  {
    if(/*pMouseEvent.getButton() == MouseEvent.BUTTON3 && */pMouseEvent.getClickCount() == 1)
      _showContextMenu(pMouseEvent.getPoint());
  }

  /**
   * Creats the UI-Components
   */
  private void _buildUI()
  {
    // Contruct LayeredPane
    JLayeredPane lp = new JLayeredPane();
    JPanel panel = new JPanel();
    panel.setOpaque(true);
    panel.setLayout(new BorderLayout());
    panel.setSize(_SIZE, _SIZE);
    JPanel glass = new JPanel();
    glass.setOpaque(false);
    glass.setLayout(new BorderLayout());
    glass.setSize(_SIZE, _SIZE);

    // Redmine-Icon
    ImageIcon icon = new ImageIcon(RManager.class.getResource("redmine_12.png"));
    JLabel iconLabel = new JLabel(icon);
    panel.add(iconLabel, BorderLayout.CENTER);

    // Badge
    JComponent badgeContainer = NotificationBadgeFactory.createContainer(NotificationBadgeFactory.createBusyBadge(() -> busyState));
    JPanel inner = new JPanel(new BorderLayout());
    inner.add(badgeContainer, BorderLayout.SOUTH);
    inner.setOpaque(false);
    glass.add(inner, BorderLayout.EAST);

    // Add LayeredPane to this component
    lp.add(panel, Integer.valueOf(1));
    lp.add(glass, Integer.valueOf(2));
    add(lp, BorderLayout.CENTER);
  }


  /**
   * Shows the context menu
   *
   * @param pPoint point at which the menu should be shown
   */
  private void _showContextMenu(Point pPoint)
  {
    JBPopupMenu menu = new JBPopupMenu();

    JBCheckboxMenuItem enableVisualFeedback = new JBCheckboxMenuItem("Visual Feedback");
    enableVisualFeedback.setState(manager.getPreferences().getCurrentSettings().isVisualFeedbackEnabled());
    enableVisualFeedback.addChangeListener(e -> manager.getPreferences().getCurrentSettings().setEnableVisualFeedback(enableVisualFeedback.getState()));
    menu.add(enableVisualFeedback);

    menu.addSeparator();

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
