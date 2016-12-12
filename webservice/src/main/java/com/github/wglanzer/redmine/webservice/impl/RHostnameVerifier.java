package com.github.wglanzer.redmine.webservice.impl;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author w.glanzer, 12.12.2016.
 */
public class RHostnameVerifier implements HostnameVerifier
{
  private static RHostnameVerifier INSTANCE;

  public static RHostnameVerifier getInstance()
  {
    if(INSTANCE == null)
      INSTANCE = new RHostnameVerifier();
    return INSTANCE;
  }

  private final List<String> doNotVerify = new ArrayList<>();

  private RHostnameVerifier()
  {
  }

  @Override
  public boolean verify(String pURL, SSLSession pSSLSession)
  {
    if(doNotVerify.contains(pURL))
      return true;
    return SSLConnectionSocketFactory.getDefaultHostnameVerifier().verify(pURL, pSSLSession);
  }

  /**
   * Adds an URL, that should (not) be verified by this verifier
   *
   * @param pURL    URL
   * @param pVerify <tt>true</tt> if this url should be verified
   */
  public void setVerify(String pURL, boolean pVerify)
  {
    try
    {
      synchronized(doNotVerify)
      {
        pURL = new URL(pURL).getHost();
        if(pVerify)
          doNotVerify.remove(pURL);
        else
          doNotVerify.add(pURL);
      }
    }
    catch(Exception e)
    {
      throw new RuntimeException("Could not mark url as notVerify-URL (url: " + pURL + ")", e);
    }
  }
}
