package com.jeecms.lucene.domain.dto;

import com.jeecms.common.constants.WebConstants;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.constants.ContentConstant.SearchKeyCondition;
import com.jeecms.content.constants.ContentConstant.SearchPosition;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.dto.ContentLucene;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.SortField.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 内容索引
 * 
 * @author: tom
 * @date: 2019年5月27日 下午4:11:38
 */
public class ContentLuceneApache {

	/**
	 * 创建Lucene格式的Document
	 * 
	 * @param content
	 *            内容
	 * @return
	 */
	public static Document createDocument(Content content) {
		Document doc = new Document();
		doc.add(new StringField(ID, content.getId().toString(), Field.Store.YES));
		/** 分词存储用于前台搜索 */
		doc.add(new TextField(TITLE, content.getTitle(), Field.Store.YES));
		/** 不分词存储title字段用于后台查询是否重复 */
		doc.add(new StringField(TITLE_STR, content.getTitle(), Field.Store.YES));
		String description = content.getContentExt().getDescription();
		if (StringUtils.isBlank(description)) {
			description = "";
		}
		doc.add(new TextField(DESCRIPTION, description, Field.Store.NO));
		String issueNum = content.getContentExt().getIssueNum();
		if (StringUtils.isBlank(issueNum)) {
			issueNum = "";
		}
		doc.add(new StringField(ISSUE_NUM, issueNum, Field.Store.YES));

		Integer issueOrg = content.getContentExt().getIssueOrg();
		if (issueOrg == null) {
			issueOrg = 0;
		}
		doc.add(new StringField(ISSUE_ORG, issueOrg.toString(), Field.Store.YES));
		Integer issueYear = content.getContentExt().getIssueYear();
		if (issueYear == null) {
			issueYear = 0;
		}
		doc.add(new StringField(ISSUE_YEAR, issueYear.toString(), Field.Store.YES));

		Integer topVal = ZERO;
		if (content.getTop()) {
			topVal = ONE;
		}
		doc.add(new StringField(TOP, topVal.toString(), Field.Store.YES));
		Integer releaseAppVal = ZERO;
		if (content.getReleaseApp()) {
			releaseAppVal = ONE;
		}
		doc.add(new StringField(RELEASE_APP, releaseAppVal.toString(), Field.Store.YES));
		Integer releaseMiniprogramVal = ZERO;
		if (content.getReleaseMiniprogram()) {
			releaseMiniprogramVal = ONE;
		}
		doc.add(new StringField(RELEASE_MINIPROGRAM, releaseMiniprogramVal.toString(), Field.Store.YES));
		Integer releasePcVal = ZERO;
		if (content.getReleasePc()) {
			releasePcVal = ONE;
		}
		doc.add(new StringField(RELEASE_PC, releasePcVal.toString(), Field.Store.YES));
		Integer releaseWapVal = ZERO;
		if (content.getReleaseWap()) {
			releaseWapVal = ONE;
		}
		doc.add(new StringField(RELEASE_WAP, releaseWapVal.toString(), Field.Store.YES));

		Date releaseTime = content.getReleaseTime();
		if (releaseTime == null) {
			releaseTime = Calendar.getInstance().getTime();
		}
		long releaseTimeLong = releaseTime.getTime();
		doc.add(new LongPoint(RELEASE_TIME, releaseTimeLong));
		doc.add(new StoredField(RELEASE_TIME, releaseTimeLong));
		doc.add(new NumericDocValuesField(RELEASE_TIME, releaseTimeLong));
		doc.add(new StringField(SITE_ID, content.getSiteId().toString(), Field.Store.YES));
		doc.add(new StringField(CHANNEL_ID, content.getChannelId().toString(), Field.Store.YES));
		if (content.getTypeIds() != null) {
			for (Integer id : content.getTypeIds()) {
				doc.add(new StringField(TYPE_IDS, id.toString(), Field.Store.YES));
			}
		}
		if (content.getTagIds() != null) {
			for (Integer id : content.getTagIds()) {
				doc.add(new StringField(TAG_IDS, id.toString(), Field.Store.YES));
			}
		}
		if (content.getChannel().getNodeIdList() != null) {
			for (Integer id : content.getChannel().getNodeIdList()) {
				doc.add(new StringField(CHANNEL_IDS, id.toString(), Field.Store.YES));
			}
		}
		if (content.getTxts() != null) {
			for (String txt : content.getTxts()) {
				if (StringUtils.isNotBlank(txt)) {
					doc.add(new TextField(TXTS, txt, Field.Store.NO));
				}
			}
		}
		doc.add(new FacetField(CHANNEL_IDS, content.getChannel().getId().toString()));
		doc.add(new StringField(STATUS, content.getStatus().toString(), Field.Store.YES));
		return doc;
	}

