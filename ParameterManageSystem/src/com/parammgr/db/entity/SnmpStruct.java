package com.parammgr.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.snmp4j.smi.OID;

public class SnmpStruct implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String snmpstructId = null;
	private String projectId = "";
	private String structName = "";
	private String oid = "";
	private boolean singleTable = false;
	private Date createDate = new Date();
	private Set<SnmpStructMember> members = new HashSet<SnmpStructMember>();
	
	public String getSnmpstructId() {
		return snmpstructId;
	}

	public void setSnmpstructId(String snmpstructId) {
		this.snmpstructId = snmpstructId;
		for(SnmpStructMember elmt : this.members){
			elmt.setSnmpstructId(snmpstructId);
		}
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getStructName() {
		return structName;
	}

	public void setStructName(String structName) {
		this.structName = structName.trim();
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) throws Exception {
		try{
			OID structOid = new OID(oid);
			this.oid = structOid.toString();
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("Invalid OID: " + oid);
		}
	}

	public boolean isSingleTable() {
		return singleTable;
	}

	public void setSingleTable(boolean singleTable) {
		this.singleTable = singleTable;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Set<SnmpStructMember> getMembers() {
		return members;
	}

	public void setMembers(Set<SnmpStructMember> members) {
		this.members = members;
		for(SnmpStructMember elmt : this.members){
			elmt.setSnmpstructId(snmpstructId);
		}
	}
	
	public void addMember(SnmpStructMember member) throws Exception{
		member.setSnmpstructId(snmpstructId);
		this.members.add(member);
	}

	public SnmpStructMember getMember(int oid){
		for(SnmpStructMember member : this.members){
			if(Integer.parseInt(member.getOid()) == oid){
				return member;
			}
		}
		
		return null;
	}
	
	public SnmpStructMember getMember(String memberName){
		for(SnmpStructMember member : this.members){
			if(member.getMemberName().equals(memberName)){
				return member;
			}
		}
		
		return null;
	}
	
	public boolean isSingleInstance(){
		for(SnmpStructMember member : this.members){
			if(member.isPrimaryKey()){
				return false;
			}
		}
		
		return true;
	}
	
	public List<SnmpStructMember> getPrimaryKeyMembers(){
		List<SnmpStructMember> members = new ArrayList<SnmpStructMember>();
		for(SnmpStructMember member : this.members){
			if(member.isPrimaryKey()){
				members.add(member);
			}
		}
		return members;
	}
	
	public void check() throws Exception{
		if(null == this.structName || this.structName.isEmpty()){
			throw new Exception("Empty struct name");
		}
		
		OID structOid = new OID(oid);
		if(structOid.toString().isEmpty()){
			throw new Exception("Empty struct OID");
		}
		
		if(null != this.members){
			Set<String> elmtNames = new HashSet<String>();
			Set<String> memberNames = new HashSet<String>();
			Set<String> oids = new HashSet<String>();
			
			for(SnmpStructMember member : this.members){
				String memberName = member.getMemberName();
				String structName = member.getStructName();
				String elementName = member.getElementName();
				String oid = member.getOid();
				if(!elementName.equals("RowStatus") && (null == memberName || memberName.isEmpty())){
					throw new Exception("Empty member name in " + this.structName);
				}
				
				if(elmtNames.contains(elementName.toLowerCase())){
					throw new Exception("Duplicate element name[" + elementName + "] in " + this.structName);
				}
				elmtNames.add(elementName.toLowerCase());

				String memberId = structName + "." + memberName;
				if(memberNames.contains(memberId.toLowerCase())){
					throw new Exception("Duplicate member [" + memberId + "] in " + this.structName);
				}
				memberNames.add(memberId);
				
				OID elmtOid = new OID(oid);
				if(oids.contains(elmtOid.toString())){
					throw new Exception("Duplicate member OID[" + oid + "] in " + this.structName);
				}
				oids.add(elmtOid.toString());
			}
		}
	}
}
