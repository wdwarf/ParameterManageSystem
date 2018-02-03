package com.parammgr.action;

import java.io.InputStream;

import com.parammgr.db.service.IDBStructService;
import com.parammgr.db.service.IWebStructService;
import com.parammgr.utils.AutoDeleteFileInputStream;

public class WebStructExportAction extends ActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectId = "";
	private IDBStructService dbStructService;
	private IWebStructService webStructService;

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

	public IWebStructService getWebStructService() {
		return webStructService;
	}

	public void setWebStructService(IWebStructService webStructService) {
		this.webStructService = webStructService;
	}

	/**
	 * 导出xml
	 * 
	 * @return
	 */
	public InputStream getXmlFile() {
		try {
			return new AutoDeleteFileInputStream(this.webStructService.exportToXmlFile(this.projectId));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 导出excel文件
	 * 
	 * @return
	 */
	public InputStream getExcelFile() {
		try {
			String file = this.webStructService.exportToExcelFile(this.projectId);
			if(null != file && !file.isEmpty()){
				return new AutoDeleteFileInputStream(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
