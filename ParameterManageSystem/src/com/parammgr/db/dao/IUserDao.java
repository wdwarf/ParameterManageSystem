package com.parammgr.db.dao;

import java.util.List;

import com.parammgr.db.entity.User;

public interface IUserDao {
	public List<User> getAllUsers();
	public User getUser(String userId);
	public User getUserByName(String userName);
	public void addUser(User user);
	public void updateUser(User user);
	public void deleteUser(String userId);
}
