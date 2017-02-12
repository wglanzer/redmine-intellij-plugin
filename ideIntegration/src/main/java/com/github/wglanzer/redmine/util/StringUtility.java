package com.github.wglanzer.redmine.util;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author w.glanzer, 11.12.2016.
 */
public class StringUtility
{

  /**
   * Converts a ticket to a display String
   *
   * @param pServer server the ticket belongs to, can be <tt>null</tt>
   * @param pTicket ticket that should be converted
   * @return String, not <tt>null</tt>
   */
  @NotNull
  public static String toDisplayString(@Nullable IServer pServer, ITicket pTicket, boolean pIDAsURL)
  {
    String ticketID = pIDAsURL && pServer != null ? toURL(pServer, pTicket) : "#" + pTicket.getID();
    return ticketID + ": " + pTicket.getSubject();
  }

  /**
   * Converts a project to a display String
   *
   * @param pServer server the project belongs to, can be <tt>null</tt>
   * @param pProject project that should be converted
   * @return String, not <tt>null</tt>
   */
  @NotNull
  public static String toDisplayString(@Nullable IServer pServer, IProject pProject, boolean pIDAsURL)
  {
    return pIDAsURL ? toURL(pServer, pProject) : pProject.getName();
  }

  /**
   * Constructs the URL to get to a ticket directly
   *
   * @param pServer Server
   * @param pTicket Ticket
   * @return the url to the server -> ticket
   */
  @NotNull
  public static String toURL(IServer pServer, ITicket pTicket)
  {
    String url = pServer.getURL() + "/issues/" + pTicket.getID();
    return "<a href=\"" + url + "\">" + "#" + pTicket.getID() + "</a>";
  }

  /**
   * Constructs the URL to get to a project directly
   *
   * @param pServer  Server
   * @param pProject Project
   * @return the url to the server -> project
   */
  @NotNull
  public static String toURL(IServer pServer, IProject pProject)
  {
    String url = pServer.getURL() + "/projects/" + pProject.getID();
    return "<a href=\"" + url + "\">" + pProject.getID() + "</a>";
  }

  /**
   * <p>Abbreviates a String using ellipses. This will turn
   * "Now is the time for all good men" into "...is the time for..."</p>
   *
   * <p>Works like {@code abbreviate(String, int)}, but allows you to specify
   * a "left edge" offset.  Note that this left edge is not necessarily going to
   * be the leftmost character in the result, or the first character following the
   * ellipses, but it will appear somewhere in the result.
   *
   * <p>In no case will it return a String of length greater than
   * {@code maxWidth}.</p>
   *
   * <pre>
   * StringUtils.abbreviate(null, *, *)                = null
   * StringUtils.abbreviate("", 0, 4)                  = ""
   * StringUtils.abbreviate("abcdefghijklmno", -1, 10) = "abcdefg..."
   * StringUtils.abbreviate("abcdefghijklmno", 0, 10)  = "abcdefg..."
   * StringUtils.abbreviate("abcdefghijklmno", 1, 10)  = "abcdefg..."
   * StringUtils.abbreviate("abcdefghijklmno", 4, 10)  = "abcdefg..."
   * StringUtils.abbreviate("abcdefghijklmno", 5, 10)  = "...fghi..."
   * StringUtils.abbreviate("abcdefghijklmno", 6, 10)  = "...ghij..."
   * StringUtils.abbreviate("abcdefghijklmno", 8, 10)  = "...ijklmno"
   * StringUtils.abbreviate("abcdefghijklmno", 10, 10) = "...ijklmno"
   * StringUtils.abbreviate("abcdefghijklmno", 12, 10) = "...ijklmno"
   * StringUtils.abbreviate("abcdefghij", 0, 3)        = IllegalArgumentException
   * StringUtils.abbreviate("abcdefghij", 5, 6)        = IllegalArgumentException
   * </pre>
   *
   * @param str      the String to check, may be null
   * @param offset   left edge of source String
   * @param maxWidth maximum length of result String, must be at least 4
   * @return abbreviated String, {@code null} if null String input
   * @throws IllegalArgumentException if the width is too small
   * @since 2.0
   */
  public static String abbreviate(final String str, int offset, final int maxWidth)
  {
    if(str == null)
    {
      return null;
    }
    if(maxWidth < 4)
    {
      throw new IllegalArgumentException("Minimum abbreviation width is 4");
    }
    if(str.length() <= maxWidth)
    {
      return str;
    }
    if(offset > str.length())
    {
      offset = str.length();
    }
    if(str.length() - offset < maxWidth - 3)
    {
      offset = str.length() - (maxWidth - 3);
    }
    final String abrevMarker = "...";
    if(offset <= 4)
    {
      return str.substring(0, maxWidth - 3) + abrevMarker;
    }
    if(maxWidth < 7)
    {
      throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
    }
    if(offset + maxWidth - 3 < str.length())
    {
      return abrevMarker + abbreviate(str.substring(offset), 0, maxWidth - 3);
    }
    return abrevMarker + str.substring(str.length() - (maxWidth - 3));
  }

  /**
   * Converts an exception to a readable string
   *
   * @param pException Exception which is about to be shown
   * @return String
   */
  public static String toLogString(Throwable pException)
  {
    Throwable currEx = pException;
    StringBuilder builder = new StringBuilder();
    for(int i = 0; currEx != null; i++)
    {
      StringBuilder myExLine = new StringBuilder();
      for(int whitespaces = 0; whitespaces < i; whitespaces++)
        myExLine.append("  ");

      // Log exception
      myExLine.append(currEx.getClass()).append(": ").append(currEx.getLocalizedMessage());
      for(StackTraceElement currElement : currEx.getStackTrace())
        myExLine.append(" [->] ").append(currElement);

      // Shorten it
      if(myExLine.length() > 255)
        myExLine.delete(255, myExLine.length()).append("[...continued...]");

      currEx = currEx.getCause();
      if(currEx != null)
        myExLine.append("\n");
      builder.append(myExLine);
    }
    return builder.toString();
  }

}
