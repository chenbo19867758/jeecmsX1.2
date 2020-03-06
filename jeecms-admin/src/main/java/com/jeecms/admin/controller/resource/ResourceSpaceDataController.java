/*
@Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.admin.controller.resource;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.domain.CoreUserExt;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.Zipper;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.domain.dto.ResourceDataCopyDot;
import com.jeecms.resource.domain.dto.ResourcesSpaceDataShiftDto;
import com.jeecms.resource.domain.dto.ResourcesSpaceShareDto;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.util.SystemContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:资源管理
 * @author: tom
 * @date: 2018年4月16日 下午4:12:55
 */
@RestController
@RequestMapping("/resourceSpaceDatas")
public class ResourceSpaceDataController extends BaseController<ResourcesSpaceData, Integer> {

	private static final Logger log = LoggerFactory.getLogger(ResourceSpaceDataController.class);

	@PostConstruct
	public void init() {
		String[] queryParams = {"[id,storeResourcesSpaceId]_EQ_Integer", "alias_LIKE", "resourceType_EQ_Integer",
				"shareStatus_EQ_Integer", "[beginCreateTime,createTime]_GTE_Timestamp",
				"[endCreateTime,createTime]_LTE_Timestamp"};
		super.setQueryParams(queryParams);
	}

	@Autowired
	private ResourcesSpaceDataService service;
	@Autowired
	private CoreUserService userService;

	/**
	 * 获取自己的资源文件
	 */
	@GetMapping("/user/page")
	@SerializeField(clazz = ResourcesSpaceData.class, includes = {"id", "dimensions", "size", "duration", "alias",
			"resourceType", "url", "shareStatus", "createTime", "suffix", "sizeUnit", "width", "height"})
	public ResponseInfo page(Integer id, String alias, Short resourceType, Short shareStatus, Date beginCreateTime,
							 Date endCreateTime, HttpServletRequest request,
							 @PageableDefault(sort = "createTime", direction = Direction.DESC) Pageable pageable)
			throws GlobalException {
		Page<ResourcesSpaceData> page = service.getPage(alias, SystemContextUtils.getUserId(request), id, resourceType,
				shareStatus, beginCreateTime, endCreateTime, pageable);
		return new ResponseInfo(page);
	}

	/**
	 * 获取用户分享资源
	 *
	 * @param request  HttpServletRequest
	 * @param pageable 分页组件
	 * @return ResponseInfo
	 */
	@GetMapping("/page")
	@SerializeField(clazz = ResourcesSpaceData.class, includes = {"id", "dimensions", "size", "alias", "resourceType",
			"url", "shareStatus", "createTime", "suffix", "sizeUnit", "shareTime", "createUser", "width", "height",
			"duration"})
	public ResponseInfo sharePage(String alias, String username, Short resourceType, Integer id,
								  HttpServletRequest request,
								  @PageableDefault(sort = "createTime", direction = Direction.DESC) Pageable pageable) {
		Page<ResourcesSpaceData> list = service.getPage(alias, username, resourceType, id,
				SystemContextUtils.getUserId(request), pageable);
		return new ResponseInfo(list);
	}

	/**
	 * 通过id进行物理删除数据（删除多个）
	 */
	@DeleteMapping()
	public ResponseInfo deletes(@RequestBody @Valid DeleteDto dels) throws GlobalException {
		service.deleteFile(dels.getIds());
		return new ResponseInfo();
	}

	/**
	 * 获取资源空间数据详细信息
	 *
	 * @param id 资源空间数据id
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@GetMapping("/{id}")
	@Override
	@SerializeField(clazz = ResourcesSpaceData.class, includes = {"dimensions", "size", "duration", "alias",
			"resourceType", "createTime", "createUser", "suffix", "sizeUnit", "width", "height"})
	public ResponseInfo get(@PathVariable(name = "id") Integer id) throws GlobalException {
		validateId(id);
		return super.get(id);
	}

	/**
	 * 共享资源给其他用户
	 *
	 * @param dto 资源库分享Dto
	 * @return ResponseInfo
	 * @throws GlobalException 全局异常
	 */
	@PostMapping("/share")
	public ResponseInfo share(@RequestBody @Valid ResourcesSpaceShareDto dto) throws GlobalException {
		validateIds(dto.getIds());
		service.share(dto);
		return new ResponseInfo();
	}

