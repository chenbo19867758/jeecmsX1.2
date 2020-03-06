package com.jeecms.admin.controller.content;

import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentVersion;
import com.jeecms.content.domain.ContentVersion.RecoveryVersion;
import com.jeecms.content.domain.ContentVersion.SaveVersion;
import com.jeecms.content.domain.ContentVersion.UpdateVersion;
import com.jeecms.content.domain.dto.VersionCompareDto;
import com.jeecms.content.service.ContentService;
import com.jeecms.content.service.ContentVersionService;
import com.jeecms.system.domain.CmsDataPerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 内容版本管理
 * @author: chenming
 * @date:   2019年6月21日 上午11:31:59
 */
@RequestMapping("/contentVersion")
@RestController
public class ContentVersionController extends BaseController<ContentVersion, Integer> {

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}
	
	/**
	 * 底下很多方法如：传入的对象为null将直接return null
	 * 因为出现这种可能是因为他人通过接口传入错误数据，其它情况不可能出现此情况，
	 * 这种情况我认为抛出异常不是一个好的处理办法，不做任何操作才更好
	 * 
	 */
	
	/**
	 * 新增一个版本
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseInfo save(@RequestBody @Validated(value = SaveVersion.class) ContentVersion contentVersion,BindingResult result) 
			throws GlobalException {
		super.validateBindingResult(result);
		Content content = contentService.findById(contentVersion.getContentId());
		if (content == null) {
			return new ResponseInfo();
		}
		if (!contentService.validType(CmsDataPerm.OPE_CONTENT_EDIT, null, content)) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		service.save(content,contentVersion.getRemark());
		return new ResponseInfo();
	}
	
	/**
	 * 分页查询列表
	 */
	@SerializeField(clazz = ContentVersion.class, includes = { 
			"id","versionCode","remark","createTime","createUser" 
			})
	@RequestMapping(value = "/page",method = RequestMethod.GET)
	public ResponseInfo page(HttpServletRequest request,
			@RequestParam(value = "contentId") Integer contentId,
			@PageableDefault(sort = "createTime", direction = Direction.DESC) Pageable pageable)
			throws GlobalException {
		Map<String,String[]> map = new HashMap<String,String[]>(1);
		map.put("EQ_contentId_Integer", new String[] {String.valueOf(contentId)});
		super.setSearchParams(map);
		return super.getPage(request, pageable, false);
	}
	
	/**
	 * 查询单个版本
	 */
	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
	@SerializeField(clazz = ContentVersion.class, includes = { 
			"id","jsonTxt","remark","createTime","createUser","versionCode" 
			})
	public ResponseInfo get(@PathVariable(name = "id") Integer id) throws GlobalException {
		return new ResponseInfo(service.findById(id));
	}

    /**
     * 文档版本对比
     *
     * @param dto    文档id, 版本id
     * @param result 对比结果
     * @return com.jeecms.common.response.ResponseInfo
     * @author Zhu Kaixiao
     * @date 2019/7/22 14:34
     **/
    @PostMapping("/compare")
    public ResponseInfo versionCompare(@RequestBody @Valid VersionCompareDto dto, BindingResult result)
            throws GlobalException {
        super.validateBindingResult(result);
        return new ResponseInfo(service.versionCompare(dto));
    }


	/**
	 * 修改->修改备注
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseInfo update(@RequestBody @Validated(value = UpdateVersion.class) ContentVersion contentVersion) 
			throws GlobalException {
		ContentVersion version = service.findById(contentVersion.getId());
		if (version == null) {
			return new ResponseInfo();
		}
		Content content = contentService.findById(version.getContentId());
		if (content == null) {
			return new ResponseInfo();
		}
		if (!contentService.validType(CmsDataPerm.OPE_CONTENT_EDIT, null, content)) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		version.setRemark(contentVersion.getRemark());
		service.updateAll(version);
		return new ResponseInfo();
	}
	
	/**
	 * 删除版本
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseInfo delete(@RequestBody @Valid DeleteDto ids, BindingResult result) 
			throws GlobalException {
		super.validateBindingResult(result);
		ContentVersion version = service.findById(ids.getIds()[0]);
		if (version == null) {
			return new ResponseInfo();
		}
		// 如果传入的id中出现多个内容值，说明它人是通过接口调用的，无需处理
		Content content = contentService.findById(version.getContentId());
		if (content == null) {
			return new ResponseInfo();
		}
		if (!contentService.validType(CmsDataPerm.OPE_CONTENT_EDIT, null, content)) {
			return new ResponseInfo(UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getCode(),
					UserErrorCodeEnum.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
		}
		super.deleteBeatch(ids.getIds());
		return new ResponseInfo();
	}
	
	/**
	 * 恢复版本
	 * 恢复版本中如果出现字段不匹配(之前的字段叫做一个名称，现在的字段又是一个新的名称),将出现字段丢失
	 * 此处已经讨论过，直接丢失字段
	 */
	@RequestMapping(value = "/recovery",method = RequestMethod.PUT)
	public ResponseInfo recoveryVersion(
			@RequestBody @Validated(value = RecoveryVersion.class) ContentVersion contentVersion)
			throws GlobalException {
		ContentVersion version = service.findById(contentVersion.getId());
		if (version == null) {
			return new ResponseInfo();
		}
		return contentService.recoveryVersion(version, version.getContentId());
	}
	
	@Autowired
	private ContentService contentService;
	@Autowired
	private ContentVersionService service;
}