	/**
	 * 根据Lucene doc转换 ProductLucene
	 * 
	 * @Title: getProductLuceneByDocument
	 * @param doc
	 *            Document
	 * @return: ProductLucene
	 */
	public static ContentLucene getContentLuceneByDocument(Document doc) {
		ContentLucene contentLucene = new ContentLucene();
		contentLucene.setId(Integer.valueOf(doc.getField(ID).stringValue()));
		contentLucene.setIssueNum(doc.getField(ISSUE_NUM).stringValue());
		Number issueOrg = doc.getField(ISSUE_ORG).numericValue();
		contentLucene.setIssueOrg((Integer) issueOrg);
		Number issueYear = doc.getField(ISSUE_YEAR).numericValue();
		contentLucene.setIssueYear((Integer) issueYear);
		Number releaseApp = doc.getField(RELEASE_APP).numericValue();
		contentLucene.setReleaseApp((Integer) releaseApp);
		Number releaseMiniprogram = doc.getField(RELEASE_MINIPROGRAM).numericValue();
		contentLucene.setReleaseMiniprogram((Integer) releaseMiniprogram);
		Number releasePc = doc.getField(RELEASE_PC).numericValue();
		contentLucene.setReleasePc((Integer) releasePc);
		Number releaseWap = doc.getField(RELEASE_WAP).numericValue();
		contentLucene.setReleaseWap((Integer) releaseWap);
		Number releaseTime = doc.getField(RELEASE_TIME).numericValue();
		if (releaseTime != null) {
			contentLucene.setReleaseTime(new Date((long) releaseTime));
		}
		contentLucene.setSiteId(Integer.parseInt(doc.getField(SITE_ID).stringValue()));
		contentLucene.setChannelId(Integer.parseInt(doc.getField(CHANNEL_ID).stringValue()));
		contentLucene.setTitle(doc.getField(TITLE).stringValue());
		Number top = doc.getField(TOP).numericValue();
		contentLucene.setTop((Integer) top);
		IndexableField[] typeIdField = doc.getFields(TYPE_IDS);
		List<Integer> typeIds = new ArrayList<Integer>();
		if (typeIdField != null) {
			for (IndexableField f : typeIdField) {
				typeIds.add(Integer.parseInt(f.stringValue()));
			}
			contentLucene.setTypeIds(typeIds);
		}

		IndexableField[] tagIdField = doc.getFields(TAG_IDS);
		List<Integer> tagIds = new ArrayList<Integer>();
		if (tagIdField != null) {
			for (IndexableField f : tagIdField) {
				tagIds.add(Integer.parseInt(f.stringValue()));
			}
			contentLucene.setTagIds(tagIds);
		}

		IndexableField[] channelIdField = doc.getFields(CHANNEL_IDS);
		List<Integer> channelIds = new ArrayList<Integer>();
		if (channelIdField != null) {
			for (IndexableField f : channelIdField) {
				channelIds.add(Integer.parseInt(f.stringValue()));
			}
			contentLucene.setChannelIds(channelIds);
		}

		/**
		 * IndexableField[] txtField = doc.getFields(TXTS); List<String> txts =
		 * new ArrayList<String>(); if (txtField != null) { for (IndexableField
		 * f : txtField) { txts.add(f.stringValue()); }
		 * contentLucene.setTxts(txts); }
		 * contentLucene.setDescription(doc.getField(DESCRIPTION).stringValue())
		 * ;
		 */
		return contentLucene;
	}

	/**
	 * 根据ID集合查询索引
	 * 
	 * @Title: createQueryByIds
	 * @param ids
	 *            id集合
	 * @return: Query
	 */
	public static Query createQueryByIds(Iterable<Integer> ids) {
		Builder queryBuild = new BooleanQuery.Builder();
		if (ids != null) {
			for (Integer id : ids) {
				Query query = new TermQuery(new Term(ID, id.toString()));
				BooleanClause q = new BooleanClause(query, Occur.SHOULD);
				queryBuild.add(q);
			}
		}
		return queryBuild.build();
	}

