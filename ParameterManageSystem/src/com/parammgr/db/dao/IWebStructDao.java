package com.parammgr.db.dao;

import java.util.List;

import com.parammgr.db.entity.WebStruct;
import com.parammgr.db.entity.WebStructMember;

public interface IWebStructDao {
	//struct
	public List<WebStruct> getAllStructs(String projectId);
	public List<WebStruct> getAllStructIdsAndNames(String projectId);
	public List<String> getAllStructNames(String projectId);
	public WebStruct getStruct(String webstructId);
	public WebStruct getStruct(String projectId, String structName);
	public WebStruct getStruct(String projectId, int structId);
	public void addStruct(WebStruct webstruct) throws Exception;
	public void updateStruct(WebStruct webstruct) throws Exception;
	public void deleteStruct(String webstructId);
	public void clearStructs(String projectId);
	
	//member
	public WebStructMember getStructMember(String webstructmemberId);
	public void addStructMember(WebStructMember member);
	public void deleteStructMember(WebStructMember member);
	public void clearStructMembers(String webstructId);
}
