package com.parammgr.db.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;

import com.parammgr.db.dao.IProjectDao;
import com.parammgr.db.entity.Project;

public class ProjectDao implements IProjectDao {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Project getProjectById(String projectId) {
		List<Project> projects = sessionFactory.getCurrentSession()
				.createQuery("from Project where projectId=:projectId", Project.class)
				.setParameter("projectId", projectId).getResultList();

		if (!projects.isEmpty()) {
			return projects.get(0);
		}
		return null;
	}

	@Override
	public Project getProjectByName(String projectName) {
		List<Project> projects = sessionFactory.getCurrentSession()
				.createQuery("from Project where projectName=:projectName", Project.class)
				.setParameter("projectName", projectName).getResultList();

		if (!projects.isEmpty()) {
			return projects.get(0);
		}
		return null;
	}

	@Override
	public List<Project> getProjectsByName(String projectName) {
		List<Project> projects = sessionFactory.getCurrentSession()
				.createQuery("from Project where projectName like :projectName", Project.class)
				.setParameter("projectName", "%" + projectName + "%").getResultList();

		return projects;
	}

	@Override
	public void addProject(Project project) {
		this.sessionFactory.getCurrentSession().save(project);
	}

	@Override
	public void deleteProject(Project project) {
		this.sessionFactory.getCurrentSession().delete(project);
		this.sessionFactory.getCurrentSession().flush();
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
		if (null != project) {
			this.sessionFactory.getCurrentSession().delete(project);
			this.sessionFactory.getCurrentSession().flush();
		}
	}

	@Override
	public void updateProject(Project project) {
		this.sessionFactory.getCurrentSession().update(project);
	}

	@Override
	public List<Project> getAllProjects() {
		List<Project> projects = this.sessionFactory.getCurrentSession().createQuery("from Project", Project.class)
				.getResultList();
		return projects;
	}

}