	/**
	 * 创建lucene查询
	 * 
	 * @Title: createQuery
	 * @param keyword
	 *            关键词
	 * @param keyIsTermQuery
	 *            关键词 是否完全匹配
	 * @param searchPos
	 *            索引位置 标题、正文、标题或者正文
	 * @param siteId
	 *            站点ID
	 * @param channelIds
	 *            栏目ID
	 * @param excludeChannelIds
	 *            不包含的栏目ID
	 * @param typeIds
	 *            类型Id
	 * @param tagIds
	 *            tagId
	 * @param releaseTimeBegin
	 *            发布时间开始
	 * @param releaseTimeEnd
	 *            发布时间结束
	 * @param issueOrg
	 *            发文字号的机关代号
	 * @param issueYear
	 *            发文字号的年份
	 * @param issueNum
	 *            发文字号的顺序号
	 * @param isTop
	 *            是否置顶
	 * @param releasePc
	 *            是否支持PC通道
	 * @param releaseWap
	 *            是否支持手机通道
	 * @param releaseApp
	 *            是否支持app通道
	 * @param releaseMiniprogram
	 *            是否支持小程序通道
	 * @param status
	 *            內容状态 为空则忽略内容状态
	 * @param oderBy
	 *            排序
	 * @param analyzer
	 *            Analyzer
	 * @throws ParseException
	 *             ParseException
	 * @return: Query
	 */
	public static Query createQuery(String keyword, Boolean keyIsTermQuery, SearchPosition searchPos,
			SearchKeyCondition keyCondition, Integer siteId, List<Integer> channelIds, List<Integer> excludeChannelIds,
			List<Integer> typeIds, List<Integer> tagIds, Date releaseTimeBegin, Date releaseTimeEnd, Integer issueOrg,
			Integer issueYear, String issueNum, Boolean isTop, Boolean releasePc, Boolean releaseWap,
			Boolean releaseApp, Boolean releaseMiniprogram, List<Integer> status, int oderBy, Analyzer analyzer)
					throws ParseException {
		Builder queryBuild = createQueryBuild(keyword, keyIsTermQuery, searchPos, keyCondition, siteId, channelIds,
				excludeChannelIds, typeIds, tagIds, releaseTimeBegin, releaseTimeEnd, issueOrg, issueYear, issueNum,
				isTop, releasePc, releaseWap, releaseApp, releaseMiniprogram, status, analyzer);
		BooleanQuery bq = queryBuild.build();
		return bq;
	}

