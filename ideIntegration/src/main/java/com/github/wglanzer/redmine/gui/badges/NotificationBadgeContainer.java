package com.github.wglanzer.redmine.gui.badges;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a badge that can be shown on top of the redmineWidget
 *
 * @author w.glanzer, 12.02.2017.
 */
class NotificationBadgeContainer extends JPanel
{

  private final List<INotificationBadge> badges = new ArrayList<>();

  public NotificationBadgeContainer(INotificationBadge... pBadges)
  {
    for(INotificationBadge badge : pBadges)
      addBadge(badge);

    setPreferredSize(new Dimension(8, 8));
    setOpaque(true);
  }

  /**
   * Adds a new badge for this container
   *
   * @param pBadge Badge that should be added
   */
  public void addBadge(INotificationBadge pBadge)
  {
    synchronized(badges)
    {
      badges.add(pBadge);
    }
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    synchronized(badges)
    {
      badges.forEach(pBadge -> pBadge.paintBadge(getWidth(), getHeight(), g));
    }
  }
}
