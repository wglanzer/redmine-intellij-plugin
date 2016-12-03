package com.github.wglanzer.redmine.model.impl.cache;

import java.io.File;

/**
 * It can build caches :-)
 *
 * @author w.glanzer, 27.11.2016.
 */
public class TicketCacheBuilder
{

  /**
   * Creates a new cache-instance that will store its content to the given path
   *
   * @param pCacheFolder Folder for this cache
   * @return a ticketCache which stores its tickets to disk
   */
  public static ITicketCache createPersistent(File pCacheFolder)
  {
    return new PersistentTicketCache(pCacheFolder);
  }

}
