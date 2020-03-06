/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentRecord;
import com.jeecms.content.domain.dto.CheckUpdateDto;
import com.jeecms.content.domain.dto.SpliceCheckUpdateDto;
import com.jeecms.system.domain.GlobalConfig;

/**
 * 内容操作记录service接口
 * @author: chenming
 * @date:   2019年5月15日 下午5:24:44
 */
public interface ContentRecordService extends IBaseService<ContentRecord, Integer> {

	/**
	 * 获取操作记录中的备注
	 * @Title: getOpreateRemark  
	 * @param oldUpdateDto	老的
	 * @param newUpdateDto	新的
	 * @param globalConfig	全局配置
	 * @throws GlobalException   全局异常   
	 * @return: String
	 */
	String getOpreateRemark(SpliceCheckUpdateDto oldUpdateDto, SpliceCheckUpdateDto newUpdateDto,
			GlobalConfig globalConfig,List<CheckUpdateDto> dtos,List<CheckUpdateDto> newDtos,
			List<CmsModelItem> modelItems) throws GlobalException;
	
	/**
	 * 分页查询内容操作记录
	 * @Title: getPage  
	 * @param contentId	内容id
	 * @param startTime	起始时间
	 * @param endTime	截止时间
	 * @param userName	操作用户名
	 * @param pageable	分页查询信息
	 * @return: Page
	 */
	Page<ContentRecord> getPage(Integer contentId,Date startTime,Date endTime,String userName,Pageable pageable);
	
	
	void checkUpdate(SpliceCheckUpdateDto oldUpdateDto, SpliceCheckUpdateDto newUpdateDto, 
			GlobalConfig globalConfig,Content bean) throws GlobalException;
	
}
