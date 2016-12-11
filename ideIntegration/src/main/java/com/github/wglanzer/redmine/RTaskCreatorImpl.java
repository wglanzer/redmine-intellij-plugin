package com.github.wglanzer.redmine;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TaskCreator to connect intelliJ-UI to redmine tasks
 *
 * @author w.glanzer, 11.12.2016.
 */
class RTaskCreatorImpl implements IRTaskCreator
{

  private final AtomicBoolean _inited = new AtomicBoolean(false);
  private final List<Task> _preInitTasks = new ArrayList<>();

  public RTaskCreatorImpl()
  {
    // Only if the first project was opened, you can successfully add background tasks...
    ProjectManager.getInstance().addProjectManagerListener(new ProjectManagerAdapter()
    {
      @Override
      public void projectOpened(Project project)
      {
        ProjectManager.getInstance().removeProjectManagerListener(this);
        synchronized(_inited)
        {
          _inited.set(true);
          _preInitTasks.forEach(Task::queue);
        }
      }
    });
  }

  @Override
  public <T extends ITask> T executeInBackground(T pTask)
  {
    _executeWhenProjectLoaded(new Task.Backgroundable(null, pTask.getName(), false)
    {
      @Override
      public void run(@NotNull ProgressIndicator pProgressIndicator)
      {
        pProgressIndicator.setText(pTask.getName());
        pTask.accept(new _IDEA_Indicator(pProgressIndicator));
      }
    });

    return pTask;
  }

  /**
   * Executes a task, when the first project was loaded
   *
   * @param pIdeaTask Task that should be executed
   */
  private void _executeWhenProjectLoaded(Task pIdeaTask)
  {
    synchronized(_inited)
    {
      if(_inited.get())
        pIdeaTask.queue();
      else
        _preInitTasks.add(pIdeaTask);
    }
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
