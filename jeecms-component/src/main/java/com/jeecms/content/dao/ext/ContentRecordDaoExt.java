package com.jeecms.content.dao.ext;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.content.domain.ContentRecord;

/**
 * 内容操作记录dao扩展接口
 * @author: chenming
 * @date:   2019年6月24日 下午3:18:28
 */
public interface ContentRecordDaoExt {
	
	/**
	 * 分页查询操作记录
	 * @Title: getPage  
	 * @param contentId	内容id
	 * @param startTime	起始时间
	 * @param endTime	截止时间
	 * @param userName	用户名称
	 * @param pageable	分页信息
	 * @return: Page
	 */
	Page<ContentRecord> getPage(Integer contentId, Date startTime, Date endTime, 
			String userName, Pageable pageable);
}
