/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.resource;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.controller.BaseTreeController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.resource.domain.ResourcesSpace;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.domain.dto.ResourcesSpaceMoveDto;
import com.jeecms.resource.domain.dto.ResourcesSpaceShareDto;
import com.jeecms.resource.domain.vo.ResourcesSpaceVo;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.resource.service.ResourcesSpaceService;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 平台资源文件夹管理
 *
 * @author xiaohui
 * @version 1.0
 * @date 2018/05/21 09:30:58
 */
@RestController
@RequestMapping("/resourceSpaces")
public class ResourceSpaceController extends BaseTreeController<ResourcesSpace, Integer> {

	/**
	 * 查询资源空间文件夹（树形结构）
	 *
	 * @return ResponseInfo
	 */
	@GetMapping(value = "/page")
	@MoreSerializeField({@SerializeField(clazz = ResourcesSpace.class,
			includes = {"id", "name", "parentId", "children", "isShare"})})
	public ResponseInfo page(HttpServletRequest request) {
		Map<String, String[]> params = new HashMap<String, String[]>(1);
		params.put("EQ_userId_Integer", new String[]{SystemContextUtils.getUserId(request).toString()});
		List<ResourcesSpace> spaceList = service.getList(params, null, true);
		List<ResourcesSpace> resultList = super.getTree(spaceList, null);
		return new ResponseInfo(resultList);
	}

	/**
	 * 查询共享资源空间文件夹（树形结构）
	 *
	 * @return ResponseInfo
	 */
	@GetMapping(value = "/sharePage")
	@MoreSerializeField({@SerializeField(clazz = ResourcesSpaceVo.class, includes = {"name", "userId", "children"}),
			@SerializeField(clazz = ResourcesSpace.class, includes = {"id", "name", "parentId", "children",
					"userName", "isShare"})})
	public ResponseInfo shareUserList(HttpServletRequest request) {
		Integer userId = SystemContextUtils.getUserId(request);
		List<ResourcesSpace> list = service.getListByUserIdAndShare(null, true);
		List<ResourcesSpaceData> spaceDatas = dataService.getByUserId(userId);
		//共享给该用户的资源空间
		List<ResourcesSpace> spaces = new ArrayList<ResourcesSpace>();
		//循环资源空间获取分享用户
		for (ResourcesSpace space : list) {
			List<CoreUser> coreUsers = space.getUsers();
			/*
			1、循环分享用户
			2、判断该用户是否在分享用户中
			 */
			for (CoreUser coreUser : coreUsers) {
				if (coreUser.getId().equals(userId)) {
					spaces.add(space);
					break;
				}
			}
		}
		Set<Integer> userIds = new HashSet<Integer>();
		Map<Integer, String> usernames = new HashMap<Integer, String>();
		for (ResourcesSpaceData spaceData : spaceDatas) {
				userIds.add(spaceData.getUserId());
				usernames.put(spaceData.getUserId(), spaceData.getCreateUser());
		}
		Map<Integer, List<ResourcesSpace>> map = spaces.parallelStream().filter(o -> o.getUserId() != null)
				.collect(Collectors.groupingBy(ResourcesSpace::getUserId));
		List<ResourcesSpaceVo> vos = new ArrayList<>();
		for (Integer integer : map.keySet()) {
			List<ResourcesSpace> tree = super.getTree(map.get(integer), null);
			vos.add(new ResourcesSpaceVo(map.get(integer).get(0).getUserName(), integer, tree));
		}
		for (ResourcesSpaceVo vo : vos) {
			userIds.remove(vo.getUserId());
		}
		for (Integer id : userIds) {
			vos.add(new ResourcesSpaceVo(usernames.get(id), id, new ArrayList<ResourcesSpace>(0)));
		}
		return new ResponseInfo(vos);
	}

