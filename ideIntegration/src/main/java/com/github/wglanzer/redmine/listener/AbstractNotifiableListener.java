package com.github.wglanzer.redmine.listener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Abstract implementation for ideIntegrated listener-impls
 *
 * @author w.glanzer, 13.12.2016.
 */
abstract class AbstractNotifiableListener
{

  private final INotifier notifier;

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private final List<EventListener> strongListenerRefs = new ArrayList<>();

  public AbstractNotifiableListener(INotifier pNotifier)
  {
    notifier = pNotifier;
  }

  protected <T extends EventListener> T strongRef(T pListener)
  {
    synchronized(strongListenerRefs)
    {
      strongListenerRefs.add(pListener);
      return pListener;
    }
  }

  protected INotifier getNotifier()
  {
    return notifier;
  }
}
