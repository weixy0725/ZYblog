package com.zhiyu.blog.webapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhiyu.blog.bean.MessageBean;
import com.zhiyu.blog.service.MessageService;
import com.zhiyu.blog.util.IpUtil;
import com.zhiyu.blog.util.JSONResultUtil;
import com.zhiyu.blog.util.ResultCodeEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 留言管理接口
 * 
 * @author xinyuan.wei
 * @date 2019年6月3日
 */
@Slf4j
@Api(tags = "留言管理")
@RestController
@RequestMapping(value = "/messageManagement")
public class MessageController {
	
	@Autowired
	private MessageService messageService;
	
	@ApiOperation(value = "新增留言")
	@PostMapping(value = "/message")
	private JSONObject addMessage(
			@ApiParam(name = "articleId", value = "文章编号", required = true, example = "1") @RequestParam(required = true) Long articleId,
			@ApiParam(name = "message", value = "留言内容", required = true, example = "1") @RequestParam(required = true) String message,
			@ApiParam(name = "type", value = "0为回复，1为留言", required = true, example = "1") @RequestParam(required = true) Integer type,
			HttpServletRequest request) {	
		try {	
			messageService.saveMessage(articleId, message, type, request);
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(ResultCodeEnum.Exception.getValue(), "", e.getMessage());
		}
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), new JSONObject(), new JSONArray());
	}
	
	@ApiOperation(value = "获取留言")
	@GetMapping(value = "/message")
	private JSONObject getMessage(
			@ApiParam(name = "articleId", value = "文章编号", required = true, example = "1") @RequestParam(required = true) Long articleId) {
		JSONArray jsonArray = new JSONArray();
		try {	
			List<MessageBean> messageList=messageService.getMessage(articleId);
			if(null!=messageList) {
				List<MessageBean> messageBeans = new ArrayList<MessageBean>();
				Iterator<MessageBean> iterator = messageList.iterator();
				while(iterator.hasNext()) {
					MessageBean m = iterator.next();
					m.setIp(IpUtil.hiddenIP(m.getIp()));
					messageBeans.add(m);
				}
				jsonArray = JSONArray.parseArray(JSON.toJSONString(messageBeans));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(ResultCodeEnum.Exception.getValue(), "", e.getMessage());
		}
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), new JSONObject(), jsonArray);
	}

}
