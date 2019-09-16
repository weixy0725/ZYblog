package com.zhiyu.blog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zhiyu.blog.bean.UserBean;

public interface UserDao extends JpaRepository<UserBean, Integer>, JpaSpecificationExecutor<UserBean> {
	
	UserBean findByUsername(String name);
	
	UserBean findByToken(String token);

}
