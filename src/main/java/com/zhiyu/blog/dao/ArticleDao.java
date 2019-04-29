package com.zhiyu.blog.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.infobip.spring.data.ExtendedQueryDslJpaRepository;
import com.zhiyu.blog.bean.ArticleBean;


public interface ArticleDao  extends ExtendedQueryDslJpaRepository<ArticleBean, Long>, JpaSpecificationExecutor<ArticleBean>{

}
