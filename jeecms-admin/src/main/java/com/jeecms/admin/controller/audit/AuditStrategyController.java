/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.audit;

import com.google.gson.internal.LinkedHashTreeMap;
import com.jeecms.admin.controller.BaseAdminController;
import com.jeecms.audit.constants.ContentAuditConstants;
import com.jeecms.audit.domain.AuditStrategy;
import com.jeecms.audit.domain.dto.AuditStrategySaveDto;
import com.jeecms.audit.domain.dto.AuditStrategySceneDto;
import com.jeecms.audit.domain.dto.AuditStrategyUpdateNameDto;
import com.jeecms.audit.service.AuditStrategyService;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

import static com.jeecms.audit.constants.ContentAuditConstants.AUDIT_PICTURE;
import static com.jeecms.audit.constants.ContentAuditConstants.AUDIT_TEXT;

/**
 * 审核策略控制层
 *
 * @author xiaohui
 * @version 1.2
 * @date 2019/12/18 9:16
 */
@RestController
@RequestMapping("/auditStrategy")
public class AuditStrategyController extends BaseAdminController<AuditStrategy, Integer> {

	@PostConstruct
	public void init() {
		String[] queryParams = {"name_LIKE"};
		super.setQueryParams(queryParams);
	}

	@Autowired
	private AuditStrategyService service;

	/**
	 * 分页
	 *
	 * @param request  {@link HttpServletRequest}
	 * @param pageable 分页组件
	 * @return ResponseInfo
	 * @throws GlobalException 全局异常
	 */
	@GetMapping("/page")
	@SerializeField(clazz = AuditStrategy.class, includes = {"id", "name", "status", "textAuditScene", "pictureAuditScene"})
	public ResponseInfo getPage(HttpServletRequest request, @PageableDefault(sort = "createTime",
		direction = Direction.DESC) Pageable pageable) throws GlobalException {
		return super.getPage(request, pageable, false);
	}

	/**
	 * 创建审核策略
	 *
	 * @param dto     新建审核策略Dto
	 * @param result  {@link BindingResult}
	 * @param request {@link HttpServletRequest}
	 * @return ResponseInfo
	 * @throws GlobalException 全局异常
	 */
	@PostMapping
	public ResponseInfo save(@RequestBody @Valid AuditStrategySaveDto dto, BindingResult result,
							 HttpServletRequest request) throws GlobalException {
		validateBindingResult(result);
		service.save(dto, SystemContextUtils.getSiteId(request));
		return new ResponseInfo(true);
	}

	/**
	 * 校验策略名称是否可用
	 *
	 * @param request {@link HttpServletRequest}
	 * @param id      策略id
	 * @param name    策略名称
	 * @return true 可用， false 不可用
	 */
	@GetMapping("/unique")
	public ResponseInfo unique(HttpServletRequest request, Integer id, String name) {
		return new ResponseInfo(service.unique(name, id, SystemContextUtils.getSiteId(request)));
	}

	/**
	 * 删除策略
	 *
	 * @param dto 删除Dto
	 * @return ResponseInfo
	 * @throws GlobalException 全局异常
	 */
	@DeleteMapping
	public ResponseInfo delete(@RequestBody @Valid DeleteDto dto) throws GlobalException {
		service.delete(dto);
		return new ResponseInfo(true);
	}

	/**
	 * 获取审核场景
	 *
	 * @param type 类型 1文本，2图片
	 * @return ResponseInfo
	 */
	@GetMapping("/scene")
	public ResponseInfo getScene(@RequestParam("type") Integer type) {
		Map<String, String> map = new LinkedHashTreeMap<>();
		if (AUDIT_TEXT.equals(type)) {
			map = ContentAuditConstants.getTextScene();
		} else if (AUDIT_PICTURE.equals(type)) {
			map = ContentAuditConstants.getPictureScene();
		}
		return new ResponseInfo(map);
	}

	/**
	 * 开启策略
	 *
	 * @param id      策略id
	 * @param request {@link HttpServletRequest}
	 * @return ResponseInfo
	 * @throws GlobalException 全局异常
	 */
	@GetMapping("/open")
	public ResponseInfo openAudit(Integer id, HttpServletRequest request) throws GlobalException {
		service.updateStatus(true, id, SystemContextUtils.getSiteId(request));
		return new ResponseInfo(true);
	}

	/**
	 * 关闭策略
	 *
	 * @param id      策略id
	 * @param request {@link HttpServletRequest}
	 * @return ResponseInfo
	 * @throws GlobalException 全局异常
	 */
	@GetMapping("/close")
	public ResponseInfo closeAudit(Integer id, HttpServletRequest request) throws GlobalException {
		service.updateStatus(false, id, SystemContextUtils.getSiteId(request));
		return new ResponseInfo(true);
	}

	/**
	 * 修改审核策略名称
	 *
	 * @param dto     审核策略修改名称Dto
	 * @param request {@link HttpServletRequest}
	 * @return ResponseInfo
	 * @throws GlobalException 全局异常
	 */
	@PutMapping("/name")
	public ResponseInfo updateName(HttpServletRequest request, @RequestBody @Valid AuditStrategyUpdateNameDto dto,
								   BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.updateName(dto.getName(), dto.getId(), SystemContextUtils.getSiteId(request));
		return new ResponseInfo(true);
	}

	/**
	 * 修改文本审核场景
	 *
	 * @param dto     修改场景Dto
	 * @param result  {@link BindingResult}
	 * @param request {@link HttpServletRequest}
	 * @return ResponseInfo
	 * @throws GlobalException 全局异常
	 */
	@PutMapping("/scene/text")
	public ResponseInfo updateTextScene(@RequestBody @Valid AuditStrategySceneDto dto, BindingResult result,
										HttpServletRequest request) throws GlobalException {
		validateBindingResult(result);
		service.updateScene(dto.getId(), AUDIT_TEXT,
			dto.getScene(), SystemContextUtils.getSiteId(request));
		return new ResponseInfo(true);
	}

	/**
	 * 修改图片审核场景
	 *
	 * @param dto     修改场景Dto
	 * @param result  {@link BindingResult}
	 * @param request {@link HttpServletRequest}
	 * @return ResponseInfo
	 * @throws GlobalException 全局异常
	 */
	@PutMapping("/scene/picture")
	public ResponseInfo updatePictureScene(@RequestBody @Valid AuditStrategySceneDto dto, BindingResult result,
										   HttpServletRequest request) throws GlobalException {
		validateBindingResult(result);
		service.updateScene(dto.getId(), AUDIT_PICTURE,
			dto.getScene(), SystemContextUtils.getSiteId(request));
		return new ResponseInfo(true);
	}
}
