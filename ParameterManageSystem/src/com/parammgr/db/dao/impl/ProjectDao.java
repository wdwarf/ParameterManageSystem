package com.parammgr.db.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;

import com.parammgr.db.dao.IProjectDao;
import com.parammgr.db.entity.Project;

public class ProjectDao implements IProjectDao {
	private SessionFactory sessionFactory;

	@Override
	public Project getProjectById(String projectId) {
		List<Project> projects = sessionFactory.openSession().createQuery("from Project where projectId=:projectId")
				.setParameter("projectId", projectId).getResultList();

		if (!projects.isEmpty()) {
			return projects.get(0);
		}
		return null;
	}

	@Override
	public Project getProjectByName(String projectName) {
		List<Project> projects = sessionFactory.openSession().createQuery("from Project where projectName=:projectName")
				.setParameter("projectName", projectName).getResultList();

		if (!projects.isEmpty()) {
			return projects.get(0);
		}
		return null;
	}

	@Override
	public void addProject(Project project) {
		sessionFactory.openSession().saveOrUpdate(project);
	}

	@Override
	public void deleteProject(Project project) {
		sessionFactory.openSession().delete(project);
	}

	@Override
	public void deleteProjectById(String projectId) {
		Project project = new Project();
		project.setProjectId(projectId);
		this.deleteProject(project);
	}

	@Override
	public void deleteProjectByName(String projectName) {
		Project project = this.getProjectByName(projectName);
		if(null != project) {
			sessionFactory.openSession().delete(project);
		}
	}

	@Override
	public void updateProject(Project project) {
		sessionFactory.openSession().saveOrUpdate(project);
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
