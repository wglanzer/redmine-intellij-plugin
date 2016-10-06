package com.github.wglanzer.redmine.config.gui;

import com.github.wglanzer.redmine.config.model.RAppSettingsModel;
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
  private RAppSettingsModel model;

  public SourcesList(RAppSettingsModel pModel)
  {
    super(new _Model(pModel));
    model = pModel;
  }

  @Override
  public void run(AnActionButton pButton)
  {
  }

  /**
   * ListModel-Impl for SourcesList
   */
  private static class _Model extends CollectionListModel<Object>
  {
    private RAppSettingsModel model;

    public _Model(RAppSettingsModel pModel)
    {
      model = pModel;
    }
  }
}
