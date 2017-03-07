package com.github.wglanzer.redmine.notifiers;

import com.github.wglanzer.redmine.config.SettingsDataModel;
import com.github.wglanzer.redmine.model.*;
import com.github.wglanzer.redmine.model.impl.condition.ConditionFactory;
import com.github.wglanzer.redmine.notifiers.balloons.BalloonComponentFactory;
import com.intellij.notification.NotificationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Notifier with filter / watches / conditions
 *
 * @author w.glanzer, 07.03.2017.
 */
public class FilteredNotifier extends SimpleNotifier
{

  public FilteredNotifier(Supplier<SettingsDataModel> pSettings)
  {
    super(pSettings);
  }

  @Override
  public void notifyNewTicket(@NotNull IServer pServer, @NotNull ITicket pTicket)
  {
    IWatch watch = _getMatchedWatch(pServer.getWatches(), pTicket);
    if(watch != null)
    {
      showCustomBalloon(BalloonComponentFactory.createTicketChangedBalloon(pServer, pTicket, watch, null));
      logInEventLog(NOTIFICATION_ID, "ticket created: " + pTicket.getID(), NotificationType.INFORMATION);
    }
  }

  @Override
  public void notifyTicketPropertyChanged(@NotNull IServer pServer, @NotNull ITicket pTicket, @NotNull Map<String, Map.Entry<Object, Object>> pProperties)
  {
    IWatch watch = _getMatchedWatch(pServer.getWatches(), pTicket);
    if(watch != null)
    {
      showCustomBalloon(BalloonComponentFactory.createTicketChangedBalloon(pServer, pTicket, watch, pProperties));
      logInEventLog(NOTIFICATION_ID, "ticket changed: " + pTicket.getID(), NotificationType.INFORMATION);
    }
  }

  /**
   * Returns the first watch which matches the given ticket.
   * If no watch is given -> nothing will be accepted
   *
   * @param pWatches All available watches
   * @param pTicket  Ticket which should be checked
   * @return the matched watch or <tt>null</tt> if no watch was found
   */
  @Nullable
  private IWatch _getMatchedWatch(List<IWatch> pWatches, ITicket pTicket)
  {
    ArrayList<IWatch> watches = new ArrayList<>(pWatches); //mutable copy
    if(watches.size() == 0)
      return null; // no watch -> ACCEPT_NOTHING

    for(IWatch watch : pWatches)
    {
      ICondition cond = _createCondition(watch);
      if(cond != null && cond.test(pTicket))
        return watch;
    }

    return null;
  }

  @Nullable
  private ICondition _createCondition(IWatch pWatch)
  {
    List<IConditionDescription> descriptions = new ArrayList<>(pWatch.getConditionDescriptions()); //mutable copy
    if(descriptions.size() == 0)
      return null;

    ICondition condition = ConditionFactory.create(descriptions.remove(0));
    for(IConditionDescription desc : descriptions)
      condition.and(ConditionFactory.create(desc));

    return condition;
  }

}
