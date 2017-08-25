package com.parammgr.action;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.parammgr.db.entity.Project;
import com.parammgr.db.service.IProjectService;

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
		Project project = this.getProjectService().getProjectById("142129c1-8798-11e7-8482-525400bbd1a8");
		System.out.println(project.getProjectName());
		project.setProjectName("prjName0");
		this.projectService.updateProject(project);
		return ActionSupport.SUCCESS;
	}
}
