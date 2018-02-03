package com.parammgr.db.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import com.parammgr.db.dao.ICheckPointDao;
import com.parammgr.db.entity.CheckPoint;

public class CheckPointDao implements ICheckPointDao {
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void addCheckPoint(CheckPoint checkPoint) {
		checkPoint.setCheckpointId(null);
		this.sessionFactory.getCurrentSession().save(checkPoint);
		this.sessionFactory.getCurrentSession().flush();
	}

	@Override
	public CheckPoint getCheckPoint(String checkpointId){
		List<CheckPoint> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from CheckPoint where checkpointId=:checkpointId", CheckPoint.class)
				.setParameter("checkpointId", checkpointId)
				.getResultList();
		if(!datas.isEmpty()){
			return datas.get(0);
		}
		
		return null;
	}
	
	@Override
	public List<CheckPoint> getAllCheckPoints(String projectId, String type){
		List<Object[]> datas = this.sessionFactory.getCurrentSession()
			.createNativeQuery("select checkpointId,projectId,description,createDate from CheckPoint where projectId=:projectId and type=:type order by createDate desc")
			.setParameter("projectId", projectId).setParameter("type", type)
			.getResultList();
		
		List<CheckPoint> cps = new ArrayList<CheckPoint>();
		for(Object[] objs : datas){
			CheckPoint cp = new CheckPoint();
			cp.setCheckpointId(objs[0].toString());
			cp.setProjectId(objs[1].toString());
			cp.setDescription(objs[2].toString());
			cp.setCreateDate((Date)objs[3]);
			
			cps.add(cp);
		}
		return cps;
	}
	
	@Override
	public void deleteCheckPoint(String checkpointId){
		CheckPoint cp = this.getCheckPoint(checkpointId);
		this.sessionFactory.getCurrentSession().delete(cp);
		this.sessionFactory.getCurrentSession().flush();
	}
	
	@Override
	public void clearAllCheckPoint(String projectId){
		this.sessionFactory.getCurrentSession()
				.createQuery("delete from CheckPoint where projectId=:projectId", CheckPoint.class)
				.setParameter("projectId", projectId).executeUpdate();
		this.sessionFactory.getCurrentSession().flush();
	}
	
	@Override
	public void clearAllCheckPoint(String projectId, String type) {
		this.sessionFactory.getCurrentSession()
				.createQuery("delete from CheckPoint where projectId=:projectId and type=:type", CheckPoint.class)
				.setParameter("projectId", projectId).setParameter("type", type).executeUpdate();
		this.sessionFactory.getCurrentSession().flush();
	}

	@Override
	public void updateCheckPointDescription(String checkpointId, String description){
		CheckPoint cp = this.getCheckPoint(checkpointId);
		if(null == cp) return;
		cp.setDescription(description);
		this.sessionFactory.getCurrentSession().update(cp);
		this.sessionFactory.getCurrentSession().flush();
	}
	
}
