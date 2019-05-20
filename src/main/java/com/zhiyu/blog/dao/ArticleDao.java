package com.zhiyu.blog.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

import com.infobip.spring.data.ExtendedQueryDslJpaRepository;
import com.zhiyu.blog.bean.ArticleBean;

public interface ArticleDao
		extends ExtendedQueryDslJpaRepository<ArticleBean, Long>, JpaSpecificationExecutor<ArticleBean> {
	
	ArticleBean findByArticleId(Long articleId);
	
	@Transactional
	@Modifying
	void deleteByArticleId(Long articleId);

}
 