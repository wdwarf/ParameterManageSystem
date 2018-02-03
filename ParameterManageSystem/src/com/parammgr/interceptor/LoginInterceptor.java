package com.parammgr.interceptor;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.parammgr.action.LoginAction;

public class LoginInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		return invocation.invoke();
		/*
		Object action = invocation.getAction();
		
		if(action instanceof LoginAction){
			return invocation.invoke();
		}else{
			Map<String, Object> session = ActionContext.getContext().getSession();
			Object user = session.get("user");
			if(null != user){
				return invocation.invoke();
			}
		}
		
		System.out.println("Not login.");
		return ActionSupport.LOGIN;
		*/
	}

}
