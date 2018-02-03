package com.parammgr.action;

import java.io.File;
import com.opensymphony.xwork2.ActionSupport;
import com.parammgr.db.entity.CheckPoint;
import com.parammgr.db.service.ICheckPointService;
import com.parammgr.db.service.IDBStructService;

public class DBStructImportAction extends ActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String projectId = "";
	private File zipFile;
	private String zipFileFileName;
	private String zipFileContentType;
	private IDBStructService dbStructService;
	private ICheckPointService checkPointService;

	public ICheckPointService getCheckPointService() {
		return checkPointService;
	}

	public void setCheckPointService(ICheckPointService checkPointService) {
		this.checkPointService = checkPointService;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public IDBStructService getDbStructService() {
		return dbStructService;
	}

	public void setDbStructService(IDBStructService dbStructService) {
		this.dbStructService = dbStructService;
	}

	public File getZipFile() {
		return zipFile;
	}

	public void setZipFile(File zipFile) {
		this.zipFile = zipFile;
	}

	public String getZipFileFileName() {
		return zipFileFileName;
	}

	public void setZipFileFileName(String zipFileFileName) {
		this.zipFileFileName = zipFileFileName;
	}

	public String getZipFileContentType() {
		return zipFileContentType;
	}

	public void setZipFileContentType(String zipFileContentType) {
		this.zipFileContentType = zipFileContentType;
	}

	private String getSuffix(String fileName) {
		int pos = fileName.lastIndexOf(".");
		if (pos < 0) {
			return "";
		}
		return fileName.substring(pos + 1);
	}

	@Override
	public String execute() {
		if (null == zipFile) {
			this.addActionError("Not a zip file.");
			return ActionSupport.INPUT;
		}

		String subfix = getSuffix(this.zipFileFileName).toLowerCase();

		if (!"zip".equals(subfix)) {
			this.addActionError("Not a zip file.");
			return ActionSupport.INPUT;
		}

		CheckPoint cp = null;
		try {
			cp = this.dbStructService.createCheckPoint(projectId, "导入参数之前创建的还原点");
			this.dbStructService.importStructs(this.projectId, zipFile);
			cp = this.dbStructService.createCheckPoint(projectId, "导入参数");
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			e.printStackTrace();
			if(null != cp){
				try{
					this.checkPointService.deleteCheckPoint(cp.getCheckpointId());
				}catch(Exception ex){}
			}
			return ActionSupport.INPUT;
		}
		
		return ActionSupport.SUCCESS;
	}
}
