package com.trans;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

public class MyTransMgr extends HibernateTransactionManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void disconnectOnCompletion(Session session) {
		// TODO Auto-generated method stub
		System.out.println("disconnectOnCompletion");
		super.disconnectOnCompletion(session);
	}

	@Override
	protected void doBegin(Object arg0, TransactionDefinition arg1) {
		// TODO Auto-generated method stub
		System.out.println("doBegin");
		super.doBegin(arg0, arg1);
	}

	@Override
	protected void doCommit(DefaultTransactionStatus arg0) {
		// TODO Auto-generated method stub
		System.out.println("doCommit");
		super.doCommit(arg0);
	}

	@Override
	protected Object doGetTransaction() {
		// TODO Auto-generated method stub
		System.out.println("doGetTransaction");
		return super.doGetTransaction();
	}

	@Override
	protected void doResume(Object transaction, Object suspendedResources) {
		// TODO Auto-generated method stub
		System.out.println("doResume");
		super.doResume(transaction, suspendedResources);
	}

	@Override
	protected void doRollback(DefaultTransactionStatus arg0) {
		// TODO Auto-generated method stub
		System.out.println("doRollback");
		super.doRollback(arg0);
	}

	@Override
	protected Object doSuspend(Object transaction) {
		// TODO Auto-generated method stub
		System.out.println("doSuspend");
		return super.doSuspend(transaction);
	}

	@Override
	public DataSource getDataSource() {
		// TODO Auto-generated method stub
		System.out.println("getDataSource");
		return super.getDataSource();
	}

	@Override
	public void setDataSource(DataSource dataSource) {
		// TODO Auto-generated method stub
		System.out.println("setDataSource");
		super.setDataSource(dataSource);
	}

}
