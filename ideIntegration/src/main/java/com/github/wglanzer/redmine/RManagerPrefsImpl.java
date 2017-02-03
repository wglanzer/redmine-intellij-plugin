package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.config.RAppSettings;
import com.github.wglanzer.redmine.config.SettingsDataModel;
import com.github.wglanzer.redmine.notifiers.INotifier;
import com.github.wglanzer.redmine.util.IntelliJIDEAUtility;

import java.io.File;

/**
 * Implementation of IManagerPrefs to work with IntelliJ.
 * Contains all necessary implementations for redmine plugin
 *
 * @author w.glanzer, 03.12.2016.
 */
class RManagerPrefsImpl implements IRManagerPrefs
{
  private final IRLoggingFacade loggingFacade;
  private final IRTaskCreator taskCreator;

  public RManagerPrefsImpl(INotifier pNotifier)
  {
    loggingFacade = new RLoggingFacadeImpl(pNotifier);
    taskCreator = new RTaskCreatorImpl();
  }

  @Override
  public IRLoggingFacade getLoggingFacade()
  {
    return loggingFacade;
  }

  @Override
  public IRTaskCreator getTaskCreator()
  {
    return taskCreator;
  }

  @Override
  public SettingsDataModel getCurrentSettings()
  {
    return RAppSettings.getSettings();
  }

  @Override
  public File getTicketCacheDirectory()
  {
    return IntelliJIDEAUtility.getTicketCacheDirectory();
  }
}
