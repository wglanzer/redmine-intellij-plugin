package com.github.wglanzer.redmine.config.gui;

import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.components.JBList;

/**
 * Component inside settings.
 * Watches
 *
 * @author w.glanzer, 06.10.2016.
 */
class WatchesList extends JBList implements AnActionButtonRunnable
{
  public WatchesList()
  {
    super(new CollectionListModel<>("meineWatch1", "meineWatch2", "meineWatch3"));
  }

  @Override
  public void run(AnActionButton pButton)
  {

  }
}
