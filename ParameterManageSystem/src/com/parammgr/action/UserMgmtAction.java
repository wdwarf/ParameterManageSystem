package com.parammgr.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.parammgr.db.entity.User;
import com.parammgr.db.service.IUserService;

public class UserMgmtAction extends ActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IUserService userService;
	
	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public String execute() {
		User user = (User)ActionContext.getContext().getSession().get("user");
		if(null != user){
			if("admin".equals(user.getUserName())){
				return ActionSupport.SUCCESS;
			}
		}
		
		this.addActionError("无权限，请用管理员身份登录");
		return ActionSupport.ERROR;
	}
}
