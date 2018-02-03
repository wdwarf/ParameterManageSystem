package com.parammgr.db.service.impl;

import java.io.InputStream;
import java.util.List;

import com.parammgr.db.dao.ICheckPointDao;
import com.parammgr.db.entity.CheckPoint;
import com.parammgr.db.service.ICheckPointService;

public class CheckPointService implements ICheckPointService {
	private ICheckPointDao checkPointDao;

	public ICheckPointDao getCheckPointDao() {
		return checkPointDao;
	}

	public void setCheckPointDao(ICheckPointDao checkPointDao) {
		this.checkPointDao = checkPointDao;
	}

	@Override
	public void addCheckPoint(CheckPoint checkPoint) {
		this.checkPointDao.addCheckPoint(checkPoint);
	}

	@Override
	public CheckPoint getCheckPoint(String checkpointId) {
		return this.checkPointDao.getCheckPoint(checkpointId);
	}

	@Override
	public List<CheckPoint> getAllCheckPoints(String projectId, String type) {
		return this.checkPointDao.getAllCheckPoints(projectId, type);
	}

	@Override
	public void deleteCheckPoint(String checkpointId) {
		this.checkPointDao.deleteCheckPoint(checkpointId);
	}

	@Override
	public void clearAllCheckPoint(String projectId) {
		this.checkPointDao.clearAllCheckPoint(projectId);
	}

	@Override
	public void clearAllCheckPoint(String projectId, String type) {
		this.checkPointDao.clearAllCheckPoint(projectId, type);
	}

	@Override
	public void updateCheckPointDescription(String checkpointId, String description) {
		this.checkPointDao.updateCheckPointDescription(checkpointId, description);
	}

}
