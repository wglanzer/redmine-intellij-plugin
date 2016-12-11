package com.github.wglanzer.redmine.webservice.spi;

import com.github.wglanzer.redmine.webservice.impl.RRestArgumentImpl;

/**
 * @author w.glanzer, 04.12.2016.
 */
public interface IRRestArgument
{

  IRRestArgument PROJECT_ID = new RRestArgumentImpl().requestName("project_id").resultName("id");
  IRRestArgument PROJECT_NAME = new RRestArgumentImpl().resultName("name");
  IRRestArgument PROJECT_DESCRIPTION = new RRestArgumentImpl().resultName("description");
  IRRestArgument TICKET_ID = new RRestArgumentImpl().resultName("id");
  IRRestArgument TICKET_SUBJECT = new RRestArgumentImpl().resultName("subject");
  IRRestArgument TICKET_DESCRIPTION = new RRestArgumentImpl().resultName("description");
  IRRestArgument TICKET_STATUS = new RRestArgumentImpl().requestName("status_id").resultName("status");
  IRRestArgument TICKET_AUTHOR = new RRestArgumentImpl().resultName("author");
  IRRestArgument TICKET_ASSIGNEE = new RRestArgumentImpl().resultName("assigned_to");
  IRRestArgument TICKET_PRIORITY = new RRestArgumentImpl().resultName("priority");
  IRRestArgument TICKET_TRACKER = new RRestArgumentImpl().resultName("tracker");
  IRRestArgument TICKET_CATEGORY = new RRestArgumentImpl().resultName("category");
  IRRestArgument UPDATED_ON = new RRestArgumentImpl().resultName("updated_on");
  IRRestArgument CREATED_ON = new RRestArgumentImpl().resultName("created_on");
  IRRestArgument API_KEY = new RRestArgumentImpl().resultName("key");
  IRRestArgument SORT = new RRestArgumentImpl().resultName("sort");
  IRRestArgument PAGE_OFFSET = new RRestArgumentImpl().resultName("offset");
  IRRestArgument PAGE_LIMIT = new RRestArgumentImpl().resultName("limit");
  IRRestArgument PAGE_TOTALCOUNT = new RRestArgumentImpl().resultName("total_count");

  /**
   * Sets the value of this argument
   *
   * @param pValue new value
   * @return a copy of this argument, value set to pValue
   */
  IRRestArgument value(String pValue);

  /**
   * @return Returns the name of this argument, used in result
   */
  String getResultName();

  /**
   * @return Returns the request name of this argument, used in request
   */
  String getRequestName();

  /**
   * @return Returns the value of this argument
   */
  String getValue();

}
