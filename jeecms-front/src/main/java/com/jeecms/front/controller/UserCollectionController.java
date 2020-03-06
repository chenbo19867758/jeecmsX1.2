/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.front.controller;

import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.service.ContentFrontService;
import com.jeecms.member.domain.UserCollection;
import com.jeecms.util.SystemContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
    ContentFrontService contentFrontService;
    @Autowired
    private CoreUserService userService;

    @PostConstruct
    public void init() {
        String[] queryParams = new String[]{};
        super.setQueryParams(queryParams);
    }

    /**
     * 添加
     *
     * @param userCollection 收藏对象
     * @param request        HttpServletRequest
     * @param result         BindingResult
     * @return ResponseInfo
     * @throws GlobalException 异常
     */
    @PostMapping
    public ResponseInfo save(@RequestBody @Valid UserCollection userCollection, HttpServletRequest request,
                             BindingResult result) throws GlobalException {
        Integer userId = SystemContextUtils.getUserId(request);
        if (userId != null) {
            Integer contentId = userCollection.getContentId();
            userCollection = new UserCollection();
            userCollection.setUser(userService.findById(userId));
            userCollection.setUserId(userId);
            userCollection.setContent(contentFrontService.findById(contentId));
            userCollection.setContentId(contentId);
            return super.save(userCollection, result);
        } else {
            return new ResponseInfo(SystemExceptionEnum.ACCOUNT_NOT_LOGIN.getCode(),
                    SystemExceptionEnum.ACCOUNT_NOT_LOGIN.getDefaultMessage());
        }

    }

}
