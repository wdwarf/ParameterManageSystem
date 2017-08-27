package com.parammgr.db.entity;

import java.io.Serializable;

public class DBStructPK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectId = "";
	private int structId = 0;
	
	public DBStructPK() {
		
	}
	
	public DBStructPK(String projectId, int structId){
		this.projectId = projectId;
		this.structId = structId;
	}
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public int getStructId() {
		return structId;
	}
	public void setStructId(int structId) {
		this.structId = structId;
	}
}
