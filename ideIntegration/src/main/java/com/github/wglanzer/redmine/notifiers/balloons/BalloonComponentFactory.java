package com.github.wglanzer.redmine.notifiers.balloons;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;
import com.github.wglanzer.redmine.util.StringUtility;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Factory for IntelliJ Balloons
 *
 * @author w.glanzer, 14.12.2016.
 */
public class BalloonComponentFactory
{

  private static final Color _NOTIFICATION_COLOR = MessageType.INFO.getPopupBackground();
  private static final Color _LINE_CHANGE_COLOR = Color.RED.darker();
  private static final int _FADEOUT_TIME = 7000;

  /**
   * Creates a balloon which informs, that a ticket has changed
   *
   * @param pServer            Server that caused this change
   * @param pTicket            Ticket that caused this change
   * @param pChangedProperties Properties that have changed.
   *                           The map contains only those properties, who are worth to be notified ("updatedOn"..)
   *                           <tt>null</tt> or empty, if this ticket was newly created (or should just be shown)
   * @return new balloon
   */
  @NotNull
  public static Balloon createTicketChangedBalloon(@NotNull IServer pServer, @NotNull ITicket pTicket, @Nullable Map<String, Map.Entry<Object, Object>> pChangedProperties)
  {
    String myID = StringUtility.toURL(pServer, pTicket);
    _Line[] lines = _extractLines(pChangedProperties, "Category: " + pTicket.getCategory(), "Priority: " + pTicket.getPriority());
    return _createRedmineChangedBalloon(myID, pTicket.getSubject(),
        lines[0].line, lines[0].hasChanged ? _LINE_CHANGE_COLOR : null, lines[1].line, lines[1].hasChanged ? _LINE_CHANGE_COLOR : null);
  }

  /**
   * Creates a balloon which informs, that a project has changed
   *
   * @param pServer            Server that caused this change
   * @param pProject           Project that caused this change
   * @param pChangedProperties Properties that have changed.
   *                           The map contains only those properties, who are worth to be notified ("updatedOn"..)
   *                           <tt>null</tt> or empty, if this ticket was newly created (or should just be shown)
   * @return new balloon
   */
  @NotNull
  public static Balloon createProjectChangedBalloon(@NotNull IServer pServer, @NotNull IProject pProject, @Nullable Map<String, Map.Entry<Object, Object>> pChangedProperties)
  {
    String myID = StringUtility.toURL(pServer, pProject);
    _Line[] lines = _extractLines(pChangedProperties, "Description: " + pProject.getDescription(), "CreatedOn: " + pProject.getCreatedOn());
    return _createRedmineChangedBalloon(myID, pProject.getName(),
        lines[0].line, lines[0].hasChanged ? _LINE_CHANGE_COLOR : null, lines[1].line, lines[1].hasChanged ? _LINE_CHANGE_COLOR : null);
  }

  /**
   * Creates a balloon, which can present HTML-Text
   *
   * @param pMessage Message, which should be notified (html)
   * @param pType    Type of this balloon
   * @return the balloon
   */
  @NotNull
  public static Balloon createHTMLBalloon(String pMessage, MessageType pType)
  {
    return JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(pMessage, pType, new BalloonHyperlinkListener())
        .setFadeoutTime(_FADEOUT_TIME)
        .setHideOnLinkClick(true)
        .createBalloon();
  }

  /**
   * Creates a balloon which informs, that something in redmine has changed
   *
   * @param pID         ID of ticket/project that has changed
   * @param pTitle      title of ticket/project
   * @param pLine1      String which will be shown on line 1
   * @param pLine1Color Color, that line1 will have
   * @param pLine2      String which will be shown on line 2
   * @param pLine2Color Color, that line2 will have
   * @return new Balloon
   */
  @NotNull
  private static Balloon _createRedmineChangedBalloon(String pID, String pTitle, String pLine1, Color pLine1Color, String pLine2, Color pLine2Color)
  {
    return JBPopupFactory.getInstance().createBalloonBuilder(new BalloonRedmineChanged(pID, pTitle, pLine1, pLine1Color, pLine2, pLine2Color))
        .setFillColor(_NOTIFICATION_COLOR)
        .setFadeoutTime(_FADEOUT_TIME)
        .setCloseButtonEnabled(true)
        .setHideOnLinkClick(true)
        .createBalloon();
  }

  /**
   * Extracts lines which can be presented 2 user.
   *
   * @param pChangedProperties Properties that maybe have changed
   * @param pDefaultLines      Defaultlines
   * @return Array with lines. Exact same size as pDefaultLines
   */
  private static _Line[] _extractLines(@Nullable Map<String, Map.Entry<Object, Object>> pChangedProperties, String... pDefaultLines)
  {
    _Line[] result = new _Line[pDefaultLines.length];
    for(int i = 0; i < pDefaultLines.length; i++)
    {
      _Line line = new _Line();
      line.line = pDefaultLines[i];
      result[i] = line;
    }

    if(pChangedProperties != null && pChangedProperties.size() > 0)
    {
      List<_Line> changedPropertyLines = pChangedProperties.entrySet().stream()
          .map(pPropertyEntry ->
          {
            String propName = pPropertyEntry.getKey();
            Object oldVal = pPropertyEntry.getValue().getKey();
            Object newVal = pPropertyEntry.getValue().getValue();
            _Line line = new _Line();
            line.line = propName + ": " + oldVal + " -> " + newVal;
            line.hasChanged = true;
            return line;
          })
          .limit(pDefaultLines.length)
          .collect(Collectors.toList());

      for(int i = 0; i < changedPropertyLines.size(); i++)
      {
        if(i == result.length)
          break;
        result[result.length - 1 - i] = changedPropertyLines.get(i);
      }

      return result;
    }
    else
      return result;
  }

  /**
   * Bag for _extractLines
   *
   * @see BalloonComponentFactory#_extractLines(Map, String...)
   */
  private static class _Line
  {
    private String line;
    private boolean hasChanged = false;
  }
}
