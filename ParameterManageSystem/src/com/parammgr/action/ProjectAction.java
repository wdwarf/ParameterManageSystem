package com.parammgr.action;

import com.opensymphony.xwork2.ActionSupport;
import com.parammgr.db.entity.Project;
import com.parammgr.db.service.IProjectService;

public class ProjectAction extends ActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String structName;
	private String projectId;
	private IProjectService projectService;
	private Project project = null;
	
	public String getStructName() {
		return structName;
	}

	public void setStructName(String structName) {
		this.structName = structName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public IProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(IProjectService projectService) {
		this.projectService = projectService;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String execute() {
		if(null != this.projectId){
			this.project = this.projectService.getProjectById(projectId);
		}
		return ActionSupport.SUCCESS;
	}
}
