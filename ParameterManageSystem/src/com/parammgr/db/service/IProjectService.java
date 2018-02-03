package com.parammgr.db.service;

import java.util.List;

import com.parammgr.db.entity.Project;

public interface IProjectService {
	public List<Project> getAllProjects();
	public Project getProjectById(String projectId);
	public Project getProjectByName(String projectName);
	public List<Project> getProjectsByName(String projectName);
	public void addProject(Project project) throws Exception;
	public void deleteProject(Project project) throws Exception;
	public void deleteProjectById(String projectId) throws Exception;
	public void deleteProjectByName(String projectName) throws Exception;
	public void updateProject(Project project) throws Exception;
	public void updateProjectName(String projectId, String projectName)  throws Exception;
}
