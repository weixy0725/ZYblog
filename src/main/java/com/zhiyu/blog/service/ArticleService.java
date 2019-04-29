package com.zhiyu.blog.service;

/**
 * 文章业务接口类
 * 
 * @author xinyuan.wei
 * @date 2019年4月29日
 */
public interface ArticleService {
	/**
	 * 保存文章
	 * 
	 * @param articleName
	 * @param classificationId
	 * @param article
	 */
     void save(String articleName,Integer classificationId,String article);
}
