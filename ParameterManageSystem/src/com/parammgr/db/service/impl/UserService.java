package com.parammgr.db.service.impl;

import java.util.List;

import com.opensymphony.xwork2.ActionContext;
import com.parammgr.db.dao.IUserDao;
import com.parammgr.db.entity.User;
import com.parammgr.db.service.IUserService;

public class UserService implements IUserService {
	private IUserDao userDao;
	
	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public List<User> getAllUsers() {
		return this.userDao.getAllUsers();
	}

	@Override
	public User getUser(String userId) {
		return this.userDao.getUser(userId);
	}

	@Override
	public User getUserByName(String userName) {
		return this.userDao.getUserByName(userName);
	}

	@Override
	public void addUser(User user) throws Exception {
		User u = this.getUserByName(user.getUserName().trim());
		if(null != u){
			throw new Exception("User [" + user.getUserName() + "] has exists");
		}
		
		User newUser = new User();
		newUser.setUserName(user.getUserName().trim());
		newUser.setNickName(user.getNickName().trim());
		newUser.setPassword(user.getPassword());
		this.userDao.addUser(newUser);
	}

	@Override
	public void updateUser(User user) throws Exception {
		this.userDao.updateUser(user);
	}

	@Override
	public void deleteUser(String userId) {
		this.userDao.deleteUser(userId);
	}

	@Override
	public User userLogin(String userName, String password){
		User user = this.userDao.getUserByName(userName);
		if(null != user && user.getPassword().equals(password)){
			return user;
		}
		return null;
	}

	@Override
	public boolean changePassword(String userId, String password) {
		User user = this.userDao.getUser(userId);
		if(null != user){
			user.setPassword(password);
			this.userDao.updateUser(user);
			return true;
		}
		return false;
	}
	
	@Override
	public User getLoginUser(){
		return (User)ActionContext.getContext().getSession().get("user");
	}
}
