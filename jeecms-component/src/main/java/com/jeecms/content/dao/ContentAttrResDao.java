/**
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.dao;

import java.util.List;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.content.domain.ContentAttrRes;

/**
 * 内容自定义属性-多资源表dao接口
 * @author ljw
 * @version 1.0
 * @date 2019-05-15
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface ContentAttrResDao extends IBaseDao<ContentAttrRes, Integer> {

	/**
	 * 通过内容自定义id集合查询出多资源集合对象
	 * 
	 * @Title: findByResIdIn
	 * @param ids 内容自定义id集合
	 * @return: List
	 */
	List<ContentAttrRes> findByResIdIn(List<Integer> ids);
}
