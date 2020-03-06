/*
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.service;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.page.Paginable;
import com.jeecms.resource.domain.ResourcesSpace;

import java.util.List;
import java.util.Map;

/**
 * @Description:ResourcesSpace service接口
 * @author: tom
 * @date: 2018年4月16日 上午10:10:01
 */

public interface ResourcesSpaceService extends IBaseService<ResourcesSpace, Integer> {

        /**
         * 树形结构查询出平台、店铺的资源文件夹
         *
         * @param params
         * @param paginable
         * @return
         * @Title: getList
         * @return: List<ResourcesSpace>
         */
        List<ResourcesSpace> getList(Map<String, String[]> params, Paginable paginable);

        /**
         * 通过父级查找对象数量
         *
         * @param parentId 父级id
         * @return int
         */
        int getNumByParentId(Integer parentId);

        /**
         * 获取用户的分享数据
         *
         * @param userId  用户id
         * @param isShare 分享状态（true 分享）
         * @return list
         */
        List<ResourcesSpace> getListByUserIdAndShare(Integer userId, Boolean isShare);

        /**
         * 拖到资源空间
         *
         * @param id       资源空间id
         * @param parentId 资源空间父级id
         * @param sortNum  排序
         * @return ResourcesSpace
         * @throws GlobalException 异常
         */
        ResourcesSpace move(Integer id, Integer parentId, Integer sortNum) throws GlobalException;

        /**
         * 分享资源空间
         *
         * @param orgIds  组织id数组
         * @param roleIds 角色id数组
         * @param userIds 用户id数组
         * @param id      资源空间id
         * @return ResourcesSpace
         * @throws GlobalException 异常
         */
        ResourcesSpace share(Integer[] orgIds, Integer[] roleIds, Integer[] userIds, Integer id) throws GlobalException;

        /**
         * 取消资源空间共享
         *
         * @param ids 资源空间id数组
         * @throws GlobalException 全局异常
         */
        void unShare(Integer[] ids) throws GlobalException;

        /**
         * 取消资源空间共享(资源空间下没有共享文件时取消)
         *
         * @param ids 资源空间id数组
         * @throws GlobalException 全局异常
         */
        void unShareSpace(Integer[] ids) throws GlobalException;

        /**
         * 修改资源空间状态为下属资源被分享
         *
         * @param parentId 父级id
         * @param list 用户列表
         * @throws GlobalException 异常
         */
        void shareChildShared(Integer parentId, List<CoreUser> list) throws GlobalException;

        /**
         * 删除资源空间并重新排序
         *
         * @param ids 资源空间id数组
         * @throws GlobalException 异常
         */
        void deleteAndSort(Integer[] ids) throws GlobalException;

        /**
         * 校验名称是否存在
         *
         * @param name   资源空间
         * @param id     资源空间id
         * @param userId 用户id
         * @return true 可用(存在) false 不可用(不存在)
         */
        boolean checkByName(String name, Integer id, Integer userId);
}
