/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.controller;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.base.domain.DeleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.ContentAttr;
import com.jeecms.content.domain.ContentAttrRes;
import com.jeecms.content.domain.vo.ContentFrontVo;
import com.jeecms.content.service.ContentFrontService;
import com.jeecms.member.domain.UserCollection;
import com.jeecms.member.service.UserCollectionService;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 我的收藏
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-24
 */
@RequestMapping("/usercollections")
@RestController
public class UserCollectionController extends BaseController<UserCollection, Integer> {

    @Autowired
    private UserCollectionService service;
    @Autowired
    ContentFrontService contentFrontService;

    @PostConstruct
    public void init() {
        String[] queryParams = new String[]{};
        super.setQueryParams(queryParams);
    }

    /**
     * 列表分页
     *
     * @param request   {@link HttpServletRequest}
     * @param title     标题
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageable  分页组件
     * @return ResponseInfo
     * @throws GlobalException 
     */
    @GetMapping(value = "/page")
    @MoreSerializeField({
    	@SerializeField(clazz = UserCollection.class, includes = {"id", "title", "url",
                "releaseTimeString", "source", "createTime", "createDate","contentMobileVo"}),
    	@SerializeField(clazz = ContentFrontVo.class, includes = {"modelId","videoJson","imageJson",
    			"multiImageUploads"}),
    	@SerializeField(clazz = ContentAttr.class, includes = {"resourcesSpaceData","contentAttrRes"}),
        @SerializeField(clazz = ContentAttrRes.class, includes = {"resourcesSpaceData"}),
    	@SerializeField(clazz = ResourcesSpaceData.class, includes = { "resourceType", "dimensions", "url",
				"resourceDate", "suffix" })
    })
    public ResponseInfo page(HttpServletRequest request, String title, Date startTime, Date endTime,
                             @PageableDefault(sort = "createTime", direction =
                                     Direction.DESC) Pageable pageable) throws GlobalException {
        CoreUser user = SystemContextUtils.getUser(request);
        if (user != null) {
        	Page<UserCollection> collections = service.getPage(title, startTime, endTime, user.getUserId(), pageable);
        	for (UserCollection userCollection : collections) {
				ContentFrontVo vo = contentFrontService.initPartVo(userCollection.getContent());
				userCollection.setContentMobileVo(vo);
			}
            return new ResponseInfo(service.getPage(title, startTime, endTime, user.getUserId(), pageable));
        } else {
            return new ResponseInfo();
        }
    }

    /**
     * 列表分页
     *
     * @param request   {@link HttpServletRequest}
     * @param title     标题
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageable  分页组件
     * @return ResponseInfo
     */
    @GetMapping(value = "/mobile/page")
	@MoreSerializeField({
			@SerializeField(clazz = UserCollection.class, includes = { "id", "title", "collectionTime", "createTime",
					"createDate", "contentMobileVo" }),
			@SerializeField(clazz = ContentFrontVo.class, excludes = {"title","titleIsBold","titleColor","createTime"}),
			@SerializeField(clazz = ContentAttr.class, includes = {"resourcesSpaceData","contentAttrRes"}),
            @SerializeField(clazz = ContentAttrRes.class, includes = {"resourcesSpaceData"}),
			@SerializeField(clazz = ResourcesSpaceData.class, includes = { "resourceType", "dimensions", "url",
					"resourceDate", "suffix" })
	})
    public ResponseInfo mobilePage(HttpServletRequest request, String title, Date startTime, Date endTime,
                             @PageableDefault(sort = "createTime", direction =
                                     Direction.DESC) Pageable pageable) throws GlobalException {
        CoreUser user = SystemContextUtils.getUser(request);
        if (user != null) {
            Page<UserCollection> page = service.getPage(title, startTime, endTime, user.getUserId(), pageable);
            for (UserCollection userCollection : page) {
                ContentFrontVo contentMobileVo = contentFrontService.initMobileVo(new ContentFrontVo(), userCollection.getContent());
                userCollection.setContentMobileVo(contentMobileVo);
            }
            return new ResponseInfo(page);
        } else {
            return new ResponseInfo();
        }
    }

	/**
	 * 取消收藏
	 *
	 * @param id 收藏id
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@GetMapping("/cancel")
	public ResponseInfo delete(Integer id, HttpServletRequest request) throws GlobalException {
		validateId(id);
		Integer userId = SystemContextUtils.getUserId(request);
		if (userId == null) {
			return new ResponseInfo(SystemExceptionEnum.ACCOUNT_NOT_LOGIN.getCode(),
				SystemExceptionEnum.ACCOUNT_NOT_LOGIN.getDefaultMessage());
		}
		return super.physicalDelete(id);
	}

	/**
	 * 取消收藏（根据内容）
	 *
	 * @param id 收藏id
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@GetMapping("/recall")
	public ResponseInfo cancel(Integer id, HttpServletRequest request) throws GlobalException {
		validateId(id);
		contentFrontService.findById(id);
		Integer userId = SystemContextUtils.getUserId(request);
		if (userId == null) {
			return new ResponseInfo(SystemExceptionEnum.ACCOUNT_NOT_LOGIN.getCode(),
				SystemExceptionEnum.ACCOUNT_NOT_LOGIN.getDefaultMessage());
		}
		UserCollection userCollection = service.findByContentIdAndUserId(id, userId);
		if (userCollection != null) {
			return super.physicalDelete(userCollection.getId());
		}
		return new ResponseInfo(false);
	}

    /**
     * 取消收藏
     *
     * @param deleteDto 删除及批量删除dto
     * @param result    BindingResult
     * @return ResponseInfo
     * @throws GlobalException 异常
     */
    @DeleteMapping("/delete")
    public ResponseInfo delete(@RequestBody @Valid DeleteDto deleteDto, HttpServletRequest request,
                               BindingResult result) throws GlobalException {
        Integer userId = SystemContextUtils.getUserId(request);
        if (userId == null) {
			return new ResponseInfo(SystemExceptionEnum.ACCOUNT_NOT_LOGIN.getCode(),
					SystemExceptionEnum.ACCOUNT_NOT_LOGIN.getDefaultMessage());
		}
        return super.physicalDelete(deleteDto, result);
    }

	/**
	 * 一键清空
	 *
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	@DeleteMapping("/deleteAll")
	public ResponseInfo deleteAll(HttpServletRequest request) throws GlobalException {
		Integer userId = SystemContextUtils.getUserId(request);
		if (userId == null) {
			return new ResponseInfo(SystemExceptionEnum.ACCOUNT_NOT_LOGIN.getCode(),
					SystemExceptionEnum.ACCOUNT_NOT_LOGIN.getDefaultMessage());
		}
		List<UserCollection> list = service.findAllByUserId(userId);
		return super.physicalDeleteInBatch(list);
	}
}
