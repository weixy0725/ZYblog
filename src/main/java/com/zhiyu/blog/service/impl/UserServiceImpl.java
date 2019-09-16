package com.zhiyu.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhiyu.blog.bean.UserBean;
import com.zhiyu.blog.dao.UserDao;
import com.zhiyu.blog.service.UserService;


@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;

	@Override
	public UserBean findByName(String userName) {
		UserBean userBean = userDao.findByUsername(userName);
		return userBean;
	}
	
	public UserBean findByToken(String token) {
		UserBean userBean = userDao.findByToken(token);
		return userBean;
	}

	@Override
	public void save(UserBean userBean) {
		userDao.save(userBean);
	}

}
