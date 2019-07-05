package com.zhiyu.blog.webapi.v0;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhiyu.blog.bean.ArticleBean;
import com.zhiyu.blog.bean.MessageBean;
import com.zhiyu.blog.service.ArticleService;
import com.zhiyu.blog.service.MessageService;
import com.zhiyu.blog.util.DateFormatUtil;
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
	
	@Autowired
	private ArticleService articleService;

	@ApiOperation(value = "新增留言")
	@PostMapping(value = "/message")
	private JSONObject addMessage(
			@ApiParam(name = "articleId", value = "文章编号", required = true, example = "1") @RequestParam(required = true) Long articleId,
			@ApiParam(name = "message", value = "留言内容", required = true, example = "1") @RequestParam(required = true) String message,
			@ApiParam(name = "type", value = "0为回复，1为留言", required = true, example = "1") @RequestParam(required = true) Integer type,
			@ApiParam(name = "id", value = "留言编号", required = false, example = "1") @RequestParam(required = false) Long id,
			HttpServletRequest request) {
		try {
			messageService.saveMessage(articleId, message, type, id,request);
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(ResultCodeEnum.Exception.getValue(), "", e.getMessage());
		}
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), new JSONObject(), new JSONArray());
	}

	@ApiOperation(value = "获取留言")
	@GetMapping(value = "/message")
	private JSONObject getMessage(
			@ApiParam(name = "articleId", value = "文章编号", required = true, example = "1") @RequestParam(required = true) Long articleId,
			@ApiParam(name = "pageIndex", value = "页码", required = true, example = "1", defaultValue = "1") @RequestParam(defaultValue = "1", required = true) Integer pageIndex,
			@ApiParam(name = "pageSize", value = "页数据条数", required = true, example = "10", defaultValue = "10") @RequestParam(defaultValue = "10", required = true) Integer pageSize) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		try {
			List<MessageBean> messageList = messageService.getMessage(articleId,pageIndex,pageSize);
			ArticleBean articleBean = articleService.findByArticleId(articleId);
			if (null != messageList) {
				List<MessageBean> messageBeans = new ArrayList<MessageBean>();
				Iterator<MessageBean> iterator = messageList.iterator();
				while (iterator.hasNext()) {
					MessageBean m = iterator.next();
					m.setIp(IpUtil.hiddenIP(m.getIp()));
					messageBeans.add(m);
				}
				jsonArray = JSONArray.parseArray(JSON.toJSONString(messageBeans));
				jsonObject.put("count", articleBean.getMessageCount());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(ResultCodeEnum.Exception.getValue(), "", e.getMessage());
		}
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), jsonObject, jsonArray);
	}

	@ApiOperation(value = "变更留言状态")
	@PutMapping(value = "/message")
	private JSONObject updateMessageState(
			@ApiParam(name = "id", value = "留言编号", required = true, example = "1") @RequestParam(required = true) Long id,
			@ApiParam(name = "state", value = "状态（0正常，1删除）", required = true, example = "1") @RequestParam(required = true) Integer state) {
		try {
			messageService.updateMessage(id, state);
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(ResultCodeEnum.Exception.getValue(), "", e.getMessage());
		}
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), new JSONObject(), new JSONArray());
	}
	
	@ApiOperation(value = "获取留言")
	@GetMapping(value = "/messageList")
	private JSONObject getMessageList(
			@ApiParam(name = "pageIndex", value = "页码", required = true, example = "1", defaultValue = "1") @RequestParam(defaultValue = "1", required = true) Integer pageIndex,
			@ApiParam(name = "pageSize", value = "页数据条数", required = true, example = "10", defaultValue = "10") @RequestParam(defaultValue = "10", required = true) Integer pageSize) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		try {
			List<Object[]> messageList = messageService.findAll(pageIndex, pageSize);
			int count = messageService.findAllSize();
			if (null != messageList) {
				Iterator<Object[]> iterator = messageList.iterator();
				while (iterator.hasNext()) {
					Object[] o = iterator.next();
					JSONObject object = new JSONObject();
					object.put("id",Long.valueOf(o[0].toString()));
					object.put("articleId",Long.valueOf(o[1].toString()));
					object.put("message", o[2].toString());
					object.put("datetime", o[3].toString().substring(0, 19));
					object.put("type", o[4].toString());
					object.put("ip", o[5].toString());
					object.put("state", o[6].toString());
					object.put("articleName", o[7].toString());
					object.put("articleType", o[8].toString());
					jsonArray.add(object);
				}
				jsonObject.put("count", count);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(ResultCodeEnum.Exception.getValue(), "", e.getMessage());
		}
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), jsonObject, jsonArray);
	}

}
