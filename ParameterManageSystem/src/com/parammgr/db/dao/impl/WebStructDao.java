package com.parammgr.db.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import com.parammgr.db.dao.IWebStructDao;
import com.parammgr.db.entity.CheckPoint;
import com.parammgr.db.entity.WebStruct;
import com.parammgr.db.entity.WebStructMember;

public class WebStructDao implements IWebStructDao {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<WebStruct> getAllStructs(String projectId) {
		List<WebStruct> datas = this.sessionFactory.getCurrentSession()
		.createQuery("from WebStruct where projectId=:projectId order by structId", WebStruct.class)
		.setParameter("projectId", projectId)
		.getResultList();
		return datas;
	}
	
	@Override
	public List<WebStruct> getAllStructIdsAndNames(String projectId){
		List<Object[]> datas = this.sessionFactory.getCurrentSession()
				.createNativeQuery(
						"select structId,structName from WebStruct where projectId=:projectId order by structName asc")
				.setParameter("projectId", projectId).getResultList();

		List<WebStruct> structs = new ArrayList<WebStruct>();
		for (Object[] objs : datas) {
			WebStruct s = new WebStruct();
			s.setProjectId(projectId);
			s.setStructId(Integer.parseInt(objs[0].toString()));
			s.setStructName(objs[1].toString());

			structs.add(s);
		}
		return structs;
	}
	
	@Override
	public List<String> getAllStructNames(String projectId){
		return this.sessionFactory.getCurrentSession()
				.createQuery("select structName from WebStruct where projectId=:projectId order by structName", String.class)
				.setParameter("projectId", projectId)
				.getResultList();
	}

	@Override
	public WebStruct getStruct(String webstructId){
		List<WebStruct> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from WebStruct where webstructId=:webstructId", WebStruct.class)
				.setParameter("webstructId", webstructId)
				.getResultList();
		if(null != datas && !datas.isEmpty()){
			return datas.get(0);
		}
		
		return null;
	}
	
	@Override
	public WebStruct getStruct(String projectId, String structName){
		List<WebStruct> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from WebStruct where projectId=:projectId and structName=:structName", WebStruct.class)
				.setParameter("projectId", projectId).setParameter("structName", structName)
				.getResultList();
		if(null != datas && !datas.isEmpty()){
			return datas.get(0);
		}
		
		return null;
	}
	
	@Override
	public WebStruct getStruct(String projectId, int structId){
		List<WebStruct> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from WebStruct where projectId=:projectId and structId=:structId", WebStruct.class)
				.setParameter("projectId", projectId).setParameter("structId", structId)
				.getResultList();
		if(null != datas && !datas.isEmpty()){
			return datas.get(0);
		}
		
		return null;
	}
	
	@Override
	public void addStruct(WebStruct webstruct) throws Exception{
		webstruct.setWebstructId(null);
		for(WebStructMember m : webstruct.getMembers()){
			m.setWebstructmemberId(null);
		}
		webstruct.check();
		this.sessionFactory.getCurrentSession().save(webstruct);
		this.sessionFactory.getCurrentSession().flush();
	}
	
	@Override
	public void updateStruct(WebStruct webstruct) throws Exception{
		this.deleteStruct(webstruct.getWebstructId());
		this.sessionFactory.getCurrentSession().flush();
		this.addStruct(webstruct);
		//this.sessionFactory.getCurrentSession().update(webstruct);
	}
	
	@Override
	public void deleteStruct(String webstructId){
		WebStruct webstruct = this.getStruct(webstructId);
		this.sessionFactory.getCurrentSession().delete(webstruct);
		this.sessionFactory.getCurrentSession().flush();
	}
	
	@Override
	public void clearStructs(String projectId){
		this.sessionFactory.getCurrentSession()
		.createQuery("delete from WebStruct where projectId=:projectId")
		.setParameter("projectId", projectId).executeUpdate();
		this.sessionFactory.getCurrentSession().flush();
	}

	//member
	@Override
	public WebStructMember getStructMember(String webstructmemberId){
		List<WebStructMember> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from WebStructMember where webstructmemberId=:webstructmemberId", WebStructMember.class)
				.setParameter("webstructmemberId", webstructmemberId)
				.getResultList();
		if(null != datas && !datas.isEmpty()){
			return datas.get(0);
		}
		
		return null;
	}

	@Override
	public void addStructMember(WebStructMember member){
		this.sessionFactory.getCurrentSession().save(member);
	}

	@Override
	public void deleteStructMember(WebStructMember member){
		WebStructMember m = this.getStructMember(member.getWebstructmemberId());
		this.sessionFactory.getCurrentSession().delete(m);
		this.sessionFactory.getCurrentSession().flush();
	}

	@Override
	public void clearStructMembers(String webstructId){
		this.sessionFactory.getCurrentSession()
		.createQuery("delete from WebStructMember where webstructId=:webstructId")
		.setParameter("webstructId", webstructId).executeUpdate();
		this.sessionFactory.getCurrentSession().flush();
	}
}
