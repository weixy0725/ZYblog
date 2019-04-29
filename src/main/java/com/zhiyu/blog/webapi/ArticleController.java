package com.zhiyu.blog.webapi;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhiyu.blog.bean.ArticleClassificationBean;
import com.zhiyu.blog.bean.ArticleTypeBean;
import com.zhiyu.blog.service.ArticleService;
import com.zhiyu.blog.util.JSONResultUtil;
import com.zhiyu.blog.util.ResultCodeEnum;

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
			@ApiParam(name = "typeId", value = "文章类型编号", required = true, example = "1") @RequestParam(required = true) Integer typeId,
			@ApiParam(name = "classificationId", value = "文章具体分类编号", required = true, example = "1") @RequestParam(required = true) Integer classificationId,
			@ApiParam(name = "article", value = "文章内容", required = true, example = "1") @RequestParam(required = true) String article) {
		try {
			articleService.save(articleName, typeId, classificationId, article);
			logger.info("存储文章:{}成功！", articleName);
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(ResultCodeEnum.Exception.getValue(), "", e.getMessage());
		}
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), new JSONObject(), new JSONArray());
	}

	@ApiOperation(value = "获取文章类型")
	@GetMapping(value = "/type")
	public JSONObject getType() {
		JSONArray array = new JSONArray();
		try {
			List<ArticleTypeBean> articleTypes = articleService.findAllArticleType();
			Iterator<ArticleTypeBean> iterator = articleTypes.iterator();
			while (iterator.hasNext()) {
				ArticleTypeBean a = iterator.next();
				JSONObject object = new JSONObject();
				object.put("typeId", a.getId());
				object.put("type", a.getArticleType());
				array.add(object);
			}
			logger.info("获取文章类型成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(ResultCodeEnum.Exception.getValue(), "", e.getMessage());
		}
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), new JSONObject(), array);
	}

	@ApiOperation(value = "获取文章具体分类")
	@GetMapping(value = "/classification")
	public JSONObject getClassification(
			@ApiParam(name = "typeId", value = "文章类型编号", required = true, example = "1") @RequestParam(required = true) Integer typeId) {
		JSONArray array = new JSONArray();
		try {
			List<ArticleClassificationBean> articleClassifications = articleService
					.findAllArticleClassification(typeId);
			Iterator<ArticleClassificationBean> iterator = articleClassifications.iterator();
			while(iterator.hasNext()) {
				ArticleClassificationBean a = iterator.next();
				JSONObject object = new JSONObject();
				object.put("classificationId", a.getId());
				object.put("classification", a.getClassification());
				array.add(object);
			}
			logger.info("获取文章具体分类成功!");

		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(ResultCodeEnum.Exception.getValue(), "", e.getMessage());
		}

		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), new JSONObject(), array);
	}

}
