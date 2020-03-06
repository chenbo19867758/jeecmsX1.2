/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.ContentRelation;
import com.jeecms.content.domain.dto.ContentRelationDto;

import java.util.List;

/**
 * 内容"相关内容"service接口
 * @author: chenming
 * @date: 2019年6月21日 下午4:30:48
 */
public interface ContentRelationService extends IBaseService<ContentRelation, Integer> {

	void save(ContentRelationDto dto) throws GlobalException;

	void sort(ContentRelationDto dto) throws GlobalException;

	/**
	 * 通过内容id查找相关内容
	 *
	 * @param contentId 内容id
	 * @return 相关内容列表
	 */
	List<ContentRelation> findByContentId(Integer contentId);
}
