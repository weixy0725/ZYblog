package com.zhiyu.blog.webapi.v1;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.querydsl.core.Tuple;
import com.zhiyu.blog.bean.ArticleBean;
import com.zhiyu.blog.bean.ClassificationBean;
import com.zhiyu.blog.bean.QArticleBean;
import com.zhiyu.blog.bean.QClassificationBean;
import com.zhiyu.blog.bean.QTypeBean;
import com.zhiyu.blog.bean.TypeBean;
import com.zhiyu.blog.service.ArticleService;
import com.zhiyu.blog.util.DateFormatUtil;
import com.zhiyu.blog.util.JSONResultUtil;
import com.zhiyu.blog.util.ResultCodeEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取近期文章的controller
 * 
 * @author xinyuan.wei
 * @date 2019年7月16日
 */
@Slf4j
@Api(tags = "文章管理")
@RestController
@RequestMapping(value = "/recent")
public class RecentController {

	@Autowired
	private ArticleService articleService;

	@ApiOperation(value = "获取所有文章信息", notes = "全局搜索另做")
	@GetMapping(value = "/articles")
	public JSONObject getAllArticleInfo(
			@ApiParam(name = "typeId", value = "文章类型编号", required = false, example = "1") @RequestParam(required = false) Integer typeId,
			@ApiParam(name = "classificationId", value = "文章具体分类编号", required = false, example = "1") @RequestParam(required = false) Integer classificationId,
			@ApiParam(name = "pageIndex", value = "页码", required = true, example = "1", defaultValue = "1") @RequestParam(defaultValue = "1", required = true) Integer pageIndex,
			@ApiParam(name = "pageSize", value = "页数据条数", required = true, example = "10", defaultValue = "10") @RequestParam(defaultValue = "10", required = true) Integer pageSize) {
		JSONObject object = new JSONObject();
		JSONArray array = new JSONArray();

		try {
			List<Tuple> articles = articleService.findArticlesPaging(typeId, classificationId, pageIndex, pageSize,0);

			Long count = articleService.findArticlesCount(typeId, classificationId,0);

			object.put("count", count);

			Iterator<Tuple> iterator = articles.iterator();
			while (iterator.hasNext()) {
				Tuple t = iterator.next();
				JSONObject o = new JSONObject();
				ArticleBean articleBean = t.get(QArticleBean.articleBean);
				ClassificationBean classificationBean = t.get(QClassificationBean.classificationBean);
				TypeBean typeBean = t.get(QTypeBean.typeBean);
				o.put("articleId", articleBean.getArticleId());
				o.put("articleName", articleBean.getArticleName());
				o.put("isOriginal", articleBean.getIsOriginal());
				o.put("articleSummarize", articleBean.getArticleSummarize());
				o.put("classificationId", articleBean.getClassificationId());
				o.put("classification", classificationBean.getClassification());
				o.put("datetime", DateFormatUtil.DateFormat(articleBean.getDatetime()));
				o.put("browseTimes", articleBean.getBrowseTimes());
				o.put("messageCount", articleBean.getMessageCount());
				o.put("cover", articleBean.getCover() == null ? "" : articleBean.getCover());
				o.put("typeId", typeBean.getId());
				o.put("type", typeBean.getArticleType());
				array.add(o);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return JSONResultUtil.failResult(ResultCodeEnum.Exception.getValue(), "", e.getMessage());
		}
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), object, array);
	}

}
