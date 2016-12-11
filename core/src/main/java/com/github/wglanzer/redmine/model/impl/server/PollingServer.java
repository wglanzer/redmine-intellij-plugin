package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.IRLoggingFacade;
import com.github.wglanzer.redmine.IRTaskCreator;
import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ISource;
import com.github.wglanzer.redmine.webservice.impl.RRestConnectionBuilder;
import com.github.wglanzer.redmine.webservice.spi.IRRestConnection;
import com.github.wglanzer.redmine.webservice.spi.IRRestRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
  private final IRLoggingFacade loggingFacade;
  private final IRTaskCreator taskCreator;
  private final PollingProjectDirectory directory;
  private final PollingExecutor executor;
  private final List<IServerListener> listenerList = new ArrayList<>();

  public PollingServer(IRLoggingFacade pLoggingFacade, IRTaskCreator pTaskCreator, ISource pSource)
  {
    taskCreator = pTaskCreator;
    source = pSource;
    connection = RRestConnectionBuilder.createConnection(pLoggingFacade, source.getURL(), source.getAPIKey(), source.getPageSize());
    loggingFacade = pLoggingFacade;
    directory = new PollingProjectDirectory(connection);
    executor = new PollingExecutor(pLoggingFacade, pTaskCreator, () ->
    {
      pollProjects();
      for(IProject currProject : getProjects())
        ((PollingProject) currProject).pollTickets();
    }, pSource.getPollInterval(), false);
  }

  @Override
  public void connectAsync()
  {
    taskCreator.executeInBackground(new IRTaskCreator.ITask()
    {
      @Override
      public String getName()
      {
        return "Tickets preloading...";
      }

      @Override
      public void accept(IRTaskCreator.IProgressIndicator pProgressIndicator)
      {
        try
        {
          performPreload(pProgressIndicator); // 80%

          // ... and start poll
          executor.start();
          pProgressIndicator.addPercentage(0.1);

          _fireConnectionStatusChanged(true);
          pProgressIndicator.addPercentage(0.1);
        }
        catch(Exception e)
        {
          loggingFacade.error(new Exception("Failed to preload server (url: '" + getURL() + "')", e));
        }
      }
    });
  }

  @Override
  public void disconnect()
  {
    // end listening
    _fireConnectionStatusChanged(false);
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

  @Nullable
  @Override
  public String getDisplayName()
  {
    return source.getDisplayName();
  }

  @Override
  public void addServerListener(IServerListener pListener)
  {
    synchronized(listenerList)
    {
      listenerList.add(pListener);
    }
  }

  @Override
  public void removeServerListener(IServerListener pListener)
  {
    synchronized(listenerList)
    {
      listenerList.remove(pListener);
    }
  }

  @Override
  public String toString()
  {
    return "Server: " + source.getDisplayName() + "\n" +
        "URL: " + source.getURL();
  }

  /**
   * Performs a preload of all server related data
   *
   * @param pProgressIndicator indicator for progress, you have "80%"
   */
  protected void performPreload(IRTaskCreator.IProgressIndicator pProgressIndicator) throws Exception
  {
    pollProjects(); //Load all Projects into directory
    pProgressIndicator.addPercentage(0.2);

    Collection<IProject> allProjects = getProjects();
    double percentagePerProject = 60.0 / (double) allProjects.size();
    for(IProject currProject : allProjects) // Load all tickets into projects directory
    {
      ((PollingProject) currProject).pollTickets();
      pProgressIndicator.addPercentage(percentagePerProject);
    }
  }

  /**
   * Polls all projects from redmien server and
   * performs update to IProject instances
   */
  protected void pollProjects() throws Exception
  {
    List<String> allOldProjectIDs = getProjects().stream()
        .map(IProject::getID)
        .collect(Collectors.toList());

    List<IProject> allNewProjects = connection.doGET(IRRestRequest.GET_PROJECTS).getResultNodes()
        .map(directory::updateProject)
        .collect(Collectors.toList());

    // Fire that a project was added
    allNewProjects.stream()
        .filter(pProject -> !allOldProjectIDs.contains(pProject.getID()))
        .forEach(this::_fireProjectAdded);

    // Remove all projects that are not in the result list from webservice
    getProjects().stream()
        .filter(pProject -> !allNewProjects.contains(pProject))
        .forEach((project) ->
        {
          _fireProjectRemoved(project);
          directory.removeProjectFromCache(project);
        });
  }

  /**
   * Fires that a project was created
   *
   * @param pCreated New created project
   */
  private void _fireProjectAdded(IProject pCreated)
  {
    synchronized(listenerList)
    {
      listenerList.forEach(pListener -> pListener.projectCreated(pCreated));
    }
  }

  /**
   * Fires that a project was removed
   *
   * @param pRemoved Removed project
   */
  private void _fireProjectRemoved(IProject pRemoved)
  {
    synchronized(listenerList)
    {
      listenerList.forEach(pListener -> pListener.projectRemoved(pRemoved));
    }
  }

  /**
   * Fires, that the status of the connection changed
   *
   * @param pIsConnectedNow <tt>true</tt>, if the server is running now
   */
  private void _fireConnectionStatusChanged(boolean pIsConnectedNow)
  {
    synchronized(listenerList)
    {
      listenerList.forEach(pListener -> pListener.connectionStatusChanged(pIsConnectedNow));
    }
  }

}
