package com.parammgr.db.service;

import java.util.List;

import com.parammgr.db.entity.CheckPoint;

public interface ICheckPointService {
	public void addCheckPoint(CheckPoint checkPoint);
	public CheckPoint getCheckPoint(String checkpointId);
	public List<CheckPoint> getAllCheckPoints(String projectId, String type);
	public void deleteCheckPoint(String checkpointId);
	public void clearAllCheckPoint(String projectId);
	public void clearAllCheckPoint(String projectId, String type);
	public void updateCheckPointDescription(String checkpointId, String description);
}
