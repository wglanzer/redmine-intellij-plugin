package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.config.ISettings;

import java.io.File;

/**
 * Preferences for the RedmineManager
 *
 * @author w.glanzer, 03.12.2016.
 */
public interface IRManagerPrefs
{

  IRLoggingFacade getLoggingFacade();

  IRTaskCreator getTaskCreator();

  ISettings getCurrentSettings();

  File getTicketCacheDirectory();

}
