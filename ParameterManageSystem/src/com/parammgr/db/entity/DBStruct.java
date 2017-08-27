package com.parammgr.db.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBStruct {
	private DBStructPK primaryKey;
	private String structName = "";
	private boolean tempTable = false;
	private Date createDate = new Date();
	private Set<DBStructElement> elements = new HashSet<DBStructElement>();
	
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
	public Set<DBStructElement> getElements() {
		return elements;
	}
	public void setElements(Set<DBStructElement> elements) {
		this.elements = elements;
	}
}
