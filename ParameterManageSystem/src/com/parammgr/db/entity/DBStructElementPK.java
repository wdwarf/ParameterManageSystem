package com.parammgr.db.entity;

import java.io.Serializable;

public class DBStructElementPK implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectId = "";
	private int structId = 0;
	private int elementId = 0;
	
	public DBStructElementPK() {
		
	}
	
	public DBStructElementPK(String projectId, int structId, int elementId) {
		this.projectId = projectId;
		this.structId = structId;
		this.elementId = elementId;
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
	public int getElementId() {
		return elementId;
	}
	public void setElementId(int elementId) {
		this.elementId = elementId;
	}
}
