package com.parammgr.action;

import java.io.InputStream;

import com.opensymphony.xwork2.ActionSupport;
import com.parammgr.db.service.IDBStructService;
import com.parammgr.db.service.ISnmpStructService;
import com.parammgr.utils.AutoDeleteFileInputStream;

public class SnmpStructExportAction extends ActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectId = "";
	private IDBStructService dbStructService;
	private ISnmpStructService snmpStructService;
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

	public ISnmpStructService getSnmpStructService() {
		return snmpStructService;
	}

	public void setSnmpStructService(ISnmpStructService snmpStructService) {
		this.snmpStructService = snmpStructService;
	}

	public InputStream getFile() {
		return expFile;
	}

	@Override
	public String execute(){
		try {
			expFile = new AutoDeleteFileInputStream(this.snmpStructService.exportStructs(this.projectId));
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			e.printStackTrace();
			return ActionSupport.ERROR;
		}

		return ActionSupport.SUCCESS;
	}
}
