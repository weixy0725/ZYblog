package com.zhiyu.blog.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.zhiyu.blog.bean.ArticleBean;
import com.zhiyu.blog.bean.ClassificationBean;
import com.zhiyu.blog.bean.TypeBean;
import com.zhiyu.blog.bean.QArticleBean;
import com.zhiyu.blog.bean.QClassificationBean;
import com.zhiyu.blog.bean.QTypeBean;
import com.zhiyu.blog.dao.ClassificationDao;
import com.zhiyu.blog.dao.ArticleDao;
import com.zhiyu.blog.dao.TypeDao;
import com.zhiyu.blog.service.ArticleService;
import com.zhiyu.blog.service.LuceneIndexService;
import com.zhiyu.blog.util.ResultCodeEnum;
import com.zhiyu.blog.util.WatermarkUtil;

/**
 * 文章业务实现类
 * 
 * @author xinyuan.wei
 * @date 2019年4月29日
 */
@Service
public class ArticleServiceImpl implements ArticleService {

	@Value("${img.save.path}")
	private String imgLocalSavePath;

	@Autowired
	private ArticleDao articleDao;

	@Autowired
	private TypeDao articleTypeDao;

	@Autowired
	private ClassificationDao articleClassificationDao;
	
	@Autowired
	private LuceneIndexService luceneIndexService;

	@Override
	public void save(String articleName, String articleSummarize, Integer typeId, Integer classificationId,
			Integer isOriginal, String article, String cover,Integer state) throws IOException {
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
		if(!StringUtils.isEmpty(cover)) {
			articleBean.setCover(cover);
		}
		
		if(null!=state) {
			articleBean.setState(state);
		}
		articleDao.saveAndFlush(articleBean);
		
		luceneIndexService.addIndex(articleBean);
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
			Integer pageSize,Integer state) {
		return articleDao.query(q -> querySQL(q, typeId, classificationId, false,state).limit(pageSize)
				.offset((pageIndex - 1) * pageSize).fetch());
	}

	@Override
	public Long findArticlesCount(Integer typeId, Integer classificationId,Integer state) {
		return articleDao.query(q -> querySQL(q, typeId, classificationId, true,state).fetchCount());
	}

	@Override
	public List<Tuple> findArticlesForIndex(){
		return articleDao.query(q -> querySQL(q)).fetch();
	}
	
	/**
	 * 构建querydsl查询过程
	 * 
	 * @param q
	 * @param typeId
	 * @param classificationId
	 * @return
	 */
	private JPAQuery<Tuple> querySQL(JPAQuery<?> q, Integer typeId, Integer classificationId, Boolean isCount,Integer state) {

		QArticleBean article = QArticleBean.articleBean;
		QClassificationBean classification = QClassificationBean.classificationBean;
		QTypeBean typeBean = QTypeBean.typeBean;

		JPAQuery<Tuple> query = null;

		// 仅计数不做左联查询

		if (isCount) {
			query = q.select(article.articleId, article.typeId, article.classificationId).from(article);
		} else {
			query = q.select(article, classification, typeBean).from(article).leftJoin(classification)
					.on(article.classificationId.eq(classification.id)).leftJoin(typeBean)
					.on(article.typeId.eq(typeBean.id)).orderBy(article.datetime.desc());
		}

		assert query != null;

		BooleanBuilder queryCondition = new BooleanBuilder();

		if (null==typeId) {
			queryCondition.and(article.typeId.ne(2));
		}else if(typeId!=0){
			queryCondition.and(article.typeId.eq(typeId));
		}

		if (null != classificationId) {
			queryCondition.and(article.classificationId.eq(classificationId));
		}
		
		if(null!=state) {
			queryCondition.and(article.state.eq(state));
		}

		query.where(queryCondition);

		return query;

	}
	
	/**
	 * 构建querydsl查询过程用于查询所有文章
	 * 
	 * @param q
	 * @return
	 */
	private JPAQuery<Tuple> querySQL(JPAQuery<?> q) {

		QArticleBean article = QArticleBean.articleBean;
		QClassificationBean classification = QClassificationBean.classificationBean;
		QTypeBean typeBean = QTypeBean.typeBean;

		JPAQuery<Tuple> query = null;
		
        query = q.select(article, classification, typeBean).from(article).leftJoin(classification)
					.on(article.classificationId.eq(classification.id)).leftJoin(typeBean)
					.on(article.typeId.eq(typeBean.id)).orderBy(article.datetime.desc());
        
        BooleanBuilder queryCondition = new BooleanBuilder();
        queryCondition.and(article.state.eq(0));

		assert query != null;

		return query;

	}

