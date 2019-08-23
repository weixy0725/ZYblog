package com.zhiyu.blog.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.querydsl.core.Tuple;
import com.zhiyu.blog.bean.ArticleBean;
import com.zhiyu.blog.bean.ClassificationBean;
import com.zhiyu.blog.bean.QArticleBean;
import com.zhiyu.blog.bean.QClassificationBean;
import com.zhiyu.blog.bean.QTypeBean;
import com.zhiyu.blog.bean.TypeBean;
import com.zhiyu.blog.dao.ClassificationDao;
import com.zhiyu.blog.dao.TypeDao;
import com.zhiyu.blog.dto.ArticleDto;
import com.zhiyu.blog.service.ArticleService;
import com.zhiyu.blog.service.LuceneIndexService;
import com.zhiyu.blog.util.DateFormatUtil;
import com.zhiyu.blog.util.JSONResultUtil;
import com.zhiyu.blog.util.ResultCodeEnum;

/**
 * Lucene索引的业务逻辑实现类
 * 
 * @author xinyuan.wei
 * @date 2019年8月20日
 */
@Service
public class LuceneIndexServiceImpl implements LuceneIndexService{

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private IndexWriter indexWriter;
	
	@Autowired
	private Analyzer analyzer;
	
	@Autowired
	private SearcherManager searcherManager;
	
	@Autowired
	private TypeDao typeDao;
	
	@Autowired
	private ClassificationDao classificationDao;

	@Override
	public void createIndex() throws IOException {
		List<Tuple> articles = articleService.findArticlesForIndex();
		if(articles.size()>0) {
			Iterator<Tuple> iterator = articles.iterator();
			List<Document> docs = new ArrayList<Document>();
			while(iterator.hasNext()) {
				Tuple t = iterator.next();
				ArticleBean articleBean = t.get(QArticleBean.articleBean);
				ClassificationBean classificationBean = t.get(QClassificationBean.classificationBean);
				TypeBean typeBean = t.get(QTypeBean.typeBean);
				Document doc = new Document();
				//StringField不会进行分词
				doc.add(new StringField("articleId", articleBean.getArticleId().toString(), Field.Store.YES));	
				doc.add(new TextField("articleName", articleBean.getArticleName(), Field.Store.YES));
				doc.add(new TextField("articleSummarize", articleBean.getArticleSummarize(), Field.Store.YES));
				doc.add(new TextField("articleContent", articleBean.getArticleContent(), Field.Store.YES));
				doc.add(new StringField("dateTime", DateFormatUtil.DateFormat(articleBean.getDatetime()), Field.Store.YES));
				doc.add(new TextField("classification", classificationBean.getClassification(), Field.Store.YES));
				doc.add(new TextField("articleType", typeBean.getArticleType(), Field.Store.YES));
				doc.add(new StringField("type", typeBean.getId().toString(), Field.Store.YES));
				doc.add(new StringField("isOriginal", articleBean.getIsOriginal().toString(), Field.Store.YES));
				docs.add(doc);
			}
			if(docs.size()>0) {
				indexWriter.addDocuments(docs);
				indexWriter.commit();
			}
		}
		        
	}

