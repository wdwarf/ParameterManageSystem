package com.parammgr.db.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parammgr.db.dao.IProjectDao;
import com.parammgr.db.dao.impl.ProjectDao;
import com.parammgr.db.entity.Project;
import com.parammgr.db.service.IProjectService;


//@Service  
//@Transactional
public class ProjectService implements IProjectService {
	//@Autowired
	private IProjectDao projectDao;

	public IProjectDao getProjectDao() {
		return projectDao;
	}

	public void setProjectDao(IProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	@Override
	public List<Project> getAllProjects() {
		return this.projectDao.getAllProjects();
	}

	@Override
	public Project getProjectById(String projectId) {
		return this.projectDao.getProjectById(projectId);
	}

	@Override
	public Project getProjectByName(String projectName) {
		return this.projectDao.getProjectByName(projectName);
	}
	
	@Override
	public List<Project> getProjectsByName(String projectName){
		return this.projectDao.getProjectsByName(projectName);
	}

	@Override
	public void addProject(Project project) {
		this.projectDao.addProject(project);
	}

	@Override
	public void deleteProject(Project project) {
		this.projectDao.deleteProject(project);
	}

	@Override
	public void deleteProjectById(String projectId) {
		this.projectDao.deleteProjectById(projectId);
	}

	@Override
	public void deleteProjectByName(String projectName) {
		this.projectDao.deleteProjectByName(projectName);
	}

	@Override
	public void updateProject(Project project) {
		this.projectDao.updateProject(project);
	}
}
