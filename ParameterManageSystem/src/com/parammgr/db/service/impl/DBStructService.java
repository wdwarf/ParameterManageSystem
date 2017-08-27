package com.parammgr.db.service.impl;

import java.util.List;

import com.parammgr.db.dao.IDBStructDao;
import com.parammgr.db.entity.DBStruct;
import com.parammgr.db.entity.DBStructPK;
import com.parammgr.db.service.IDBStructService;

public class DBStructService implements IDBStructService {
	
	private IDBStructDao dbStructDao;

	@Override
	public List<DBStruct> getAllStructs(String projectId) {
		return this.dbStructDao.getAllStructs(projectId);
	}

	public IDBStructDao getDbStructDao() {
		return dbStructDao;
	}

	public void setDbStructDao(IDBStructDao dbStructDao) {
		this.dbStructDao = dbStructDao;
	}

}
