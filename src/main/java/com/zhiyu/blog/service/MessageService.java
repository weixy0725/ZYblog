package com.zhiyu.blog.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.zhiyu.blog.bean.MessageBean;

/**
 * 留言接口类
 * 
 * @author xinyuan.wei
 * @date 2019年6月3日
 */
public interface MessageService {

	/**
	 * 新增留言接口
	 * 
	 * @param articleId
	 * @param message
	 * @param request
	 * @throws Exception
	 */
	void saveMessage(Long articleId, String message, Integer type, HttpServletRequest request) throws Exception;

	/**
	 * 获取文章下的留言信息
	 * 
	 * @param articleId
	 * @return 
	 */
	List<MessageBean> getMessage(Long articleId);

}
