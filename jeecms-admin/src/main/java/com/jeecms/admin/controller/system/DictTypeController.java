package com.jeecms.admin.controller.system;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.DictData;
import com.jeecms.system.domain.DictType;
import com.jeecms.system.service.DictTypeService;

/**
 * @Description:字典管理
 * @author: ztx
 * @date: 2018年6月20日 上午9:31:16
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RequestMapping(value = "/dictTypeManager")
@RestController
public class DictTypeController extends BaseController<DictType, Integer> {

	@PostConstruct
	public void init() {
		String[] queryParams = new String[] { "dictName_LIKE", "dictType_EQ" };
		super.setQueryParams(queryParams);
	}

	/**
	 * 分页,参数查询
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = DictType.class, excludes = { "datas" }) })
	public ResponseInfo page(HttpServletRequest request,
			@PageableDefault(sort = { "sortNum" }, direction = Direction.ASC) Pageable pageable)
					throws GlobalException {
		return super.getPage(request, pageable, false);
	}

	/**
	 * 参数查询,不分页
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = DictType.class, includes = { "id", "dictName",
			"dictType", "datas" }),
			@SerializeField(clazz = DictData.class, includes = { "id", "dictLabel", "dictCode",
					"dictTypeId","dictType" }) })
	public ResponseInfo list(HttpServletRequest request, Paginable paginable) throws GlobalException {
		paginable.setSort(new Sort(Direction.ASC, "sortNum"));
		return super.getList(request, paginable, false);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = DictType.class, excludes = { "datas" }) })
	@Override
	public ResponseInfo get(Integer id) throws GlobalException {
		return super.get(id);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@Override
	public ResponseInfo save(@RequestBody @Valid DictType dictType, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		Boolean dictTypeResult = this.ckDictType(dictType.getDictType(), null);
		if (!dictTypeResult) {
			return new ResponseInfo(SettingErrorCodeEnum.DICTTYPE_ALREADY_EXIST.getCode(),
					SettingErrorCodeEnum.DICTTYPE_ALREADY_EXIST.getDefaultMessage());
		}
		service.save(dictType);
		return new ResponseInfo();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@Override
	public ResponseInfo delete(@RequestBody @Valid DeleteDto ids, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		return super.deleteBeatch(ids.getIds());
	}

	/**
	 * 修改
	 * @Title: update
	 * @param request HttpServletRequest
	 * @param dictType 字典类型
	 * @param result BindingResult
	 * @return ResponseInfo
	 * @throws GlobalException GlobalException
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseInfo update(HttpServletRequest request, @RequestBody @Valid DictType dictType,
			BindingResult result)throws GlobalException {
		super.validateBindingResult(result);
		Boolean dictTypeResult = this.ckDictType(dictType.getDictType(), dictType.getId());
		if (!dictTypeResult) {
			return new ResponseInfo(SettingErrorCodeEnum.DICTTYPE_ALREADY_EXIST.getCode(),
					SettingErrorCodeEnum.DICTTYPE_ALREADY_EXIST.getDefaultMessage());
		}
		dictTypeService.update(dictType);
		return new ResponseInfo();
	}

	@RequestMapping(value = "/changeSys", method = RequestMethod.GET)
	public ResponseInfo changeSys(@RequestParam Integer id, @RequestParam Boolean isSystem) throws GlobalException {
		dictTypeService.changeSys(id, isSystem);
		return new ResponseInfo();
	}

	@RequestMapping(value = "/checkDictType", method = RequestMethod.GET)
	public ResponseInfo checkDictType(@RequestParam String dictType,
			@RequestParam(name = "id", required = false) Integer id) throws GlobalException {
		Boolean result = this.ckDictType(dictType, id);
		return new ResponseInfo(result);
	}

	/**
	 * 判断字典类型是否唯一
	 * @param allowMyself 是否允许自己绑定,默认为false
	 */
	private Boolean ckDictType(String dictType, Integer typeId) throws GlobalException {
		DictType dictTypeType = dictTypeService.findByDictType(dictType);
		if (typeId == null) {
			if (dictTypeType != null) {
				return false;
			}
			return true;
		}
		DictType type = dictTypeService.findById(typeId);
		if (dictTypeType != null && !dictType.equals(type.getDictType())) {
			return false;
		}
		return true;
	}

	@Autowired
	private DictTypeService dictTypeService;
}
