package com.zhiyu.blog.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 文章类型实体类
 * 
 * @author xinyuan.wei
 * @date 2019年4月29日
 */

@Data
@Entity
@Table(name = "tb_type")
public class TypeBean {

	/**
	 * 文章类型编号
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/**
	 * 文章类型
	 */
	@Column(name = "article_type")
	private String articleType;
}
