package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.IRLoggingFacade;
import com.github.wglanzer.redmine.IRTaskCreator;
import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ISource;
import com.github.wglanzer.redmine.model.IWatch;
import com.github.wglanzer.redmine.util.WeakListenerList;
import com.github.wglanzer.redmine.webservice.impl.RRestConnectionBuilder;
import com.github.wglanzer.redmine.webservice.spi.IRRestConnection;
import com.github.wglanzer.redmine.webservice.spi.IRRestRequest;
import com.github.wglanzer.redmine.webservice.spi.IRRestResult;
import javafx.beans.property.SimpleBooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
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
  private final WeakListenerList<IServerListener> listenerList = new WeakListenerList<>();
  private final String id = UUID.randomUUID().toString();
  private final SimpleBooleanProperty invalidated = new SimpleBooleanProperty(false);

  public PollingServer(IRLoggingFacade pLoggingFacade, IRTaskCreator pTaskCreator, ISource pSource)
  {
    taskCreator = pTaskCreator;
    source = pSource;
    connection = RRestConnectionBuilder.createConnection(pLoggingFacade, source.getURL(), source.getAPIKey(), source.getPageSize());
    loggingFacade = pLoggingFacade;
    directory = new PollingProjectDirectory(connection);
    executor = new PollingExecutor(pLoggingFacade, pTaskCreator, () ->
    {
      _fireBusyStateChanged(true);
      pollProjects(false);
      for(IProject currProject : getProjects())
        ((PollingProject) currProject).pollTickets(false);
      _fireBusyStateChanged(false);
    }, pSource.getPollInterval(), false);
  }

  @NotNull
  @Override
  public String getID()
  {
    return id;
  }

  @Override
  public void connectAsync()
  {
    if(invalidated.get())
      throw new UnsupportedOperationException("A PollingServer can not be reopened!");

    taskCreator.executeInBackground(new IRTaskCreator.ITask()
    {
      @Override
      public String getName()
      {
        return "Preloading Tickets...";
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
    listenerList.clear();
    invalidated.set(true);
    executor.stop();
    directory.clearCaches();
  }

  @Override
  public boolean isValid()
  {
    return !invalidated.get() && connection.isValid();
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

  @NotNull
  @Override
  public List<IWatch> getWatches()
  {
    return source.getWatches();
  }

  @Override
  public void addWeakServerListener(IServerListener pListener)
  {
    synchronized(listenerList)
    {
      listenerList.add(pListener);
    }
  }

  @Override
  public void removeWeakServerListener(IServerListener pListener)
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
    pollProjects(true); //Load all Projects into directory
    pProgressIndicator.addPercentage(0.2);

    Collection<IProject> allProjects = getProjects();
    double percentagePerProject = 0.6D / (double) allProjects.size();
    for(IProject currProject : allProjects) // Load all tickets into projects directory
    {
      ((PollingProject) currProject).pollTickets(true);
      pProgressIndicator.addPercentage(percentagePerProject);
    }
  }

  /**
   * Polls all projects from redmien server and
   * performs update to IProject instances
   *
   * @param pIsPreload <tt>true</tt>, if this project was created during preload-phase
   */
  protected void pollProjects(boolean pIsPreload) throws Exception
  {
    List<String> allOldProjectIDs = getProjects().stream()
        .map(IProject::getID)
        .collect(Collectors.toList());

    IRRestResult result = connection.doGET(IRRestRequest.GET_PROJECTS, invalidated::not);
    if(result == null) // invalidated during loading
      return;

    List<IProject> allNewProjects = result.getResultNodes()
        .map(directory::updateProject)
        .collect(Collectors.toList());

    // Fire that a project was added
    allNewProjects.stream()
        .filter(pProject -> !allOldProjectIDs.contains(pProject.getID()))
        .forEach((created) -> _fireProjectAdded(created, pIsPreload));

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
   * @param pCreated              New created project
   * @param pCreatedDuringPreload <tt>true</tt>, if this project was created during preload-phase
   */
  private void _fireProjectAdded(IProject pCreated, boolean pCreatedDuringPreload)
  {
    synchronized(listenerList)
    {
      listenerList.forEach(pListener -> pListener.projectCreated(pCreated, pCreatedDuringPreload));
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


  /**
   * Fires, that the busy-state of the connection changed
   *
   * @param pIsBusyNow <tt>true</tt>, if the server is busy now
   */
  private void _fireBusyStateChanged(boolean pIsBusyNow)
  {
    synchronized(listenerList)
    {
      listenerList.forEach(pListener -> pListener.busyStateChanged(pIsBusyNow));
    }
  }

}
