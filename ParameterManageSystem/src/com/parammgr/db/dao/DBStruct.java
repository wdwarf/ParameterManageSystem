package com.parammgr.db.dao;

import java.util.Date;

public class DBStruct {
	private DBStructPK primaryKey = new DBStructPK();
	private String structName = "";
	private boolean tempTable = false;
	private Date createDate = new Date();
	
	public String getProjectId() {
		return primaryKey.getProjectId();
	}
	public void setProjectId(String projectId) {
		primaryKey.setProjectId(projectId);
	}
	public int getStructId() {
		return primaryKey.getStructId();
	}
	public void setStructId(int structId) {
		primaryKey.setStructId(structId);
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
	public DBStructPK getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(DBStructPK primaryKey) {
		this.primaryKey = primaryKey;
	}
}
