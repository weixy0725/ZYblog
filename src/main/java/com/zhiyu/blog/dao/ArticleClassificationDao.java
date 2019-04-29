package com.zhiyu.blog.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zhiyu.blog.bean.ArticleClassificationBean;

public interface ArticleClassificationDao
		extends JpaRepository<ArticleClassificationBean, Integer>, JpaSpecificationExecutor<ArticleClassificationBean> {

	List<ArticleClassificationBean> findByTypeId(Integer typeId);
}
