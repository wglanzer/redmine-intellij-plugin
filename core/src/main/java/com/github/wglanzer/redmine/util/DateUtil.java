package com.github.wglanzer.redmine.util;

import java.time.Instant;
import java.time.temporal.ChronoField;

/**
 * @author w.glanzer, 04.12.2016.
 */
public class DateUtil
{

  /**
   * Converts an timestamp (received by redmine-server-RESTAPI)
   * to an Instant-Object, without milliseconds
   *
   * @param pTimestamp Timestamp from redmine-Server
   * @return Converted Redmine
   */
  public static Instant toInstant(String pTimestamp)
  {
    Instant myTime = Instant.parse(pTimestamp);
    myTime = myTime.minusMillis(myTime.get(ChronoField.MILLI_OF_SECOND));
    return myTime;
  }

}
