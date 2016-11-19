package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.IRLoggingFacade;
import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ISource;
import com.github.wglanzer.redmine.webservice.impl.RRestConnection;
import com.github.wglanzer.redmine.webservice.spi.ERRestRequest;
import com.github.wglanzer.redmine.webservice.spi.IRRestConnection;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements an server, which polls every fixed period
 *
 * @author w.glanzer, 16.11.2016.
 */
public class PollingServer implements IServer
{
  public static final int DEFAULT_POLLINTERVAL = 300;

  private final ISource source;
  private final IRRestConnection connection;
  private final PollingProjectDirectory directory;
  private final PollingExecutor executor;

  public PollingServer(ISource pSource, IRLoggingFacade pLoggingFacade)
  {
    source = pSource;
    connection = new RRestConnection(source.getURL(), source.getAPIKey(), pLoggingFacade);
    directory = new PollingProjectDirectory();
    executor = new PollingExecutor(this::pollProjects, pSource.getPollInterval());
  }

  @Override
  public void connect()
  {
    // start listening ...
    performPreload();

    // ... and start poll
    executor.start();
  }

  @Override
  public void disconnect()
  {
    // end listening
    executor.stop();
    directory.clearCaches();
  }

  @Override
  public boolean isValid()
  {
    return connection.isValid();
  }

  @NotNull
  @Override
  public Collection<IProject> getProjects()
  {
    return directory.getProjects();
  }

  @NotNull
  @Override
  public String getURL()
  {
    return source.getURL();
  }

  /**
   * Performs a preload of all server related data
   */
  protected void performPreload()
  {
    pollProjects(); //Load all Projects into directory
  }

  /**
   * Polls all projects from redmien server and
   * performs update to IProject instances
   */
  protected void pollProjects()
  {
    List<IProject> allProjects = connection.doGET(ERRestRequest.GET_PROJECTS)
        .map(directory::updateProject)
        .collect(Collectors.toList());

    // Remove all projects that are not in the result list from webservice
    getProjects().stream()
        .filter(pProject -> !allProjects.contains(pProject))
        .forEach(directory::removeProjectFromCache);
  }

}
