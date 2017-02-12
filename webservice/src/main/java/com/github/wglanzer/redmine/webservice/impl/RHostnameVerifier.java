package com.github.wglanzer.redmine.webservice.impl;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

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

  private final Map<String, Certificate> doNotVerify = new HashMap<>();

  private RHostnameVerifier()
  {
  }

  @Override
  public boolean verify(String pURL, SSLSession pSSLSession)
  {
    if(doNotVerify.containsKey(pURL))
      return true;
    return SSLConnectionSocketFactory.getDefaultHostnameVerifier().verify(pURL, pSSLSession);
  }

  /**
   * Adds an URL, that should (not) be verified by this verifier.
   * If it was verified ONCE as TRUE, it won't be called anymore.
   *
   * @param pURL    URL
   * @param pCheckCertificate <tt>true</tt> if this url should be verified
   */
  public void setVerify(String pURL, boolean pCheckCertificate)
  {
    try
    {
      synchronized(doNotVerify)
      {
        pURL = new URL(pURL).getHost();
        if(pCheckCertificate)
          doNotVerify.remove(pURL);
        else
        {
          Certificate cert = _getCertificate(pURL);
          if(cert != null)
            doNotVerify.put(pURL, cert);
        }
      }
    }
    catch(Exception e)
    {
      throw new RuntimeException("Could not mark url as notVerify-URL (url: " + pURL + ")", e);
    }
  }

  /**
   * Löscht alle Zuordnungen URL->Certificate.
   * Brauchbar bspw in Unittests
   */
  public void clear()
  {
    doNotVerify.clear();
  }

  @Override
  public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException
  {
    return doNotVerify.containsValue(chain[0]);
  }

  private Certificate _getCertificate(String pHost)
  {
    try
    {
      HttpsURLConnection con = (HttpsURLConnection) new URL("https://" + pHost).openConnection();
      con.setHostnameVerifier(new HostnameVerifier()
      {
        @Override
        public boolean verify(String pS, SSLSession pSSLSession)
        {
          return false;
        }
      });
      con.setSSLSocketFactory(SSLContexts.custom()
          .loadTrustMaterial(null, (chain, authType) -> true)
          .build().getSocketFactory());
      con.connect();
      Certificate[] certs = con.getServerCertificates();
      if(certs.length == 0)
        return null;
      return certs[0];
    }
    catch(Exception e)
    {
      return null;
    }
  }
}
