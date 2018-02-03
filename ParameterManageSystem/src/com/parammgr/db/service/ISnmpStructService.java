package com.parammgr.db.service;

import java.io.File;
import java.util.List;

import com.parammgr.db.entity.CheckPoint;
import com.parammgr.db.entity.SnmpStruct;
import com.parammgr.db.entity.SnmpStructMember;

public interface ISnmpStructService {
	public List<SnmpStruct> getAllStructs(String projectId);
	public List<String> getAllStructNames(String projectId);
	public List<String> getAllStructOids(String projectId);
	public List<SnmpStruct> getAllStructOidsAndNames(String projectId);
	public SnmpStruct getStruct(String dbstructId);
	public SnmpStruct getStructByStructName(String projectId, String structName);
	public SnmpStruct getStructByOid(String projectId, String oid);
	public void addStruct(SnmpStruct dbstruct) throws Exception;
	public void updateStruct(SnmpStruct dbstruct) throws Exception;
	public void deleteStruct(String dbstructId) throws Exception;
	public void deleteStructByStructName(String projectId, String structName) throws Exception;
	public void deleteStructByOid(String projectId, String oid) throws Exception;
	public void clearStructs(String projectId);

	//member
	public SnmpStructMember getStructMember(String dbstructmemberId);
	public void addStructMember(SnmpStructMember member) throws Exception;
	public void deleteStructMember(SnmpStructMember member);
	public void clearStructMembers(String dbstructId);
	
	//import/export
	public void importStructs(String projectId, File file) throws Exception;
	public void exportStructs(String projectId, File file) throws Exception;
	public String exportStructs(String projectId) throws Exception;

	public CheckPoint createCheckPoint(String projectId, String description) throws Exception;
	public boolean checkPointRecovery(String checkpointId);
}
