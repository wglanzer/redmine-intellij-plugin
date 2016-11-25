package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.*;

/**
 * Directory for PollingTickets.
 * All PollingTicket-instances where saved here.
 *
 * @author w.glanzer, 19.11.2016.
 */
class PollingTicketDirectory
{

  private final Map<Long, PollingTicket> directory = Collections.synchronizedMap(new HashMap<>());

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
    directory.forEach((pID, pTicket) -> pTicket.destroy());
    directory.clear();
  }

  /**
   * Updates an specific ticket.
   * If the ticket does not exist, a new instance will be created
   *
   * @param pTicket JSON-Object from PollingProject
   * @return Ticket instance which was created or updated
   */
  protected ITicket updateTicket(JSONObject pTicket)
  {
    Long ticketID = pTicket.getLong("id");
    String subject = pTicket.getString("subject");
    String description = pTicket.getString("description");
    String status = pTicket.getJSONObject("status").getString("name");
    String author = pTicket.getJSONObject("author").getString("name");
    String priority = pTicket.getJSONObject("priority").getString("name");
    String tracker = pTicket.getJSONObject("tracker").getString("name");
    String category = "";//pTicket.getJSONObject("category").getString("name"); todo

    if(!directory.containsKey(ticketID))
    {
      // No updateProperties neccessary here!
      PollingTicket pp = new PollingTicket(ticketID, subject, description, status, author, priority, tracker, category);
      directory.put(ticketID, pp);
      return pp;
    }
    else
    {
      PollingTicket ppToUpdate = directory.get(ticketID);
      ppToUpdate.updateProperties(subject, description, status, author, priority, tracker, category);
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
    Long projectID = pTicket.getID();
    PollingTicket project = directory.remove(projectID);
    if(project != null)
      project.destroy();
  }

}
