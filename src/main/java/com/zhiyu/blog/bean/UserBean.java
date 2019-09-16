package com.zhiyu.blog.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 用户实体
 * 
 * @author xinyuan.wei
 * @date 2019年8月27日
 */
@Data
@Entity
@Table(name = "tb_user")
public class UserBean {
	
	/**
	 * 用户编号
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/**
	 * 用户名
	 */
	@Column(name = "name")
	private String username;
	
	/**
	 * 密码
	 */
	@Column(name = "password")
	private String password;
	
	/**
	 * token
	 */
	@Column(name = "token")
	private String token;

}
