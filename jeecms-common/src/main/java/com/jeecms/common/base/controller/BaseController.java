package com.jeecms.common.base.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;

import com.jeecms.common.base.domain.AbstractIdDomain;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.base.domain.SortDto;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.constants.ServerModeEnum;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.IllegalParamExceptionInfo;
import com.jeecms.common.exception.IllegalParamValidationUtils;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.page.Paginable;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.util.RequestUtils;

/**
 * Class controller层
 * 
 * @author: tom
 * @date: 2018年1月24日 下午15:40:30
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BaseController<T extends AbstractIdDomain<ID>, ID extends Serializable> {

	/** 
	 * 动态查询条件约束 
	 */
	private String[] queryParams;
	/**
	 * 动态查询条件
	*/
	private Map<String, String[]> searchParams;
	
	/**
	 * 禁止自动映射的字段
	 * 初始化值只有 hasDeleted逻辑删除字段
	 */
	private String[] disAllowedFields = new String[]{"hasDeleted"};
	
	@Autowired
	protected IBaseService<T, ID> service;
	@Value("${spring.profiles.active}")
	private String serverMode;

	/**
	 * 查询分页列表（jpa）
	 * 
	 * @Title: getPage
	 * @Description:
	 * @param cacheable
	 * 
	 * @param: @param
	 *             request
	 * @param: @param
	 *             pageable
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	protected ResponseInfo getPage(HttpServletRequest request, Pageable pageable, boolean cacheable)
			throws GlobalException {
		Page<T> page = service.getPage(getCommonParams(request), pageable, cacheable);
		return new ResponseInfo(page);
	}
	

	/**
	 * 查询列表（jpa）,paginable为空则查询所有
	 * 
	 * @Title: getList
	 * @Description:
	 * @param cacheable
	 * 
	 * @param: @param
	 *             request
	 * @param: @param
	 *             paginable
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	protected ResponseInfo getList(HttpServletRequest request, Paginable paginable, boolean cacheable)
			throws GlobalException {
		List<T> list = service.getList(getCommonParams(request), paginable, cacheable);
		return new ResponseInfo(list);
	}

	/**
	  查询列表（jpa）查询所有
	 * @param: @param
	 *             request
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	protected List<T> getList(HttpServletRequest request) throws GlobalException {
		return service.getList(getCommonParams(request), null, false);
	}

	/**
	 * ID获取对象
	 * 
	 * @Title: get
	 * @Description:
	 * @param request
	 * 
	 * @param: @param
	 *             id
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	protected ResponseInfo get(@PathVariable ID id) throws GlobalException {
		if (id == null) {
			throw new GlobalException(new IllegalParamExceptionInfo());
		}
		T obj = service.findById(id);
		return new ResponseInfo(obj);
	}

	protected ResponseInfo findAllById(Iterable<ID> ids) throws GlobalException {
		if (ids == null || !ids.iterator().hasNext()) {
			throw new GlobalException(new IllegalParamExceptionInfo());
		}
		List<T> list = service.findAllById(ids);
		return new ResponseInfo(list);
	}

	/**
	 * 根据条件查询条数（jpa动态查询）
	 * 
	 * @Title: count
	 * @Description:
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: ResponseInfo
	 */
	protected ResponseInfo count(HttpServletRequest request) {
		return new ResponseInfo(service.count(getCommonParams(request)));
	}

	/**
	 * 保存对象
	 * 
	 * @Title: save
	 * @Description:
	 * @param: @param
	 *             entity
	 * @param: @param
	 *             result
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	protected ResponseInfo save(T entity, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.save(entity);
		return new ResponseInfo();
	}

	/**
	 * 批量保存对象
	 * 
	 * @Title: saveInBatch
	 * @Description:
	 * @param: @param
	 *             entities
	 * @param: @param
	 *             result
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	protected ResponseInfo saveInBatch(List<T> entities, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.saveAll(entities);
		return new ResponseInfo();
	}

	/**
	 * 对象更新
	 * 
	 * @Title: update
	 * @Description:
	 * @param request
	 * 
	 * @param: @param
	 *             entity
	 * @param: @param
	 *             result
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	protected ResponseInfo update(T entity, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.update(entity);
		return new ResponseInfo();
	}

	protected ResponseInfo updateAll(T entity, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.updateAll(entity);
		return new ResponseInfo();
	}

	protected ResponseInfo updateInBatch(List<T> entities, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.batchUpdate(entities);
		return new ResponseInfo();
	}

	/**
	 * 批量逻辑删除
	 * 
	 * @Title: delete
	 * @Description:
	 * @param request
	 * 
	 * @param: @param
	 *             ids id数组
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	protected ResponseInfo deleteBeatch(ID[] ids) throws GlobalException {
		if (ids == null || ids.length <= 0) {
			throw new GlobalException(new IllegalParamExceptionInfo());
		}
		service.delete(ids);
		return new ResponseInfo();
	}

	/**
	 * 根据ID逻辑删除
	 * 
	 * @Title: delete
	 * @Description:
	 * @param request
	 * 
	 * @param: @param
	 *             ids id数组
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	protected ResponseInfo delete(ID id) throws GlobalException {
		if (id == null) {
			throw new GlobalException(new IllegalParamExceptionInfo());
		}
		service.delete(id);
		return new ResponseInfo();
	}

	@SuppressWarnings("unchecked")
	protected ResponseInfo delete(DeleteDto dto, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.delete((ID[]) dto.getIds());
		return new ResponseInfo();
	}

	/**
	 * 根据对象逻辑删除
	 * 
	 * @Title: delete
	 * @Description:
	 * @param request
	 * 
	 * @param: @param
	 *             ids id数组
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	protected ResponseInfo delete(T t) throws GlobalException {
		service.delete(t);
		return new ResponseInfo();
	}

	/**
	 * 物理删除
	 * 
	 * @Title: physicalDelete
	 * @Description:
	 * @param request
	 * 
	 * @param: @param
	 *             id
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	public ResponseInfo physicalDelete(ID id) throws GlobalException {
		if (id == null) {
			throw new GlobalException(new IllegalParamExceptionInfo());
		}
		service.physicalDelete(id);
		return new ResponseInfo();
	}

	/**
	 * 根据id数组物理删除
	 * 
	 * @Title: physicalDelete
	 * @Description:
	 * @param request
	 * 
	 * @param: @param
	 *             ids
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	public ResponseInfo physicalDelete(ID[] ids) throws GlobalException {
		if (ids == null || ids.length <= 0) {
			throw new GlobalException(new IllegalParamExceptionInfo());
		}
		service.physicalDelete(ids);
		return new ResponseInfo();
	}

	@SuppressWarnings("unchecked")
	protected ResponseInfo physicalDelete(DeleteDto dto, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.physicalDelete((ID[]) dto.getIds());
		return new ResponseInfo();
	}

	/**
	 * 根据对象集合物理删除
	 * 
	 * @Title: physicalDeleteInBatch
	 * @Description:
	 * @param request
	 * 
	 * @param: @param
	 *             entities
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	public ResponseInfo physicalDeleteInBatch(Iterable<T> entities) throws GlobalException {
		service.physicalDeleteInBatch(entities);
		return new ResponseInfo();
	}

	/**
	 * 批量排序
	 * 
	 * @Title: sort
	 * @Description:
	 * @param request
	 * 
	 * @param: @param
	 *             sort
	 * @param: @return
	 * @param: @throws
	 *             GlobalException
	 * @return: ResponseInfo
	 */
	protected ResponseInfo sort(SortDto sort, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.sort(sort.getSorts());
		return new ResponseInfo();
	}

	protected void checkServerMode() throws GlobalException {
		if (ServerModeEnum.prod.toString().equals(serverMode)) {
			throw new GlobalException(SystemExceptionEnum.CANNOT_CHANGE_DATAS);
		}
	}

	protected void validateBindingResult(BindingResult result) throws GlobalException {
		/** 对字段进行校验 */
		if (result.hasErrors()) {
			/** 初始化非法参数的提示信息。 */
			IllegalParamValidationUtils
					.initIllegalParamMsg(result);
			/** 获取非法参数异常信息对象，并抛出异常。 */
			throw new GlobalException(IllegalParamValidationUtils.getIllegalParamExceptionInfo());
		}
	}

	protected void validateIds(ID[] ids) throws GlobalException {
		if (ids == null || ids.length <= 0) {
			throw new GlobalException(new IllegalParamExceptionInfo());
		}
	}

	protected void validateId(ID id) throws GlobalException {
		if (id == null) {
			throw new GlobalException(new IllegalParamExceptionInfo());
		}
	}

	/**
	 * 获取通用参数
	 * @Title: getCommonParams  
	 * @param request request
	 * @return  Map map
	 */
	public Map<String, String[]> getCommonParams(HttpServletRequest request) {
		Map<String, String[]> params = getSearchParams();
		if (params == null) {
			params = new HashMap<String, String[]>(3);
		}
		params.putAll(RequestUtils.getParamValuesMap(request, queryParams));
		return params;
	}

	public String[] getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(String[] queryParams) {
		this.queryParams = queryParams;
	}

	public Map<String, String[]> getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(Map<String, String[]> searchParams) {
		this.searchParams = searchParams;
	}

	public String[] getDisAllowedFields() {
		return disAllowedFields;
	}

	public void setDisAllowedFields(String[] disAllowedFields) {
		this.disAllowedFields = disAllowedFields;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields(getDisAllowedFields());
		binder.setAllowedFields(new String[]{});
	}

}
