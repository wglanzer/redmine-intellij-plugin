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

  private final IChangeNotifier notifier;

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private final List<EventListener> strongListenerRefs = new ArrayList<>();

  public AbstractNotifiableListener(IChangeNotifier pNotifier)
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

  protected IChangeNotifier getNotifier()
  {
    return notifier;
  }
}
