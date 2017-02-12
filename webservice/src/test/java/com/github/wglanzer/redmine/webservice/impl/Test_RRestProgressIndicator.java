package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.spi.IRRestArgument;
import com.github.wglanzer.redmine.webservice.spi.IRRestConnection;
import com.github.wglanzer.redmine.webservice.spi.IRRestRequest;
import com.github.wglanzer.redmine.webservice.spi.IRRestResult;
import javafx.beans.property.SimpleBooleanProperty;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * JUnit-Test for RRestProgresIndicator
 *
 * @author w.glanzer, 03.02.2017.
 */
public class Test_RRestProgressIndicator
{

  private IRRestConnection connection;

  @Before
  public void setUp()
  {
    connection = RRestConnectionBuilder.createConnection(new DummyRRestLoggingFacade(), "http://wglanzer.m.redmine.org", "cebe0b29b08ef68fbbea113826199461eccae5b4", 100);
  }

  /**
   * Tests, that connection.doGET() returns NULL if the
   * RRestProgressIndicator instatly returns alive=false
   */
  @Test
  public void test_instantNotAlive() throws Exception
  {
    SimpleBooleanProperty alive = new SimpleBooleanProperty(true);
    alive.set(false);

    try
    {
      IRRestResult result = connection.doGET(IRRestRequest.GET_TICKETS, () -> alive);
      Assert.assertNull(result);
    }
    catch(Exception pE)
    {
      Assert.fail();
    }
  }

  /**
   * Tests, that connection.doGET() returns NULL if the
   * RRestProgressIndicator returns alive=false after about 200ms
   */
  @Test
  public void test_delayedNotAlive() throws Exception
  {
    SimpleBooleanProperty alive = new SimpleBooleanProperty(true);
    alive.set(true);

    try
    {
      CompletableFuture<Void> getResult = CompletableFuture.runAsync(() ->
      {
        try
        {
          IRRestResult res = connection.doGET(IRRestRequest.GET_TICKETS.argument(IRRestArgument.PROJECT_ID.value("1")), () -> alive);
          Assert.assertNull(res);
        }
        catch(Exception e)
        {
          Assert.fail();
        }
      });

      new Thread(() -> {
        try
        {
          Thread.sleep(200);
        }
        catch(InterruptedException ignored)
        {
        }

        alive.set(false);
      }).start();

      getResult.get();
    }
    catch(Exception e)
    {
      Assert.fail();
    }
  }

  /**
   * Tests, that connection.doGET() returns NOT NULL if the
   * RRestProgressIndicator returns alive=true
   */
  @Test
  public void test_alive() throws Exception
  {
    SimpleBooleanProperty alive = new SimpleBooleanProperty(true);
    alive.set(true);

    try
    {
      IRRestResult res = connection.doGET(IRRestRequest.GET_TICKETS.argument(IRRestArgument.PROJECT_ID.value("1")), () -> alive);
      Assert.assertNotNull(res);
    }
    catch(Exception e)
    {
      Assert.fail();
    }
  }

}
