package com.jeecms.admin.controller.system;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
import com.jeecms.common.base.domain.SortDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.DictData;
import com.jeecms.system.service.DictDataService;

/**
 * 字典数据管理
 * 
 * @author: ztx
 * @date: 2018年6月20日 上午9:31:02
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@RequestMapping(value = "/dictDataManager")
@RestController
public class DictDataController extends BaseController<DictData, Integer> {

	@PostConstruct
	public void init() {
		String[] queryParams = new String[] { "dictLabel_LIKE", "dictTypeId_EQ", "dictCode_EQ" };
		super.setQueryParams(queryParams);
	}

	/**
	 * 参数查询,分页
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	@MoreSerializeField({ @SerializeField(clazz = DictData.class, excludes = { "coreDictType" }) })
	public ResponseInfo page(HttpServletRequest request,
			@PageableDefault(sort = { "sortNum" }, direction = Direction.ASC) Pageable pageable)
					throws GlobalException {
		ResponseInfo responseInfo = super.getPage(request, pageable, true);
		return responseInfo;
	}

	/**
	 * 保存排序
	 */
	@RequestMapping(value = "/sort", method = RequestMethod.POST)
	@Override
	public ResponseInfo sort(@RequestBody @Valid SortDto sort, BindingResult result) throws GlobalException {
		return super.sort(sort, result);
	}

	/**
	 * (状态)删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@Override
	public ResponseInfo delete(@RequestBody @Valid DeleteDto ids, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		service.delete(ids.getIds());
		return new ResponseInfo();
	}

	/**
	 * 详细信息
	 */
	@MoreSerializeField({ @SerializeField(clazz = DictData.class, excludes = { "coreDictType" }) })
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@Override
	public ResponseInfo get(Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@Override
	public ResponseInfo save(@RequestBody @Valid DictData dictData, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		// 如果code为空则直接插入
		if (!StringUtils.isBlank(dictData.getDictCode())) {
			Boolean dictCodeResult = this.ckDictCode(dictData.getDictCode(),
					dictData.getDictTypeId(), null);
			if (!dictCodeResult) {
				return new ResponseInfo(SettingErrorCodeEnum.DICTCODE_ALREADY_EXIST.getCode(),
						SettingErrorCodeEnum.DICTCODE_ALREADY_EXIST.getDefaultMessage());
			}
		}
		dictData = service.save(dictData);
		return new ResponseInfo();
	}

	/**
	 * 检查字典code是否存在
	 */

	@RequestMapping(value = "/checkCode", method = RequestMethod.GET)
	public ResponseInfo checkCode(@RequestParam String dictCode, @RequestParam Integer dictTypeId,
			@RequestParam(name = "id", required = false) Integer id) throws GlobalException {
		Boolean dictCodeResult = this.ckDictCode(dictCode, dictTypeId, id);
		return new ResponseInfo(dictCodeResult);
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseInfo update(HttpServletRequest request, @RequestBody @Valid DictData dictData,
			BindingResult result)throws GlobalException {
		super.validateBindingResult(result);
		// 如果code为空则直接插入
		if (!StringUtils.isBlank(dictData.getDictCode())) {
			Boolean dictCodeResult = this.ckDictCode(dictData.getDictCode(), dictData.getDictTypeId(),
					dictData.getId());
			if (!dictCodeResult) {
				return new ResponseInfo(SettingErrorCodeEnum.DICTCODE_ALREADY_EXIST.getCode(),
						SettingErrorCodeEnum.DICTCODE_ALREADY_EXIST.getDefaultMessage());
			}
		}
		service.update(dictData);
		return new ResponseInfo();
	}

	@RequestMapping(value = "/changeSys", method = RequestMethod.GET)
	public ResponseInfo changeSys(@RequestParam Integer id, @RequestParam Boolean isSystem) throws GlobalException {
		service.changeSys(id, isSystem);
		return new ResponseInfo();
	}

	/**
	 * 判断同一字典类型下的字典编码是否唯一
	 */
	private Boolean ckDictCode(String dictCode, Integer dictTypeId, Integer codeId) throws GlobalException {
		DictData dictDataCode = service.findByTypeAndCode(dictTypeId, dictCode);
		if (codeId == null) {
			if (dictDataCode != null) {
				return false;
			}
			return true;
		}
		DictData data = service.findById(codeId);
		if (dictDataCode != null && !dictCode.equals(data.getDictCode())) {
			return false;
		}
		return true;
	}

	@Autowired
	private DictDataService service;
}
