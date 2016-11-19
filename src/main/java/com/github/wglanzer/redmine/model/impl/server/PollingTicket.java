package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

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
  private String status;
  private String author;
  private String priority;
  private String tracker;
  private String category;

  public PollingTicket(long pID, String pSubject, String pDescription, String pStatus, String pAuthor, String pPriority, String pTracker, String pCategory)
  {
    id = pID;
    updateProperties(pSubject, pDescription, pStatus, pAuthor, pPriority, pTracker, pCategory);
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

  protected void updateProperties(String pSubject, String pDescription, String pStatus, String pAuthor, String pPriority, String pTracker, String pCategory)
  {
    subject = pSubject;
    description = pDescription;
    status = pStatus;
    author = pAuthor;
    priority = pPriority;
    tracker = pTracker;
    category = pCategory;
  }

}
