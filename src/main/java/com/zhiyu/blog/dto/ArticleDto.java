package com.zhiyu.blog.dto;

import lombok.Data;

/**
 * 需要生成索引的内容
 * 
 * @author xinyuan.wei
 * @date 2019年8月20日
 */
@Data
public class ArticleDto {
	/**
	 * 文章编号
	 */
	private Long articleId;
	
	/**
	 * 文章名称
	 */
	private String articleName;
	
	/**
	 * 文章梗概
	 */
	private String articleSummarize;
	
	/**
	 * 文章内容
	 */
	private String articleContent;
	
	/**
	 * 文章分类
	 */
	private String type;
	
	/**
	 * 文章具体分类
	 */
	private String classificationName;
	
	/**
	 * 文章发布时间
	 */
	private String time;
	
	/**
	 * 类型编号
	 */
	private Integer typeId;
	
	/**
	 * 
	 */
	private Integer isOriginal;

}
