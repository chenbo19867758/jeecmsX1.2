/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.weibo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyBeanUtils;
import com.jeecms.common.weibo.api.user.FansService;
import com.jeecms.common.weibo.bean.request.user.WeiboFansRequest;
import com.jeecms.common.weibo.bean.response.user.WeiboFansResponse.WeiboFansCommon;
import com.jeecms.weibo.dao.WeiboFansDao;
import com.jeecms.weibo.domain.WeiboFans;
import com.jeecms.weibo.domain.WeiboInfo;
import com.jeecms.weibo.service.WeiboFansService;
import com.jeecms.weibo.service.WeiboInfoService;

/**
 * 微博粉丝实现类
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-06-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WeiboFansServiceImpl extends BaseServiceImpl<WeiboFans, WeiboFansDao, Integer>
		implements WeiboFansService {

	@Autowired
	private FansService fansService;
	@Autowired
	private WeiboInfoService weiboInfoService;

	@Override
	public ResponseInfo sync(Integer id) throws GlobalException {
		List<WeiboFans> list = new ArrayList<WeiboFans>(10);
		// 根据id查询授权账户
		WeiboInfo info = weiboInfoService.findById(id);
		// 先删除全部粉丝
		deleteFans(Long.valueOf(info.getUid()));
		List<WeiboFansCommon> commons = fansService
				.getWeiboFans(new WeiboFansRequest(info.getAccessToken(), 
						Long.valueOf(info.getUid())));
		for (WeiboFansCommon common : commons) {
			WeiboFans fans = new WeiboFans();
			MyBeanUtils.copyProperties(common, fans);
			fans.setUid(Long.valueOf(info.getUid()));
			list.add(fans);
		}
		super.saveAll(list);
		return new ResponseInfo();
	}

	@Override
	public void deleteFans(Long uid) throws GlobalException {
		dao.deleteAllFans(uid);
	}

}