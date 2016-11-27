package com.github.wglanzer.redmine.webservice.spi;

import com.github.wglanzer.redmine.webservice.impl.RRestRequestImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Contains all available requests to
 * redmine server via REST-API
 *
 * @author w.glanzer, 27.11.2016.
 */
public interface IRRestRequest
{

  /**
   * GET_PROJECTS is a request to query all
   * available projects from a redmine server
   */
  IRRestRequest GET_PROJECTS = new RRestRequestImpl().subpage("projects").resultTopLevel("projects");

  /**
   * GET_TICKETS is a request to query all
   * available tickets from a redmine server.
   * It should contain the "project_id"-argument to filter by projectid
   */
  IRRestRequest GET_TICKETS = new RRestRequestImpl().subpage("issues").resultTopLevel("issues");

  /**
   * This method sets the subpage-ID
   * For example: "projects" to query "http://redmine.myurl.com/projects.json"
   *
   * @param pSubPage Subpage as string, or <tt>null</tt> to clear
   * @return a new instance-copy of this request
   */
  IRRestRequest subpage(@NotNull String pSubPage);

  /**
   * This method sets the result toplevel.
   * If the redmine server API answers with:
   * projects:
   *   project:
   *     ...
   *   project:
   *     ...
   *
   * you can set "projects" as result toplevel, so that "projects"
   * wont be shown in result-Stream within the connection
   *
   * @param pTopLevelResult toplevel as string, or <tt>null</tt> to clear
   * @return a new instance-copy of this request
   */
  IRRestRequest resultTopLevel(@Nullable String pTopLevelResult);

  /**
   * Adds an argument to this query
   *
   * @param pArgument Argument key
   * @param pValue    Argument value
   * @return a new instance-copy of this request
   */
  IRRestRequest argument(@NotNull String pArgument, @Nullable String pValue);

  /**
   * Returns the subpage-ID of this request
   *
   * @return subpage-ID as String, not <tt>null</tt>
   */
  @NotNull
  String getSubPage();

  /**
   * Returns the result toplevel of this request
   *
   * @return result toplevel as String, not <tt>null</tt>
   */
  @Nullable
  String getResultTopLevel();

  /**
   * A Map of all set arguments
   *
   * @return all arguments
   */
  @NotNull
  Map<String, String> getArguments();

}
