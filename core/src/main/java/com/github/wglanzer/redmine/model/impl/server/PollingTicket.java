package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.*;

/**
 * Ticket that was created by PollingTicketDirectory
 *
 * @author w.glanzer, 19.11.2016.
 */
class PollingTicket implements ITicket
{
  private final List<ITicketListener> listenerList = new ArrayList<>();
  private final long id;
  private String subject;
  private String description;
  private Instant updatedOn;
  private Instant createdOn;
  private String status;
  private String author;
  private String priority;
  private String tracker;
  private String category;

  public PollingTicket(long pID, String pSubject, String pDescription, Instant pCreatedOn, Instant pUpdatedOn, String pStatus, String pAuthor, String pPriority, String pTracker, String pCategory)
  {
    id = pID;
    updateProperties(pSubject, pDescription, pCreatedOn, pUpdatedOn, pStatus, pAuthor, pPriority, pTracker, pCategory, false);
  }

  @Override
  public long getID()
  {
    return id;
  }

  @NotNull
  @Override
  public String getSubject()
  {
    return subject;
  }

  @NotNull
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

  @NotNull
  @Override
  public String getStatus()
  {
    return status;
  }

  @NotNull
  @Override
  public String getAuthor()
  {
    return author;
  }

  @NotNull
  @Override
  public String getPriority()
  {
    return priority;
  }

  @NotNull
  @Override
  public String getTracker()
  {
    return tracker;
  }

  @NotNull
  @Override
  public String getCategory()
  {
    return category;
  }

  @NotNull
  @Override
  public Map<String, String> getAdditionalProperties()
  {
    return Collections.emptyMap(); //todo
  }

  @Override
  public void addTicketListener(@NotNull ITicketListener pListener)
  {
    synchronized(listenerList)
    {
      listenerList.add(pListener);
    }
  }

  @Override
  public void removeTicketListener(@NotNull ITicketListener pListener)
  {
    synchronized(listenerList)
    {
      listenerList.remove(pListener);
    }
  }

  protected void destroy()
  {
    subject = null;
    description = null;
    status = null;
    author = null;
    priority = null;
    tracker = null;
    category = null;
  }

  protected boolean updateProperties(String pSubject, String pDescription, Instant pCreatedOn, Instant pUpdatedOn, String pStatus, String pAuthor, String pPriority, String pTracker, String pCategory, boolean pFireChanges)
  {
    Map<String, Map.Entry<Object, Object>> changedProps = new HashMap<>();

    if(!Objects.equals(subject, pSubject))
    {
      changedProps.put("subject", new AbstractMap.SimpleEntry<>(subject, pSubject));
      subject = pSubject;
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

    if(!Objects.equals(status, pStatus))
    {
      changedProps.put("status", new AbstractMap.SimpleEntry<>(status, pStatus));
      status = pStatus;
    }

    if(!Objects.equals(author, pAuthor))
    {
      changedProps.put("author", new AbstractMap.SimpleEntry<>(author, pAuthor));
      author = pAuthor;
    }

    if(!Objects.equals(priority, pPriority))
    {
      changedProps.put("priority", new AbstractMap.SimpleEntry<>(priority, pPriority));
      priority = pPriority;
    }

    if(!Objects.equals(tracker, pTracker))
    {
      changedProps.put("tracker", new AbstractMap.SimpleEntry<>(tracker, pTracker));
      tracker = pTracker;
    }

    if(!Objects.equals(category, pCategory))
    {
      changedProps.put("category", new AbstractMap.SimpleEntry<>(category, pCategory));
      category = pCategory;
    }

    if(pFireChanges && !changedProps.isEmpty())
      changedProps.forEach((pChangedKey, pOldNewValues) -> _firePropertyChange(pChangedKey, pOldNewValues.getKey(), pOldNewValues.getValue()));
    return !changedProps.isEmpty();
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
    synchronized(listenerList)
    {
      listenerList.forEach(pListener -> pListener.redminePropertyChanged(pProperty, pOldValue, pNewValue));
    }
  }

}
