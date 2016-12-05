package com.github.wglanzer.redmine.webservice.spi;

import com.github.wglanzer.redmine.webservice.impl.RRestRequestImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
   * Adds an argument to this query
   *
   * @param pArgument Argument
   * @return a new instance-copy of this request
   */
  IRRestRequest argument(@NotNull IRRestArgument pArgument);

  /**
   * A List of all set arguments
   *
   * @return all arguments
   */
  @NotNull
  ArrayList<IRRestArgument> getArguments();

}
