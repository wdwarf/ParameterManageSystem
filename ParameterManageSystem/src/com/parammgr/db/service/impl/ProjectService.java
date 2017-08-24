package com.parammgr.db.service.impl;

import java.util.List;

import org.hibernate.SessionFactory;

import com.parammgr.db.dao.Project;
import com.parammgr.db.service.IProjectService;

public class ProjectService implements IProjectService {
	private SessionFactory sessionFactory;
	
	@Override
	public Project getProjectById(String projectId) {
		List<Project> projects = sessionFactory.openSession().createQuery("from Project where project_id=:projectId").setParameter("projectId", projectId)
				.getResultList();

		if (!projects.isEmpty()) {
			return projects.get(0);
		}
		return null;
	}

	@Override
	public Project getProjectByName(String projectName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addProject(Project project) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProject(Project project) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProjectById(String projectId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProjectByName(String projectName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProject(Project project) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Project> getAllProjects() {
		List<Project> projects = sessionFactory.openSession().createQuery("from Project").getResultList();
		return projects;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
