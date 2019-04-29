package com.zhiyu.blog.webapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhiyu.blog.service.ArticleService;
import com.zhiyu.blog.util.JSONResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 文章管理接口类
 * 
 * @author xinyuan.wei
 * @date 2019年4月29日
 */
@Api(tags = "文章管理")
@RestController(value = "/articleManagement")
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;
	
	private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

	@ApiOperation(value = "新增文章")
	@PostMapping(value = "/article")
	public JSONObject addArticle(
			@ApiParam(name = "articleName", value = "文章名称", required = true, example = "1") @RequestParam(required = true) String articleName,
			@ApiParam(name = "classificationId", value = "文章分类编号", required = true, example = "1") @RequestParam(required = true) Integer classificationId,
			@ApiParam(name = "article", value = "文章内容", required = true, example = "1") @RequestParam(required = true)  String article) {
		try {	  
			articleService.save(articleName, classificationId, article);
			logger.info("存储文章:{}成功！",articleName);
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(1, "", "");
		}
		return JSONResultUtil.successResult(0, new JSONObject(), new JSONArray());
	}

}
