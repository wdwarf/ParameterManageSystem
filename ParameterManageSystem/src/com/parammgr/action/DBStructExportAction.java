package com.parammgr.action;

import java.io.InputStream;

import com.opensymphony.xwork2.ActionSupport;
import com.parammgr.db.service.IDBStructService;
import com.parammgr.utils.AutoDeleteFileInputStream;

public class DBStructExportAction extends ActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectId = "";
	private IDBStructService dbStructService;
	private AutoDeleteFileInputStream expFile = null;

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

	/**
	 * 导出oam.db
	 * 
	 * @return
	 */
	public InputStream getDbFile() {
		return expFile;
	}

	/**
	 * 导出excel文件
	 * 
	 * @return
	 */
	public InputStream getExcelFile() {
		return expFile;
	}

	public String exportDbFile(){
		try {
			String file = this.dbStructService.exportToDBFile(this.projectId);
			if(null != file && !file.isEmpty()){
				expFile = new AutoDeleteFileInputStream(file);
			}
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			e.printStackTrace();
			return ActionSupport.ERROR;
		}
		return ActionSupport.SUCCESS;
	}

	public String exportExcelFile(){
		try {
			String file = this.dbStructService.exportToExcelFile(this.projectId);
			if(null != file && !file.isEmpty()){
				expFile = new AutoDeleteFileInputStream(file);
			}
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			e.printStackTrace();
			return ActionSupport.ERROR;
		}
		return ActionSupport.SUCCESS;
	}
}