	/**
	 * 创建 Builder
	 * 
	 * @Title: createQueryBuild
	 * @param keyword
	 *            关键词
	 * @param searchPos
	 *            索引位置 标题、正文、标题或者正文
	 * @param siteId
	 *            站点ID
	 * @param channelIds
	 *            栏目ID
	 * @param excludeChannelIds
	 *            不包含的栏目ID
	 * @param typeIds
	 *            类型Id
	 * @param tagIds
	 *            tagId
	 * @param releaseTimeBegin
	 *            发布时间开始
	 * @param releaseTimeEnd
	 *            发布时间结束
	 * @param issueOrg
	 *            发文字号的机关代号
	 * @param issueYear
	 *            发文字号的年份
	 * @param issueNum
	 *            发文字号的顺序号
	 * @param isTop
	 *            是否置顶
	 * @param releasePc
	 *            是否支持PC通道
	 * @param releaseWap
	 *            是否支持手机通道
	 * @param releaseApp
	 *            是否支持app通道
	 * @param releaseMiniprogram
	 *            是否支持小程序通道
	 * @param status
	 *            內容状态 为空则忽略内容状态
	 * @param analyzer
	 *            Analyzer
	 * @throws ParseException
	 *             ParseException
	 * @return: Builder
	 */
	public static Builder createQueryBuild(String keyword, Boolean keyIsTermQuery, SearchPosition searchPos,
			SearchKeyCondition keyCondition, Integer siteId, List<Integer> channelIds, List<Integer> excludeChannelIds,
			List<Integer> typeIds, List<Integer> tagIds, Date releaseTimeBegin, Date releaseTimeEnd, Integer issueOrg,
			Integer issueYear, String issueNum, Boolean isTop, Boolean releasePc, Boolean releaseWap,
			Boolean releaseApp, Boolean releaseMiniprogram, List<Integer> status, Analyzer analyzer)
					throws ParseException {
		Builder queryBuild = new BooleanQuery.Builder();
		if (!StringUtils.isBlank(keyword)) {
			if (keyIsTermQuery != null && keyIsTermQuery) {
				/** 目前精准查询只用于标题 */
				if (SearchPosition.title.equals(searchPos)) {
					Query query = new TermQuery(new Term(TITLE_STR, keyword.toString()));
					BooleanClause q = new BooleanClause(query, Occur.MUST);
					queryBuild.add(q);
				}
			} else {
				String[] fields = new String[] { TITLE, TXTS };
				Occur keyOccur = Occur.SHOULD;
				if (keyCondition != null) {
					if (keyCondition.equals(SearchKeyCondition.all)) {
						keyOccur = Occur.MUST;
					} else if (keyCondition.equals(SearchKeyCondition.notInclude)) {
						keyOccur = Occur.MUST_NOT;
					}
				}
				BooleanClause.Occur[] flags = new BooleanClause.Occur[] { BooleanClause.Occur.SHOULD,
						BooleanClause.Occur.SHOULD };
				if (SearchPosition.title.equals(searchPos)) {
					flags = new BooleanClause.Occur[] { BooleanClause.Occur.SHOULD };
					fields = new String[] { TITLE };
				} else if (SearchPosition.txt.equals(searchPos)) {
					flags = new BooleanClause.Occur[] { BooleanClause.Occur.SHOULD };
					fields = new String[] { TXTS };
				}
				if (StringUtils.isNotBlank(keyword)) {
					if (keyCondition.equals(SearchKeyCondition.any)) {
						Query query = MultiFieldQueryParser.parse(keyword, fields, flags, analyzer);
						BooleanClause q = new BooleanClause(query, Occur.MUST);
						queryBuild.add(q);
					} else {
						String[] keywords = keyword.split(WebConstants.ARRAY_SPT);
						for (String key : keywords) {
							if (!StringUtils.isBlank(key)) {
								Query query = MultiFieldQueryParser.parse(key, fields, flags, analyzer);
								BooleanClause q = new BooleanClause(query, keyOccur);
								queryBuild.add(q);
							}
						}
					}
				}
			}
		}
		if (siteId != null) {
			Query query = new TermQuery(new Term(SITE_ID, siteId.toString()));
			BooleanClause q = new BooleanClause(query, Occur.MUST);
			queryBuild.add(q);
		}
		if (channelIds != null) {
			channelIds = channelIds.stream().filter(c -> c != null).collect(Collectors.toList());
			if (channelIds.size() > 0) {
				BooleanQuery.Builder builder = new BooleanQuery.Builder();
				for (Integer id : channelIds) {
					Query q = new TermQuery(new Term(CHANNEL_IDS, id.toString()));
					builder.add(q, Occur.SHOULD);
				}
				BooleanQuery booleanQuery = builder.build();
				BooleanClause bq = new BooleanClause(booleanQuery, Occur.MUST);
				queryBuild.add(bq);
			}
		}
		if (excludeChannelIds != null && excludeChannelIds.size() > 0) {
			BooleanQuery.Builder builder = new BooleanQuery.Builder();
			for (Integer id : excludeChannelIds) {
				Query q = new TermQuery(new Term(CHANNEL_ID, id.toString()));
				builder.add(q, Occur.SHOULD);
			}
			BooleanQuery booleanQuery = builder.build();
			BooleanClause bq = new BooleanClause(booleanQuery, Occur.MUST_NOT);
			queryBuild.add(bq);
		}
		if (typeIds != null && typeIds.size() > 0) {
			BooleanQuery.Builder builder = new BooleanQuery.Builder();
			for (Integer id : typeIds) {
				Query q = new TermQuery(new Term(TYPE_IDS, id.toString()));
				builder.add(q, Occur.SHOULD);
			}
			BooleanQuery booleanQuery = builder.build();
			BooleanClause bq = new BooleanClause(booleanQuery, Occur.MUST);
			queryBuild.add(bq);
		}
		if (tagIds != null && tagIds.size() > 0) {
			BooleanQuery.Builder builder = new BooleanQuery.Builder();
			for (Integer id : tagIds) {
				Query q = new TermQuery(new Term(TAG_IDS, id.toString()));
				builder.add(q, Occur.SHOULD);
			}
			BooleanQuery booleanQuery = builder.build();
			BooleanClause bq = new BooleanClause(booleanQuery, Occur.MUST);
			queryBuild.add(bq);
		}
		if (releaseTimeBegin != null) {
			Query q = LongPoint.newRangeQuery(RELEASE_TIME, releaseTimeBegin.getTime(), Long.MAX_VALUE);
			BooleanClause bq = new BooleanClause(q, Occur.MUST);
			queryBuild.add(bq);
		}
		if (releaseTimeEnd != null) {
			Query q = LongPoint.newRangeQuery(RELEASE_TIME, Long.MIN_VALUE, releaseTimeEnd.getTime());
			BooleanClause bq = new BooleanClause(q, Occur.MUST);
			queryBuild.add(bq);
		}
		if (issueOrg != null) {
			Query query = new TermQuery(new Term(ISSUE_ORG, issueOrg.toString()));
			BooleanClause q = new BooleanClause(query, Occur.MUST);
			queryBuild.add(q);
		}
		if (issueYear != null) {
			Query q = new TermQuery(new Term(ISSUE_YEAR, issueYear.toString()));
			BooleanClause bq = new BooleanClause(q, Occur.MUST);
			queryBuild.add(bq);
		}
		if (issueNum != null) {
			Query q = new TermQuery(new Term(ISSUE_NUM, issueNum));
			BooleanClause bq = new BooleanClause(q, Occur.MUST);
			queryBuild.add(bq);
		}
		if (isTop != null) {
			Query q;
			if (isTop) {
				q = new TermQuery(new Term(TOP, ONE.toString()));
			} else {
				q = new TermQuery(new Term(TOP, ZERO.toString()));
			}
			BooleanClause bq = new BooleanClause(q, Occur.MUST);
			queryBuild.add(bq);
		}
		if (releaseApp != null) {
			Query q;
			if (releaseApp) {
				q = new TermQuery(new Term(RELEASE_APP, ONE.toString()));
			} else {
				q = new TermQuery(new Term(RELEASE_APP, ZERO.toString()));
			}
			BooleanClause bq = new BooleanClause(q, Occur.MUST);
			queryBuild.add(bq);
		}
		if (releasePc != null) {
			Query q;
			if (releasePc) {
				q = new TermQuery(new Term(RELEASE_PC, ONE.toString()));
			} else {
				q = new TermQuery(new Term(RELEASE_PC, ZERO.toString()));
			}
			BooleanClause bq = new BooleanClause(q, Occur.MUST);
			queryBuild.add(bq);
		}
		if (releaseMiniprogram != null) {
			Query q;
			if (releaseMiniprogram) {
				q = new TermQuery(new Term(RELEASE_MINIPROGRAM, ONE.toString()));
			} else {
				q = new TermQuery(new Term(RELEASE_MINIPROGRAM, ZERO.toString()));
			}
			BooleanClause bq = new BooleanClause(q, Occur.MUST);
			queryBuild.add(bq);
		}
		if (releaseWap != null) {
			Query q;
			if (releaseWap) {
				q = new TermQuery(new Term(RELEASE_WAP, ONE.toString()));
			} else {
				q = new TermQuery(new Term(RELEASE_WAP, ZERO.toString()));
			}
			BooleanClause bq = new BooleanClause(q, Occur.MUST);
			queryBuild.add(bq);
		}
		if (status != null && status.size() > 0) {
			BooleanQuery.Builder builder = new BooleanQuery.Builder();
			for (Integer statu : status) {
				Query q = new TermQuery(new Term(STATUS, statu.toString()));
				builder.add(q, Occur.SHOULD);
			}
			BooleanQuery booleanQuery = builder.build();
			BooleanClause bq = new BooleanClause(booleanQuery, Occur.MUST);
			queryBuild.add(bq);
		}
		return queryBuild;
	}