	@Override
	public String uploadPictrue(MultipartFile file,Boolean isMark) throws Exception, IOException {
		if (null != file) {
			// 图片本地要存储至的路径
			Path localPath = Paths.get(imgLocalSavePath);
			// 文件原名称
			String fileName = file.getOriginalFilename();
			// 判断文件类型
			String type = fileName.indexOf(".") != -1
					? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length())
					: null;
			if (null != type) {
				if ("png".equals(type.toLowerCase()) || "jpg".equals(type.toLowerCase())
						|| "jpeg".equals(type.toLowerCase()) || "gif".equals(type.toLowerCase())) {
					String newImgName = UUID.randomUUID().toString().replace("-", "") + "." + type.toLowerCase();
					Path savePath = localPath.resolve(newImgName);
					file.transferTo(savePath.toFile());
					if(isMark) {
						String markImgName =UUID.randomUUID().toString().replace("-", "") + "." + type.toLowerCase();
						Path markPath = localPath.resolve(markImgName);
						WatermarkUtil.textWatermark(savePath.toString(), markPath.toString(), type.toLowerCase());
						return markImgName;
					}else {
					   return newImgName;
					}
				}
			}

		}
		return null;
	}

	@Override
	public ArticleBean findByArticleId(Long articleId) {
		return articleDao.findByArticleId(articleId);
	}

	@Override
	public void deleteByArticleId(Long articleId) throws IOException {
		articleDao.deleteByArticleId(articleId);
		luceneIndexService.deleteIndex(articleId);
	}

	@Override
	public int updateArticle(Long articleId, String articleName, String articleSummarize, Integer typeId,
			Integer classificationId, Integer isOriginal, String article, String cover,Integer state) throws IOException {
		ArticleBean articleBean = articleDao.findByArticleId(articleId);
		if (null == articleBean) {
			return ResultCodeEnum.Fail.getValue();
		} else {
			articleBean.setArticleName(articleName);
			articleBean.setArticleSummarize(articleSummarize);
			articleBean.setTypeId(typeId);
			articleBean.setClassificationId(classificationId);
			articleBean.setIsOriginal(isOriginal);
			articleBean.setArticleContent(article);
			//articleBean.setBrowseTimes(0);
			//articleBean.setMessageCount(0);
			articleBean.setCover(cover);
			if(null==state) {
				articleBean.setState(0);
				//删除旧的索引
				luceneIndexService.deleteIndex(articleId);
				//新增新的索引
				luceneIndexService.addIndex(articleBean);
			}else {
				articleBean.setState(state);
				if(state.intValue()==2) {
					//删除索引不显示
					luceneIndexService.deleteIndex(articleId);
				}else {
					//删除旧的索引
					luceneIndexService.deleteIndex(articleId);
					//新增新的索引
					luceneIndexService.addIndex(articleBean);
				}
			}
			articleDao.save(articleBean);
			return ResultCodeEnum.Success.getValue();
		}

	}

	@Override
	public TypeBean findById(Integer typeId) {
		TypeBean typeBean = articleTypeDao.findById(typeId).get();
		return typeBean;
	}

	@Override
	public void addClassification(Integer typeId, String classification) {
		ClassificationBean classificationBean = new ClassificationBean();
		classificationBean.setTypeId(typeId);
		classificationBean.setClassification(classification);
		articleClassificationDao.saveAndFlush(classificationBean);
	}

	@Override
	public int updateClassification(Integer classificaitonId, String classification) {
		ClassificationBean classificationBean = articleClassificationDao.getOne(classificaitonId);
		if(null!=classificationBean) {
			classificationBean.setClassification(classification);
			articleClassificationDao.save(classificationBean);
		}else {
			return ResultCodeEnum.Fail.getValue();
		}
		return ResultCodeEnum.Success.getValue();
	}

	@Override
	public void deleteClassification(Integer classificaitonId) {
		articleClassificationDao.deleteById(classificaitonId);
	}

	@Override
	public void save(ArticleBean articelBean) {
		articleDao.save(articelBean);
	}

}
