package com.zhiyu.blog.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zhiyu.blog.bean.MessageBean;

public interface MessageDao extends JpaRepository<MessageBean, Integer>, JpaSpecificationExecutor<MessageBean>{
	
	List<MessageBean> findByArticleId(Long articleId);

}
