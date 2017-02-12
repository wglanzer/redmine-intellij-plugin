package com.github.wglanzer.redmine.webservice.impl;

import org.apache.http.conn.ssl.TrustStrategy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author w.glanzer, 12.12.2016.
 */
public class RHostnameVerifier implements HostnameVerifier, TrustStrategy
{
  private static RHostnameVerifier INSTANCE;

  public static RHostnameVerifier getInstance()
  {
    if(INSTANCE == null)
      INSTANCE = new RHostnameVerifier();
    return INSTANCE;
  }

  private RHostnameVerifier()
  {
  }

  @Override
  public boolean verify(String pURL, SSLSession pSSLSession)
  {
    return true;
  }

  @Override
  public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException
  {
    return true;
  }
}
