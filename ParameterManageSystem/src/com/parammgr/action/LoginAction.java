package com.parammgr.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.parammgr.db.entity.User;
import com.parammgr.db.service.IProjectService;
import com.parammgr.db.service.IUserService;

public class LoginAction extends ActionBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IUserService userService;
	private User user = new User();

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public String execute() {
		return ActionSupport.SUCCESS;
	}
	
	/**
	 * 登录
	 * @return
	 */
	public String doLogin(){
		if(null == this.user || null == this.user.getUserName() || "".equals(this.user.getUserName())
				|| null == this.user.getPassword() || "".equals(this.user.getPassword())){
			this.addActionError("请输入用户名和密码");
			return ActionSupport.LOGIN;
		}
		
		User loginUser = this.userService.userLogin(user.getUserName(), user.getPassword());
		if(null != loginUser){
			System.out.println("Current Login user: " + loginUser.getUserName());
			ActionContext.getContext().getSession().put("user", loginUser);
			
			return ActionSupport.SUCCESS;
		}
		
		this.addActionError("用户名或密码错误");
		return ActionSupport.LOGIN;
	}

	/**
	 * 登出
	 * @return
	 */
	public String doLogout(){
		User user = (User)ActionContext.getContext().getSession().get("user");
		if(null != user){
			System.out.println("user " + user.getUserName() + " logout.");
		}
		ActionContext.getContext().getSession().remove("user");
		
		return ActionSupport.SUCCESS;
	}
}
