package com.zhiyu.blog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.zhiyu.blog.bean.ArticleTypeBean;

public interface ArticleTypeDao
		extends JpaRepository<ArticleTypeBean, Integer>, JpaSpecificationExecutor<ArticleTypeBean> {

}
