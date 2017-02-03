package com.github.wglanzer.redmine;

import com.github.wglanzer.redmine.config.beans.SettingsDataModel;

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

  SettingsDataModel getCurrentSettings();

  File getTicketCacheDirectory();

}
