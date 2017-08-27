package com.parammgr.db.service;

import java.util.List;

import com.parammgr.db.entity.DBStruct;
import com.parammgr.db.entity.DBStructPK;

public interface IDBStructService {
	public List<DBStruct> getAllStructs(String projectId);
}
