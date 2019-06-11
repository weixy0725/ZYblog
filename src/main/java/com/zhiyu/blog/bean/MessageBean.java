package com.zhiyu.blog.bean;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 文章留言实体
 * 
 * @author xinyuan.wei
 * @date 2019年6月3日
 */
@Data
@Entity
@Table(name="tb_message")
public class MessageBean {
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 文章
	 */
	@Column(name = "article_id")
	private Long articleId;
	
	/**
	 * 留言内容
	 */
	@Column(name="message")
	private  String message;
	
	/**
	 * 留言时间
	 */
	@Column(name="datetime")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime datetime;
	
	/**
	 * 类型(0为回复，1为留言)
	 */
	@Column(name="type")
	private Integer type;
	
	/**
	 * 留言/回复者IP
	 */
	@Column(name="ip")
	private String ip;
	
	/**
	 * 0正常，1删除
	 */
	@Column(name="state")
	private Integer state;
}
