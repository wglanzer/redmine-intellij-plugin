package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.exceptions.NotConnectedException;
import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ISource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * Implements an server, which polls every fixed period
 *
 * @author w.glanzer, 16.11.2016.
 */
public class PollingServer implements IServer
{

  private final ISource source;
  private final int pollingInterval;

  public PollingServer(ISource pSource, int pPollingInterval)
  {
    source = pSource;
    pollingInterval = pPollingInterval;
  }

  @Override
  public void connect()
  {

  }

  @Override
  public void disconnect()
  {

  }

  @Override
  public boolean isHealthy()
  {
    return true;
  }

  @NotNull
  @Override
  public Collection<IProject> getProjects() throws NotConnectedException
  {
    return Collections.emptyList();
  }

}
