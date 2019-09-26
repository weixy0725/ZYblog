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
	 * @throws IOException 
	 */
	void save(String articleName, String articleSummarize, Integer typeId, Integer classificationId, Integer isOriginal,
			String article, String cover,Integer state) throws IOException;

	/**
	 * 获取文章内容
	 * 
	 * @param articleId
	 * @return
	 */
	ArticleBean findByArticleId(Long articleId);
	
	/**
	 * 获取类型名称
	 * 
	 * @param typeId
	 * @return
	 */
	TypeBean findById(Integer typeId);

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
	List<Tuple> findArticlesPaging(Integer typeId, Integer classificationId, Integer pageIndex, Integer pageSize,Integer state);

	/**
	 * 获取文章总数
	 * 
	 * @param typeId
	 * @param classificationId
	 * @return
	 */
	Long findArticlesCount(Integer typeId, Integer classificationId,Integer state);

	/**
	 * 上传图片
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	String uploadPictrue(MultipartFile file,Boolean isMark) throws Exception, IOException;

	/**
	 * 删除文章
	 * 
	 * @param articleId
	 * @throws IOException 
	 */
	public void deleteByArticleId(Long articleId) throws IOException;
	
	/**
	 * 更新文章
	 * 
	 * @param articleId
	 * @throws IOException 
	 */
	public int updateArticle(Long articleId,String articleName, String articleSummarize, Integer typeId, Integer classificationId, Integer isOriginal,
			String article, String cover,Integer state) throws IOException;
	/**
	 * 新增文章具体分类
	 * 
	 * @param typeId
	 * @param classification
	 */
	public void addClassification(Integer typeId,String classification);
	
	/**
	 * 更新文章具体分类
	 * 
	 * @param classificaitonId
	 * @param classification
	 */
	public int updateClassification(Integer classificaitonId,String classification);
	
	/**
	 * 删除具体分类
	 * 
	 * @param classificaitonId
	 * @return
	 */
	public void deleteClassification(Integer classificaitonId);
	
	/**
	 * 更新或保存文章实体
	 */
	public void save(ArticleBean articelBean);
	
	/**
	 * 查询所有文章相关内容
	 * @return
	 */
	public List<Tuple> findArticlesForIndex();
	

}
