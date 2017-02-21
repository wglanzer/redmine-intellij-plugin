package com.github.wglanzer.redmine.gui.badges;

import com.github.wglanzer.redmine.RManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

/**
 * Factory for badges
 *
 * @author w.glanzer, 21.02.2017.
 */
public class NotificationBadgeFactory
{

  /**
   * Creates the container that holds the badges
   *
   * @param pBadges Badges to be initially shown
   * @return Component
   */
  public static JComponent createContainer(INotificationBadge... pBadges)
  {
    return new NotificationBadgeContainer(pBadges);
  }

  /**
   * Creates a badge that shows the "busy"-state
   *
   * @param pIsBusy supplier for the busy-state
   * @return the badge
   */
  public static INotificationBadge createBusyBadge(Supplier<Boolean> pIsBusy)
  {
    return new _BusyBadge(pIsBusy);
  }

  /**
   * Badge for the busy-State
   */
  private static class _BusyBadge implements INotificationBadge
  {
    private final Color COLOR = Color.cyan.darker();
    private final Supplier<Boolean> busySupplier;

    public _BusyBadge(Supplier<Boolean> pBusySupplier)
    {
      busySupplier = pBusySupplier;
    }

    @Override
    public void paintBadge(int pWidth, int pHeight, Graphics pGraphics)
    {
      if(busySupplier.get() && RManager.getInstance().getPreferences().getCurrentSettings().isVisualFeedbackEnabled())
      {
        pGraphics.setColor(COLOR);
        pGraphics.fillRect(2, 2, 4, 4);
      }
    }
  }

}
