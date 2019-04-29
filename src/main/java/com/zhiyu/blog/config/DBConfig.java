package com.zhiyu.blog.config;

import javax.persistence.EntityManager;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.infobip.spring.data.SimpleExtendedQueryDslJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * JPA配置类
 * 
 * @author xinyuan.wei
 * @date 2019年4月28日
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(repositoryBaseClass = SimpleExtendedQueryDslJpaRepository.class,
basePackages = {"com.zhiyu.blog.dao"})
@EntityScan(basePackages = {"com.zhiyu.blog.bean"})
public class DBConfig {
	
	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}
}
