package com.parammgr.action;

import com.opensymphony.xwork2.ActionSupport;
import com.parammgr.utils.FileUtil;

public class ActionBase extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String systemName = "参数管理平台";
	
	public String getTempDir(){
		return FileUtil.getTempDir();
	}
	
	@Override
	public String execute() {
		
		return ActionSupport.SUCCESS;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
}
