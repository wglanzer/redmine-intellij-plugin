package com.github.wglanzer.redmine.util;

import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Listener list (based on ArrayList) with weak-functionality
 *
 * @author w.glanzer, 13.12.2016.
 */
public class WeakListenerList<T> extends AbstractList<T>
{

  private final List<WeakReference<T>> listenerList = new ArrayList<>();

  @Override
  public int size()
  {
    return listenerList.size();
  }

  @Override
  public T get(int pIndex)
  {
    synchronized(listenerList)
    {
      return listenerList.get(pIndex).get();
    }
  }

  public boolean add(T pListener)
  {
    synchronized(listenerList)
    {
      return listenerList.add(new WeakReference<>(pListener));
    }
  }

  public boolean remove(Object pListener)
  {
    synchronized(listenerList)
    {
      return listenerList.removeIf(pWeakRef -> pWeakRef.get() == null || Objects.equals(pWeakRef.get(), pListener));
    }
  }

}
