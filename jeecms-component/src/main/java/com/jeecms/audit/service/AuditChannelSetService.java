/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.audit.service;

import com.jeecms.audit.domain.AuditChannelSet;
import com.jeecms.audit.domain.dto.AuditChannelDto;
import com.jeecms.audit.domain.vo.AuditChannelTreeVo;
import com.jeecms.audit.domain.vo.AuditChannelVo;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;

import java.util.List;

/**
 * 栏目审核Service
 * @author ljw
 * @version 1.0
 * @date 2019-12-16
 */
public interface AuditChannelSetService extends IBaseService<AuditChannelSet, Integer> {

	/**
	 * 获取栏目设置集合
	 * @Title: getList
	 * @param siteId 站点ID
	 * @throws GlobalException 异常
	 */
	List<AuditChannelVo> getList(Integer siteId) throws GlobalException;

	/**
	 * 选择栏目详情
	 * @Title: getChannels
	 * @param strategyId 策略ID
	 * @param siteId 站点ID
	 * @throws GlobalException 异常
	 */
	List<AuditChannelTreeVo> getChannels(Integer strategyId, Integer siteId) throws GlobalException;

	/**
	 * 保存选择栏目
	 * @Title: saveChannelSet
	 * @param dto 传输
	 * @throws GlobalException 异常
	 */
	ResponseInfo saveChannelSet(AuditChannelDto dto) throws GlobalException;

	/**
	 * 根据栏目ID查询策略设置
	 * @Title: findByChannelId
	 * @param channelId    栏目ID值
	 * @return: AuditChannelSet
	 */
	AuditChannelSet findByChannelId(Integer channelId,boolean checkStatus);

	/**
	 * 根据策略id查找
	 * @param strategyIds 策略id集合
	 * @return List
	 */
	List<AuditChannelSet> findByStrategyIds(Integer[] strategyIds);

}