	/**
	 * 应用Sort排序
	 * 
	 * @Title: querySort
	 * @param oderBy
	 * @return: Sort
	 */
	public static Sort querySort(Query query, int oderBy) {
		Sort sort = new Sort();
		if (oderBy == ContentConstant.ORDER_TYPE_RELEASE_TIME_DESC) {
			/** 发布时间降序 */
			sort.setSort(new SortField(RELEASE_TIME, Type.LONG, true));
		} else if (oderBy == ContentConstant.ORDER_TYPE_RELEASE_TIME_ASC) {
			/** 发布时间升序 */
			sort.setSort(new SortField(RELEASE_TIME, Type.LONG, false));
		} else {
			/** 默认相关度排序 */
			sort = Sort.RELEVANCE;
		}
		return sort;
	}

	/**
	 * 删除lucene索引
	 * 
	 * @Title: delete
	 * @param channelIds
	 *            栏目
	 * @param siteId
	 *            站点ID
	 * @param writer
	 *            IndexWriter
	 * @param analyzer
	 *            Analyzer
	 * @throws CorruptIndexException
	 *             CorruptIndexException
	 * @throws IOException
	 *             IOException
	 * @throws ParseException
	 *             ParseException
	 * @return: void
	 */
	public static void delete(List<Integer> channelIds, Integer siteId, IndexWriter writer, Analyzer analyzer)
			throws CorruptIndexException, IOException, ParseException {
		writer.deleteDocuments(createQuery(null, false, null, null, siteId, channelIds, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, 1, analyzer));
		writer.commit();
	}

