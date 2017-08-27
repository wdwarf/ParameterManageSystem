package com.parammgr.db.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;

import com.parammgr.db.dao.IDBStructDao;
import com.parammgr.db.entity.DBStruct;
import com.parammgr.db.entity.DBStructPK;

public class DBStructDao implements IDBStructDao {
	private SessionFactory sessionFactory;

	@Override
	public List<DBStruct> getAllStructs(String projectId) {
		return this.sessionFactory.getCurrentSession()
		.createQuery("from DBStruct where primaryKey.projectId=:projectId")
		.setParameter("projectId", projectId)
		.getResultList();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
