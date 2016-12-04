package com.github.wglanzer.redmine.util;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

/**
 * Unittest for: DateUtil
 *
 * @author w.glanzer, 04.12.2016.
 */
public class Test_DateUtil
{

  @Test
  public void test_toInstant()
  {
    String dateFromRedmineAPI = "2014-01-02T08:12:32Z";
    Instant parsedTime = DateUtil.toInstant(dateFromRedmineAPI);

    Assert.assertEquals(dateFromRedmineAPI, parsedTime.toString());
    Assert.assertEquals(parsedTime.getEpochSecond(), 1388650352);
    Assert.assertNotNull(parsedTime);
  }

}
