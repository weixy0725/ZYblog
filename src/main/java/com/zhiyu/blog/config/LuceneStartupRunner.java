package com.zhiyu.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.zhiyu.blog.service.LuceneIndexService;

/**
 * 服务启动时生成lucene的索引
 * 
 * @author xinyuan.wei
 * @date 2019年8月20日
 */
@Configuration
public class LuceneStartupRunner implements CommandLineRunner{
	
	@Autowired
	private LuceneIndexService luceneIndexService;

	@Override
	public void run(String... args) throws Exception {		
		luceneIndexService.createIndex();
	}

}
