
package com.jeecms.system.service;

import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.domain.dto.BeatchDto;

import java.util.List;

/**
 * 组织service接口
 * 
 * @author: tom
 * @date: 2018年11月5日 下午1:59:58
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface CmsOrgService extends IBaseService<CmsOrg, Integer> {

        /**
         * 获取默认的组织（ID 1）
         * 
         * @Title: findDefault
         * @return CmsOrg
         */
        CmsOrg findDefault();

        /**
         * 查询顶层组织列表
         * 
         * @Title: findTopList
         * @param disabled
         *                启用状态 true 启用 false 未启用，空则不控制该属性
         * @return List
         */
        List<CmsOrg> findTopList(Boolean disabled);

        /**
         * 查询子组织列表
         * 
         * @Title: findListByParentId
         * @param parentId
         *                父id
         * @param disabled
         *                启用状态 true 启用 false 未启用，空则不控制该属性
         * @return List
         */
        List<CmsOrg> findListByParentId(Integer parentId, Boolean disabled);

        /**
         * 查询组织列表(递归含子组织)
         * 
         * @Title: findList
         * @param parentId
         *                父id
         * @param disabled
         *                启用状态 true 启用 false 未启用，空则不控制该属性
         * @return List
         */
        List<CmsOrg> findList(Integer parentId, Boolean disabled);

        
        /**
         * 组织列表
         * @Title: cmsList
         * @param isVirtual 是否虚拟组织
         * @param key 查询关键词
         * @param ids 组织IDs
         * @return List
         */
        List<CmsOrg> cmsList(Boolean isVirtual, String key, List<Integer> ids);

        /**
         * 验证组织名称是否重复
         * 
         * @param orgname 组织名称
         * @param id 组织ID
         * @return Boolean
         */
        Boolean validName(String orgname, Integer id);
        
        /**
         * 批量删除（条件为具有可操作数据）
         * @Title: deleteOrg
         * @param  dto 批量操作对象
         * @return: ResponseInfo
         * @throws GlobalException GlobalException
         */
        ResponseInfo deleteOrg(BeatchDto dto,List<Integer>currUserOrgIds) throws GlobalException;
        
        /**
         * 组织排序
        * @Title: sort 
        * @param dto 传输
        * @return ResponseInfo 响应
        * @throws GlobalException 异常
         */
        ResponseInfo sort(DragSortDto dto, List<CmsOrg> orgs) throws GlobalException;
        
        /**
         * 无权限限制 更新
         */
        CmsOrg updateDirect(CmsOrg bean) throws GlobalException;
        
        /**
         * 清空所有组织权限
         * @Title: clearAllOrgCache
         * @return: void
         */
        void clearAllOrgCache();
        
}
