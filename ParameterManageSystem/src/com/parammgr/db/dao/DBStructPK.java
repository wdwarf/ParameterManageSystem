package com.parammgr.db.dao;

import java.io.Serializable;

public class DBStructPK implements Serializable{
	private String projectId = "";
	private int structId = 0;
	
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
