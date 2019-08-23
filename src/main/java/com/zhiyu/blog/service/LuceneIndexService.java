package com.zhiyu.blog.service;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import com.alibaba.fastjson.JSONObject;
import com.zhiyu.blog.bean.ArticleBean;
import com.zhiyu.blog.dto.ArticleDto;

/**
 * Lucene索引的业务逻辑接口类
 * 
 * @author xinyuan.wei
 * @date 2019年8月20日
 */
public interface LuceneIndexService {
	
	/**
	 * 生成lucene index
	 *
	 * @throws IOException
	 */
	void createIndex() throws IOException;
	
	/**
	 * 检索index
	 * 
	 * @param text
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws InvalidTokenOffsetsException 
	 */
	JSONObject search(String text,Integer pageIndex,Integer pageSize) throws IOException, ParseException, InvalidTokenOffsetsException;

	/**
	 * 删除对应index
	 * @throws IOException 
	 */
	void deleteIndex(Long articleId) throws IOException;
	
	/**
	 * 新增索引 
	 * 
	 * @param articleBean
	 * @throws IOException 
	 */
	void addIndex(ArticleBean articleBean) throws IOException;
}
