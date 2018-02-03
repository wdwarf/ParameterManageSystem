package com.parammgr.db.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;

import com.parammgr.db.dao.IUserDao;
import com.parammgr.db.entity.CheckPoint;
import com.parammgr.db.entity.DBStruct;
import com.parammgr.db.entity.User;

public class UserDao implements IUserDao {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from User order by userName asc", User.class).getResultList();
		return datas;
	}

	@Override
	public User getUser(String userId) {
		List<User> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from User where userId=:userId", User.class).setParameter("userId", userId)
				.getResultList();
		if (!datas.isEmpty()) {
			return datas.get(0);
		}

		return null;
	}

	@Override
	public User getUserByName(String userName) {
		List<User> datas = this.sessionFactory.getCurrentSession()
				.createQuery("from User where userName=:userName", User.class).setParameter("userName", userName)
				.getResultList();
		if (!datas.isEmpty()) {
			return datas.get(0);
		}

		return null;
	}

	@Override
	public void addUser(User user) {
		user.setUserId(null);
		this.sessionFactory.getCurrentSession().save(user);
		this.sessionFactory.getCurrentSession().flush();
	}

	@Override
	public void updateUser(User user) {
		this.sessionFactory.getCurrentSession().update(user);
		this.sessionFactory.getCurrentSession().flush();
	}

	@Override
	public void deleteUser(String userId) {
		User user = this.getUser(userId);
		if (null != user) {
			this.sessionFactory.getCurrentSession().delete(user);
			this.sessionFactory.getCurrentSession().flush();
		}
	}

}
