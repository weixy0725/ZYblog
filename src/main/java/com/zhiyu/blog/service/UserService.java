package com.zhiyu.blog.service;

import com.zhiyu.blog.bean.UserBean;

public interface UserService {
	
	UserBean findByName(String userName);
	
	UserBean findByToken(String token);
	
	void save(UserBean userBean);

}
