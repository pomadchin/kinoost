package com.cyber.kinoost.db.repositories;

import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;

import com.cyber.kinoost.db.DatabaseHelper;
import com.cyber.kinoost.db.models.User;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class UserRepository {
	DatabaseHelper dbHelper;
	
	private RuntimeExceptionDao<User, Integer> userDao = null;
	
	public UserRepository(Context context) {
		dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		
		userDao = dbHelper.getUserRuntimeExceptionDao();
	}
	
	public UserRepository(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
		
		userDao = dbHelper.getUserRuntimeExceptionDao();
	}
	
	public User getUser() {
		List<User> users = userDao.queryForAll();
		if(users.size() > 0) return users.get(0);
		else return new User();
	}
	
	public List<User> getUserList() {
		return userDao.queryForAll();
	}
	
	public void createUser(User user) {
		if(user == null) return;
		
		userDao.create(user);
	}
	
	public void createUserList(final List<User> users) {
		if(users == null) return;
		
        userDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (User user : users) {
	                userDao.create(user);
	            }
	            return null;
	        }
	    });
	}
	
	public void editUser(User user) {
		if(user == null) return;
		
		userDao.createOrUpdate(user);
	}
	
	public void editUserList(final List<User> users) {
		if(users == null) return;
		
        userDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (User user : users) {
	                userDao.createOrUpdate(user);
	            }
	            return null;
	        }
	    });
	}
	
	public void deleteUser(User user) {
		if(user == null) return;
		
		userDao.delete(user);
	}
	
	public void deleteUserList(List<User> users) {
		if(users == null) return;
		
		userDao.delete(users);
	}
}
