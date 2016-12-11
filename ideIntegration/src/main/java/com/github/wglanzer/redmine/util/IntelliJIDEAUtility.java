package com.github.wglanzer.redmine.util;

import com.github.wglanzer.redmine.RApplicationComponent;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

/**
 * Utility for IDEA-Actions
 *
 * @author w.glanzer, 27.11.2016.
 */
public class IntelliJIDEAUtility
{

  /**
   * Returns the whole plugin directory on HDD
   *
   * @return plugin dir, not <tt>null</tt>
   */
  @NotNull
  public static File getPluginDirectory()
  {
    IdeaPluginDescriptor myPluginDescriptor = PluginManager.getPlugin(PluginId.findId("redmine-plugin"));
    assert myPluginDescriptor != null;
    return myPluginDescriptor.getPath();
  }

  /**
   * Returns the directory for ticket caching
   *
   * @return ticket dir, not <tt>null</tt>
   */
  @NotNull
  public static File getTicketCacheDirectory()
  {
    File pluginDirectory = getPluginDirectory();
    File ticketCachesFolder = new File(pluginDirectory, "ticketCaches");
    if(!ticketCachesFolder.exists())
      ticketCachesFolder.mkdir();
    return ticketCachesFolder;
  }

  /**
   * Shows a message on all opened projects
   *
   * @param pTitle        Title that should be shown
   * @param pDetails      Details of the message
   * @param pType         MessageType (Error, Info, ...)
   * @param pOnlyEventLog <tt>true</tt> if the message should only appear in event-log
   */
  public static void showMessage(@Nullable String pTitle, @Nullable String pDetails, @NotNull NotificationType pType, boolean pOnlyEventLog)
  {
    Notification notification = new Notification(RApplicationComponent.NOTIFICATION_ID, new ImageIcon(), RApplicationComponent.NOTIFICATION_ID, pTitle != null ? pTitle : "", pDetails, pType, null);
    if(pOnlyEventLog)
      notification.expire();
    for(Project project : ProjectManager.getInstance().getOpenProjects())
      Notifications.Bus.notify(notification, project);
  }

}
