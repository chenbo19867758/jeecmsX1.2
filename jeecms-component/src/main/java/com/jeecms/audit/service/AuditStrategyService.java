/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.audit.service;

import com.jeecms.audit.domain.AuditStrategy;
import com.jeecms.audit.domain.dto.AuditStrategySaveDto;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;

import java.util.List;

/**
 * 审核策略Service
 *
 * @author ljw
 * @version 1.0
 * @date 2019-12-16
 */
public interface AuditStrategyService extends IBaseService<AuditStrategy, Integer> {

	/**
	 * 创建审核策略
	 *
	 * @param dto    新增审核策略Dto
	 * @param siteId 站点id
	 * @return AuditStrategy
	 * @throws GlobalException 全局异常
	 */
	AuditStrategy save(AuditStrategySaveDto dto, Integer siteId) throws GlobalException;

	/**
	 * 修改审核策略Dto
	 *
	 * @param name   审核策略名称
	 * @param id     审核策略id
	 * @param siteId 站点id
	 * @return AuditStrategy
	 * @throws GlobalException 全局异常
	 */
	AuditStrategy updateName(String name, Integer id, Integer siteId) throws GlobalException;

	/**
	 * 修改审核场景
	 *
	 * @param id     审核策略id
	 * @param type   审核场景类型
	 * @param scene  场景id
	 * @param siteId 站点id
	 * @return AuditStrategy
	 * @throws GlobalException 全局异常
	 */
	AuditStrategy updateScene(Integer id, Integer type, Integer scene, Integer siteId) throws GlobalException;

	/**
	 * 修改策略状态
	 *
	 * @param status true 开启， false 关闭
	 * @param id     策略id
	 * @param siteId 站点id
	 * @return AuditStrategy
	 * @throws GlobalException 全局异常
	 */
	AuditStrategy updateStatus(Boolean status, Integer id, Integer siteId) throws GlobalException;

	/**
	 * 校验名称是否可用
	 *
	 * @param name   策略名称
	 * @param id     策略id
	 * @param siteId 站点id
	 * @return true 可用
	 */
	boolean unique(String name, Integer id, Integer siteId);

	/**
	 * 删除策略
	 *
	 * @param deleteDto 删除Dto
	 */
	void delete(DeleteDto deleteDto) throws GlobalException;

	/**
	 * 通过栏目id获取到审核场景
	 *
	 * @param channelId 栏目id
	 * @param isText    true->文本审核场景，false->图片审核场景
	 * @Title: findByChannel
	 * @return: List
	 */
	List<Integer> findByChannel(Integer channelId, boolean isText);
}
