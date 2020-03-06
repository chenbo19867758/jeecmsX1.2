/*   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.dao;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.content.dao.ext.ContentCheckDetailDaoExt;
import com.jeecms.content.domain.ContentCheckDetail;

/**
 * 内容智能审核详情dao接口
 * @author: chenming
 * @date:   2019年12月25日 下午2:34:44
 */
public interface ContentCheckDetailDao extends IBaseDao<ContentCheckDetail, Integer>,ContentCheckDetailDaoExt {
	
	/**
	 * 根据审核标识(内容审核标识)，查询未被删除的 ContentCheckDetail
	 * @Title: findByCheckMarkAndHasDeleted
	 * @param checkMark		内容审核标识
	 * @param hasDeleted	逻辑删除标识
	 * @return: ContentCheckDetail
	 */
	ContentCheckDetail findByCheckMarkAndHasDeleted(String checkMark,Boolean hasDeleted);
	
	ContentCheckDetail findByCheckMarkAndHasDeletedAndStatus(String checkMark,Boolean hasBoolean,Integer status);
}
