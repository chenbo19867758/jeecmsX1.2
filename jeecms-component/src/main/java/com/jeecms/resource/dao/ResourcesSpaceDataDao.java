/*
@Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
*/

package com.jeecms.resource.dao;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.resource.dao.ext.ResourcesSpaceDataDaoExt;
import com.jeecms.resource.domain.ResourcesSpaceData;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Description:ResourcesSpaceData dao查询接口
 * @author: tom
 * @date: 2018年4月16日 上午11:05:40
 */
public interface ResourcesSpaceDataDao extends IBaseDao<ResourcesSpaceData, Integer>, ResourcesSpaceDataDaoExt {


	/**
	 * 通过id查找资源对象
	 *
	 * @param id
	 * @param hasDeleted
	 * @return
	 * @Title: findByIdAndHasDeleted
	 * @return: ResourcesSpaceData
	 */
	ResourcesSpaceData findByIdAndHasDeleted(Integer id, Boolean hasDeleted);

	/**
	 * 查询分享了资源的用户
	 *
	 * @param shareStatus 分享状态
	 * @return 用户列表
	 */
	@Query(value = "select bean.user from ResourcesSpaceData bean where bean.shareStatus = ?1 group by bean"
			+ ".user")
	List<CoreUser> findUserByShareStatus(Short shareStatus);

	/**
	 * 通过资源空间id获取资源
	 *
	 * @param storeResourcesSpaceId 资源空间id
	 * @return list
	 */
	List<ResourcesSpaceData> findBySpaceId(Integer storeResourcesSpaceId);

	/**
	 *
	 * @param alias 别名前缀
	 * @param userId 用户ID
	 * @param spaceId 资源Id
	 * @return
	 */
	Long countByAliasStartingWithAndUserIdAndSpaceId(String alias,Integer userId,Integer spaceId);

}
