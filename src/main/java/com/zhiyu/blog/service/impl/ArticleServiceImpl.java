package com.zhiyu.blog.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.zhiyu.blog.bean.ArticleBean;
import com.zhiyu.blog.bean.ClassificationBean;
import com.zhiyu.blog.bean.TypeBean;
import com.zhiyu.blog.bean.QArticleBean;
import com.zhiyu.blog.bean.QClassificationBean;
import com.zhiyu.blog.dao.ClassificationDao;
import com.zhiyu.blog.dao.ArticleDao;
import com.zhiyu.blog.dao.TypeDao;
import com.zhiyu.blog.service.ArticleService;

/**
 * 文章业务实现类
 * 
 * @author xinyuan.wei
 * @date 2019年4月29日
 */
@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDao articleDao;

	@Autowired
	private TypeDao articleTypeDao;

	@Autowired
	private ClassificationDao articleClassificationDao;

	@Override
	public void save(String articleName, String articleSummarize, Integer typeId, Integer classificationId,
			Integer isOriginal,String article) {
		ArticleBean articleBean = new ArticleBean();
		articleBean.setArticleName(articleName);
		articleBean.setArticleSummarize(articleSummarize);
		articleBean.setTypeId(typeId);
		articleBean.setClassificationId(classificationId);
		articleBean.setIsOriginal(isOriginal);
		articleBean.setArticleContent(article);
		articleBean.setDatetime(LocalDateTime.now());
		articleBean.setBrowseTimes(0);
		articleBean.setMessageCount(0);
		articleDao.save(articleBean);
	}

	@Override
	public List<TypeBean> findAllArticleType() {
		return articleTypeDao.findAll();
	}

	@Override
	public List<ClassificationBean> findAllArticleClassification(Integer typeId) {
		return articleClassificationDao.findByTypeId(typeId);
	}

	@Override
	public List<Tuple> findArticlesPaging(Integer typeId, Integer classificationId, Integer pageIndex,
			Integer pageSize) {
		return articleDao.query(q -> querySQL(q, typeId, classificationId, false).limit(pageSize)
				.offset((pageIndex - 1) * pageSize).fetch());
	}
	

	@Override
	public Long findArticlesCount(Integer typeId, Integer classificationId) {
		return articleDao.query(q -> querySQL(q, typeId, classificationId, true).fetchCount());
	}

	/**
	 * 构建querydsl查询过程
	 * 
	 * @param q
	 * @param typeId
	 * @param classificationId
	 * @return
	 */
	private JPAQuery<Tuple> querySQL(JPAQuery<?> q, Integer typeId, Integer classificationId, Boolean isCount) {

		QArticleBean article = QArticleBean.articleBean;
		QClassificationBean classification = QClassificationBean.classificationBean;

		JPAQuery<Tuple> query = null;
		
        //仅计数不做左联查询
		
		if (isCount) {
			query = q.select(article.articleId,article.typeId,article.classificationId).from(article);
		} else {
			query = q.select(article, classification).from(article).leftJoin(classification)
					.on(article.classificationId.eq(classification.id)).orderBy(article.datetime.desc());
		}

		assert query != null;

		BooleanBuilder queryCondition = new BooleanBuilder();

		if (null != typeId) {
			queryCondition.and(article.typeId.eq(typeId));
		}

		if (null != classificationId) {
			queryCondition.and(article.classificationId.eq(classificationId));
		}

		query.where(queryCondition);

		return query;

	}

}
