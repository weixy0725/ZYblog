package com.zhiyu.blog.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.ControlledRealTimeReopenThread;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Lucene 配置类
 * 
 * @author xinyuan.wei
 * @date 2019年8月20日
 */
@Configuration
public class LuceneConfig {
	
	/**
	 * lucene索引,存放位置
	 */
	@Value("${lucene.path}")
	private String lucenePath;
	
	/**
	 * 创建一个 Analyzer实例,使用中文分词
	 * 
	 * @return
	 */
	@Bean
	public Analyzer analyzer() {
		return new SmartChineseAnalyzer();
	}

	/**
	 * 索引位置
	 * 
	 * @return
	 * @throws IOException
	 */
	@Bean
	public Directory directory() throws IOException {
		
		Path path = Paths.get(lucenePath);
		File file = path.toFile();
		if(!file.exists()) {
			//如果文件夹不存在,则创建
			file.mkdirs();
		}
		return FSDirectory.open(path);
	}
	
	/**
	 * 创建indexWriter
	 * 
	 * @param directory
	 * @param analyzer
	 * @return
	 * @throws IOException
	 */
	@Bean
	public IndexWriter indexWriter(Directory directory, Analyzer analyzer) throws IOException {
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		// 清空索引
		indexWriter.deleteAll();
		indexWriter.commit();
		return indexWriter;
	}

	/**
	 * SearcherManager管理
	 * 
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "resource", "rawtypes", "unchecked" })
	@Bean
	public SearcherManager searcherManager(Directory directory, IndexWriter indexWriter) throws IOException {
		SearcherManager searcherManager = new SearcherManager(indexWriter, false, false, new SearcherFactory());
		/*如果想获得最新的searcher，就需要周期性的调用maybeRefresh来更新searcher引用。
		 * Lucene提供了ControlledRealTimeReopenThread线程工具类来负责周期性的打开ReferenceManager(调用ReferenceManager.maybeRefresh)
		 * 如何判断是否有人等待？
         * TrackingIndexWriter封装了IndexWriter，为每次更新索引的操作赋予一个标记(generation,代数)，递增变化。用户使用ControlledRealTimeReopenThread.waitForGeneration告诉其期望获得更新代数，ControlledRealTimeReopenThread记录了当前已打开的代数，当期望更新代数大于已打开代数时，就表示有用户期望获得最新Search。
         * 如果想利用TrackingIndexWriter提供的代数概念，每次使用IndexWriter都要经过TrackingIndexWriter，否则代数没意义。
         * 但是如果只希望每隔10s打开一次新searcher，并不在意某次更新快速看到，那就可以忽略TrackingIndexWriter，ControlledRealTimeReopenThread就退化为一个clock线程。
		 * */
		ControlledRealTimeReopenThread cRTReopenThead = new ControlledRealTimeReopenThread(indexWriter, searcherManager,
				10.0, 0.5);
		//设置为守护线程
		cRTReopenThead.setDaemon(true);
		//线程名称
		cRTReopenThead.setName("更新IndexReader线程");
		// 开启线程
		cRTReopenThead.start();
		return searcherManager;
	}

}
