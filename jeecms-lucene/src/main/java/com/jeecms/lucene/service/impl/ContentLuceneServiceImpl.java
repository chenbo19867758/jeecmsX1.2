/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.lucene.service.impl;

import com.hankcs.lucene.HanLPIndexAnalyzer;
import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.service.ChannelService;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.component.listener.ContentListener;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.constants.ContentConstant.ReleaseTimeStage;
import com.jeecms.content.constants.ContentConstant.SearchKeyCondition;
import com.jeecms.content.constants.ContentConstant.SearchPosition;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentLuceneError;
import com.jeecms.content.domain.dto.ContentLucene;
import com.jeecms.content.domain.dto.ContentSearchDto;
import com.jeecms.content.domain.vo.ContentChannelCountVo;
import com.jeecms.content.service.ContentGetService;
import com.jeecms.content.service.ContentLuceneErrorService;
import com.jeecms.content.service.ContentLuceneService;
import com.jeecms.content.service.ContentService;
import com.jeecms.lucene.domain.dto.ContentLuceneApache;
import com.jeecms.lucene.util.LuceneUtil;
import com.jeecms.system.service.SysSearchWordService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.facet.*;
import org.apache.lucene.facet.taxonomy.FastTaxonomyFacetCounts;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * 内容索引服务
 * 
 * @author: tom
 * @date: 2019年5月11日 下午7:00:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
@ConditionalOnMissingClass(value = "com.jeecms.common.configuration.EsConfig")
public class ContentLuceneServiceImpl implements ContentLuceneService, ContentListener {

	private Logger logger = LoggerFactory.getLogger(ContentLuceneServiceImpl.class);

	/**
	 * 文档索引路径
	 */
	private volatile Directory dir;
	/**
	 * 文档按栏目的分类索引路径
	 */
	private volatile Directory taxodir;
	private FacetsConfig facetsConfig = new FacetsConfig();

