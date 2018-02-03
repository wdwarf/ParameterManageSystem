package com.parammgr.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBStructMember implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dbstructmemberId = null;
	private String dbstructId = "";
	private int memberId = 0;
	private String memberName = "";
	private String memberType = "";
	private int memberSize = 0;
	private boolean primaryKey = false;
	private String defaultValue = "";
	private String valueRegex = "";
	private String refStruct = "";
	private String refMember = "";
	private boolean unique = false;
	private String memo = "";
	private Date createDate = new Date();
	private List<DBStructDefInstance> defInstances = new ArrayList<DBStructDefInstance>();
	
	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getDbstructmemberId() {
		return dbstructmemberId;
	}
	public void setDbstructmemberId(String dbstructmemberId) {
		this.dbstructmemberId = dbstructmemberId;

		if(null != this.dbstructmemberId){
			this.dbstructmemberId = this.dbstructmemberId.trim();
		}
		
		for(DBStructDefInstance instance : defInstances){
			instance.setDbstructmemberId(this.dbstructmemberId);
		}
	}
	public String getDbstructId() {
		return dbstructId;
	}
	public void setDbstructId(String dbstructId) {
		this.dbstructId = dbstructId;

		if(null != this.dbstructId){
			this.dbstructId = this.dbstructId.trim();
		}
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;

		if(null != this.memberName){
			this.memberName = this.memberName.trim();
		}
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;

		if(null != this.memberType){
			this.memberType = this.memberType.trim();
		}
	}
	public int getMemberSize() {
		return memberSize;
	}
	public void setMemberSize(int memberSize) {
		this.memberSize = memberSize;
	}
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;

		if(null != this.defaultValue){
			this.defaultValue = this.defaultValue.trim();
		}
	}
	public String getValueRegex() {
		return valueRegex;
	}
	public void setValueRegex(String valueRegex) {
		this.valueRegex = valueRegex;

		if(null != this.valueRegex){
			this.valueRegex = this.valueRegex.trim();
		}
	}
	
	public String getRefStruct() {
		return refStruct;
	}
	public void setRefStruct(String refStruct) {
		this.refStruct = refStruct;

		if(null != this.refStruct){
			this.refStruct = this.refStruct.trim();
		}
	}
	public String getRefMember() {
		return refMember;
	}
	public void setRefMember(String refMember) {
		this.refMember = refMember;

		if(null != this.refMember){
			this.refMember = this.refMember.trim();
		}
	}
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public List<DBStructDefInstance> getDefInstances() {
		return defInstances;
	}
	public void setDefInstances(List<DBStructDefInstance> defInstances) {
		this.defInstances = defInstances;

		for(DBStructDefInstance instance : this.defInstances){
			instance.setDbstructmemberId(this.dbstructmemberId);
		}
	}
}
