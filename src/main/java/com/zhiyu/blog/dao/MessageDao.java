package com.zhiyu.blog.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.zhiyu.blog.bean.MessageBean;

public interface MessageDao extends JpaRepository<MessageBean, Long>, JpaSpecificationExecutor<MessageBean>{
	
	@Query(nativeQuery=true,value="select * from tb_message where article_id=?1 and state=?2 order by datetime desc limit ?3,?4")
	List<MessageBean> findByArticleIdAndState(Long articleId,int state,Integer pageIndex,Integer pageSize);
	
	@Query(nativeQuery=true,value="select m.*,a.article_name,t.article_type from tb_message m left join tb_article a on a.article_id=m.article_id left join tb_type t ON a.type_id =t.id order by m.datetime desc limit ?1,?2")
	List<Object[]> findAll(Integer pageIndex,Integer pageSize);

}
