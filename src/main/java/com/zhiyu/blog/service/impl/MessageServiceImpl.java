package com.zhiyu.blog.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhiyu.blog.bean.ArticleBean;
import com.zhiyu.blog.bean.MessageBean;
import com.zhiyu.blog.dao.ArticleDao;
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
	
	@Autowired
	private ArticleDao articleDao;

	@Override
	public void saveMessage(Long articleId, String message, Integer type,Long id,HttpServletRequest request) throws Exception {
		MessageBean  messageBean = new MessageBean();
		MessageBean  m = null;
		if(null!=id) {
			m=messageDao.getOne(id);
		}
		messageBean.setArticleId(articleId);
		messageBean.setMessage(message);
		messageBean.setType(type);
		messageBean.setDatetime(LocalDateTime.now());
		messageBean.setState(0);
		String ip = "";
		if(null!=m) {
			ip=m.getIp();
		}else{
			ip=IpUtil.getIpAddr(request);
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
				throw new Exception("您当前无法留言！");
			}
		}
		System.out.println(ip);
		messageBean.setIp(ip);
		messageDao.save(messageBean);
		//更新文章的留言数
		ArticleBean articleBean = articleDao.findByArticleId(articleId);
		int count = articleBean.getMessageCount()+1;
		articleBean.setMessageCount(count);
		articleDao.save(articleBean);
	}

	@Override
	public List<MessageBean> getMessage(Long articleId,Integer pageIndex,Integer pageSize) {
		Integer index =(pageIndex-1)*pageSize;
		List<MessageBean> messageList = messageDao.findByArticleIdAndState(articleId,0,index,pageSize);
		return messageList;
	}

	@Override
	public void updateMessage(Long id,Integer state) {
		MessageBean messageBean = messageDao.getOne(id);
		ArticleBean articleBean = articleDao.findByArticleId(messageBean.getArticleId());
		if(state==0) {
			articleBean.setMessageCount(articleBean.getMessageCount()+1);
		}else {
			articleBean.setMessageCount(articleBean.getMessageCount()-1);
		}
		messageBean.setState(state);
		messageDao.save(messageBean);
		articleDao.save(articleBean);
	}

	@Override
	public List<Object[]> findAll(Integer pageIndex, Integer pageSize) {
		Integer index =(pageIndex-1)*pageSize;
		List<Object[]> messageList = messageDao.findAll(index, pageSize);
		return messageList;
	}
	
	@Override
	public Integer findAllSize() {
		return messageDao.findAll().size();
	}

}
