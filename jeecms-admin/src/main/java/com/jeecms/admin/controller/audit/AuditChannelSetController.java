/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.audit;

import com.jeecms.audit.domain.AuditChannelSet;
import com.jeecms.audit.domain.dto.AuditChannelDto;
import com.jeecms.audit.service.AuditChannelSetService;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.util.SystemContextUtils;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 审核栏目设置控制器
 * @author: ljw
 * @version: 1.0
 * @date 2019-12-16
 */
@RequestMapping("/auditchannelset")
@RestController
public class AuditChannelSetController extends BaseController<AuditChannelSet, Integer> {

	@Autowired
	private AuditChannelSetService auditChannelSetService;

	/**
	 * 栏目设置列表
	 * @param request 请求
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 响应
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@SerializeField(clazz = AuditChannelSet.class, includes = { "id", "channelId", "isCompel", "status", 
			"channelName" })
	public ResponseInfo list(HttpServletRequest request) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		return new ResponseInfo(auditChannelSetService.getList(siteId));
	}

	/**
	 * 强制通过
	 * @param: dto 传输
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 响应
	 */
	@RequestMapping(value = "/pass", method = RequestMethod.POST)
	public ResponseInfo pass(@RequestBody @Valid DeleteDto dto) throws GlobalException {
		List<AuditChannelSet> sets = auditChannelSetService.findAllById(Arrays.asList(dto.getIds()));
		for (AuditChannelSet auditChannelSet : sets) {
			auditChannelSet.setIsCompel(true);
		}
		auditChannelSetService.batchUpdate(sets);
		return new ResponseInfo();
	}
	
	/**
	 * 强制不通过
	 * @param: dto 传输
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 响应
	 */
	@RequestMapping(value = "/nopass", method = RequestMethod.POST)
	public ResponseInfo nopass(@RequestBody @Valid DeleteDto dto) throws GlobalException {
		List<AuditChannelSet> sets = auditChannelSetService.findAllById(Arrays.asList(dto.getIds()));
		for (AuditChannelSet auditChannelSet : sets) {
			auditChannelSet.setIsCompel(false);
		}
		auditChannelSetService.batchUpdate(sets);
		return new ResponseInfo();
	}
	
	/**
	 * 开启状态
	 * @param: dto 传输
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 响应
	 */
	@RequestMapping(value = "/on", method = RequestMethod.POST)
	public ResponseInfo on(@RequestBody @Valid DeleteDto dto) throws GlobalException {
		List<AuditChannelSet> sets = auditChannelSetService.findAllById(Arrays.asList(dto.getIds()));
		for (AuditChannelSet auditChannelSet : sets) {
			auditChannelSet.setStatus(true);;
		}
		auditChannelSetService.batchUpdate(sets);
		return new ResponseInfo();
	}
	
	/**
	 * 关闭状态
	 * @param: dto 传输
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 响应
	 */
	@RequestMapping(value = "/off", method = RequestMethod.POST)
	public ResponseInfo off(@RequestBody @Valid DeleteDto dto) throws GlobalException {
		List<AuditChannelSet> sets = auditChannelSetService.findAllById(Arrays.asList(dto.getIds()));
		for (AuditChannelSet auditChannelSet : sets) {
			auditChannelSet.setStatus(false);;
		}
		auditChannelSetService.batchUpdate(sets);
		return new ResponseInfo();
	}
	
	/**
	 * 选择栏目详情
	 * @param: strategyId 策略ID
	 * @throws GlobalException 异常
	 * @return: ResponseInfo 响应
	 */
	@GetMapping()
	public ResponseInfo get(HttpServletRequest request, 
			@NotNull Integer strategyId) throws GlobalException {
		Integer siteId = SystemContextUtils.getSiteId(request);
		return new ResponseInfo(auditChannelSetService.getChannels(strategyId, siteId));
	}

	/**
	 * 保存选择栏目
	 * 
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping()
	public ResponseInfo save(@RequestBody @Valid AuditChannelDto dto) throws GlobalException {
		return auditChannelSetService.saveChannelSet(dto);
	}

	/**
	 * 	删除
	 * @Title: delete
	 * @param: ids 栏目设置IDs
	 * @throws GlobalException 异常
	 * @return: ResponseInfo
	 */
	@DeleteMapping()
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dels) throws GlobalException {
		return super.deleteBeatch(dels.getIds());
	}
	
	/**
	 * 通过栏目id查询审核栏目设置看是否允许强制通过
	 */
	@GetMapping("/channelId")
	public ResponseInfo get(@RequestParam Integer channelId) {
		// 查询该内容所在的栏目是否存在策略
		AuditChannelSet auditChannelSet = auditChannelSetService.findByChannelId(channelId, false);
		if (auditChannelSet != null) {
			// 如果存在策略则直接查看按钮
			return new ResponseInfo(auditChannelSet.getIsCompel());
		}
		return new ResponseInfo(false);
	}
}