	/**
	 * 添加一个资源空间文件夹（平台资源空间）
	 *
	 * @param space   资源空间实体
	 * @param result  BindingResult
	 * @param request HttpServletRequest
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PostMapping()
	@SerializeField(clazz = ResourcesSpace.class, includes = {"id"})
	public ResponseInfo savePlatform(@RequestBody @Valid ResourcesSpace space, BindingResult result,
									 HttpServletRequest request) throws GlobalException {
		validateBindingResult(result);
		if (!service.checkByName(space.getName(), null, space.getUserId())) {
			return new ResponseInfo(SettingErrorCodeEnum.RESOURCE_SPACE_ALREADY_EXIST.getCode(),
					SettingErrorCodeEnum.RESOURCE_SPACE_ALREADY_EXIST.getDefaultMessage());
		}
		//设置排序为当前目录下数量+1
		space.setSortNum(service.getNumByParentId(space.getParentId()) + 1);
		space.setIsShare(ResourcesSpace.NOT_SHARED);
		space.setUserId(SystemContextUtils.getUserId(request));
		return new ResponseInfo(service.save(space));
	}

	/**
	 * 校验资源空间名称是否存在
	 *
	 * @param name    资源空间名
	 * @param id      资源id
	 * @param request HttpServletRequest
	 * @return true 不存在 false 存在
	 */
	@GetMapping("/name/unique")
	public ResponseInfo check(String name, Integer id, HttpServletRequest request) {
		return new ResponseInfo(service.checkByName(name, id, SystemContextUtils.getUserId(request)));
	}

	/**
	 * 修改资源空间文件夹名称
	 *
	 * @param space  资源空间实体
	 * @param result BindingResult
	 * @return ResponseInfo
	 * @throws GlobalException GlobalException
	 */
	@PutMapping()
	@Override
	public ResponseInfo update(@RequestBody @Valid ResourcesSpace space,
							   BindingResult result) throws GlobalException {
		validateId(space.getId());
		validateBindingResult(result);
		if (!service.checkByName(space.getName(), space.getId(), space.getUserId())) {
			return new ResponseInfo(SettingErrorCodeEnum.RESOURCE_SPACE_ALREADY_EXIST.getCode(),
					SettingErrorCodeEnum.RESOURCE_SPACE_ALREADY_EXIST.getDefaultMessage());
		}
		service.update(space);
		return new ResponseInfo(true);
	}

	/**
	 * 删除资源空间
	 *
	 * @param ids    资源空间id
	 * @param result BindingResult
	 * @return ResponseInfo
	 * @throws GlobalException 全局异常
	 */
	@DeleteMapping()
	@Override
	public ResponseInfo delete(@RequestBody @Valid DeleteDto ids,
							   BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.deleteAndSort(ids.getIds());
		return new ResponseInfo(true);
	}

	/**
	 * 拖动资源空间
	 *
	 * @param dto    移动资源空间Dto
	 * @param result BindingResult
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PutMapping("/move")
	public ResponseInfo move(@RequestBody @Valid ResourcesSpaceMoveDto dto,
							 BindingResult result) throws GlobalException {
		validateBindingResult(result);
		if (service.move(dto.getId(), dto.getParentId(), dto.getSortNum()) == null) {
			return new ResponseInfo(SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getCode(),
					SystemExceptionEnum.DOMAIN_NOT_FOUND_ERROR.getDefaultMessage());
		}
		return new ResponseInfo();
	}


	/**
	 * 分享资源空间
	 *
	 * @param dto    资源库分享Dto
	 * @param result BindingResult
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@PostMapping("/share")
	public ResponseInfo share(@RequestBody @Valid ResourcesSpaceShareDto dto,
							  BindingResult result) throws GlobalException {
		validateId(dto.getId());
		service.share(dto.getOrgIds(), dto.getRoleIds(), dto.getUserIds(), dto.getId());
		return new ResponseInfo();
	}

	/**
	 * 取消资源空间共享
	 *
	 * @param dto 资源空间id数组Dto
	 * @return ResponseInfo
	 * @throws GlobalException 全局异常
	 */
	@PostMapping("/unShare")
	public ResponseInfo unShare(@RequestBody @Valid DeleteDto dto, BindingResult result) throws GlobalException {
		validateBindingResult(result);
		service.unShare(dto.getIds());
		return new ResponseInfo();
	}

	@Autowired
	private ResourcesSpaceService service;
	@Autowired
	private ResourcesSpaceDataService dataService;
}
