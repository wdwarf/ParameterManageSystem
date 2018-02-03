package com.parammgr.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.opensymphony.xwork2.ActionSupport;
import com.parammgr.db.entity.CheckPoint;
import com.parammgr.db.service.ICheckPointService;
import com.parammgr.db.service.IDBStructService;
import com.parammgr.db.service.IWebStructService;
import com.parammgr.utils.FileUtil;

public class WebStructImportAction extends ActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String projectId = "";
	private File file;
	private String fileFileName;
	private String fileContentType;
	private IDBStructService dbStructService;
	private IWebStructService webStructService;
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

	public IWebStructService getWebStructService() {
		return webStructService;
	}

	public void setWebStructService(IWebStructService webStructService) {
		this.webStructService = webStructService;
	}

	private String getSuffix(String fileName) {
		int pos = fileName.lastIndexOf(".");
		if (pos < 0) {
			return "";
		}
		return fileName.substring(pos + 1);
	}

	public String execute0() {
		if (null == file) {
			this.addActionError("Not a zip file.");
			return ActionSupport.INPUT;
		}

		String suffix = getSuffix(this.fileFileName).toLowerCase();

		if (!"xml".equals(suffix) && !("xlsx".equals(suffix) || "xls".equals(suffix))) {
			this.addActionError("Invalid file.");
			return ActionSupport.INPUT;
		}

		try {
			switch(suffix){
			case "xml":{
				this.webStructService.importStructsOfXml(this.projectId, file);
				break;
			}
			case "xls":
			case "xlsx":{
				this.webStructService.importStructsOfExcel(this.projectId, file);
				break;
			}
			}
		} catch (Exception e) {
			this.addActionError(e.getMessage());
			e.printStackTrace();
			return ActionSupport.INPUT;
		}
		
		return ActionSupport.SUCCESS;
	}

	@Override
	public String execute() {
		if (null == file) {
			this.addActionError("Invalid data model file.");
			return ActionSupport.INPUT;
		}

		String subfix = getSuffix(this.fileFileName).toLowerCase();

		String ulTempDir = FileUtil.getTempDir() + File.separator + UUID.randomUUID().toString();
		File f_ulTempDir = new File(ulTempDir);
		if (!f_ulTempDir.exists()) {
			f_ulTempDir.mkdirs();
		}
		CheckPoint cp = null;
		try {
			switch (subfix) {
			case "zip": {
				FileUtil.unzipFile(file, ulTempDir);
				List<String> files = new ArrayList<String>();
				FileUtil.listFiles(files, ulTempDir, "xlsx");
				//FileUtil.listFiles(files, ulTempDir, "xls");
				FileUtil.listFiles(files, ulTempDir, "xml");
				
				cp = this.webStructService.createCheckPoint(projectId, "导入参数之前创建的还原点");
				this.webStructService.importStructs(projectId, files);
				cp = this.webStructService.createCheckPoint(projectId, "导入参数");
				break;
			}
			case "xml": {
				cp = this.webStructService.createCheckPoint(projectId, "导入参数之前创建的还原点");
				this.webStructService.importStructsOfXml(projectId, file);
				cp = this.webStructService.createCheckPoint(projectId, "导入参数");
				break;
			}
			case "xls":
			case "xlsx": {
				cp = this.webStructService.createCheckPoint(projectId, "导入参数之前创建的还原点");
				this.webStructService.importStructsOfExcel(projectId, file);
				cp = this.webStructService.createCheckPoint(projectId, "导入参数");
				break;
			}
			default:{
				this.addActionError("Invalid data model file[" + this.fileFileName + "]");
				return ActionSupport.INPUT;
			}
			}

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
