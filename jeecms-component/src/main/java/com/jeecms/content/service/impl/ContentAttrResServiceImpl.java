/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.content.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.dao.ContentAttrResDao;
import com.jeecms.content.domain.ContentAttrRes;
import com.jeecms.content.service.ContentAttrResService;

/**
 * 多资源service实现类
 * @author: chenming
 * @date:   2019年5月23日 上午10:57:55
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentAttrResServiceImpl extends BaseServiceImpl<ContentAttrRes, ContentAttrResDao, Integer>
		implements ContentAttrResService {

	@Override
	public void deleteByContentAttrs(List<Integer> ids) throws GlobalException {
		List<ContentAttrRes> contentAttrRes = dao.findByResIdIn(ids);
		super.physicalDeleteInBatch(contentAttrRes);
	}

	@Override
	public List<Integer> getSecretByRes(Integer resId) throws GlobalException {
		List<Integer> secrets = new ArrayList<Integer>(10);
		List<ContentAttrRes> contentAttrRes = dao.findByResIdIn(Arrays.asList(resId));
		if (!contentAttrRes.isEmpty()) {
			//过滤密级ID为空的情况
			secrets = contentAttrRes.stream().filter(x -> x.getSecretId() != null)
			.map(ContentAttrRes::getSecretId).collect(Collectors.toList());
			return secrets;
		}
		return secrets;
	}

}