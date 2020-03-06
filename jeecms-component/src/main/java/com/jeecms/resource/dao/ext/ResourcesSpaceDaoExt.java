package com.jeecms.resource.dao.ext;

import com.jeecms.resource.domain.ResourcesSpace;

import java.util.List;

/**
 * @Description:ResourcesSpace dao查询接口
 * @author: tom
 * @date: 2018年4月16日 上午11:05:40
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */
public interface ResourcesSpaceDaoExt {

	/**
	 * 获取目录下资源空间列表(传入id标识同级目录)
	 *
	 * @param id       资源空间id
	 * @param parentId 父级id
	 * @param sortNum  排序值
	 * @param compare  比较方式
	 * @return List
	 */
	List<ResourcesSpace> getListBySortNum(Integer id, Integer parentId, Integer sortNum, String compare);

	/**
	 * 获取目录下资源空间列表(传入id标识同级目录)
	 *
	 * @param userId  用户id
	 * @param isShare 是否分享（true 已分享 false 未分享）
	 * @return List
	 */
	List<ResourcesSpace> getListByUserIdAndShare(Integer userId, Boolean isShare);
}

 