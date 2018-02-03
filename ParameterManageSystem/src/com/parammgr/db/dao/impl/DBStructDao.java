package com.parammgr.db.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;

import com.parammgr.db.dao.IDBStructDao;
import com.parammgr.db.entity.DBStruct;
import com.parammgr.db.entity.DBStructDefInstance;
import com.parammgr.db.entity.DBStructMember;
import com.parammgr.db.entity.WebStruct;

public class DBStructDao implements IDBStructDao {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<DBStruct> getAllStructs(String projectId) {
		List<DBStruct> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from DBStruct where projectId=:projectId order by structId", DBStruct.class)
				.setParameter("projectId", projectId).getResultList();
		return datas;
	}

	@Override
	public List<String> getAllStructNames(String projectId) {
		return this.sessionFactory.getCurrentSession()
				.createQuery("select structName from DBStruct where projectId=:projectId order by structName",
						String.class)
				.setParameter("projectId", projectId).getResultList();
	}

	@Override
	public List<DBStruct> getAllStructIdsAndNames(String projectId){
		List<Object[]> datas = this.sessionFactory.getCurrentSession()
				.createNativeQuery(
						"select structId,structName from DBStruct where projectId=:projectId order by structName asc")
				.setParameter("projectId", projectId).getResultList();

		List<DBStruct> structs = new ArrayList<DBStruct>();
		for (Object[] objs : datas) {
			DBStruct s = new DBStruct();
			s.setProjectId(projectId);
			s.setStructId(Integer.parseInt(objs[0].toString()));
			s.setStructName(objs[1].toString());

			structs.add(s);
		}
		return structs;
	}

	@Override
	public List<Integer> getAllStructIds(String projectId) {
		return this.sessionFactory.getCurrentSession()
				.createQuery("select structId from DBStruct where projectId=:projectId order by structId",
						Integer.class)
				.setParameter("projectId", projectId).getResultList();
	}

	@Override
	public DBStruct getStruct(String dbstructId) {
		List<DBStruct> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from DBStruct where dbstructId=:dbstructId", DBStruct.class)
				.setParameter("dbstructId", dbstructId).getResultList();
		if (null != datas && !datas.isEmpty()) {
			return datas.get(0);
		}

		return null;
	}

	@Override
	public DBStruct getStruct(String projectId, String structName) {
		List<DBStruct> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from DBStruct where projectId=:projectId and structName=:structName", DBStruct.class)
				.setParameter("projectId", projectId).setParameter("structName", structName).getResultList();
		if (null != datas && !datas.isEmpty()) {
			return datas.get(0);
		}

		return null;
	}

	@Override
	public DBStruct getStruct(String projectId, int structId) {
		List<DBStruct> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from DBStruct where projectId=:projectId and structId=:structId", DBStruct.class)
				.setParameter("projectId", projectId).setParameter("structId", structId).getResultList();
		if (null != datas && !datas.isEmpty()) {
			return datas.get(0);
		}

		return null;
	}

	@Override
	public void addStruct(DBStruct dbstruct) throws Exception {
		dbstruct.check();
		this.sessionFactory.getCurrentSession().save(dbstruct);
		this.sessionFactory.getCurrentSession().flush();
	}

	@Override
	public void updateStruct(DBStruct dbstruct) throws Exception {
		this.deleteStruct(dbstruct.getDbstructId());
		this.sessionFactory.getCurrentSession().flush();
		this.addStruct(dbstruct);
		// this.sessionFactory.getCurrentSession().update(dbstruct);
	}

	@Override
	public void deleteStruct(String dbstructId) {
		DBStruct dbstruct = this.getStruct(dbstructId);
		this.sessionFactory.getCurrentSession().delete(dbstruct);
		this.sessionFactory.getCurrentSession().flush();
	}

	@Override
	public void clearStructs(String projectId) {
		this.sessionFactory.getCurrentSession().createQuery("delete from DBStruct where projectId=:projectId")
				.setParameter("projectId", projectId).executeUpdate();
		this.sessionFactory.getCurrentSession().flush();
	}

	// member
	@Override
	public DBStructMember getStructMember(String dbstructmemberId) {
		List<DBStructMember> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from DBStructMember where dbstructmemberId=:dbstructmemberId", DBStructMember.class)
				.setParameter("dbstructmemberId", dbstructmemberId).getResultList();
		if (null != datas && !datas.isEmpty()) {
			return datas.get(0);
		}

		return null;
	}

	@Override
	public void addStructMember(DBStructMember member) {
		this.sessionFactory.getCurrentSession().save(member);
	}

	@Override
	public void deleteStructMember(DBStructMember member) {
		DBStructMember m = this.getStructMember(member.getDbstructmemberId());
		this.sessionFactory.getCurrentSession().delete(m);
		this.sessionFactory.getCurrentSession().flush();
	}

	@Override
	public void clearStructMembers(String dbstructId) {
		this.sessionFactory.getCurrentSession().createQuery("delete from DBStructMember where dbstructId=:dbstructId")
				.setParameter("dbstructId", dbstructId).executeUpdate();
		this.sessionFactory.getCurrentSession().flush();
	}

	// def instance
	@Override
	public DBStructDefInstance getDefInstance(String dbstructdefinstanceId) {
		List<DBStructDefInstance> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from DBStructDefInstance where dbstructdefinstanceId=:dbstructdefinstanceId",
						DBStructDefInstance.class)
				.setParameter("dbstructdefinstanceId", dbstructdefinstanceId).getResultList();
		if (null != datas && !datas.isEmpty()) {
			return datas.get(0);
		}

		return null;
	}

	@Override
	public void addDefInstance(DBStructDefInstance instance) {
		DBStructDefInstance ins = new DBStructDefInstance();
		ins.setDbstructmemberId(instance.getDbstructmemberId());
		ins.setDefValue(instance.getDefValue());
		this.sessionFactory.getCurrentSession().save(ins);
	}

	public void addDefInstance(String dbstructmemberId, DBStructDefInstance instance) {
		DBStructDefInstance ins = new DBStructDefInstance();
		ins.setDbstructmemberId(dbstructmemberId);
		ins.setDefValue(instance.getDefValue());
		this.sessionFactory.getCurrentSession().save(ins);
	}

	public void addDefInstance(String dbstructmemberId, List<DBStructDefInstance> instances) {
		for (int i = 0; i < instances.size(); ++i) {
			DBStructDefInstance instance = instances.get(i);
			DBStructDefInstance ins = new DBStructDefInstance();
			ins.setDbstructmemberId(dbstructmemberId);
			ins.setDefValue(instance.getDefValue());
			ins.setInstanceIndex(i);
			this.sessionFactory.getCurrentSession().save(ins);
		}
	}

	@Override
	public void deleteDefInstance(DBStructDefInstance instance) {
		DBStructDefInstance ins = this.getDefInstance(instance.getDbstructdefinstanceId());
		this.sessionFactory.getCurrentSession().delete(ins);
		this.sessionFactory.getCurrentSession().flush();
	}

	@Override
	public void clearDefInstancesOfMember(String dbstructmemberId) {
		this.sessionFactory.getCurrentSession()
				.createQuery("delete from DBStructDefInstance where dbstructmemberId=:dbstructmemberId")
				.setParameter("dbstructmemberId", dbstructmemberId).executeUpdate();
		this.sessionFactory.getCurrentSession().flush();
	}
}
