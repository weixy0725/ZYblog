package com.zhiyu.blog.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhiyu.blog.bean.MessageBean;
import com.zhiyu.blog.dao.MessageDao;
import com.zhiyu.blog.service.MessageService;
import com.zhiyu.blog.util.IpUtil;

/**
 * 留言管理业务层实现类
 * 
 * @author xinyuan.wei
 * @date 2019年6月3日
 */
@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private MessageDao messageDao;

	@Override
	public void saveMessage(Long articleId, String message, Integer type, HttpServletRequest request) throws Exception {
		MessageBean  messageBean = new MessageBean();
		messageBean.setArticleId(articleId);
		messageBean.setMessage(message);
		messageBean.setType(type);
		messageBean.setDatetime(LocalDateTime.now());
		messageBean.setState(0);
		String ip = IpUtil.getIpAddr(request);
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			throw new Exception("您当前无法留言！");
		}
		messageBean.setIp(ip);
		messageDao.save(messageBean);
	}

	@Override
	public List<MessageBean> getMessage(Long articleId) {
		List<MessageBean> messageList = messageDao.findByArticleId(articleId);
		return null;
	}

}
