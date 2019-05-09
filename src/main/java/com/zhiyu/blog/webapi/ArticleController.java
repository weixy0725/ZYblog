package com.zhiyu.blog.webapi;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.querydsl.core.Tuple;
import com.zhiyu.blog.bean.ArticleBean;
import com.zhiyu.blog.bean.ClassificationBean;
import com.zhiyu.blog.bean.TypeBean;
import com.zhiyu.blog.bean.QArticleBean;
import com.zhiyu.blog.bean.QClassificationBean;
import com.zhiyu.blog.service.ArticleService;
import com.zhiyu.blog.util.DateFormatUtil;
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
@RestController
@RequestMapping(value = "/articleManagement")
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

	@ApiOperation(value = "新增文章")
	@PostMapping(value = "/article")
	public JSONObject addArticle(
			@ApiParam(name = "articleName", value = "文章名称", required = true, example = "1") @RequestParam(required = true) String articleName,
			@ApiParam(name = "articleSummarize", value = "文章梗概", required = true, example = "1") @RequestParam(required = true) String articleSummarize,
			@ApiParam(name = "typeId", value = "文章类型编号", required = true, example = "1") @RequestParam(required = true) Integer typeId,
			@ApiParam(name = "classificationId", value = "文章具体分类编号", required = true, example = "1") @RequestParam(required = true) Integer classificationId,
			@ApiParam(name = "isOriginal", value = "是否原创（0为转载，1为原创）", required = true, example = "1") @RequestParam(required = true) Integer isOriginal,
			@ApiParam(name = "article", value = "文章内容", required = true, example = "1") @RequestParam(required = true) String article) {
		try {
			articleService.save(articleName, articleSummarize, typeId, classificationId, isOriginal, article);
			logger.info("存储文章:{}成功！", articleName);
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(ResultCodeEnum.Exception.getValue(), "", e.getMessage());
		}
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), new JSONObject(), new JSONArray());
	}

	@ApiOperation(value = "分页获取所有文章信息", notes = "全局搜索另做")
	@GetMapping(value = "/articles")
	public JSONObject getAllArticleInfo(
			@ApiParam(name = "typeId", value = "文章类型编号", required = false, example = "1") @RequestParam(required = false) Integer typeId,
			@ApiParam(name = "classificationId", value = "文章具体分类编号", required = false, example = "1") @RequestParam(required = false) Integer classificationId,
			@ApiParam(name = "pageIndex", value = "页码", required = true, example = "1", defaultValue = "1") @RequestParam(defaultValue = "1", required = true) Integer pageIndex,
			@ApiParam(name = "pageSize", value = "页数据条数", required = true, example = "10", defaultValue = "10") @RequestParam(defaultValue = "10", required = true) Integer pageSize) {
		JSONObject object = new JSONObject();
		JSONArray array = new JSONArray();

		try {
			List<Tuple> articles = articleService.findArticlesPaging(typeId, classificationId, pageIndex, pageSize);

			Long count = articleService.findArticlesCount(typeId, classificationId);

			object.put("count", count);

			Iterator<Tuple> iterator = articles.iterator();
			while (iterator.hasNext()) {
				Tuple t = iterator.next();
				JSONObject o = new JSONObject();
				ArticleBean articleBean = t.get(QArticleBean.articleBean);
				ClassificationBean classificationBean = t.get(QClassificationBean.classificationBean);
				o.put("articleId", articleBean.getArticleId());
				o.put("articleName", articleBean.getArticleName());
				o.put("isOriginal", articleBean.getIsOriginal());
				o.put("articleSummarize", articleBean.getArticleSummarize());
				o.put("classificationId", articleBean.getClassificationId());
				o.put("classification", classificationBean.getClassification());
				o.put("datetime", DateFormatUtil.DateFormat(articleBean.getDatetime()));
				o.put("browseTimes", articleBean.getBrowseTimes());
				o.put("messageCount", articleBean.getMessageCount());
				array.add(o);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(ResultCodeEnum.Exception.getValue(), "", e.getMessage());
		}
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), object, array);
	}

	@ApiOperation(value = "获取文章类型")
	@GetMapping(value = "/type")
	public JSONObject getType() {
		JSONArray array = new JSONArray();
		try {
			List<TypeBean> articleTypes = articleService.findAllArticleType();
			Iterator<TypeBean> iterator = articleTypes.iterator();
			while (iterator.hasNext()) {
				TypeBean a = iterator.next();
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
			List<ClassificationBean> articleClassifications = articleService.findAllArticleClassification(typeId);
			Iterator<ClassificationBean> iterator = articleClassifications.iterator();
			while (iterator.hasNext()) {
				ClassificationBean a = iterator.next();
				JSONObject object = new JSONObject();
				object.put("classificationId", a.getId() + "");
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

	/**
	 * 上传背景图片
	 * 
	 * @param file
	 * @param buildingId
	 * @return JSONObject
	 */
	@ApiOperation(value = "上传图片", notes = "上传图片")
	@PostMapping(value = "/uploadPicture")
	public JSONObject uploadPicture(
			@ApiParam(name = "file", value = "图片", required = true) @RequestParam("file") MultipartFile file) {
		JSONObject object = new JSONObject();
		try {
			String result = articleService.uploadPictrue(file);
			if (StringUtils.isEmpty(result)) {
				return JSONResultUtil.failResult(ResultCodeEnum.Fail.getValue(), "上传图片失败！", "");
			}
			object.put("url", result);

		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(ResultCodeEnum.Exception.getValue(), "", e.getMessage());
		}

		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), object, new JSONArray());
	}

}
