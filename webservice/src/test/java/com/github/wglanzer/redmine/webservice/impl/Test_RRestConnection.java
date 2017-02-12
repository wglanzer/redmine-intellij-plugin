package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.spi.IRRestArgument;
import com.github.wglanzer.redmine.webservice.spi.IRRestConnection;
import com.github.wglanzer.redmine.webservice.spi.IRRestRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.security.GeneralSecurityException;

/**
 * Tests the RRestConnection
 *
 * @author w.glanzer, 06.02.2017.
 */
public class Test_RRestConnection
{

  @Test
  public void test_expiredCert() throws Exception
  {
    _testWithInvalidCert("https://expired.badssl.com");
  }

  @Test
  public void test_selfsignedCert() throws Exception
  {
    _testWithInvalidCert("https://selfsigned.badssl.com");
  }

  @Test
  public void test_wrongHostCert() throws Exception
  {
    _testWithInvalidCert("https://wrong.host.badssl.com");
  }

  @Test
  public void test_validCert() throws Exception
  {
    _testWithInvalidCert("https://sha256.badssl.com/");
  }

  private void _testWithInvalidCert(String pURL)
  {
    try
    {
      IRRestConnection con = _createConnection(pURL);
      con.doGET(IRRestRequest.GET_TICKETS.argument(IRRestArgument.CREATED_ON));
      Assert.fail(); // <-- Because we do not expect a valid CREATED_ON-Answer..
    }
    catch(Exception e)
    {
      boolean containsCertEx = _exceptionChainContains(e, GeneralSecurityException.class);
      Assert.assertFalse(containsCertEx);
    }
  }

  /**
   * Creats a new instance of the RRestConnection.
   *
   * @param pURL        URL to connect to
   * @return new RRestConnection
   */
  @NotNull
  private IRRestConnection _createConnection(String pURL)
  {
    return new RRestConnection(new DummyRRestLoggingFacade(), pURL, "", 100);
  }

  /**
   * Checks, if the Exceptionchain contains an exception of the given type
   *
   * @param pEx     Exception which chain should be searched
   * @param pSearch The searched Exception-Type
   * @return <tt>true</tt>, if it does contain the searched class
   */
  private boolean _exceptionChainContains(Throwable pEx, Class<? extends Throwable> pSearch)
  {
    Throwable ex = pEx;
    while(ex != null)
    {
      if(pSearch.isAssignableFrom(ex.getClass()))
        return true;
      ex = ex.getCause();
    }

    return false;
  }

}
