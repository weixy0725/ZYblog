package com.zhiyu.blog.service.impl;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhiyu.blog.bean.ArticleBean;
import com.zhiyu.blog.bean.MessageBean;
import com.zhiyu.blog.dao.ArticleDao;
import com.zhiyu.blog.dao.MessageDao;
import com.zhiyu.blog.service.MessageService;
import com.zhiyu.blog.util.IpUtil;
import com.zhiyu.blog.util.MailUtil;

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
	public void saveMessage(Long articleId, String message, Integer type,Long id,String nickname,String email,HttpServletRequest request) throws Exception {
		MessageBean  messageBean = new MessageBean();
		
		messageBean.setArticleId(articleId);
		messageBean.setMessage(message);
		messageBean.setType(type);
		messageBean.setDatetime(LocalDateTime.now());
		messageBean.setState(0);
		String ip = "";
	    String emailTemp="";
	    String nickNameTmp="";
		MessageBean  m = null;
		if(null!=id) {
			m=messageDao.getOne(id);
			if(null!=m) {
				ip=m.getIp();
				emailTemp=m.getEmail();
				nickNameTmp=m.getNickname();
			}
		}else {
		
			ip=IpUtil.getIpAddr(request);
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
					throw new Exception("您当前无法留言！");
			}
		}
		messageBean.setIp(ip);
		//回复
		if(type.intValue()==0) {
			if(!StringUtils.isEmpty(emailTemp)) {
				messageBean.setEmail(emailTemp);
			}
			messageBean.setNickname(nickNameTmp);
			messageBean.setIsSend(0);
		//留言
		}else {
			if(!StringUtils.isEmpty(email)) {
				messageBean.setEmail(email);
			}
			
			if(!StringUtils.isEmpty(nickname)) {
				messageBean.setNickname(nickname);
			}else {
				messageBean.setNickname("游客");
			}
			messageBean.setIsSend(0);
		}
		messageDao.saveAndFlush(messageBean);
		//更新文章的留言数
		ArticleBean articleBean = articleDao.findByArticleId(articleId);
		int count = articleBean.getMessageCount()+1;
		articleBean.setMessageCount(count);
		articleDao.save(articleBean);
		//留言存储成功推送邮件
		if(type.intValue()==1) {
			MailUtil.sendMail(1, "weixy0725@sina.com", message);
		}
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

	@Override
	public String sendMail(Long id) throws UnsupportedEncodingException, MessagingException {
		MessageBean messageBean = messageDao.getOne(id);
		if(!StringUtils.isEmpty(messageBean.getEmail())&&!StringUtils.isEmpty(messageBean.getMessage())) {
			MailUtil.sendMail(0, messageBean.getEmail(), messageBean.getMessage());
		}else {
			return "邮件地址与通知内容不可为空！";
		}
		
		return null;
		
	}

	@Override
	public void updateMessage(Integer isSend, Long id) {
		MessageBean messageBean = messageDao.getOne(id);
		if(null!=messageBean) {
			messageBean.setIsSend(isSend);
			messageDao.save(messageBean);
		}
		
	}

}
