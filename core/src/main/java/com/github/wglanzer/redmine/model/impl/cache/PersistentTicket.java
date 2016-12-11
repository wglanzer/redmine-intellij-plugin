package com.github.wglanzer.redmine.model.impl.cache;

import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Ticket-Impl that will be persistent on disk.
 * It has to be serializable, because it will be saved to disk!
 *
 * @author w.glanzer, 27.11.2016.
 */
class PersistentTicket implements ITicket, Serializable
{
  private static final long serialVersionUID = 7208869555811111902L;

  private final long id;
  private final String subject;
  private final String description;
  private final Instant updatedOn;
  private final Instant createdOn;
  private final String status;
  private final String author;
  private final String priority;
  private final String tracker;
  private final String category;
  private final HashMap<String, String> arguments;

  public PersistentTicket(ITicket pTicket)
  {
    id = pTicket.getID();
    subject = pTicket.getSubject();
    description = pTicket.getDescription();
    createdOn = pTicket.getCreatedOn();
    updatedOn = pTicket.getUpdatedOn();
    status = pTicket.getStatus();
    author = pTicket.getAuthor();
    priority = pTicket.getPriority();
    tracker = pTicket.getTracker();
    category = pTicket.getCategory();
    arguments = new HashMap<>(pTicket.getAdditionalProperties());
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
    return arguments;
  }

  @Override
  public void addTicketListener(@NotNull ITicketListener pListener)
  {
    throw new UnsupportedOperationException("addTicketListener() not implemented");
  }

  @Override
  public void removeTicketListener(@NotNull ITicketListener pListener)
  {
    throw new UnsupportedOperationException("removeTicketListener() not implemented");
  }
}
