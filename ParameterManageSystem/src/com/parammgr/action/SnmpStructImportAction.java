package com.parammgr.action;

import java.io.File;
import java.util.UUID;

import com.opensymphony.xwork2.ActionSupport;
import com.parammgr.db.entity.CheckPoint;
import com.parammgr.db.service.ICheckPointService;
import com.parammgr.db.service.IDBStructService;
import com.parammgr.db.service.ISnmpStructService;
import com.parammgr.utils.FileUtil;

public class SnmpStructImportAction extends ActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String projectId = "";
	private File file;
	private String fileFileName;
	private String fileContentType;
	private IDBStructService dbStructService;
	private ISnmpStructService snmpStructService;
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

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public ISnmpStructService getSnmpStructService() {
		return snmpStructService;
	}

	public void setSnmpStructService(ISnmpStructService snmpStructService) {
		this.snmpStructService = snmpStructService;
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
		if (null == file) {
			this.addActionError("Invalid data model file.");
			return ActionSupport.INPUT;
		}

		String subfix = getSuffix(this.fileFileName).toLowerCase();
		if(!"xml".equals(subfix)){
			this.addActionError("Invalid data model file[" + this.fileFileName + "]");
			return ActionSupport.INPUT;
		}

		String ulTempDir = FileUtil.getTempDir() + File.separator + UUID.randomUUID().toString();
		File f_ulTempDir = new File(ulTempDir);
		if (!f_ulTempDir.exists()) {
			f_ulTempDir.mkdirs();
		}

		CheckPoint cp = null;
		try {
			cp = this.snmpStructService.createCheckPoint(projectId, "导入参数之前创建的还原点");
			this.snmpStructService.importStructs(projectId, file);
			cp = this.snmpStructService.createCheckPoint(projectId, "导入参数");
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			e.printStackTrace();
			if(null != cp){
				try{
					this.checkPointService.deleteCheckPoint(cp.getCheckpointId());
				}catch(Exception ex){}
			}
			return ActionSupport.INPUT;
		} finally {
			FileUtil.rm(f_ulTempDir);
		}

		return ActionSupport.SUCCESS;
	}
}
