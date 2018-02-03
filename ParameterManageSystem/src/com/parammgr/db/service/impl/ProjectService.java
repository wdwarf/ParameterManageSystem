package com.parammgr.db.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.parammgr.db.dao.IProjectDao;
import com.parammgr.db.entity.Project;
import com.parammgr.db.service.IProjectService;

//@Service  
//@Transactional
public class ProjectService implements IProjectService {
	// @Autowired
	private IProjectDao projectDao;

	public IProjectDao getProjectDao() {
		return projectDao;
	}

	public void setProjectDao(IProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	@Override
	public List<Project> getAllProjects() {
		try{
			return this.projectDao.getAllProjects();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<Project>();
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
	public List<Project> getProjectsByName(String projectName) {
		try{
			return this.projectDao.getProjectsByName(projectName);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<Project>();
	}

	@Override
	public void addProject(Project project) throws Exception {
		try {
			this.projectDao.addProject(project);
		} catch (Exception e) {
			throw new Exception("Add project failed, msg: " + e.getMessage());
		}
	}

	@Override
	public void deleteProject(Project project) throws Exception {
		try {
			this.projectDao.deleteProject(project);
		} catch (Exception e) {
			throw new Exception("Delete project failed, msg: " + e.getMessage());
		}
	}

	@Override
	public void deleteProjectById(String projectId) throws Exception {
		try {
			this.projectDao.deleteProjectById(projectId);
		} catch (Exception e) {
			throw new Exception("Delete project failed, msg: " + e.getMessage());
		}
	}

	@Override
	public void deleteProjectByName(String projectName) throws Exception {
		try {
			this.projectDao.deleteProjectByName(projectName);
		} catch (Exception e) {
			throw new Exception("Delete project failed, msg: " + e.getMessage());
		}
	}

	@Override
	public void updateProject(Project project)  throws Exception {
		try {
			this.projectDao.updateProject(project);
		} catch (Exception e) {
			throw new Exception("Add project failed, msg: " + e.getMessage());
		}
	}
	
	@Override
	public void updateProjectName(String projectId, String projectName)  throws Exception{
		Project project = this.getProjectById(projectId);
		Project projectByName = this.getProjectByName(projectName);
		if(null != project 
				&& null != projectByName 
				&& project.getProjectId() != projectByName.getProjectId()){
			throw new Exception("project [" + projectName + "] has exists");
		}
		
		project.setProjectName(projectName);
		this.updateProject(project);
	}
}
