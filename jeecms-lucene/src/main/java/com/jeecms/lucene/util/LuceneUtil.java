package com.jeecms.lucene.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  lucene工具类
 * @author: tom
 * @date:   2019年5月28日 上午10:57:09
 */
public class LuceneUtil {
	private static Logger logger = LoggerFactory.getLogger(LuceneUtil.class);

	/**
	 * 获取指定分词器的分词结果
	 * 
	 * @param analyzeStr
	 *            要分词的字符串
	 * 
	 * @param analyzer
	 *            分词器
	 * 
	 * @return 分词结果
	 */
	public static List<String> getAnalyseResult(String analyzeStr, String fieldName, Analyzer analyzer) {
		List<String> response = new ArrayList<String>();
		TokenStream tokenStream = null;
		try {
			tokenStream = analyzer.tokenStream(fieldName, new StringReader(analyzeStr));
			CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				response.add(attr.toString());
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (tokenStream != null) {
				try {
					tokenStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return response;
	}
}
