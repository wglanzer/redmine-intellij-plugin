package com.github.wglanzer.redmine.config.gui;

import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.components.JBList;

/**
 * Component inside settings.
 * Redmine-Sources
 *
 * @author w.glanzer, 06.10.2016.
 */
class SourcesList extends JBList implements AnActionButtonRunnable
{
  public SourcesList()
  {
    super(new CollectionListModel<>("meinElement1", "meinElement2", "meinElement3"));
  }

  @Override
  public void run(AnActionButton pButton)
  {

  }
}
