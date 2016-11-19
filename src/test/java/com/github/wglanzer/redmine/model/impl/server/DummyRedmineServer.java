package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.model.ISource;
import org.jetbrains.annotations.NotNull;

/**
 * @author w.glanzer, 19.11.2016.
 */
public class DummyRedmineServer
{

  public static void main(String[] args)
  {
    PollingServer server = new PollingServer(new ISource()
    {
      @NotNull
      @Override
      public String getURL()
      {
        return "http://redmine.zero-division.de";
      }

      @NotNull
      @Override
      public String getAPIKey()
      {
        return "dd76c1ab88e4b3ba1e7a53228818a852eecde987";
      }
    }, 200);
    server.connect();
    System.out.println(server.getProjects());
  }

}
