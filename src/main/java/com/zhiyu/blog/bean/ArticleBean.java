package com.zhiyu.blog.bean;



import java.time.LocalDateTime;

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
	 * 文章概括
	 */
	@Column(name="article_summarize")
	private String articleSummarize;
	
	/**
	 * 文章内容
	 */
	@Column(name = "article_content")
	private String articleContent;
	
	/**
	 * 类型编号
	 */
	@Column(name = "type_id")
	private Integer typeId;
	
	/**
	 * 分类编号
	 */
	@Column(name = "classification_id")
	private Integer classificationId;
	
	/**
	 * 是否原创（0为转载，1为原创）
	 */
	@Column(name = "is_original")
	private Integer isOriginal;
	/**
	 * 时间戳
	 */
	@Column(name = "datetime")
	private LocalDateTime datetime;
	
	/**
	 * 浏览次数
	 */
	@Column(name="browse_times")
	private  Integer browseTimes;
	
	/**
	 * 留言数量
	 */
	@Column(name="message_count")
	private Integer messageCount;
	
	/**
	 * 封面
	 */
	@Column(name="cover")
	private String cover;
}
