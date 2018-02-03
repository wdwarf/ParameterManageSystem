package com.parammgr.db.entity;

import java.util.Date;

public class CheckPoint {
	private String checkpointId;
	private String projectId;
	private byte[] checkPointData = null;
	private String description;
	private String type;
	private Date createDate = new Date();
	
	public String getCheckpointId() {
		return checkpointId;
	}
	public void setCheckpointId(String checkpointId) {
		this.checkpointId = checkpointId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	public byte[] getCheckPointData() {
		return checkPointData;
	}
	public void setCheckPointData(byte[] checkPointData) {
		this.checkPointData = checkPointData;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
