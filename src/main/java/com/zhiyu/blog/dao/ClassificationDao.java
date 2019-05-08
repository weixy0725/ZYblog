package com.zhiyu.blog.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zhiyu.blog.bean.ClassificationBean;

public interface ClassificationDao
		extends JpaRepository<ClassificationBean, Integer>, JpaSpecificationExecutor<ClassificationBean> {

	List<ClassificationBean> findByTypeId(Integer typeId);
}
