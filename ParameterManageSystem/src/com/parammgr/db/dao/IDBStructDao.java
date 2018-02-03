package com.parammgr.db.dao;

import java.util.List;

import com.parammgr.db.entity.DBStruct;
import com.parammgr.db.entity.DBStructDefInstance;
import com.parammgr.db.entity.DBStructMember;
import com.parammgr.db.entity.WebStruct;

public interface IDBStructDao {
	//struct
	public List<DBStruct> getAllStructs(String projectId);
	public List<DBStruct> getAllStructIdsAndNames(String projectId);
	public List<Integer> getAllStructIds(String projectId);
	public List<String> getAllStructNames(String projectId);
	public DBStruct getStruct(String dbstructId);
	public DBStruct getStruct(String projectId, String structName);
	public DBStruct getStruct(String projectId, int structId);
	public void addStruct(DBStruct dbstruct) throws Exception;
	public void updateStruct(DBStruct dbstruct) throws Exception;
	public void deleteStruct(String dbstructId);
	public void clearStructs(String projectId);
	
	//member
	public DBStructMember getStructMember(String dbstructmemberId);
	public void addStructMember(DBStructMember member);
	public void deleteStructMember(DBStructMember member);
	public void clearStructMembers(String dbstructId);
	
	//def instance
	public DBStructDefInstance getDefInstance(String dbstructdefinstanceId);
	public void addDefInstance(DBStructDefInstance instance);
	public void addDefInstance(String dbstructmemberId, DBStructDefInstance instance);
	public void addDefInstance(String dbstructmemberId, List<DBStructDefInstance> instances);
	public void deleteDefInstance(DBStructDefInstance instance);
	public void clearDefInstancesOfMember(String dbstructmemberId);
}
