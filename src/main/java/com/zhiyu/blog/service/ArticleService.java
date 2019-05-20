package com.zhiyu.blog.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.querydsl.core.Tuple;
import com.zhiyu.blog.bean.ArticleBean;
import com.zhiyu.blog.bean.ClassificationBean;
import com.zhiyu.blog.bean.TypeBean;

/**
 * 文章业务接口类
 * 
 * @author xinyuan.wei
 * @date 2019年4月29日
 */
public interface ArticleService {
	/**
	 * 保存文章
	 * 
	 * @param articleName
	 * @param classificationId
	 * @param article
	 */
	void save(String articleName, String articleSummarize, Integer typeId, Integer classificationId, Integer isOriginal,
			String article, String cover);

	/**
	 * 获取文章内容
	 * 
	 * @param articleId
	 * @return
	 */
	ArticleBean findByArticleId(Long articleId);

	/**
	 * 获取所有文章类型
	 * 
	 * @return
	 */
	List<TypeBean> findAllArticleType();

	/**
	 * 获取所有具体分类
	 * 
	 * @return
	 */
	List<ClassificationBean> findAllArticleClassification(Integer typeId);

	/**
	 * 分页获取文章内容
	 * 
	 * @param typeId
	 * @param classificationId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	List<Tuple> findArticlesPaging(Integer typeId, Integer classificationId, Integer pageIndex, Integer pageSize);

	/**
	 * 获取文章总数
	 * 
	 * @param typeId
	 * @param classificationId
	 * @return
	 */
	Long findArticlesCount(Integer typeId, Integer classificationId);

	/**
	 * 上传图片
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	String uploadPictrue(MultipartFile file) throws Exception, IOException;

	/**
	 * 删除文章
	 * 
	 * @param articleId
	 */
	public void deleteByArticleId(Long articleId);
	
	/**
	 * 更新文章
	 * 
	 * @param articleId
	 */
	public int updateArticle(Long articleId,String articleName, String articleSummarize, Integer typeId, Integer classificationId, Integer isOriginal,
			String article, String cover);
}
