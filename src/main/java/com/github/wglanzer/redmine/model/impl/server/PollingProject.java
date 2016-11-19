package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Project, that was created by PollingServer
 *
 * @author w.glanzer, 19.11.2016.
 * @see PollingServer
 */
class PollingProject implements IProject
{
  private final List<IProjectListener> projectListeners = new ArrayList<>();

  private final String id;
  private String name;
  private String description;
  private String createdOn;
  private String updatedOn;
  private boolean valid;

  public PollingProject(String pID, String pName, String pDescription, String pCreatedOn, String pUpdatedOn)
  {
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
  public String getCreatedOn()
  {
    return createdOn;
  }

  @Override
  public String getUpdatedOn()
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
  public Map<String, ITicket> getTickets()
  {
    return Collections.emptyMap(); //todo
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
  protected void updateProperties(String pName, String pDescription, String pCreatedOn, String pUpdatedOn, boolean pFireChanges)
  {
    if(!valid && pFireChanges)
      throw new RuntimeException("updated invalid project (projectID: " + id + ")");

    valid = false;
    Map<String, Map.Entry<String, String>> changedProps = new HashMap<>();

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
    name = null;
    description = null;
    createdOn = null;
    updatedOn = null;
    valid = false;
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
  private void _firePropertyChange(String pProperty, String pOldValue, String pNewValue)
  {
    synchronized(projectListeners)
    {
      projectListeners.forEach(pListener -> pListener.redminePropertyChanged(pProperty, pOldValue, pNewValue));
    }
  }
}
