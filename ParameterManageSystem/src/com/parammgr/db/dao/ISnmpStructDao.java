package com.parammgr.db.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.parammgr.db.entity.SnmpStruct;
import com.parammgr.db.entity.SnmpStructMember;
import com.parammgr.db.entity.WebStruct;

public interface ISnmpStructDao {
	//struct
	public List<SnmpStruct> getAllStructs(String projectId);
	public List<SnmpStruct> getAllStructOidsAndNames(String projectId);
	public List<String> getAllStructOids(String projectId);
	public List<String> getAllStructNames(String projectId);
	public SnmpStruct getStruct(String snmpstructId);
	public SnmpStruct getStructByOid(String projectId, String oid);
	public SnmpStruct getStructByStructName(String projectId, String structName);
	public void addStruct(SnmpStruct snmpstruct) throws Exception;
	public void updateStruct(SnmpStruct snmpstruct) throws Exception;
	public void deleteStruct(String snmpstructId);
	public void clearStructs(String projectId);
	
	//member
	public SnmpStructMember getStructMember(String snmpstructmemberId);
	public void addStructMember(SnmpStructMember member);
	public void deleteStructMember(SnmpStructMember member);
	public void clearStructMembers(String snmpstructId);
	
}
