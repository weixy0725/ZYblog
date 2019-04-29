package com.zhiyu.blog.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhiyu.blog.bean.ArticleBean;
import com.zhiyu.blog.bean.ArticleClassificationBean;
import com.zhiyu.blog.bean.ArticleTypeBean;
import com.zhiyu.blog.dao.ArticleClassificationDao;
import com.zhiyu.blog.dao.ArticleDao;
import com.zhiyu.blog.dao.ArticleTypeDao;
import com.zhiyu.blog.service.ArticleService;

/**
 * 文章业务实现类
 * 
 * @author xinyuan.wei
 * @date 2019年4月29日
 */
@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDao articleDao;
	
	@Autowired
	private ArticleTypeDao articleTypeDao;
	
	@Autowired
	private ArticleClassificationDao articleClassificationDao;

	@Override
	public void save(String articleName, Integer typeId,Integer classificationId, String article) {
		ArticleBean articleBean = new ArticleBean();
		articleBean.setArticleName(articleName);
		articleBean.setTypeId(typeId);
		articleBean.setClassificationId(classificationId);
		articleBean.setArticleContent(article);
		articleBean.setTimestamp(new Timestamp(System.currentTimeMillis()));
		articleDao.save(articleBean);
	}

	@Override
	public List<ArticleTypeBean> findAllArticleType() {
		return articleTypeDao.findAll();
	}

	@Override
	public List<ArticleClassificationBean> findAllArticleClassification(Integer typeId) {
		return articleClassificationDao.findByTypeId(typeId);
	}

}
