package com.parammgr.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WebStruct implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String webstructId = null;
	private String projectId = "";
	private int structId = 0;
	private String structName = "";
	private String structCnName = "";
	private String classifyName = "";
	private Date createDate = new Date();
	private Set<WebStructMember> members = new HashSet<WebStructMember>();
	
	public int getStructId() {
		return structId;
	}
	public void setStructId(int structId) {
		this.structId = structId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getStructName() {
		return structName;
	}
	public void setStructName(String structName) {
		this.structName = structName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getClassifyName() {
		return classifyName;
	}
	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}
	public String getWebstructId() {
		return webstructId;
	}
	public void setWebstructId(String webstructId) {
		this.webstructId = webstructId;
		
		if(null != this.webstructId){
			this.webstructId = this.webstructId.trim();
		}
		
		for(WebStructMember m : this.members){
			m.setWebstructId(this.webstructId);
		}
	}
	public String getStructCnName() {
		return structCnName;
	}
	public void setStructCnName(String structCnName) {
		this.structCnName = structCnName;
	}
	public Set<WebStructMember> getMembers() {
		return members;
	}
	public void setMembers(Set<WebStructMember> members) {
		this.members = members;
		for(WebStructMember elmt : this.members){
			elmt.setWebstructId(webstructId);
		}
	}
	
	public void addMember(WebStructMember member) throws Exception{
		/*for(WebStructMember m : this.members){
			if(m.getMemberName().equals(member.getMemberName()) || m.getMemberId() == member.getMemberId()){
				throw new Exception("Duplicated member " + this.structName + "." + member.getMemberName() + "[" + member.getMemberId() + "]");
			}
		}*/
		member.setWebstructId(webstructId);
		this.members.add(member);
	}

	public WebStructMember getMember(int memberId){
		for(WebStructMember member : this.members){
			if(member.getMemberId() == memberId){
				return member;
			}
		}
		
		return null;
	}
	
	public WebStructMember getMember(String memberName){
		for(WebStructMember member : this.members){
			if(member.getMemberName().equals(memberName)){
				return member;
			}
		}
		
		return null;
	}
	
	public boolean isSingleInstance(){
		for(WebStructMember member : this.members){
			if(member.isPrimaryKey()){
				return false;
			}
		}
		
		return true;
	}
	
	public List<WebStructMember> getPrimaryKeyMembers(){
		List<WebStructMember> members = new ArrayList<WebStructMember>();
		for(WebStructMember member : this.members){
			if(member.isPrimaryKey()){
				members.add(member);
			}
		}
		return members;
	}
	
	public void check() throws Exception{
		if(null == this.structName || this.structName.isEmpty()){
			throw new Exception("Empty struct name");
		}
		if(this.structId <= 0 || this.structId > 99999){
			throw new Exception("Invalid struct ID");
		}
		
		if(null != this.members){
			Set<Integer> memberIds = new HashSet<Integer>();
			Set<String> memberNames = new HashSet<String>();
			
			for(WebStructMember member : this.members){
				String memberName = member.getMemberName();
				if(null == memberName || memberName.isEmpty()){
					throw new Exception("Empty member name in " + this.structName);
				}
				
				int memberId = member.getMemberId();
				if(memberId <= 0 || memberId > 999){
					throw new Exception("Invalid member ID of " + this.structName + "[" + memberName + "]");
				}
				
				if(memberIds.contains(memberId)){
					throw new Exception("Duplicate member ID[" + memberId + "] in " + this.structName);
				}
				memberIds.add(memberId);

				if(memberNames.contains(memberName)){
					throw new Exception("Duplicate member Name[" + memberName + "] in " + this.structName);
				}
				memberNames.add(memberName);
			}
		}
	}
}
