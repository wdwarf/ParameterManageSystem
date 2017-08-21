package com.parammgr.db.service;

import java.util.List;

import com.parammgr.db.dao.Project;

public interface IProjectService {

	public List<Project> getAllProjects();
	public Project getProjectById(String projectId);
	public Project getProjectByName(String projectName);
	public void addProject(Project project);
	public void deleteProject(Project project);
	public void deleteProjectById(String projectId);
	public void deleteProjectByName(String projectName);
	public void updateProject(Project project);
}