	/**
	 * 删除lucene索引 根据ID
	 * 
	 * @Title: delete
	 * @param id
	 *            数据ID
	 * @param writer
	 *            IndexWriter
	 * @throws CorruptIndexException
	 *             CorruptIndexException
	 * @throws IOException
	 *             IOException
	 * @return: void
	 */
	public static void delete(Integer id, IndexWriter writer) throws CorruptIndexException, IOException {
		writer.deleteDocuments(new Term(ID, id.toString()));
		writer.commit();
	}

	/**
	 * 删除lucene索引 根据ID
	 * 
	 * @Title: delete
	 * @param ids
	 *            id集合
	 * @param writer
	 *            IndexWriter
	 * @throws CorruptIndexException
	 *             CorruptIndexException
	 * @throws IOException
	 *             IOException
	 * @return: void
	 */
	public static void deleteByIds(Iterable<Integer> ids, IndexWriter writer)
			throws CorruptIndexException, IOException {
		writer.deleteDocuments(createQueryByIds(ids));
		writer.commit();
	}

	/**
	 * 获取内容ID分页
	 * 
	 * @Title: getResultPage
	 * @param searcher
	 *            IndexSearcher
	 * @param docs
	 *            内容检索集合
	 * @param pageable
	 *            Pageable
	 * @throws CorruptIndexException
	 *             CorruptIndexException
	 * @throws IOException
	 *             IOException
	 * @return: Page
	 */
	public static Page<ContentLucene> getResultPage(IndexSearcher searcher, TopDocs docs, Pageable pageable)
			throws CorruptIndexException, IOException {
		if (pageable == null) {
			pageable = PageRequest.of(0, ContentLucene.DEFAULT_PAGE_SIZE);
		}
		int endIndex;
		int beginIndex;
		beginIndex = pageable.getPageNumber() * pageable.getPageSize();
		endIndex = (pageable.getPageNumber() + 1) * pageable.getPageSize();
		if (beginIndex < 0) {
			beginIndex = 0;
		}
		ScoreDoc[] hits = docs.scoreDocs;
		int len = hits.length;
		List<ContentLucene> list = new ArrayList<ContentLucene>(pageable.getPageSize());
		if (beginIndex > len) {
			return new PageImpl<>(list, pageable, docs.totalHits);
		}
		if (endIndex > len || endIndex == 0) {
			endIndex = len;
		}
		for (int i = beginIndex; i < endIndex; i++) {
			ScoreDoc scoreDoc = hits[i];
			int docId = scoreDoc.doc;
			Document d = searcher.doc(docId);
			list.add(getContentLuceneByDocument(d));
		}
		Page<ContentLucene> page = new PageImpl<>(list, pageable, docs.totalHits);
		return page;
	}

