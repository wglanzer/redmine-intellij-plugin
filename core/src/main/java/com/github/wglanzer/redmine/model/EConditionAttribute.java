package com.github.wglanzer.redmine.model;

import java.util.function.Function;

/**
 * It describes which attribute is used for a specific condition
 *
 * @author w.glanzer, 24.02.2017.
 */
public enum EConditionAttribute
{
  AUTHOR(ITicket::getAuthor),
  ASSIGNEE(ITicket::getAssignee),
  CATEGORY(ITicket::getCategory),
  DESCRIPTION(ITicket::getDescription),
  PRIORITY(ITicket::getPriority),
  STATUS(ITicket::getStatus),
  SUBJECT(ITicket::getSubject),
  TRACKER(ITicket::getTracker);

  private final Function<ITicket, String> ticketValueExtractor;
  private final EConditionOperator[] possibleOperators;

  /**
   * Constructs a new attribute
   *
   * @param pValueExtractor    Defines how the specific value will be extracted from one ticket
   * @param pPossibleOperators All possible Operators for this attribute. empty = all operators
   */
  EConditionAttribute(Function<ITicket, String> pValueExtractor, EConditionOperator... pPossibleOperators)
  {
    ticketValueExtractor = pValueExtractor;
    possibleOperators = pPossibleOperators.length == 0 ? EConditionOperator.values() : pPossibleOperators;
  }

  /**
   * Extracts the specific attribute from one ticket
   *
   * @param pTicket Ticket from which should be extracted
   * @return the extrcted value
   */
  public String getValue(ITicket pTicket)
  {
    return ticketValueExtractor.apply(pTicket);
  }

  /**
   * @return Returns an array that defines which
   * EConditionOperator can be used for this attribute
   */
  public EConditionOperator[] getPossibleOperators()
  {
    return possibleOperators;
  }
}
