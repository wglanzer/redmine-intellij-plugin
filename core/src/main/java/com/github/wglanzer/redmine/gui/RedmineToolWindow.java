package com.github.wglanzer.redmine.gui;

import com.intellij.openapi.ui.SimpleToolWindowPanel;

/**
 * Toolwindow for redmine plugin
 *
 * @author w.glanzer, 19.11.2016.
 */
class RedmineToolWindow extends SimpleToolWindowPanel
{

  public RedmineToolWindow()
  {
    super(true, true);
    setContent(new RedmineToolComponent());
  }

}
