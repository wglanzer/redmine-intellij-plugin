package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.config.ISettings;
import com.github.wglanzer.redmine.config.RAppSettings;
import com.github.wglanzer.redmine.util.IntelliJIDEAUtility;
import com.intellij.openapi.project.Project;

import java.io.File;

/**
 * Implementation of IManagerPrefs to work with IntelliJ
 *
 * @author w.glanzer, 03.12.2016.
 */
class RManagerPrefsImpl implements IRManagerPrefs
{
  private IRLoggingFacade loggingFacade;
  private IRTaskCreator taskCreator;

  public RManagerPrefsImpl(Project pProject)
  {
    loggingFacade = new RLoggingFacadeImpl();
    taskCreator = new RTaskCreatorImpl(pProject);
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
  public ISettings getCurrentSettings()
  {
    return RAppSettings.getSettings();
  }

  @Override
  public File getTicketCacheDirectory()
  {
    return IntelliJIDEAUtility.getTicketCacheDirectory();
  }
}