	public static Page<Integer> getResultIds(IndexSearcher searcher, TopDocs docs, Pageable pageable)
			throws CorruptIndexException, IOException {
		if (pageable == null) {
			pageable = PageRequest.of(0, ContentLucene.DEFAULT_PAGE_SIZE);
		}
		int endIndex;
		int beginIndex;
		beginIndex = pageable.getPageNumber() * pageable.getPageSize();
		endIndex = (pageable.getPageNumber() + 1) * pageable.getPageSize();
		if (beginIndex < 0) {
			beginIndex = 0;
		}
		ScoreDoc[] hits = docs.scoreDocs;
		int len = hits.length;
		List<Integer> list = new LinkedList<Integer>();
		if (beginIndex > len) {
			return new PageImpl<>(list, pageable, docs.totalHits);
		}
		if (endIndex > len || endIndex == 0) {
			endIndex = len;
		}
		for (int i = beginIndex; i < endIndex; i++) {
			ScoreDoc scoreDoc = hits[i];
			int docId = scoreDoc.doc;
			Document d = searcher.doc(docId);
			list.add(Integer.valueOf(d.getField(ID).stringValue()));
		}
		Page<Integer> page = new PageImpl<Integer>(list, pageable, docs.totalHits);
		return page;
	}

	/**
	 * 查询索引的ID集合
	 * 
	 * @Title: getResultList
	 * @param searcher
	 *            IndexSearcher
	 * @param docs
	 *            商品检索集合
	 * @param first
	 *            第一条
	 * @param max
	 *            取多少条
	 * @throws CorruptIndexException
	 *             CorruptIndexException
	 * @throws IOException
	 *             IOException
	 * @return: List
	 */
	public static List<Integer> getResultList(IndexSearcher searcher, TopDocs docs, int first, int max)
			throws CorruptIndexException, IOException {
		List<Integer> list = new ArrayList<Integer>(max);
		ScoreDoc[] hits = docs.scoreDocs;
		if (first < 0) {
			first = 0;
		}
		if (max < 0) {
			max = 0;
		}
		int last = first + max;
		int len = hits.length;
		if (last > len) {
			last = len;
		}
		for (int i = first; i < last; i++) {
			Document d = searcher.doc(hits[i].doc);
			list.add(Integer.valueOf(d.getField(ID).stringValue()));
		}
		return list;
	}

	public static final Integer ZERO = 0;
	public static final Integer ONE = 1;

	/**
	 * 内容ID
	 */
	public static final String ID = "id";
	/**
	 * 站点ID
	 */
	public static final String SITE_ID = "siteId";
	/**
	 * 栏目ID
	 */
	public static final String CHANNEL_ID = "channelId";
	/**
	 * 栏目ID以及上级栏目ID
	 */
	public static final String CHANNEL_IDS = "channelIds";
	/**
	 * tagId
	 */
	public static final String TAG_IDS = "tagIds";
	/**
	 * 内容类型ID
	 */
	public static final String TYPE_IDS = "typeIds";
	/**
	 * 标题 -TextField (分词存储)
	 */
	public static final String TITLE = "title";
	/**
	 * 标题 StringField(不分词存储)
	 */
	public static final String TITLE_STR = "titleStr";
	/**
	 * 发布时间
	 */
	public static final String RELEASE_TIME = "releaseTime";
	/**
	 * 富文本
	 */
	public static final String TXTS = "txts";
	/**
	 * 描述
	 */
	public static final String DESCRIPTION = "description";
	/**
	 * 是否置顶
	 */
	public static final String TOP = "top";
	/**
	 * 发文字号-机关代字
	 */
	public static final String ISSUE_ORG = "issueOrg";
	/**
	 * 发文字号-年份
	 */
	public static final String ISSUE_YEAR = "issueYear";
	/**
	 * 发文字号-顺序号
	 */
	public static final String ISSUE_NUM = "issueNum";
	/**
	 * 是否发布至pc（0-否 1-是）
	 */
	public static final String RELEASE_PC = "releasePc";
	/**
	 * 是否发布至wap（0-否 1-是）
	 */
	public static final String RELEASE_WAP = "releaseWap";
	/**
	 * 是否发布至app（0-否 1-是）
	 */
	public static final String RELEASE_APP = "releaseApp";
	/**
	 * 是否发布至小程序（0-否 1-是）
	 */
	public static final String RELEASE_MINIPROGRAM = "releaseMiniprogram";
	/**
	 * 内容状态
	 */
	public static final String STATUS = "status";

}