	/**
	 * 取消共享
	 *
	 * @param deleteDto 取消共享资源id数组
	 * @param result    BindingResult
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PostMapping("/unShare")
	public ResponseInfo unShare(@RequestBody @Valid DeleteDto deleteDto, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.unShare(deleteDto.getIds());
		return new ResponseInfo();
	}

	/**
	 * 修改用户共享设置
	 *
	 * @param dto
	 * @param request
	 * @return
	 * @throws GlobalException
	 */
	@PutMapping("/share")
	public ResponseInfo setDefaultShare(@RequestBody ResourcesSpaceShareDto dto, HttpServletRequest request)
			throws GlobalException {
		userService.updateMember(SystemContextUtils.getUserId(request), dto);
		return new ResponseInfo();
	}

	@GetMapping("/share")
	@SerializeField(clazz = ResourcesSpaceShareDto.class, includes = {"orgIds", "userIds", "roleIds"})
	public ResponseInfo getDefaultShare(HttpServletRequest request) {
		CoreUser user = SystemContextUtils.getUser(request);
		CoreUserExt ext = user.getUserExt();
		String shareUserId = ext.getShareUserId();
		String shareOrgId = ext.getShareOrgId();
		String shareRoleId = ext.getShareRoleId();
		ResourcesSpaceShareDto dto = new ResourcesSpaceShareDto();
		dto.setUserIds(stringConvertInt(shareUserId));
		dto.setOrgIds(stringConvertInt(shareOrgId));
		dto.setRoleIds(stringConvertInt(shareRoleId));
		return new ResponseInfo(dto);
	}

	/**
	 * 分割string转换为Integer数组
	 *
	 * @param value string参数
	 * @return integer数组
	 */
	private Integer[] stringConvertInt(String value) {
		Integer[] intArr = new Integer[0];
		if (StringUtils.isNotBlank(value)) {
			value = value.replace("[", "");
			value = value.replace("]", "");
			if (StringUtils.isNotBlank(value)) {
				String[] valueArr = value.split(",");
				intArr = new Integer[valueArr.length];
				for (int i = 0; i < valueArr.length; i++) {
					intArr[i] = Integer.parseInt(valueArr[i].trim());
				}
			}
		}
		return intArr;
	}

	/**
	 * 保存到我的资源
	 *
	 * @param copyDot 保存到我的资源Dto
	 * @param result  BindingResult
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PostMapping()
	public ResponseInfo save(@RequestBody @Valid ResourceDataCopyDot copyDot, HttpServletRequest request,
							 BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.copyBatch(copyDot.getIds(), SystemContextUtils.getUserId(request), copyDot.getSpaceId(),
				SystemContextUtils.getSite(request));
		return new ResponseInfo();
	}

	/**
	 * 重名校验
	 *
	 * @param alias   资源空间数据别名
	 * @param id      资源空间id
	 * @param request HttpServletRequest
	 * @return ResponseInfo
	 */
	@GetMapping("/alia/unique")
	public ResponseInfo check(String alias, Integer id, Integer spaceId, HttpServletRequest request) {
		return new ResponseInfo(service.checkByAlias(alias, id, SystemContextUtils.getUserId(request), spaceId));
	}

