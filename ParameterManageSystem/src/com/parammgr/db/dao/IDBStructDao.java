package com.parammgr.db.dao;

import java.util.List;

import com.parammgr.db.entity.DBStruct;
import com.parammgr.db.entity.DBStructPK;

public interface IDBStructDao {
	List<DBStruct> getAllStructs(String projectId);
	DBStruct getStructByName(String structName);
	DBStruct getStructById(int structId);
}
