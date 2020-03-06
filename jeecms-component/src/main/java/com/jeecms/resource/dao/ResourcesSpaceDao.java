package com.jeecms.resource.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.resource.dao.ext.ResourcesSpaceDaoExt;
import com.jeecms.resource.domain.ResourcesSpace;
import org.springframework.data.jpa.repository.Query;

/**
 * @Description:ResourcesSpace dao查询接口
 * @author: tom
 * @date: 2018年4月16日 上午11:05:40
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */
public interface ResourcesSpaceDao extends IBaseDao<ResourcesSpace, Integer>, ResourcesSpaceDaoExt {

        /**
         * 获取父级为parentId的数量
         *
         * @param parentId 父级id
         * @return int
         */
        @Query(value = "select count(bean.id) from ResourcesSpace bean where 1 = 1 and bean.parentId=?1")
        int getNumByParentId(Integer parentId);

        /**
         * 通过资源空间名称和用户id查询资源空间对象
         *
         * @param name   资源空间名称
         * @param userId 用户id
         * @return ResourcesSpace
         */
        @Query(value = "select bean from ResourcesSpace bean where bean.name = ?1 and bean.userId = ?2")
        ResourcesSpace findByName(String name, Integer userId);

}
