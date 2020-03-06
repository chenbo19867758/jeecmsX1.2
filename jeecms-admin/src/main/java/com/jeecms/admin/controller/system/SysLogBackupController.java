/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysLog;
import com.jeecms.system.domain.SysLogBackup;
import com.jeecms.system.service.SysLogBackupService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 日志备份Controller
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-31 14:15:24
 */
@RequestMapping("/logBackups")
@RestController
public class SysLogBackupController extends BaseController<SysLogBackup, Integer> {

	@Autowired
	private SysLogBackupService service;

	@PostConstruct
	public void init() {
		String[] queryParams = {"[startTime_GTE_Date",
				"endTime_LTE_Date", "[beginDate,createTime]_GTE_Timestamp",
				"[endDate,createTime]_LTE_Timestamp", "backupName_LIKE_String"};
		super.setQueryParams(queryParams);
	}


	/**
	 * @Title: 列表分页
	 * @Description: TODO
	 * @param: @param request
	 * @param: @param pageable
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/page")
	@SerializeField(clazz = SysLogBackup.class, includes = {"id", "backupName", "startTime", "endTime", "remark",
			"createTime"})
	public ResponseInfo page(HttpServletRequest request, @PageableDefault(sort = "createTime",
			direction = Direction.DESC) Pageable pageable) throws GlobalException {
		return super.getPage(request, pageable, false);
	}

	/**
	 * @Title: 获取详情
	 * @Description: TODO
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/{id}")
	@Override
	@SerializeField(clazz = SysLogBackup.class, includes = {"dataCount", "backupName", "startTime", "endTime",
			"remark", "createTime"})
	public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * 校验日志备份名称是否重复
	 *
	 * @param backupName 日志备份名称
	 * @param id         日志备份id
	 * @return true 不重复 false 重复
	 */
	@GetMapping("/unique/backupName")
	public ResponseInfo check(String backupName, Integer id) {
		return new ResponseInfo(service.checkByBackupName(backupName, id));
	}

	/**
	 * @Title: 添加
	 * @Description: TODO
	 * @param: @param result
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@PostMapping("/save")
	public ResponseInfo save(@RequestBody @Valid SysLogBackup sysLogBackup,
							 HttpServletRequest request, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		CmsSite site = SystemContextUtils.getSite(request);
		return new ResponseInfo(service.backup(sysLogBackup, site));
	}

	/**
	 * 恢复日志
	 *
	 * @param id      备份id
	 * @param request HttpServletRequest
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@GetMapping(value = "/restore")
	public ResponseInfo restore(Integer id, HttpServletRequest request) throws GlobalException {
		validateId(id);
		List<SysLog> list = service.restore(id, SystemContextUtils.getSite(request));
		if (list != null && list.size() > 0) {
			return new ResponseInfo(true);
		} else {
			return new ResponseInfo(false);
		}
	}
}



