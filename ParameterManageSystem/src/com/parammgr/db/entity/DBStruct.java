package com.parammgr.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBStruct implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dbstructId = null;
	private String projectId = "";
	private int structId = 0;
	private String structName = "";
	private boolean tempTable = false;
	private Date createDate = new Date();
	private Set<DBStructMember> members = new HashSet<DBStructMember>();
	
	public int getStructId() {
		return structId;
	}
	public void setStructId(int structId) {
		this.structId = structId;
	}
	public String getDbstructId() {
		return dbstructId;
	}
	public void setDbstructId(String dbstructId) {
		this.dbstructId = dbstructId;
		for(DBStructMember elmt : members){
			elmt.setDbstructId(dbstructId);
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
		this.structName = structName;
	}
	public boolean isTempTable() {
		return tempTable;
	}
	public void setTempTable(boolean tempTable) {
		this.tempTable = tempTable;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Set<DBStructMember> getMembers() {
		return members;
	}
	public void setMembers(Set<DBStructMember> members) {
		this.members = members;
		for(DBStructMember elmt : this.members){
			elmt.setDbstructId(dbstructId);
		}
	}
	
	public void addMember(DBStructMember member){
		member.setDbstructId(dbstructId);
		this.members.add(member);
	}

	public DBStructMember getMember(int memberId){
		for(DBStructMember member : this.members){
			if(member.getMemberId() == memberId){
				return member;
			}
		}
		
		return null;
	}
	
	public DBStructMember getMember(String memberName){
		for(DBStructMember member : this.members){
			if(member.getMemberName().equals(memberName)){
				return member;
			}
		}
		
		return null;
	}
	
	public boolean isSingleInstance(){
		for(DBStructMember member : this.members){
			if(member.isPrimaryKey()){
				return false;
			}
		}
		
		return true;
	}
	
	public List<DBStructMember> getPrimaryKeyMembers(){
		List<DBStructMember> members = new ArrayList<DBStructMember>();
		for(DBStructMember member : this.members){
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
		
		//结构体ID必须小于5位数
		if(this.structId <= 0 || this.structId > 99999){
			throw new Exception("Invalid struct ID");
		}
		
		if(null != this.members){
			Set<Integer> memberIds = new HashSet<Integer>();
			Set<String> memberNames = new HashSet<String>();
			
			boolean hasPrimaryKey = false;
			boolean hasDefInstance = false;
			
			for(DBStructMember member : this.members){
				String memberName = member.getMemberName();
				if(null == memberName || memberName.isEmpty()){
					throw new Exception("Empty member name in " + this.structName);
				}
				
				int memberId = member.getMemberId();
				if(memberId <= 0 || memberId > 999){
					throw new Exception("Invalid member ID of " + this.structName + "[" + memberName + "]");
				}
				
				String memberType = member.getMemberType();
				if(null == memberType || memberType.isEmpty()){
					throw new Exception("Empty value type of " + this.structName + "[" + memberName + "]");
				}
				
				if(memberIds.contains(memberId)){
					throw new Exception("Duplicate member ID[" + memberId + "] in " + this.structName);
				}
				memberIds.add(memberId);

				if(memberNames.contains(memberName)){
					throw new Exception("Duplicate member name[" + memberName + "] in " + this.structName);
				}
				memberNames.add(memberName);
				
				if(member.isPrimaryKey()){
					hasPrimaryKey = true;
				}
				if(!member.getDefInstances().isEmpty()){
					hasDefInstance = true;
				}
			}
			
			//临时表或无主键，则不能有默认实例
			if((this.tempTable || !hasPrimaryKey) && hasDefInstance){
				throw new Exception("Must not contains default instance in " + this.structName);
			}
		}
	}
}
