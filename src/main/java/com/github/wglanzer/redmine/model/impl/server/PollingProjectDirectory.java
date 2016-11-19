package com.github.wglanzer.redmine.model.impl.server;

import com.github.wglanzer.redmine.model.IProject;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.*;

/**
 * Directory for PollingProjects.
 * All PollingProject-instances where saved here.
 *
 * @author w.glanzer, 19.11.2016.
 */
class PollingProjectDirectory
{

  private final Map<String, PollingProject> directory = Collections.synchronizedMap(new HashMap<>());

  /**
   * Returns all curretly registered projects
   *
   * @return List of all projects
   */
  @NotNull
  public Collection<IProject> getProjects()
  {
    return new ArrayList<>(directory.values());
  }

  /**
   * Clears the complete project-cache.
   * Releases all projects
   */
  public void clearCaches()
  {
    directory.forEach((pID, pProject) -> pProject.destroy());
    directory.clear();
  }

  /**
   * Updates an specific project.
   * If the project does not exist, a new instance will be created
   *
   * @param pProject JSON-Object from PollingServer
   * @return Project instance which was created or updated
   */
  protected IProject updateProject(JSONObject pProject)
  {
    String projectID = String.valueOf(pProject.getInt("id"));
    String name = pProject.getString("name");
    String description = pProject.getString("description");
    String createdOn = pProject.getString("created_on");
    String updatedOn = pProject.getString("updated_on");

    if(!directory.containsKey(projectID))
    {
      // No updateProperties neccessary here!
      PollingProject pp = new PollingProject(projectID, name, description, createdOn, updatedOn);
      directory.put(projectID, pp);
      return pp;
    }
    else
    {
      PollingProject ppToUpdate = directory.get(projectID);
      ppToUpdate.updateProperties(name, description, createdOn, updatedOn, true);
      return ppToUpdate;
    }
  }

  /**
   * Removes a specific project from cache and marks it as invalid
   *
   * @param pProject project to be removed
   */
  protected void removeProjectFromCache(IProject pProject)
  {
    String projectID = String.valueOf(pProject.getID());
    PollingProject project = directory.remove(projectID);
    if(project != null)
      project.destroy();
  }

}
