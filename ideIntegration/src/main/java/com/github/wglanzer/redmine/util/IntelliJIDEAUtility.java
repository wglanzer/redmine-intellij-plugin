package com.github.wglanzer.redmine.util;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NotNull;

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

}
