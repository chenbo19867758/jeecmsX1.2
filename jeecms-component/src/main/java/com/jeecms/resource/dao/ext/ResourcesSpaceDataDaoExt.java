package com.jeecms.resource.dao.ext;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.resource.domain.ResourcesSpaceData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * @Description:ResourcesSpace dao查询接口
 * @author: tom
 * @date: 2018年4月16日 上午11:05:40
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */
public interface ResourcesSpaceDataDaoExt {

	/**
	 * 通过id数组，查询未被逻辑删除的资源对象集合
	 *
	 * @param ids        资源id集合
	 * @param hasDeleted 是否删除
	 * @return list
	 * @throws GlobalException 异常
	 */
	List<ResourcesSpaceData> findByIds(Integer[] ids, Boolean hasDeleted) throws GlobalException;

	/**
	 * 获取分享资源
	 *
	 * @param alias                 别名
	 * @param shareUser             分享用户
	 * @param resourceType          资源类型
	 * @param storeResourcesSpaceId 资源空间
	 * @param userId                用户id
	 * @param pageable              分页
	 * @return Page
	 */
	Page<ResourcesSpaceData> getShareList(String alias, String shareUser, Short resourceType,
										  Integer storeResourcesSpaceId, Integer userId, Pageable pageable);

	/**
	 * 通过资源别名和用户id查询资源对象
	 *
	 * @param alias                 资源别名
	 * @param userId                用户id
	 * @param storeResourcesSpaceId 资源空间id
	 * @return ResourcesSpaceDate
	 */
	ResourcesSpaceData findByAlias(String alias, Integer userId, Integer storeResourcesSpaceId);

	/**
	 * 获取资源
	 *
	 * @param alias                 别名
	 * @param userId                用户id
	 * @param storeResourcesSpaceId 资源空间
	 * @param resourceType          资源类型
	 * @param shareStatus           分享状态
	 * @param beginCreateTime       开始时间
	 * @param endCreateTime         结束时间
	 * @param pageable              分页
	 * @return Page
	 */
	Page<ResourcesSpaceData> getList(String alias, Integer userId, Integer storeResourcesSpaceId,
									 Short resourceType, Short shareStatus, Date beginCreateTime,
									 Date endCreateTime, Pageable pageable);

	/**
	 * 通过资源空间id获取资源
	 *
	 * @param spaceIds 资源空间id集合
	 * @return list
	 */
	List<ResourcesSpaceData> findBySpaceIds(List<Integer> spaceIds);

	/**
	 * 根据用户获取资源列表
	 *
	 * @param userId 用户id
	 * @return list
	 */
	List<ResourcesSpaceData> getByUserId(Integer userId);

	/**
	 * 获取问卷用的图片
	 *
	 * @return List
	 */
	List<ResourcesSpaceData> findByQuestionnairePic();

}

 