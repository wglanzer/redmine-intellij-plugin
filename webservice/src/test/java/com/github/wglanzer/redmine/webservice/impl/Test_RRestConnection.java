package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.spi.IRRestArgument;
import com.github.wglanzer.redmine.webservice.spi.IRRestConnection;
import com.github.wglanzer.redmine.webservice.spi.IRRestRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
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
  public void test_expiredCertSSLon() throws Exception
  {
    _testWithInvalidCert("https://expired.badssl.com", true);
  }

  @Test
  public void test_selfsignedCertSSLon() throws Exception
  {
    _testWithInvalidCert("https://selfsigned.badssl.com", true);
  }

  @Test
  public void test_expiredCertSSLoff() throws Exception
  {
    _testWithInvalidCert("https://expired.badssl.com", false);
  }

  @Test
  public void test_selfsignedCertSSLoff() throws Exception
  {
    _testWithInvalidCert("https://selfsigned.badssl.com", false);
  }

  @After
  public void tearDown() throws Exception
  {
    RHostnameVerifier.getInstance().clear();
  }

  private void _testWithInvalidCert(String pURL, boolean pCertificateCheckEnabled)
  {
    try
    {
      IRRestConnection con = _createConnection(pURL, pCertificateCheckEnabled);
      con.doGET(IRRestRequest.GET_TICKETS.argument(IRRestArgument.CREATED_ON));
      if(pCertificateCheckEnabled)
        Assert.fail();
    }
    catch(Exception e)
    {
      boolean containsCertEx = _exceptionChainContains(e, GeneralSecurityException.class);
      if(pCertificateCheckEnabled)
        // We expect a "A JSONArray text must start with '[' at 1 [character 2 line 1]"-Exception, but NOT a SSL-Exception
        Assert.assertTrue(containsCertEx);
      else
        // We have to throw an exception -> SSL is ON
        Assert.assertFalse(containsCertEx);
    }
  }

  /**
   * Creats a new instance of the RRestConnection.
   *
   * @param pURL        URL to connect to
   * @param pSSLEnabled <tt>true</tt> if the Certificate should be checked or not
   * @return new RRestConnection
   */
  @NotNull
  private IRRestConnection _createConnection(String pURL, boolean pSSLEnabled)
  {
    return new RRestConnection(new DummyRRestLoggingFacade(), pURL, "", 100, pSSLEnabled);
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
