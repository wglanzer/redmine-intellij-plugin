package com.github.wglanzer.redmine.model.impl.cache;

import com.github.wglanzer.redmine.model.ITicket;
import org.jetbrains.annotations.NotNull;
import org.mapdb.*;
import org.mapdb.serializer.SerializerLong;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author w.glanzer, 27.11.2016.
 */
class PersistentTicketCache implements IPersistentTicketCache
{

  private static final String _CACHE_ID = "ticketCache";

  private final HTreeMap<Long, ITicket> persistentCache;
  private final DB fileDB;

  private final AtomicInteger lastAccessedTicketHash = new AtomicInteger(-1);
  private ITicket lastAccessedTicket = null;

  public PersistentTicketCache(File pCacheFolder)
  {
    if(!pCacheFolder.exists())
      pCacheFolder.mkdirs();

    String path = pCacheFolder.getAbsolutePath();
    if(!path.endsWith(File.separatorChar + ""))
      path += File.separatorChar;
    path += _CACHE_ID;

    fileDB = DBMaker.fileDB(path).checksumHeaderBypass()
        .fileChannelEnable()
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
    ITicket pers = new PersistentTicket(pTicket);
    persistentCache.put(pTicket.getID(), pers);
  }

  @Override
  public void remove(Long pTicketID)
  {
    persistentCache.remove(pTicketID);
  }

  @Override
  public ITicket get(Long pID)
  {
    return persistentCache.get(pID);
  }

  @NotNull
  @Override
  public List<ITicket> getAllTickets()
  {
    return new ArrayList<>(persistentCache.values());
  }

  @Override
  public ITicket getLastUpdatedTicket()
  {
    synchronized(lastAccessedTicketHash)
    {
      Collection<ITicket> currentValues = persistentCache.getValues();
      if(currentValues.hashCode() == lastAccessedTicketHash.get() && currentValues.size() > 0 && lastAccessedTicket != null)
        return lastAccessedTicket;
      else
      {
        //calculate lastAccessed-Ticket
        lastAccessedTicket = currentValues.parallelStream()
            .sorted(Comparator.comparing(ITicket::getUpdatedOn).reversed())
            .findFirst().orElse(null);
        lastAccessedTicketHash.set(currentValues.hashCode());
        return lastAccessedTicket;
      }
    }
  }

  @Override
  public void destroy()
  {
    persistentCache.close();
    fileDB.close();
  }
}
