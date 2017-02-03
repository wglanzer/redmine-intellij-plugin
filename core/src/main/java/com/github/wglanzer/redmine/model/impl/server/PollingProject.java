package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.ITicket;
import com.github.wglanzer.redmine.util.WeakListenerList;
import com.github.wglanzer.redmine.webservice.spi.IRRestArgument;
import com.github.wglanzer.redmine.webservice.spi.IRRestConnection;
import com.github.wglanzer.redmine.webservice.spi.IRRestRequest;
import com.github.wglanzer.redmine.webservice.spi.IRRestResult;
import javafx.beans.property.SimpleBooleanProperty;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Project, that was created by PollingServer
 *
 * @author w.glanzer, 19.11.2016.
 * @see PollingServer
 */
class PollingProject implements IProject
{
  private final WeakListenerList<IProjectListener> projectListeners = new WeakListenerList<>();
  private final PollingTicketDirectory ticketDirectory;
  private final IRRestConnection connection;
  private final String id;

  private String name;
  private String description;
  private Instant createdOn;
  private Instant updatedOn;
  private SimpleBooleanProperty invalidated;

  public PollingProject(IRRestConnection pRestConnection, String pID, String pName, String pDescription, Instant pCreatedOn, Instant pUpdatedOn)
  {
    connection = pRestConnection;
    ticketDirectory = new PollingTicketDirectory(pID);
    updateProperties(pName, pDescription, pCreatedOn, pUpdatedOn, false);
    id = pID;
    invalidated = new SimpleBooleanProperty(false);
  }

  @Override
  public String getID()
  {
    return id;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  @Override
  public Instant getCreatedOn()
  {
    return createdOn;
  }

  @Override
  public Instant getUpdatedOn()
  {
    return updatedOn;
  }

  @Override
  public boolean isValid()
  {
    return !invalidated.get();
  }

  @NotNull
  @Override
  public Collection<ITicket> getTickets()
  {
    return ticketDirectory.getTickets();
  }

  @Override
  public void addWeakProjectListener(@NotNull IProjectListener pListener)
  {
    synchronized(projectListeners)
    {
      projectListeners.add(pListener);
    }
  }

  @Override
  public void removeWeakProjectListener(@NotNull IProjectListener pListener)
  {
    synchronized(projectListeners)
    {
      projectListeners.remove(pListener);
    }
  }

  /**
   * Updates all properties to new values, if neccessary.
   *
   * @param pFireChanges <tt>true</tt>, if valueChanges should be fired to listeners
   */
  protected void updateProperties(String pName, String pDescription, Instant pCreatedOn, Instant pUpdatedOn, boolean pFireChanges)
  {
    if(invalidated.get() && pFireChanges)
      throw new RuntimeException("updated invalid project (projectID: " + id + ")");

    Map<String, Map.Entry<Object, Object>> changedProps = new HashMap<>();

    if(!Objects.equals(name, pName))
    {
      changedProps.put(IProjectListener.PROP_NAME, new AbstractMap.SimpleEntry<>(name, pName));
      name = pName;
    }

    if(!Objects.equals(description, pDescription))
    {
      changedProps.put(IProjectListener.PROP_DESCRIPTION, new AbstractMap.SimpleEntry<>(description, pDescription));
      description = pDescription;
    }

    if(!Objects.equals(createdOn, pCreatedOn))
    {
      changedProps.put(IProjectListener.PROP_CREATEDON, new AbstractMap.SimpleEntry<>(createdOn, pCreatedOn));
      createdOn = pCreatedOn;
    }

    if(!Objects.equals(updatedOn, pUpdatedOn))
    {
      changedProps.put(IProjectListener.PROP_UPDATEDON, new AbstractMap.SimpleEntry<>(updatedOn, pUpdatedOn));
      updatedOn = pUpdatedOn;
    }

    // Fire changed properties
    if(pFireChanges && !changedProps.isEmpty())
      _firePropertiesChanged(Collections.unmodifiableMap(changedProps));
  }

  /**
   * Releases all ressources this project needs
   */
  protected void destroy()
  {
    projectListeners.clear();
    invalidated.set(true);
    ticketDirectory.clearCaches();
    name = null;
    description = null;
    createdOn = null;
    updatedOn = null;
  }

  /**
   * Polls all tickets from redmine server and
   * performs update to ITicket instances
   *
   * @param pIsPreload <tt>true</tt>, if this project was created during preload-phase
   */
  protected void pollTickets(boolean pIsPreload) throws Exception
  {
    List<Long> allOldTicketIDs = getTickets().stream()
        .map(ITicket::getID)
        .collect(Collectors.toList());

    // Build main request -> Show only MY tickets, no tickets from other projects needed!
    IRRestRequest request = IRRestRequest.GET_TICKETS
        .argument(IRRestArgument.PROJECT_ID.value(id))
        .argument(IRRestArgument.TICKET_STATUS.value("*"));

    // Get only updated tickets. ">lastUpdatedTicket.updated_on"
    ITicket lastUpdatedTicket = ticketDirectory.getLastUpdatedTicket();
    if(lastUpdatedTicket != null)
      request = request.argument(IRRestArgument.UPDATED_ON.value("%3E%3D" + lastUpdatedTicket.getUpdatedOn().plusSeconds(1))); //>2014-01-02T08:12:32Z

    // Execute Request
    IRRestResult result = connection.doGET(request, invalidated::not);
    if(result == null) // project invalidated during loading
      return;

    List<ITicket> allNewTickets = ticketDirectory.updateTickets(result.getResultNodes());

    // Fire that a ticket was added
    allNewTickets.stream()
        .filter(pTicket -> !allOldTicketIDs.contains(pTicket.getID()))
        .forEach(pTicket -> _fireTicketAdded(pTicket, pIsPreload));
  }

  /**
   * Fires, that a ticket was added
   *
   * @param pAddedTicket Added ticket
   * @param pIsPreload   <tt>true</tt>, if this project was created during preload-phase
   */
  private void _fireTicketAdded(ITicket pAddedTicket, boolean pIsPreload)
  {
    synchronized(projectListeners)
    {
      projectListeners.forEach(pListener -> pListener.ticketAdded(pAddedTicket, pIsPreload));
    }
  }

  /**
   * Fires, that redmine properties have changed
   *
   * @param pProperties Properties that were changed
   */
  private void _firePropertiesChanged(Map<String, Map.Entry<Object, Object>> pProperties)
  {
    synchronized(projectListeners)
    {
      projectListeners.forEach(pListener -> pListener.redminePropertiesChanged(pProperties));
    }
  }
}
