package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Ticket that was created by PollingTicketDirectory
 *
 * @author w.glanzer, 19.11.2016.
 */
class PollingTicket implements ITicket
{
  private final long id;
  private String subject;
  private String description;
  private String updatedOn;
  private String createdOn;
  private String status;
  private String author;
  private String priority;
  private String tracker;
  private String category;

  public PollingTicket(long pID, String pSubject, String pDescription, String pCreatedOn, String pUpdatedOn, String pStatus, String pAuthor, String pPriority, String pTracker, String pCategory)
  {
    id = pID;
    updateProperties(pSubject, pDescription, pCreatedOn, pUpdatedOn, pStatus, pAuthor, pPriority, pTracker, pCategory);
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
  public String getCreatedOn()
  {
    return createdOn;
  }

  @Override
  public String getUpdatedOn()
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

  protected boolean updateProperties(String pSubject, String pDescription, String pCreatedOn, String pUpdatedOn, String pStatus, String pAuthor, String pPriority, String pTracker, String pCategory)
  {
    boolean changed = false;

    if(!Objects.equals(subject, pSubject))
    {
      subject = pSubject;
      changed = true;
    }

    if(!Objects.equals(description, pDescription))
    {
      description = pDescription;
      changed = true;
    }

    if(!Objects.equals(createdOn, pCreatedOn))
    {
      createdOn = pCreatedOn;
      changed = true;
    }

    if(!Objects.equals(updatedOn, pUpdatedOn))
    {
      updatedOn = pUpdatedOn;
      changed = true;
    }

    if(!Objects.equals(status, pStatus))
    {
      status = pStatus;
      changed = true;
    }

    if(!Objects.equals(author, pAuthor))
    {
      author = pAuthor;
      changed = true;
    }

    if(!Objects.equals(priority, pPriority))
    {
      priority = pPriority;
      changed = true;
    }

    if(!Objects.equals(tracker, pTracker))
    {
      tracker = pTracker;
      changed = true;
    }

    if(!Objects.equals(category, pCategory))
    {
      category = pCategory;
      changed = true;
    }

    return changed;
  }

}
