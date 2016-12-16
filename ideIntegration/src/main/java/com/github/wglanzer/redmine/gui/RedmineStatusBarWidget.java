package com.github.wglanzer.redmine.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerAdapter;
import com.intellij.openapi.wm.CustomStatusBarWidget;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * Widget for IntelliJ
 *
 * @author w.glanzer, 16.12.2016.
 */
public class RedmineStatusBarWidget implements CustomStatusBarWidget
{
  public static final String REDMINE_STATUSBAR_ID = "Redmine";
  private static RedmineStatusBarWidget _INSTANCE;

  public static RedmineStatusBarWidget getInstance()
  {
    if(_INSTANCE == null)
      _INSTANCE = new RedmineStatusBarWidget();
    return _INSTANCE;
  }

  public static void init()
  {
    // Add to all projects, that ARE OPENED ATM
    for(Project project : ProjectManager.getInstance().getOpenProjects())
      WindowManager.getInstance().getStatusBar(project).addWidget(RedmineStatusBarWidget.getInstance());

    // Add to all projects, that WILL BE OPENED
    ProjectManager.getInstance().addProjectManagerListener(new ProjectManagerAdapter()
    {
      @Override
      public void projectOpened(Project pProject)
      {
        WindowManager.getInstance().getStatusBar(pProject).addWidget(RedmineStatusBarWidget.getInstance());
      }
    });
  }

  private RedmineStatusBarComponent component;

  private RedmineStatusBarWidget()
  {
    component = new RedmineStatusBarComponent();
  }

  @NotNull
  @Override
  public String ID()
  {
    return REDMINE_STATUSBAR_ID;
  }

  @Nullable
  @Override
  public WidgetPresentation getPresentation(@NotNull PlatformType pPlatformType)
  {
    return new WidgetPresentation()
    {
      @Nullable
      @Override
      public String getTooltipText()
      {
        return component.getToolTipText();
      }

      @Nullable
      @Override
      public Consumer<MouseEvent> getClickConsumer()
      {
        return component::onClick;
      }
    };
  }

  @Override
  public void install(@NotNull StatusBar pStatusBar)
  {
  }

  @Override
  public void dispose()
  {
  }

  @Override
  public JComponent getComponent()
  {
    return component;
  }
}
