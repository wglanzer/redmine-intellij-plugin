package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.RManager;
import com.github.wglanzer.redmine.model.ITicket;
import com.github.wglanzer.redmine.model.impl.cache.ITicketCache;
import com.github.wglanzer.redmine.model.impl.cache.TicketCacheBuilder;
import com.github.wglanzer.redmine.util.DateUtil;
import com.github.wglanzer.redmine.webservice.spi.IRRestArgument;
import com.github.wglanzer.redmine.webservice.spi.IRRestResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Directory for PollingTickets.
 * All PollingTicket-instances where saved here.
 *
 * @author w.glanzer, 19.11.2016.
 */
class PollingTicketDirectory
{

  private final Map<Long, PollingTicket> directory = new HashMap<>();
  private final ITicketCache persistentCache;

  public PollingTicketDirectory(String pProjectID)
  {
    persistentCache = TicketCacheBuilder.createPersistent(new File(RManager.getInstance().getPreferences().getTicketCacheDirectory(), pProjectID));

    // Transfer all tickets to memory
    synchronized(directory)
    {
      persistentCache.getAllTickets().parallelStream().forEach(pTicket ->
          updateTicket(pTicket.getID(), pTicket.getSubject(), pTicket.getDescription(), pTicket.getCreatedOn(), pTicket.getUpdatedOn(), pTicket.getStatus(),
              pTicket.getAuthor(), pTicket.getPriority(), pTicket.getTracker(), pTicket.getCategory()));
    }
  }

  /**
   * Returns all curretly registered tickets
   *
   * @return List of all tickets
   */
  @NotNull
  public Collection<ITicket> getTickets()
  {
    return new ArrayList<>(directory.values());
  }

  /**
   * Clears the complete ticket-cache.
   * Releases all tickets
   */
  public void clearCaches()
  {
    persistentCache.destroy();
    directory.forEach((pID, pTicket) -> pTicket.destroy());
    directory.clear();
  }

  /**
   * Returns the last modified ticket
   *
   * @return the last modified ticket, or <tt>null</tt> if no ticket was registered
   */
  @Nullable
  public ITicket getLastUpdatedTicket()
  {
    return persistentCache.getLastUpdatedTicket();
  }

  /**
   * Updates an set of tickets.
   *
   * @param pTickets Objects from PollingProject
   * @return Ticket-instances which were created or updated
   */
  protected List<ITicket> updateTickets(Stream<IRRestResult.Node> pTickets)
  {
    return pTickets.parallel().map(pTicket -> {
      Long ticketID = Long.parseLong(pTicket.getValue(IRRestArgument.TICKET_ID));
      String subject = pTicket.getValue(IRRestArgument.TICKET_SUBJECT);
      String description = pTicket.getValue(IRRestArgument.TICKET_DESCRIPTION);
      Instant createdOn = DateUtil.toInstant(pTicket.getValue(IRRestArgument.CREATED_ON));
      Instant updatedOn = DateUtil.toInstant(pTicket.getValue(IRRestArgument.UPDATED_ON));
      String status = pTicket.getValue(IRRestArgument.TICKET_STATUS);
      String author = pTicket.getValue(IRRestArgument.TICKET_AUTHOR);
      String priority = pTicket.getValue(IRRestArgument.TICKET_PRIORITY);
      String tracker = pTicket.getValue(IRRestArgument.TICKET_TRACKER);
      String category = ""; //pTicket.getValue(IRRestArgument.TICKET_CATEGORY); TODO

      return updateTicket(ticketID, subject, description, createdOn, updatedOn, status, author, priority, tracker, category);
    }).collect(Collectors.toList());
  }

  /**
   * Updates an specific ticket.
   * If the ticket does not exist, a new instance will be created
   *
   * @return Ticket instance which was created or updated
   */
  protected ITicket updateTicket(Long pTicketID, String pSubject, String pDescription, Instant pCreatedOn, Instant pUpdatedOn, String pStatus, String pAuthor, String pPriority, String pTracker, String pCategory)
  {
    if(!directory.containsKey(pTicketID))
    {
      // No updateProperties neccessary here!
      PollingTicket pp = new PollingTicket(pTicketID, pSubject, pDescription, pCreatedOn, pUpdatedOn, pStatus, pAuthor, pPriority, pTracker, pCategory);
      directory.put(pTicketID, pp);
      persistentCache.put(pp);
      return pp;
    }
    else
    {
      PollingTicket ppToUpdate = directory.get(pTicketID);
      boolean changed = ppToUpdate.updateProperties(pSubject, pDescription, pCreatedOn, pUpdatedOn, pStatus, pAuthor, pPriority, pTracker, pCategory);
      if(changed)
      {
        persistentCache.remove(pTicketID);
        persistentCache.put(ppToUpdate);
      }

      return ppToUpdate;
    }
  }

  /**
   * Removes a specific ticket from cache and marks it as invalid
   *
   * @param pTicket ticket to be removed
   */
  protected void removeTicketFromCache(ITicket pTicket)
  {
    Long ticketID = pTicket.getID();
    PollingTicket ticket = directory.remove(ticketID);
    persistentCache.remove(ticketID);
    if(ticket != null)
      ticket.destroy();
  }

}
