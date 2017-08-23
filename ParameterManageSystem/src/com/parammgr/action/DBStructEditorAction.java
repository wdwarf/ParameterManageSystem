package com.parammgr.action;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.parammgr.db.dao.Project;
import com.parammgr.db.service.IProjectService;
import com.parammgr.db.service.impl.ProjectService;

public class DBStructEditorAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String structName;
	private String editType;
	private int structId = 0;
	private IProjectService projectService;

	public String getStructName() {
		return structName;
	}

	public void setStructName(String structName) {
		this.structName = structName;
	}

	public int getStructId() {
		return structId;
	}

	public void setStructId(int structId) {
		this.structId = structId;
	}

	public String getEditType() {
		return editType;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public IProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}

	@Override
	public String execute() {
		List<Project>projects = this.getProjectService().getAllProjects();
		System.out.println(projects.size());
		return ActionSupport.SUCCESS;
	}
}
