package com.zhiyu.blog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.zhiyu.blog.bean.TypeBean;

public interface TypeDao
		extends JpaRepository<TypeBean, Integer>, JpaSpecificationExecutor<TypeBean> {

}
