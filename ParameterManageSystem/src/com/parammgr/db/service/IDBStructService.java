package com.parammgr.db.service;

import java.io.File;
import java.util.List;

import com.parammgr.db.entity.CheckPoint;
import com.parammgr.db.entity.DBStruct;
import com.parammgr.db.entity.DBStructDefInstance;
import com.parammgr.db.entity.DBStructMember;
import com.parammgr.db.entity.WebStruct;

public interface IDBStructService {
	public List<DBStruct> getAllStructs(String projectId);
	public List<String> getAllStructNames(String projectId);
	public List<DBStruct> getAllStructIdsAndNames(String projectId);
	public List<Integer> getAllStructIds(String projectId);
	public DBStruct getStruct(String dbstructId);
	public DBStruct getStruct(String projectId, String structName);
	public DBStruct getStruct(String projectId, int structId);
	public void addStruct(DBStruct dbstruct) throws Exception;
	public void updateStruct(DBStruct dbstruct) throws Exception;
	public void deleteStruct(String dbstructId) throws Exception;
	public void deleteStruct(String projectId, String structName) throws Exception;
	public void clearStructs(String projectId);

	//member
	public DBStructMember getStructMember(String dbstructmemberId);
	public void addStructMember(DBStructMember member) throws Exception;
	public void deleteStructMember(DBStructMember member);
	public void clearStructMembers(String dbstructId);
	
	//def instance
	public DBStructDefInstance getDefInstance(String dbstructdefinstanceId);
	public void addDefInstance(DBStructDefInstance instance) throws Exception;
	public void addDefInstance(String dbstructmemberId, DBStructDefInstance instance) throws Exception;
	public void addDefInstance(String dbstructmemberId, List<DBStructDefInstance> instances) throws Exception;
	public void deleteDefInstance(DBStructDefInstance instance);
	public void clearDefInstancesOfMember(String dbstructmemberId);
	
	public void importStructs(String projectId, File zipFile) throws Exception;
	public String exportToDBFile(String projectId) throws Exception;
	public void exportToExcelFile(String projectId, File file) throws Exception;
	public String exportToExcelFile(String projectId) throws Exception;
	
	public CheckPoint createCheckPoint(String projectId, String description) throws Exception;
	public boolean checkPointRecovery(String checkpointId);
}
