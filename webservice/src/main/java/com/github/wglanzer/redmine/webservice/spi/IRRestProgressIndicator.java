package com.github.wglanzer.redmine.webservice.spi;

import javafx.beans.value.ObservableBooleanValue;

/**
 * Indicator that helps an IRRestRequest to be correctly handled
 *
 * @author w.glanzer, 03.02.2017.
 */
public interface IRRestProgressIndicator
{

  /**
   * Returns a value that represents, if the Request should be cancelled or running.
   * If this Value is set to false, the request will be cancelled automatically.
   *
   * @return BooleanValue that can be observed
   */
  ObservableBooleanValue alive();

}
