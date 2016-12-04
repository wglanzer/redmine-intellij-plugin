package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.ITicket;
import com.github.wglanzer.redmine.webservice.spi.IRRestConnection;
import com.github.wglanzer.redmine.webservice.spi.IRRestRequest;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.temporal.ChronoField;
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
  private final List<IProjectListener> projectListeners = new ArrayList<>();
  private final PollingTicketDirectory ticketDirectory;
  private final IRRestConnection connection;
  private final String id;

  private String name;
  private String description;
  private Instant createdOn;
  private Instant updatedOn;
  private boolean valid;

  public PollingProject(IRRestConnection pRestConnection, String pID, String pName, String pDescription, Instant pCreatedOn, Instant pUpdatedOn)
  {
    connection = pRestConnection;
    ticketDirectory = new PollingTicketDirectory(pID);
    updateProperties(pName, pDescription, pCreatedOn, pUpdatedOn, false);
    id = pID;
    valid = true;
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
    return valid;
  }

  @NotNull
  @Override
  public Collection<ITicket> getTickets()
  {
    return ticketDirectory.getTickets();
  }

  @Override
  public void addProjectListener(@NotNull IProjectListener pListener)
  {
    synchronized(projectListeners)
    {
      projectListeners.add(pListener);
    }
  }

  @Override
  public void removeProjectListener(@NotNull IProjectListener pListener)
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
    if(!valid && pFireChanges)
      throw new RuntimeException("updated invalid project (projectID: " + id + ")");

    valid = false;
    Map<String, Map.Entry<Object, Object>> changedProps = new HashMap<>();

    if(!Objects.equals(name, pName))
    {
      changedProps.put("name", new AbstractMap.SimpleEntry<>(name, pName));
      name = pName;
    }

    if(!Objects.equals(description, pDescription))
    {
      changedProps.put("description", new AbstractMap.SimpleEntry<>(description, pDescription));
      description = pDescription;
    }

    if(!Objects.equals(createdOn, pCreatedOn))
    {
      changedProps.put("createdOn", new AbstractMap.SimpleEntry<>(createdOn, pCreatedOn));
      createdOn = pCreatedOn;
    }

    if(!Objects.equals(updatedOn, pUpdatedOn))
    {
      changedProps.put("updatedOn", new AbstractMap.SimpleEntry<>(updatedOn, pUpdatedOn));
      updatedOn = pUpdatedOn;
    }

    valid = true;

    if(pFireChanges)
      changedProps.forEach((pChangedKey, pOldNewValues) -> _firePropertyChange(pChangedKey, pOldNewValues.getKey(), pOldNewValues.getValue()));
  }

  /**
   * Releases all ressources this project needs
   */
  protected void destroy()
  {
    ticketDirectory.clearCaches();
    name = null;
    description = null;
    createdOn = null;
    updatedOn = null;
    valid = false;
  }

  /**
   * Polls all tickets from redmine server and
   * performs update to ITicket instances
   */
  protected void pollTickets() throws Exception
  {
    List<Long> allOldTicketIDs = getTickets().stream()
        .map(ITicket::getID)
        .collect(Collectors.toList());

    // Build main request -> Show only MY tickets, no tickets from other projects needed!
    IRRestRequest request = IRRestRequest.GET_TICKETS
        .argument("project_id", id);

    // Get only updated tickets. ">lastUpdatedTicket.updated_on"
    ITicket lastUpdatedTicket = ticketDirectory.getLastUpdatedTicket();
    if(lastUpdatedTicket != null)
      request = request.argument("updated_on", "%3E%3D" + lastUpdatedTicket.getUpdatedOn().plusSeconds(1).toString()); //>2014-01-02T08:12:32Z

    // Execute Request
    List<ITicket> allNewTickets = connection.doGET(request)
        .map(ticketDirectory::updateTicket)
        .collect(Collectors.toList());

    // Fire that a ticket was added
    allNewTickets.stream()
        .filter(pProject -> !allOldTicketIDs.contains(pProject.getID()))
        .forEach(this::_fireTicketAdded);

    // Remove all tickets that are not in the result list from webservice
    getTickets().stream()
        .filter(pProject -> !allNewTickets.contains(pProject))
        .forEach((project) ->
        {
          _fireTicketRemoved(project);
          ticketDirectory.removeTicketFromCache(project);
        });
  }

  /**
   * Fires, that a ticket was added
   *
   * @param pAddedTicket Added ticket
   */
  private void _fireTicketAdded(ITicket pAddedTicket)
  {
    synchronized(projectListeners)
    {
      projectListeners.forEach(pListener -> pListener.ticketAdded(pAddedTicket));
    }
  }

  /**
   * Fires, that a ticket was removed
   *
   * @param pRemovedTicket Removed ticket
   */
  private void _fireTicketRemoved(ITicket pRemovedTicket)
  {
    synchronized(projectListeners)
    {
      projectListeners.forEach(pListener -> pListener.ticketRemoved(pRemovedTicket));
    }
  }

  /**
   * Fires, that a redmine property has Changed
   *
   * @param pProperty Property that has changed
   * @param pOldValue Old value of this property
   * @param pNewValue New value for this property
   */
  private void _firePropertyChange(String pProperty, Object pOldValue, Object pNewValue)
  {
    synchronized(projectListeners)
    {
      projectListeners.forEach(pListener -> pListener.redminePropertyChanged(pProperty, pOldValue, pNewValue));
    }
  }
}
