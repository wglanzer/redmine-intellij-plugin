package com.github.wglanzer.redmine.model;

import java.util.function.Predicate;

/**
 * @author w.glanzer, 25.02.2017.
 */
public interface ICondition extends Predicate<ITicket>
{

  /**
   * Tests if the ticket matches this condition
   * -> Example: You can check if a ticket-notification should be shown
   *
   * @param pITicket Ticket that should be checked
   * @return <tt>true</tt> if it matches this condition
   */
  @Override
  boolean test(ITicket pITicket);

}
