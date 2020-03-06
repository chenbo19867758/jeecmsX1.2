/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.member.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.member.domain.UserCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 用户收藏Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-24
 */
public interface UserCollectionService extends IBaseService<UserCollection, Integer> {

    /**
     * 获取用户收藏
     *
     * @param title     标题
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param userId    用户id
     * @param pageable  分页组件
     * @return page
     */
    Page<UserCollection> getPage(String title, Date startTime, Date endTime
            , Integer userId, Pageable pageable);

    /**
     * 获取内容管理的收藏
     *
     * @param contentId 内容id
     * @param userId    用户id
     * @return true 已收藏 false 未收藏
     */
    boolean isHaveCollection(Integer contentId, Integer userId);

    /**
     * 获取收藏
     *
     * @param contentId 内容id
     * @param userId    用户id
     * @return UserCollection
     */
    UserCollection findByContentIdAndUserId(Integer contentId, Integer userId);

    /**
     * 获取收藏
     *
     * @param userId 用户id
     * @return UserCollection
     */
    List<UserCollection> findAllByUserId(Integer userId);

}
