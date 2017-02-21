package com.github.wglanzer.redmine.gui.badges;

import java.awt.*;

/**
 * A Badge that can be shown besides the Widget
 *
 * @author w.glanzer, 21.02.2017.
 */
public interface INotificationBadge
{

  /**
   * Paints the badge on the graphcis object
   *
   * @param pWidth    Width of the complete badge
   * @param pHeight   Height of the complete badge
   * @param pGraphics Graphics to be drawn on
   */
  void paintBadge(int pWidth, int pHeight, Graphics pGraphics);

}
