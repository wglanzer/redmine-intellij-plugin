package com.github.wglanzer.redmine.model.impl.cache;

import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;
import org.mapdb.*;
import org.mapdb.serializer.SerializerLong;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author w.glanzer, 27.11.2016.
 */
class PersistentTicketCache implements ITicketCache
{

  private static final String _CACHE_ID = "ticketCache";
  private static final Long _LAST_ACCESSED_KEY = -1337L;

  private final HTreeMap<Long, ITicket> persistentCache;
  private final DB fileDB;

  public PersistentTicketCache(File pCacheFolder)
  {
    if(!pCacheFolder.exists())
      pCacheFolder.mkdirs();

    String path = pCacheFolder.getAbsolutePath();
    if(!path.endsWith(File.separatorChar + ""))
      path += File.separatorChar;
    path += _CACHE_ID;

    fileDB = DBMaker.fileDB(path).checksumHeaderBypass()
        .closeOnJvmShutdown()
        .make();
    persistentCache = fileDB.hashMap(_CACHE_ID, new SerializerLong(), new Serializer<ITicket>()
    {
      @Override
      public void serialize(@NotNull DataOutput2 out, @NotNull ITicket value) throws IOException
      {
        ObjectOutputStream out2 = new ObjectOutputStream(out);
        out2.writeObject(value);
        out2.flush();
      }

      @Override
      public ITicket deserialize(@NotNull DataInput2 in, int available) throws IOException
      {
        try
        {
          ObjectInputStream in2 = new ObjectInputStream(new DataInput2.DataInputToStream(in));
          return (PersistentTicket) in2.readObject();
        }
        catch(ClassNotFoundException e)
        {
          throw new IOException(e);
        }
      }
    }).createOrOpen();
  }

  @Override
  public void put(ITicket pTicket)
  {
    synchronized(persistentCache)
    {
      ITicket pers = new PersistentTicket(pTicket);
      persistentCache.put(pTicket.getID(), pers);

      // If the lastUpdated-Ticket is not set or the new put
      // ticket is newer than the old one, you have to set the _LAST_ACCESSED_KEY
      ITicket lastUpdatedTicket = getLastUpdatedTicket();
      if(lastUpdatedTicket == null ||
          Instant.parse(pers.getUpdatedOn()).isAfter(Instant.parse(lastUpdatedTicket.getUpdatedOn())))
        persistentCache.put(_LAST_ACCESSED_KEY, pers);

      fileDB.commit();
    }
  }

  @Override
  public void remove(Long pTicketID)
  {
    synchronized(persistentCache)
    {
      persistentCache.remove(pTicketID);
    }
  }

  @Override
  public ITicket get(Long pID)
  {
    synchronized(persistentCache)
    {
      return persistentCache.get(pID);
    }
  }

  @NotNull
  @Override
  public List<ITicket> getAllTickets()
  {
    synchronized(persistentCache)
    {
      return new ArrayList<>(persistentCache.values());
    }
  }

  @Override
  public ITicket getLastUpdatedTicket()
  {
    synchronized(persistentCache)
    {
      return persistentCache.get(_LAST_ACCESSED_KEY);
    }
  }

}