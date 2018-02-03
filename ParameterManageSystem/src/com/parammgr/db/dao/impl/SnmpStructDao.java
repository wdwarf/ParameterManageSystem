package com.parammgr.db.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;

import com.parammgr.db.dao.ISnmpStructDao;
import com.parammgr.db.entity.SnmpStruct;
import com.parammgr.db.entity.SnmpStructMember;
import com.parammgr.db.entity.WebStruct;

public class SnmpStructDao implements ISnmpStructDao {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<SnmpStruct> getAllStructs(String projectId) {
		List<SnmpStruct> datas = this.sessionFactory.getCurrentSession()
		.createQuery("from SnmpStruct where projectId=:projectId order by oid asc", SnmpStruct.class)
		.setParameter("projectId", projectId)
		.getResultList();
		return datas;
	}
	
	@Override
	public List<String> getAllStructNames(String projectId){
		return this.sessionFactory.getCurrentSession()
				.createQuery("select structName from SnmpStruct where projectId=:projectId order by structName", String.class)
				.setParameter("projectId", projectId)
				.getResultList();
	}
	
	@Override
	public List<String> getAllStructOids(String projectId){
		return this.sessionFactory.getCurrentSession()
				.createQuery("select oid from SnmpStruct where projectId=:projectId order by oid", String.class)
				.setParameter("projectId", projectId)
				.getResultList();
	}
	
	@Override
	public List<SnmpStruct> getAllStructOidsAndNames(String projectId){
		List<Object[]> datas = this.sessionFactory.getCurrentSession()
				.createNativeQuery(
						"select oid,structName from SnmpStruct where projectId=:projectId order by structName asc")
				.setParameter("projectId", projectId).getResultList();

		List<SnmpStruct> structs = new ArrayList<SnmpStruct>();
		for (Object[] objs : datas) {
			SnmpStruct s = new SnmpStruct();
			s.setProjectId(projectId);
			try{
				s.setOid(objs[0].toString());
			}catch(Exception e){
				e.printStackTrace();
			}
			s.setStructName(objs[1].toString());

			structs.add(s);
		}
		return structs;
	}

	@Override
	public SnmpStruct getStruct(String snmpstructId){
		List<SnmpStruct> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from SnmpStruct where snmpstructId=:snmpstructId", SnmpStruct.class)
				.setParameter("snmpstructId", snmpstructId)
				.getResultList();
		if(null != datas && !datas.isEmpty()){
			return datas.get(0);
		}
		
		return null;
	}
	
	@Override
	public SnmpStruct getStructByStructName(String projectId, String structName){
		List<SnmpStruct> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from SnmpStruct where projectId=:projectId and structName=:structName", SnmpStruct.class)
				.setParameter("projectId", projectId).setParameter("structName", structName)
				.getResultList();
		if(null != datas && !datas.isEmpty()){
			return datas.get(0);
		}
		
		return null;
	}
	
	@Override
	public SnmpStruct getStructByOid(String projectId, String oid){
		List<SnmpStruct> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from SnmpStruct where projectId=:projectId and oid=:oid", SnmpStruct.class)
				.setParameter("projectId", projectId).setParameter("oid", oid)
				.getResultList();
		if(null != datas && !datas.isEmpty()){
			return datas.get(0);
		}
		
		return null;
	}
	
	@Override
	public void addStruct(SnmpStruct snmpstruct) throws Exception{
		snmpstruct.setSnmpstructId(null);
		for(SnmpStructMember m : snmpstruct.getMembers()){
			m.setSnmpstructmemberId(null);
		}
		snmpstruct.check();
		this.sessionFactory.getCurrentSession().save(snmpstruct);
		this.sessionFactory.getCurrentSession().flush();
	}
	
	@Override
	public void updateStruct(SnmpStruct snmpstruct) throws Exception{
		this.deleteStruct(snmpstruct.getSnmpstructId());
		this.addStruct(snmpstruct);
		//this.sessionFactory.getCurrentSession().update(snmpstruct);
	}
	
	@Override
	public void deleteStruct(String snmpstructId){
		SnmpStruct snmpstruct = this.getStruct(snmpstructId);
		this.sessionFactory.getCurrentSession().delete(snmpstruct);
		this.sessionFactory.getCurrentSession().flush();
	}
	
	@Override
	public void clearStructs(String projectId){
		this.sessionFactory.getCurrentSession()
		.createQuery("delete from SnmpStruct where projectId=:projectId")
		.setParameter("projectId", projectId).executeUpdate();
		this.sessionFactory.getCurrentSession().flush();
	}

	//member
	@Override
	public SnmpStructMember getStructMember(String snmpstructmemberId){
		List<SnmpStructMember> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from SnmpStructMember where snmpstructmemberId=:snmpstructmemberId", SnmpStructMember.class)
				.setParameter("snmpstructmemberId", snmpstructmemberId)
				.getResultList();
		if(null != datas && !datas.isEmpty()){
			return datas.get(0);
		}
		
		return null;
	}

	@Override
	public void addStructMember(SnmpStructMember member){
		this.sessionFactory.getCurrentSession().save(member);
	}

	@Override
	public void deleteStructMember(SnmpStructMember member){
		SnmpStructMember m = this.getStructMember(member.getSnmpstructmemberId());
		this.sessionFactory.getCurrentSession().delete(m);
		this.sessionFactory.getCurrentSession().flush();
	}

	@Override
	public void clearStructMembers(String snmpstructId){
		this.sessionFactory.getCurrentSession()
		.createQuery("delete from SnmpStructMember where snmpstructId=:snmpstructId")
		.setParameter("snmpstructId", snmpstructId).executeUpdate();
		this.sessionFactory.getCurrentSession().flush();
	}
}
