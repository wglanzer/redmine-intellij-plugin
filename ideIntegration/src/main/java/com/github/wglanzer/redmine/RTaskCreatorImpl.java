package com.github.wglanzer.redmine;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * TaskCreator to connect intelliJ-UI to redmine tasks
 *
 * @author w.glanzer, 11.12.2016.
 */
class RTaskCreatorImpl implements IRTaskCreator
{

  private final Project project;

  public RTaskCreatorImpl(Project pProject)
  {
    project = pProject;
  }

  @Override
  public <T extends ITask> T executeInBackground(T pTask)
  {
    ProgressManager.getInstance().run(new Task.Backgroundable(project, pTask.getName())
    {
      @Override
      public void run(@NotNull ProgressIndicator pProgressIndicator)
      {
        pProgressIndicator.startNonCancelableSection();
        System.out.println("execute " + pTask);
        pTask.accept(new _IDEA_Indicator(pProgressIndicator));
        System.out.println("finished " + pTask);
        pProgressIndicator.finishNonCancelableSection();
      }
    });
    return pTask;
  }

  /**
   * IProgressIndicator-Impl for Intellij-IDEA
   */
  private static class _IDEA_Indicator implements IProgressIndicator
  {
    private final ProgressIndicator indicator;

    public _IDEA_Indicator(ProgressIndicator pIndicator)
    {
      indicator = pIndicator;
    }

    @Override
    public void addPercentage(double pPercentage)
    {
      indicator.setFraction(indicator.getFraction() + pPercentage);
    }
  }

}
