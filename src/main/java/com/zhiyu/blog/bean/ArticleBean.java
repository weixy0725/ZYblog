package com.zhiyu.blog.bean;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * 文章实体类
 * 
 * @author xinyuan.wei
 * @date 2019年4月28日
 */
@Data
@Entity
@Table(name = "tb_article")
public class ArticleBean {

	/**
	 * 文章编号
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "article_id")
	private Long articleId;
	
	/**
	 * 文章标题
	 */
	@Column(name = "article_name")
	private String articleName;
	
	/**
	 * 文章内容
	 */
	@Column(name = "article_content")
	private String articleContent;
	
	/**
	 * 分类编号
	 */
	@Column(name = "classification_id")
	private Integer classificationId;
	
	/**
	 * 时间戳
	 */
	@Column(name = "timestamp")
	private Timestamp timestamp;
}
