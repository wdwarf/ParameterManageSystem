package com.parammgr.db.service;

import java.io.File;
import java.util.List;

import com.parammgr.db.entity.CheckPoint;
import com.parammgr.db.entity.WebStruct;
import com.parammgr.db.entity.WebStructMember;

public interface IWebStructService {
	public List<WebStruct> getAllStructs(String projectId);
	public List<WebStruct> getAllStructIdsAndNames(String projectId);
	public List<String> getAllStructNames(String projectId);
	public WebStruct getStruct(String webstructId);
	public WebStruct getStruct(String projectId, String structName);
	public WebStruct getStruct(String projectId, int structId);
	public void addStruct(WebStruct webstruct) throws Exception;
	public void updateStruct(WebStruct webstruct) throws Exception;
	public void deleteStruct(String webstructId) throws Exception;
	public void deleteStruct(String projectId, String structName) throws Exception;
	public void clearStructs(String projectId);

	//member
	public WebStructMember getStructMember(String webstructmemberId);
	public void addStructMember(WebStructMember member) throws Exception;
	public void deleteStructMember(WebStructMember member);
	public void clearStructMembers(String webstructId);
	
	public void importStructsOfXml(String projectId, File file) throws Exception;
	public void importStructsOfExcel(String projectId, File file) throws Exception;
	public void importStructs(String projectId, List<String> files) throws Exception;
	public String exportToXmlFile(String projectId) throws Exception;
	public String exportToExcelFile(String projectId) throws Exception;

	public CheckPoint createCheckPoint(String projectId, String description) throws Exception;
	public boolean checkPointRecovery(String checkpointId);
}
