package com.github.wglanzer.redmine.webservice.spi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Contains all Rest-Requests redmine can answer
 *
 * @author w.glanzer, 19.11.2016.
 */
public enum ERRestRequest
{

  GET_ISSUES("issues", null),
  GET_PROJECTS("projects", "projects");

  private String subPage;
  private String resultTopLevel;

  ERRestRequest(String pSubPage, String pResultTopLevel)
  {
    subPage = pSubPage;
    resultTopLevel = pResultTopLevel;
  }

  /**
   * Returns the subpage as string.
   * A subpage is: "http://redmine.mydomain.com/[SUBPAGE].json"
   *
   * @return Subpage as string
   */
  @NotNull
  public String getSubPage()
  {
    return subPage;
  }

  /**
   * If the result is an array with a "toplevel"-Object it returns the toplevel-object.
   * For example "projects" is the toplevel-object for:
   * projects {
   *   project {
   *     [...]
   *   },
   *   project {
   *     [...]
   *   }
   * }
   *
   * @return The toplevel-object, or <tt>null</tt> if no toplevel-object is needed
   */
  @Nullable
  public String getResultTopLevel()
  {
    return resultTopLevel;
  }
}
