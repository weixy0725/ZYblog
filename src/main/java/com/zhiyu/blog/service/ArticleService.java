package com.zhiyu.blog.service;

import java.util.List;

import com.querydsl.core.Tuple;
import com.zhiyu.blog.bean.ArticleClassificationBean;
import com.zhiyu.blog.bean.ArticleTypeBean;

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
     void save(String articleName,String articleSummarize,Integer typeId,Integer classificationId,Integer isOriginal,String article);
     
     /**
      * 获取所有文章类型
      * 
      * @return
      */
     List<ArticleTypeBean> findAllArticleType();
     
     /**
      * 获取所有具体分类
      * 
      * @return
      */
     List<ArticleClassificationBean> findAllArticleClassification(Integer typeId);
     
     /**
      * 分页获取文章内容
      * 
      * @param typeId
      * @param classificationId
      * @param pageIndex
      * @param pageSize
      * @return
      */
     List<Tuple> findArticlesPaging(Integer typeId,Integer classificationId,Integer pageIndex,Integer pageSize);
     
     /**
      * 获取文章总数
      * 
      * @param typeId
      * @param classificationId
      * @return
      */
     Long findArticlesCount(Integer typeId,Integer classificationId);
}
