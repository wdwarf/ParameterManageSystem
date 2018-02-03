package com.parammgr.db.service;

import java.util.List;

import com.parammgr.db.entity.User;

public interface IUserService {
	public List<User> getAllUsers();
	public User getUser(String userId);
	public User getUserByName(String userName);
	public void addUser(User user) throws Exception;
	public void updateUser(User user) throws Exception;
	public void deleteUser(String userId);
	
	public User userLogin(String userName, String password);
	public boolean changePassword(String userId, String password);
	public User getLoginUser();
}
