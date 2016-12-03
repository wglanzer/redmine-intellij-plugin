package com.github.wglanzer.redmine.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * ToolFactory for IJ-Integration
 *
 * @author w.glanzer, 19.11.2016.
 */
public class RedmineToolWindowFactory implements ToolWindowFactory
{
  @Override
  public void createToolWindowContent(@NotNull Project pProject, @NotNull ToolWindow pToolWindow)
  {
    RedmineToolWindow window = new RedmineToolWindow();
    pToolWindow.getContentManager().addContent(ContentFactory.SERVICE.getInstance().createContent(window, null, false));
  }
}
