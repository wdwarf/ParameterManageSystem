package com.parammgr.db.service.impl;

import java.util.List;

import org.hibernate.Session;

import com.parammgr.db.SessionFactory;
import com.parammgr.db.dao.Project;
import com.parammgr.db.service.IProjectService;

public class ProjectService implements IProjectService {

	private org.hibernate.SessionFactory sessionFactory = SessionFactory.createSessionFactory();
	private Session session = sessionFactory.openSession();

	@Override
	public void finalize() {
		try {
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Project getProjectById(String projectId) {
		List<Project> projects = session.createQuery("from Project where project_id=:projectId").setParameter("projectId", projectId)
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
		List<Project> projects = session.createQuery("from Project").getResultList();
		return projects;
	}

}
