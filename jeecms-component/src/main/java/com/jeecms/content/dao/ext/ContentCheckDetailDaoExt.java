package com.jeecms.content.dao.ext;

import java.util.List;

import com.jeecms.content.domain.ContentCheckDetail;

/**
 * 内容审核详情dao扩展接口
 * @author: tom
 * @date:   2020年1月8日 上午10:48:16
 */
public interface ContentCheckDetailDaoExt {
	/**
	 * 获取所有未审核的内容审核标识集合
	 * @Title: getContentCheckMark
	 * @return: List
	 */
	List<ContentCheckDetail> findUnderReviews();
	
	/**
	 * 通过内容id集合查询内容审核详情集合
	 * @Title: findByContentIds
	 * @param contentIds	内容id集合
	 * @return: List
	 */
	List<ContentCheckDetail> findByContentIds(List<Integer> contentIds);
}
