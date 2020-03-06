package com.jeecms.resource.service.impl;

import com.jeecms.auth.domain.CoreRole;
import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreRoleService;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.IllegalParamExceptionInfo;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SettingErrorCodeEnum;
import com.jeecms.common.jpa.SearchFilter;
import com.jeecms.common.page.Paginable;
import com.jeecms.resource.dao.ResourcesSpaceDao;
import com.jeecms.resource.dao.impl.ResourcesSpaceDaoImpl;
import com.jeecms.resource.domain.ResourcesSpace;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.resource.service.ResourcesSpaceService;
import com.jeecms.system.domain.CmsOrg;
import com.jeecms.system.service.CmsOrgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

import static com.jeecms.resource.domain.ResourcesSpaceData.STATUS_SHARED;

/**
 * @Description:ResourcesSpace service实现
 * @author: tom
 * @date: 2018年4月16日 下午3:53:06
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourcesSpaceServiceImpl extends BaseServiceImpl<ResourcesSpace, ResourcesSpaceDao, Integer>
        implements ResourcesSpaceService {

    @SuppressWarnings("serial")
    @Override
    public List<ResourcesSpace> getList(Map<String, String[]> params, Paginable paginable) {
        Specification<ResourcesSpace> specs = SearchFilter.spec(params, ResourcesSpace.class, true);
        Specification<ResourcesSpace> sp = new Specification<ResourcesSpace>() {
            @Override
            public Predicate toPredicate(Root<ResourcesSpace> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate pred = specs.toPredicate(root, query, cb);
                                /*if (storeId != null) {
                                        if (!storeId.equals(0)) {
                                                pred = cb.and(pred, cb.equal(root.get("store").<Integer>get("id"), storeId));
                                        } else {
                                                pred = cb.and(pred, cb.isNull(root.get("store").<Integer>get("id")));
                                        }
                                }*/
                return pred;
            }
        };
        List<ResourcesSpace> list = super.dao.findAll(sp, paginable, true);
        return list;
    }

    @Override
    public int getNumByParentId(Integer parentId) {
        return dao.getNumByParentId(parentId);
    }

    @Override
    public List<ResourcesSpace> getListByUserIdAndShare(Integer userId, Boolean isShare) {
        return dao.getListByUserIdAndShare(userId, isShare);
    }

    @Override
    public ResourcesSpace move(Integer id, Integer parentId, Integer sortNum) throws GlobalException {
        if (id == null) {
            return null;
        }
        ResourcesSpace bean = super.findById(id);
        if (bean == null) {
            return null;
        }
        if (!checkByName(bean.getName(), id, bean.getUserId())) {
            throw new GlobalException(new SystemExceptionInfo(
                    SettingErrorCodeEnum.RESOURCE_SPACE_ALREADY_EXIST.getDefaultMessage(),
                    SettingErrorCodeEnum.RESOURCE_SPACE_ALREADY_EXIST.getCode()));
        }
        bean.setParentId(parentId);
        //如果sortNum==null,则把新目录的数量加1,因为是在新目录最后，所有不需要重新排序
        if (sortNum == null) {
            sortNum = dao.getNumByParentId(parentId + 1);
        } else {
            //调整新目录的排序值
            renewSort(null, bean.getParentId(), sortNum, ResourcesSpaceDaoImpl.MORE_EQUAL_THAN);
        }
        bean.setSortNum(sortNum);
        //调整原目录的排序值
        bean = super.updateAll(bean);
        if (bean.getParentId().equals(parentId)) {
            //父级目录不改变
            renewSort(bean.getId(), parentId, bean.getSortNum(), ResourcesSpaceDaoImpl.MORE_EQUAL_THAN);
        } else {
            renewSort(null, parentId, bean.getSortNum(), ResourcesSpaceDaoImpl.MORE_THAN);
        }
        return bean;
    }

    @Override
    public ResourcesSpace share(Integer[] orgIds, Integer[] roleIds, Integer[] userIds,
                                Integer id) throws GlobalException {
        //用户保存组织，角色，用户数组中获取到的用户对象
        Set<CoreUser> set = new HashSet<CoreUser>();
        ResourcesSpace resourcesSpace = super.findById(id);
        //如果都为空，抛出异常
        if (validArrayEmpty(orgIds) || validArrayEmpty(roleIds) || validArrayEmpty(userIds)) {
            //set长度不为0则操作
            set = getUser(orgIds, roleIds, userIds);
        } else {
            throw new GlobalException(new IllegalParamExceptionInfo());
        }
        if (set.size() > 0) {
            //先判断是否存在共享列表，存在则先清空
            if (resourcesSpace.getUsers() != null) {
                resourcesSpace.getUsers().clear();
            }
            List<CoreUser> list = new ArrayList<CoreUser>(set);
            resourcesSpace.setUsers(list);
            //设置为已分享状态
            resourcesSpace.setIsShare(ResourcesSpace.SHARED);
            resourcesSpace = super.update(resourcesSpace);
            //如果父级不为空则把父级设置为 2-下属资源被分享
            shareChildShared(resourcesSpace.getParentId(), list);
            //分享资源空间下的文件
            List<ResourcesSpaceData> dataList = dataService.findBySpaceId(id);
            dataService.shareAll(list, dataList);
            List<ResourcesSpace> childs = resourcesSpace.getChildren();
            //如果子资源空间不为空，则子资源空间的资源也共享出去，子资源空间状态改为已共享
            if (childs != null) {
                shareToShared(list, childs);
            }
        }
        return resourcesSpace;
    }

    @Override
    public void unShare(Integer[] ids) throws GlobalException {
        List<ResourcesSpace> spaces = super.findAllById(Arrays.asList(ids));
        for (ResourcesSpace space : spaces) {
            space.setIsShare(ResourcesSpace.NOT_SHARED);
            update(space);
            //取消分享资源空间下文件
            dataService.unShare(space.getDatas());
            //取消分享资源子空间下空间和资源
            List<ResourcesSpace> childs = space.getChildren();
            if (childs != null) {
                unShare(childs);
            }
        }
    }

    @Override
    public void unShareSpace(Integer[] ids) throws GlobalException {
        List<ResourcesSpace> spaces = super.findAllById(Arrays.asList(ids));
        for (ResourcesSpace space : spaces) {
			List<ResourcesSpaceData> datas = space.getDatas();
			long count = datas.parallelStream().filter(ResourcesSpaceData::getDisplay).filter(o -> o.getShareStatus().equals(STATUS_SHARED)).count();
			if (count == 0) {
				space.setIsShare(ResourcesSpace.NOT_SHARED);
			}
        }
        batchUpdate(spaces);
    }

    /**
     * 取消分享资源空间及资源
     *
     * @param list 资源空间数组
     * @throws GlobalException 异常
     */
    private void unShare(List<ResourcesSpace> list) throws GlobalException {
        for (ResourcesSpace space : list) {
            space.setIsShare(ResourcesSpace.NOT_SHARED);
            update(space);
            dataService.unShare(space.getDatas());
            //取消分享资源子空间下空间和资源
            List<ResourcesSpace> childs = space.getChildren();
            if (childs != null) {
                unShare(childs);
            }
        }
    }

    /**
     * 分享子空间的资源
     *
     * @param userList  用户集合
     * @param spaceList 子空间集合
     * @throws GlobalException 异常
     */
    private void shareToShared(List<CoreUser> userList, List<ResourcesSpace> spaceList) throws GlobalException {
        for (ResourcesSpace space : spaceList) {
            if (space.getUsers() != null) {
                space.getUsers().clear();
            }
            //设置为已分享状态
            space.setIsShare(ResourcesSpace.SHARED);
            space = super.update(space);
            //分享资源空间下的文件
            List<ResourcesSpaceData> dataList = dataService.findBySpaceId(space.getId());
            dataService.shareAll(userList, dataList);
            List<ResourcesSpace> childs = space.getChildren();
            if (childs != null) {
                shareToShared(userList, childs);
            }
        }
    }

    @Override
    public void shareChildShared(Integer parentId, List<CoreUser> list) throws GlobalException {
        if (parentId != null) {
            ResourcesSpace bean = super.findById(parentId);
            bean.setIsShare(ResourcesSpace.CHILD_SHARED);
            bean.setUsers(list);
            super.update(bean);
            shareChildShared(bean.getParentId(), list);
        }
    }

    @Override
    public void deleteAndSort(Integer[] ids) throws GlobalException {
        List<ResourcesSpace> list = physicalDelete(ids);
        List<ResourcesSpace> spaces = new ArrayList<ResourcesSpace>();
        for (ResourcesSpace space : list) {
            spaces.addAll(space.getChildren());
            //删除空间结构后，重新排序
            renewSort(null, space.getParentId(), space.getSortNum(), ResourcesSpaceDaoImpl.MORE_THAN);
        }
        super.physicalDeleteInBatch(spaces);
        spaces.addAll(list);
        dataService.updateToDisplay(spaces, false);
    }

    @Override
    public boolean checkByName(String name, Integer id, Integer userId) {
        if (StringUtils.isBlank(name)) {
            return true;
        }
        ResourcesSpace space = dao.findByName(name, userId);
        if (space == null) {
            return true;
        } else {
            if (id == null) {
                return false;
            }
            return space.getId().equals(id);
        }
    }

    /**
     * 获取分享用户
     *
     * @param orgIds  组织id数组
     * @param roleIds 角色id数组
     * @param userIds 用户id数组
     * @return set 可保证用户不重复
     */
    private Set<CoreUser> getUser(Integer[] orgIds, Integer[] roleIds, Integer[] userIds) {
        Set<CoreUser> set = new HashSet<CoreUser>();
        //遍历组织获取用户添加到set集合中
        if (validArrayEmpty(orgIds)) {
            for (Integer orgId : orgIds) {
                CmsOrg cmsOrg = orgService.findById(orgId);
                if (cmsOrg != null) {
                    List<CoreUser> coreUsers = cmsOrg.getUsers();
                    set.addAll(coreUsers);
                }
            }
        }
        //遍历角色获取用户添加到set集合中
        if (validArrayEmpty(roleIds)) {
            for (Integer roleId : roleIds) {
                CoreRole coreRole = roleService.findById(roleId);
                if (coreRole != null) {
                    List<CoreUser> coreUsers = coreRole.getUsers();
                    set.addAll(coreUsers);
                }


            }
        }
        //获取到角色赋值到set集合中
        if (validArrayEmpty(userIds)) {
            List<CoreUser> coreUsers = userService.findAllById(Arrays.asList(userIds));
            set.addAll(coreUsers);
        }
        return set;
    }

    /**
     * 校验数组是否为空
     *
     * @param obj 数组
     * @return true 不为空
     */
    private boolean validArrayEmpty(Integer[] obj) {
        return obj != null && obj.length > 0;
    }

    /**
     * 重新调整目录的排序值
     *
     * @param parentId 父级id
     * @param sortNum  排序值
     * @param compare  比较方式（大于 gt 大于等于 gte）
     * @throws GlobalException 异常
     */
    private void renewSort(Integer id, Integer parentId, Integer sortNum, String compare) throws GlobalException {
        List<ResourcesSpace> list = dao.getListBySortNum(id, parentId, sortNum, compare);
        //原目录下排序大于该排序值的值-1
        if (ResourcesSpaceDaoImpl.MORE_THAN.equalsIgnoreCase(compare)) {
            for (ResourcesSpace space : list) {
                space.setSortNum(space.getSortNum() - 1);
                update(space);
            }
        } else if (ResourcesSpaceDaoImpl.MORE_EQUAL_THAN.equalsIgnoreCase(compare)) {
            //新目录下排序大于等于该排序值的值+1
            for (ResourcesSpace space : list) {
                space.setSortNum(space.getSortNum() + 1);
                update(space);
            }
        }
    }

    @Autowired
    private CoreUserService userService;
    @Autowired
    private CmsOrgService orgService;
    @Autowired
    private CoreRoleService roleService;
    @Autowired
    private ResourcesSpaceDataService dataService;

}
