package com.github.wglanzer.redmine.notifiers.balloons;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;

/**
 * Global Hyperlink-Listeners for balloons, which opens links in browser
 *
 * @author w.glanzer, 14.12.2016.
 */
class BalloonHyperlinkListener implements HyperlinkListener
{
  @Override
  public void hyperlinkUpdate(HyperlinkEvent e)
  {
    if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
    {
      try
      {
        // open URLs
        Desktop.getDesktop().browse(e.getURL().toURI());
      }
      catch(Exception ex)
      {
        throw new RuntimeException(ex);
      }
    }
  }
}