	/**
	 * 重命名
	 *
	 * @param data 资源空间数据
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PutMapping()
	public ResponseInfo update(@RequestBody ResourcesSpaceData data, HttpServletRequest request)
			throws GlobalException {
		validateId(data.getId());
		ResourcesSpaceData entity = service.findById(data.getId());
		if (StringUtils.isBlank(data.getAlias())) {
			return new ResponseInfo(SystemExceptionEnum.INCOMPLETE_PARAM.getCode(),
					SystemExceptionEnum.INCOMPLETE_PARAM.getDefaultMessage());
		}
		boolean flag = service.checkByAlias(data.getAlias(), data.getId(), SystemContextUtils.getUserId(request),
				data.getStoreResourcesSpaceId());
		if (!flag) {
			return new ResponseInfo(SysOtherErrorCodeEnum.FILE_ALREADY_EXIST.getCode(),
					SysOtherErrorCodeEnum.FILE_ALREADY_EXIST.getDefaultMessage());
		}
		if (entity == null) {
			return new ResponseInfo(SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getCode(),
					SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getDefaultMessage());
		}
		entity.setAlias(data.getAlias());
		service.update(entity);
		return new ResponseInfo(true);
	}

	/**
	 * 下载文件
	 *
	 * @param dto      资源id数组
	 * @param response HttpServletResponse
	 * @return ResponseInfo
	 */
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public ResponseInfo download(@RequestBody DeleteDto dto, HttpServletResponse response) throws GlobalException {
		Integer[] ids = dto.getIds();
		validateIds(ids);
		response.setContentType("application/x-download;charset=UTF-8");
		InputStream input = null;
		OutputStream output = null;
		File tempFile = null;
		List<File> tempFiles = new ArrayList<File>();
		try {
			if (ids.length > 1) {
				List<Zipper.FileEntry> list = new ArrayList<Zipper.FileEntry>();
				for (Integer id : ids) {
					File temp = service.getFile(service.findById(id));
					tempFiles.add(temp);
					list.add(new Zipper.FileEntry("", "", temp));
				}
				response.addHeader("Content-disposition", "filename=download.zip");
				Zipper.zip(response.getOutputStream(), list, "UTF-8");
			} else {
				ResourcesSpaceData spaceData = service.findById(ids[0]);
				String filename = spaceData.getAlias();
				RequestUtils.setDownloadHeader(response, filename);
				tempFile = service.getFile(spaceData);
				input = new FileInputStream(tempFile);
				output = response.getOutputStream();
				if (input != null) {
					byte[] buff = new byte[1024];
					int len;
					while ((len = input.read(buff)) != -1) {
						output.write(buff, 0, len);
					}
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ResponseInfo(SysOtherErrorCodeEnum.IO_ERROR.getCode(),
					SysOtherErrorCodeEnum.IO_ERROR.getDefaultMessage());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			if (tempFile != null && tempFile.exists()) {
				tempFile.delete();
				new File(tempFile.getParent()).delete();
			}
			if (tempFiles != null) {
				for (File t : tempFiles) {
					if (t.exists()) {
						t.delete();
						new File(t.getParent()).delete();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 传递过来两个参数，一个是移动资源的id值，另外一个是需要移动的店铺资源空间表id值
	 *
	 * @param dto 资源移动Dto
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PutMapping("/shift")
	public ResponseInfo shift(@RequestBody ResourcesSpaceDataShiftDto dto) throws GlobalException {
		// 判断id是否为空
		validateIds(dto.getIds());
		List<ResourcesSpaceData> list = new ArrayList<>();
		for (Integer id : dto.getIds()) {
			ResourcesSpaceData entity = service.findById(id);
			if (entity == null) {
				return new ResponseInfo(SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getCode(),
						SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getDefaultMessage());
			}
			String alias = service.filename(entity.getAlias(), entity.getId(), entity.getUserId(), 0,
					dto.getStoreResourcesSpaceId());
			entity.setAlias(alias);
			entity.setStoreResourcesSpaceId(dto.getStoreResourcesSpaceId());
			list.add(entity);
		}
		service.batchUpdate(list);
		return new ResponseInfo(true);
	}
}