	@Override
	public JSONObject search(String text,Integer pageIndex, Integer pageSize) throws IOException, ParseException, InvalidTokenOffsetsException {	
		JSONObject object = new JSONObject();
		searcherManager.maybeRefresh();
		IndexSearcher indexSearcher = searcherManager.acquire();
		Builder builder = new BooleanQuery.Builder();
		Sort sort = new Sort();
		if (null!=text&&!"".equals(text.replaceAll(" ", ""))) {
			text=text.replaceAll(" ", "");
			/*Occur.MUST表示and,Occur.MUST_NOT表示not,SHOULD表示or
			 * */
			builder.add(new QueryParser("articleName", analyzer).parse(text), Occur.SHOULD);
			builder.add(new QueryParser("articleSummarize", analyzer).parse(text), Occur.SHOULD);
			builder.add(new QueryParser("articleContent", analyzer).parse(text), Occur.SHOULD);
			builder.add(new QueryParser("classification", analyzer).parse(text), Occur.SHOULD);
			builder.add(new QueryParser("articleType", analyzer).parse(text), Occur.SHOULD);
			//依据文章内容相关性降序
			sort.setSort(new SortField("articleContent", SortField.Type.SCORE, true));
		}
		
		TopDocs topDocs = indexSearcher.search(builder.build(), pageIndex * pageSize, sort);
		Long count= topDocs.totalHits;
		ScoreDoc[] hits = topDocs.scoreDocs;
		
		//对查询结果进行高亮处理
		QueryScorer scorer=new QueryScorer(builder.build());//查询出要查询的内容的得分
		Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);//通过得分获取要显示的片段
		SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<font color='#FA8072'>","</font>");//把查询结果转换成html格式预处理
		Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);//进行高亮处理，第一个参数放置html格式预处理，第二个参数是片段得分 (查询的语汇单元)
		highlighter.setTextFragmenter(fragmenter);//设置高亮要显示的片段 

		List<ArticleDto> articleList = new ArrayList<ArticleDto>();
		int i=0;
		if(pageIndex>1) {
			i=pageIndex*pageSize-pageSize-1;
		}
		
		for(;i<hits.length;i++) {
			Document doc = indexSearcher.doc(hits[i].doc);
		    ArticleDto dto = new ArticleDto();
		    dto.setArticleId(Long.valueOf(doc.get("articleId")));
		    TokenStream tokenStream=analyzer.tokenStream("articleName",new StringReader(doc.get("articleName")));
		    String articleName=highlighter.getBestFragment(tokenStream, doc.get("articleName"));
		    dto.setArticleName(articleName==null?doc.get("articleName"):articleName);
		    
		    TokenStream tokenStream2=analyzer.tokenStream("articleSummarize",new StringReader(doc.get("articleSummarize")));
		    String articleSummarize =highlighter.getBestFragment(tokenStream2, doc.get("articleSummarize"));
		    dto.setArticleSummarize(articleSummarize==null?doc.get("articleSummarize"):articleSummarize);
		    
		    TokenStream tokenStream3=analyzer.tokenStream("articleContent",new StringReader(doc.get("articleContent")));
		    String articleContent = highlighter.getBestFragment(tokenStream3, doc.get("articleContent"));
		    dto.setArticleContent(articleContent==null?"":articleContent);
		    
		    TokenStream tokenStream4=analyzer.tokenStream("articleType",new StringReader(doc.get("articleType")));
		    String articleType = highlighter.getBestFragment(tokenStream4, doc.get("articleType"));
		    dto.setType(articleType==null?doc.get("articleType"):articleType);
		    
		    TokenStream tokenStream5=analyzer.tokenStream("classification",new StringReader(doc.get("classification")));
		    String classification =highlighter.getBestFragment(tokenStream5, doc.get("classification"));
		    dto.setClassificationName(classification==null?doc.get("classification"):classification);
		    dto.setIsOriginal(Integer.valueOf(doc.get("isOriginal")));
		    dto.setTime(doc.get("dateTime"));
		    dto.setType(doc.get("type"));
		    articleList.add(dto);
		}
		
		object.put("count", count);
		JSONArray array = JSONArray.parseArray(JSON.toJSONString(articleList));
		return JSONResultUtil.successResult(ResultCodeEnum.Success.getValue(), object, array);
	}

	@Override
	public void deleteIndex(Long articleId) throws IOException {
		indexWriter.deleteDocuments(new Term("articleId",articleId.toString()));
		indexWriter.commit();
	}

	@Override
	public void addIndex(ArticleBean articleBean) throws IOException {
		List<Document> docs = new ArrayList<Document>();
		
		Document doc = new Document();
		TypeBean typeBean = typeDao.getOne(articleBean.getTypeId());
		ClassificationBean classificationBean = classificationDao.getOne(articleBean.getClassificationId());
		//StringField不会进行分词
		doc.add(new StringField("articleId", articleBean.getArticleId().toString(), Field.Store.YES));	
		doc.add(new TextField("articleName", articleBean.getArticleName(), Field.Store.YES));
		doc.add(new TextField("articleSummarize", articleBean.getArticleSummarize(), Field.Store.YES));
		doc.add(new TextField("articleContent", articleBean.getArticleContent(), Field.Store.YES));
		doc.add(new StringField("dateTime", DateFormatUtil.DateFormat(articleBean.getDatetime()), Field.Store.YES));
		doc.add(new TextField("classification", classificationBean.getClassification(), Field.Store.YES));
		doc.add(new TextField("articleType", typeBean.getArticleType(), Field.Store.YES));
		doc.add(new StringField("type", articleBean.getTypeId().toString(), Field.Store.YES));
		doc.add(new StringField("isOriginal", articleBean.getIsOriginal().toString(), Field.Store.YES));
		docs.add(doc);
		
		indexWriter.addDocuments(docs);
		indexWriter.commit();		
	}

}
