package com.github.wglanzer.redmine.webservice.spi;

import com.github.wglanzer.redmine.webservice.impl.RRestArgumentImpl;

/**
 * @author w.glanzer, 04.12.2016.
 */
public interface IRRestArgument
{

  IRRestArgument PROJECT_ID = new RRestArgumentImpl().name("id");
  IRRestArgument PROJECT_NAME = new RRestArgumentImpl().name("name");
  IRRestArgument PROJECT_DESCRIPTION = new RRestArgumentImpl().name("description");
  IRRestArgument TICKET_ID = new RRestArgumentImpl().name("id");
  IRRestArgument TICKET_SUBJECT = new RRestArgumentImpl().name("subject");
  IRRestArgument TICKET_DESCRIPTION = new RRestArgumentImpl().name("description");
  IRRestArgument TICKET_STATUS = new RRestArgumentImpl().name("status");
  IRRestArgument TICKET_AUTHOR = new RRestArgumentImpl().name("author");
  IRRestArgument TICKET_PRIORITY = new RRestArgumentImpl().name("priority");
  IRRestArgument TICKET_TRACKER = new RRestArgumentImpl().name("tracker");
  IRRestArgument TICKET_CATEGORY = new RRestArgumentImpl().name("category");
  IRRestArgument UPDATED_ON = new RRestArgumentImpl().name("updated_on");
  IRRestArgument CREATED_ON = new RRestArgumentImpl().name("created_on");
  IRRestArgument API_KEY = new RRestArgumentImpl().name("key");
  IRRestArgument SORT = new RRestArgumentImpl().name("sort");
  IRRestArgument PAGE_OFFSET = new RRestArgumentImpl().name("offset");
  IRRestArgument PAGE_LIMIT = new RRestArgumentImpl().name("limit");
  IRRestArgument PAGE_TOTALCOUNT = new RRestArgumentImpl().name("total_count");
  IRRestArgument TICKET_STATUS_ID = new RRestArgumentImpl().name("status_id"); //todo

  /**
   * Sets the value of this argument
   *
   * @param pValue new value
   * @return a copy of this argument, value set to pValue
   */
  IRRestArgument value(String pValue);

  /**
   * @return Returns the name of this argument
   */
  String getName();

  /**
   * @return Returns the value of this argument
   */
  String getValue();

}