	@Override
	public Map<String, Object> preChange(Content content) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put(ContentListener.CONTENT_IS_DEL, content.isDelete());
		return map;
	}

	@Override
	public void afterSave(Content content) throws GlobalException {
		if (!content.isDelete()) {
			createIndex(content.getId());
		}
	}

	@Override
	public void afterChange(Content content, Map<String, Object> map) throws GlobalException {
		boolean curr = !content.isDelete();
		if (!curr) {
			delete(content.getId());
		} else {
			updateIndexById(content.getId());
		}
	}

	@Override
	public void afterDelete(List<Content> contents) throws GlobalException {
		for (Content content : contents) {
			delete(content.getId());
		}
	}

	@Override
	public void afterContentRecycle(List<Integer> contentIds) throws GlobalException {
		List<Content> contents = contentService.findAllById(contentIds);
		for (Content content : contents) {
			delete(content.getId());
		}
	}

	@Override
	public boolean createIndex(Integer id) throws GlobalException {
		Content content = contentService.findById(id);
		IndexWriter writer = null;
		DirectoryTaxonomyWriter taxoWriter = null;
		try {
			writer = getIndexWriter();
			Document doc = ContentLuceneApache.createDocument(content);
			/** 写入索引的同时写入taxo索引 */
			taxoWriter = getTaxoIndexWriter();
			doc = facetsConfig.build(taxoWriter, doc);
			writer.addDocument(doc);
			writer.commit();
			taxoWriter.commit();
		} catch (IOException e) {
			logger.error(e.getMessage());
			// 保存未更新lucene记录
			luceneErrorService.saveError(id, ContentLuceneError.OP_CREATE);
		} finally {
			if (writer != null) {
				try {
					writer.close();
					closeDir();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
			if (taxoWriter != null) {
				try {
					taxoWriter.close();
					closeTaxoDir();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return true;
	}

	@Override
	public boolean createIndexBatch(Iterable<Content> contents) throws GlobalException {
		boolean succ = true;
		IndexWriter writer = null;
		DirectoryTaxonomyWriter taxoWriter = null;
		try {
			writer = getIndexWriter();
			taxoWriter = getTaxoIndexWriter();
			for (Content content : contents) {
				if (writer != null) {
					// 采用更新更好（有则修改，没有则创建），数据量小的情况下和先判断索引是否存在再添加差不多
					Document doc = ContentLuceneApache.createDocument(content);
					doc = facetsConfig.build(taxoWriter, doc);
					/** 写入索引的同时写入taxo索引 */
					writer.updateDocument(new Term("id", content.getId().toString()), doc);
				}
			}
			if (writer != null) {
				writer.commit();
			}
		} catch (IOException e) {
			succ = false;
			logger.error(e.getMessage());
		} finally {
			if (writer != null) {
				try {
					writer.close();
					closeDir();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
			if (taxoWriter != null) {
				try {
					taxoWriter.close();
					closeTaxoDir();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return succ;
	}

	@Override
	public boolean updateIndexById(Integer id) throws GlobalException {
		Content content = contentService.findById(id);
		IndexWriter writer = null;
		DirectoryTaxonomyWriter taxoWriter = null;
		try {
			/**
			 * 先删除再更新 // ContentLuceneApache.delete(id, writer); //
			 * writer.addDocument(ContentLuceneApache.createDocument(product));
			 */
			// 直接更新
			writer = getIndexWriter();
			// 采用更新更好（有则修改，没有则创建），数据量小的情况下和先判断索引是否存在再添加差不多
			Document doc = ContentLuceneApache.createDocument(content);
			/** 写入索引的同时写入taxo索引 */
			taxoWriter = getTaxoIndexWriter();
			doc = facetsConfig.build(taxoWriter, doc);
			writer.updateDocument(new Term("id", content.getId().toString()), doc);
		} catch (IOException e) {
			logger.error(e.getMessage());
			// 保存未更新lucene记录
			luceneErrorService.saveError(id, ContentLuceneError.OP_UPDATE);
		} finally {
			if (writer != null) {
				try {
					writer.close();
					closeDir();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
			if (taxoWriter != null) {
				try {
					taxoWriter.close();
					closeTaxoDir();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return true;
	}

	/**
	 * 重新生成索引
	 * 
	 * @Title: resetIndex
	 * @param channelId
	 *            栏目ID
	 * @param siteId
	 *            站点ID
	 * @param releaseTimeStart
	 *            开始发布时间
	 * @param releaseTimeEnd
	 *            结束发布时间
	 * @throws GlobalException
	 *             GlobalException
	 * @return: boolean
	 */
	public boolean resetIndex(Integer channelId, Integer siteId, Date releaseTimeStart, Date releaseTimeEnd)
			throws GlobalException {
		IndexWriter writer = null;
		/** 重置之前删除全部索引 */
		try {
			writer = getIndexWriter();
			List<Integer> channelIds = new ArrayList<Integer>();
			if (channelId != null) {
				channelIds.add(channelId);
			}
			writer.deleteDocuments(getQuery(null, null, null, siteId, channelIds, null, null, null, null,
					releaseTimeEnd, null, null, null, null, null, null, null, null, ContentConstant.ORDER_TYPE_RELATE,
					PageRequest.of(1, Integer.MAX_VALUE)));
			writer.commit();
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (writer != null) {
				try {
					writer.close();
					closeDir();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}

		int page = 1;
		int size = ContentLucene.THREAD_PROCESS_NUM;
		Pageable pageable = PageRequest.of(page, size);
		long t1 = System.currentTimeMillis();
		Integer[] channelIds = null;
		if (channelId != null) {
			List<Integer> idList = Arrays.asList(channelId);
			channelIds = idList.toArray(new Integer[idList.size()]);
		}
		ContentSearchDto query = ContentSearchDto.buildForSearch(siteId, channelIds, releaseTimeStart, releaseTimeEnd);
		Page<Content> content = contentGetService.getPages(query, pageable);
		int totalPage = content.getTotalPages();
		long t2 = System.currentTimeMillis();
		for (int i = 0; i < totalPage; i++) {
			final int p = i;
			Pageable onePage = PageRequest.of(p, size);
			Page<Content> contentPage;
			try {
				contentPage = contentGetService.getPages(query, onePage);
				createIndexBatch(contentPage.getContent());
				logger.info("resetIndex content page ="+ p+" count " + contentPage.getSize());
			} catch (GlobalException e) {
				logger.error(e.getMessage());
			}
		}
		long t3 = System.currentTimeMillis();
		logger.info("resetIndex create lucene  times " + (t3 - t2));
		return true;
	}

	@Override
	public boolean delete(Integer id) throws GlobalException {
		IndexWriter writer = null;
		boolean succ = true;
		try {
			writer = getIndexWriter();
			ContentLuceneApache.delete(id, writer);
			writer.commit();
		} catch (IOException e) {
			logger.error(e.getMessage());
			// 保存未更新lucene记录
			luceneErrorService.saveError(id, ContentLuceneError.OP_DELETE);
			succ = false;
		} finally {
			if (writer != null) {
				try {
					writer.close();
					closeDir();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return succ;
	}

	@Override
	public boolean delete(Iterable<Integer> ids) throws GlobalException {
		IndexWriter writer = null;
		boolean succ = true;
		try {
			openDir();
			writer = getIndexWriter();
			ContentLuceneApache.deleteByIds(ids, writer);
		} catch (IOException e) {
			logger.error(e.getMessage());
			// 保存未更新lucene记录
			for (Integer id : ids) {
				luceneErrorService.saveError(id, ContentLuceneError.OP_DELETE);
			}
			succ = false;
		} finally {
			if (writer != null) {
				try {
					writer.close();
					closeDir();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return succ;
	}

	@Override
	public boolean deleteIndexBatch(List<Integer> channelIds, Integer siteId) throws GlobalException {
		IndexWriter writer = null;
		boolean succ = true;
		try {
			openDir();
			writer = getIndexWriter();
			Analyzer analyzer = new HanLPIndexAnalyzer();
			ContentLuceneApache.delete(channelIds, siteId, writer, analyzer);
		} catch (Exception e) {
			logger.error(e.getMessage());
			succ = false;
		} finally {
			if (writer != null) {
				try {
					writer.close();
					closeDir();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return succ;
	}

	private Query getQuery(String keyword, SearchPosition searchPos, SearchKeyCondition keyCondition, Integer siteId,
			List<Integer> channelIds, List<Integer> excludeChannelIds, List<Integer> typeIds, List<Integer> tagIds,
			Date releaseTimeBegin, Date releaseTimeEnd, Integer issueOrg, Integer issueYear, String issueNum,
			Boolean isTop, Boolean releasePc, Boolean releaseWap, Boolean releaseApp, Boolean releaseMiniprogram,
			int oderBy, Pageable pageable) {
		Query query = null;
		Analyzer analyzer = new HanLPIndexAnalyzer();
		/** 前台搜索只搜索发布状态的内容 */
		try {
			query = ContentLuceneApache.createQuery(keyword, false, searchPos, keyCondition, siteId, channelIds,
					excludeChannelIds, typeIds, tagIds, releaseTimeBegin, releaseTimeEnd, issueOrg, issueYear, issueNum,
					isTop, releasePc, releaseWap, releaseApp, releaseMiniprogram,
					Arrays.asList(ContentConstant.STATUS_PUBLISH), oderBy, analyzer);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		return query;
	}

	@Override
	public Page<Content> searchPage(String keyword, SearchPosition searchPos, SearchKeyCondition keyCondition,
			Integer siteId, List<Integer> channelIds, List<Integer> excludeChannelIds, List<Integer> typeIds,
			List<Integer> tagIds, ReleaseTimeStage timeStage, Date releaseTimeBegin, Date releaseTimeEnd,
			Integer issueOrg, Integer issueYear, String issueNum, Boolean isTop, Boolean releasePc, Boolean releaseWap,
			Boolean releaseApp, Boolean releaseMiniprogram, int oderBy, Pageable pageable) throws GlobalException {
		String path = realPathResolver.get(WebConstants.LUCENE_PATH);
		// 得到读取索引文件的路径
		Directory dir = null;
		// Analyzer analyzer = new SmartChineseAnalyzer(true);
		Analyzer analyzer = new HanLPIndexAnalyzer();
		Page<Integer> contentIdPage;
		List<Content> contents = new LinkedList<Content>();
		Page<Content> contentPage = new PageImpl<>(contents, pageable, 0);
		IndexReader reader = null;
		if (timeStage == null) {
			timeStage = ReleaseTimeStage.timeRage;
		}
		Date now = Calendar.getInstance().getTime();
		if (releaseTimeEnd != null) {
			releaseTimeEnd = now;
		}
		if (ReleaseTimeStage.oneDay.equals(timeStage)) {
			releaseTimeBegin = MyDateUtils.getSpecficDateStart(now, 0);
		} else if (ReleaseTimeStage.oneWeek.equals(timeStage)) {
			releaseTimeBegin = MyDateUtils.getSpecficDateStart(now, -6);
		} else if (ReleaseTimeStage.oneMonth.equals(timeStage)) {
			releaseTimeBegin = MyDateUtils.getSpecficDateStart(now, -29);
		} else if (ReleaseTimeStage.oneYear.equals(timeStage)) {
			releaseTimeBegin = MyDateUtils.getSpecficDateStart(now, -364);
		}
		try {
			dir = FSDirectory.open(Paths.get(path));
			reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			Query query = getQuery(keyword, searchPos, keyCondition, siteId, channelIds, excludeChannelIds, typeIds,
					tagIds, releaseTimeBegin, releaseTimeEnd, issueOrg, issueYear, issueNum, isTop, releasePc,
					releaseWap, releaseApp, releaseMiniprogram, oderBy, pageable);
			int hitCount = (pageable.getPageNumber() + 1) * pageable.getPageSize();
			if (hitCount == 0) {
				hitCount = ContentLucene.DEFAULT_PAGE_SIZE;
			}
			Sort sort = ContentLuceneApache.querySort(query, oderBy);
			TopDocs docs = searcher.search(query, hitCount, sort);
			contentIdPage = ContentLuceneApache.getResultIds(searcher, docs, pageable);
			List<Content> contentList = new ArrayList<Content>();
			/** 此处不可调用findAllById 会打乱索引排序的结果 */
			for (Integer id : contentIdPage.getContent()) {
				contentList.add(contentService.findById(id));
			}
			contents.addAll(contentList);
			contentPage = new PageImpl<Content>(contents, pageable, contentIdPage.getTotalElements());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				reader.close();
				dir.close();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

		}
		if (StringUtils.isNoneBlank(keyword)) {
			Integer currSiteId = SystemContextUtils.getSiteId(RequestUtils.getHttpServletRequest());
			/**
			 * 搜索词拆词后入库
			 */
			List<String> nameWord = LuceneUtil.getAnalyseResult(keyword, ContentLuceneApache.TITLE, analyzer);
			for (String w : nameWord) {
				// 分词后单个字的不存储
				if (w.length() > 1 && currSiteId != null) {
					searchWordService.saveSearchWord(currSiteId, w);
				}
			}
		}

		return contentPage;
	}

	@Override
	public List<ContentChannelCountVo> searchSummary(String keyword, SearchPosition searchPos,
			SearchKeyCondition keyCondition, Integer siteId, List<Integer> channelIds, List<Integer> excludeChannelIds,
			List<Integer> typeIds, List<Integer> tagIds, ReleaseTimeStage timeStage, Date releaseTimeBegin,
			Date releaseTimeEnd, Integer issueOrg, Integer issueYear, String issueNum, Boolean isTop, Boolean releasePc,
			Boolean releaseWap, Boolean releaseApp, Boolean releaseMiniprogram) throws GlobalException {
		// 同时还需要taxonomy reader
		String path = realPathResolver.get(WebConstants.LUCENE_TOXONOMY_PATH);
		Directory taxoDirectory;
		TaxonomyReader taxoReader = null;
		IndexReader reader = null;
		List<ContentChannelCountVo> summaryResults = new ArrayList<ContentChannelCountVo>();
		try {
			taxoDirectory = FSDirectory.open(Paths.get(path));
			taxoReader = new DirectoryTaxonomyReader(taxoDirectory);
			// 相应的Collector是必不可少的
			FacetsCollector facetsCollector = new FacetsCollector();

			String lucenePath = realPathResolver.get(WebConstants.LUCENE_PATH);
			// 得到读取索引文件的路径
			Directory dir = null;
			dir = FSDirectory.open(Paths.get(lucenePath));
			reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			Query query = getQuery(keyword, searchPos, keyCondition, siteId, channelIds, excludeChannelIds, typeIds,
					tagIds, releaseTimeBegin, releaseTimeEnd, issueOrg, issueYear, issueNum, isTop, releasePc,
					releaseWap, releaseApp, releaseMiniprogram, ContentConstant.ORDER_TYPE_RELATE,
					PageRequest.of(1, 10));
			FacetsCollector.search(searcher, query, Integer.MAX_VALUE, facetsCollector);
			Facets facets = new FastTaxonomyFacetCounts(taxoReader, facetsConfig, facetsCollector);
			List<FacetResult> results = facets.getAllDims(Integer.MAX_VALUE);
			for (FacetResult fr : results) {
				if (ContentLuceneApache.CHANNEL_IDS.equals(fr.dim)) {
					LabelAndValue[] labelValues = fr.labelValues;
					for (LabelAndValue lav : labelValues) {
						String cidStr = lav.label;
						if (StringUtils.isNoneBlank(cidStr) && StrUtils.isNumeric(cidStr)) {
							Integer cid = Integer.parseInt(cidStr);
							ContentChannelCountVo s = new ContentChannelCountVo();
							Channel c = channelService.findById(cid);
							if (c != null && !c.getHasDeleted() && !c.getRecycle()) {
								s.setChannel(c);
								s.setCount((Integer) lav.value);
								summaryResults.add(s);
							}
						}
					}
				}
			}
			/**
			 * DrillDownQuery drillDownQuery = new DrillDownQuery(facetsConfig,
			 * query); drillDownQuery.add(ContentLuceneApache.CHANNEL_IDS,);
			 */
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (taxoReader != null) {
					taxoReader.close();
				}
			} catch (Exception e2) {
				logger.error(e2.getMessage());
			}
		}
		return summaryResults;
	}

	@Override
	public Long searchCount(String keyword, SearchPosition searchPosition, Integer channelId, Integer siteId,
			boolean isTermQuery, List<Integer> status) {
		String path = realPathResolver.get(WebConstants.LUCENE_PATH);
		// 得到读取索引文件的路径
		Directory dir = null;
		// Analyzer analyzer = new SmartChineseAnalyzer(true);
		Analyzer analyzer = new HanLPIndexAnalyzer();
		IndexReader reader = null;
		TopDocs docs = null;
		try {
			dir = FSDirectory.open(Paths.get(path));
			reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			Query query = ContentLuceneApache.createQuery(keyword, isTermQuery, searchPosition, SearchKeyCondition.any,
					siteId, Arrays.asList(channelId), null, null, null, null, null, null, null, null, null, null, null,
					null, null, status, ContentConstant.ORDER_TYPE_RELATE, analyzer);
			docs = searcher.search(query, Integer.MAX_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		if (docs != null) {
			return docs.totalHits;
		} else {
			return 0L;
		}
	}

	private IndexWriter getIndexWriter() throws IOException {
		openDir();
		IndexWriter writer;
		// Analyzer analyzer = new SmartChineseAnalyzer(true);
		Analyzer analyzer = new HanLPIndexAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(dir, config);
		return writer;
	}

	private DirectoryTaxonomyWriter getTaxoIndexWriter() throws IOException {
		openTaxoDir();
		DirectoryTaxonomyWriter taxoWriter = new DirectoryTaxonomyWriter(taxodir);
		return taxoWriter;
	}

	private void closeDir() throws IOException {
		if (dir != null) {
			dir.close();
		}
	}

	private void openDir() throws IOException {
		String path = realPathResolver.get(WebConstants.LUCENE_PATH);
		if (this.dir == null) {
			// 得到读取索引文件的路径
			Directory dir = FSDirectory.open(Paths.get(path));
			synchronized (dir) {
				if (this.dir == null) {
					this.dir = dir;
				}
			}
		} else {
			dir = FSDirectory.open(Paths.get(path));
		}
	}

	private void closeTaxoDir() throws IOException {
		if (taxodir != null) {
			taxodir.close();
		}
	}

	private void openTaxoDir() throws IOException {
		if (this.taxodir == null) {
			String path = realPathResolver.get(WebConstants.LUCENE_TOXONOMY_PATH);
			// 得到读取索引文件的路径
			Directory dir = FSDirectory.open(Paths.get(path));
			synchronized (dir) {
				if (this.taxodir == null) {
					this.taxodir = dir;
				}
			}
		} else {
			String path = realPathResolver.get(WebConstants.LUCENE_TOXONOMY_PATH);
			taxodir = FSDirectory.open(Paths.get(path));
		}
	}

	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private ContentService contentService;
	@Autowired
	private ContentGetService contentGetService;
	@Autowired
	private ContentLuceneErrorService luceneErrorService;
	@Autowired
	private SysSearchWordService searchWordService;
	@Autowired
	private ChannelService channelService;

}
