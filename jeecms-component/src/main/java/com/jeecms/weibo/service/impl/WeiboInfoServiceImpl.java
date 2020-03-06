/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.weibo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.WeChatExceptionInfo;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyBeanUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.weibo.api.user.WeiboUserService;
import com.jeecms.common.weibo.bean.request.user.WeiboUserRequest;
import com.jeecms.common.weibo.bean.response.user.WeiboUserResponse;
import com.jeecms.weibo.dao.WeiboInfoDao;
import com.jeecms.weibo.domain.WeiboInfo;
import com.jeecms.weibo.domain.vo.WeiboTokenVO;
import com.jeecms.weibo.service.WeiboInfoService;

/**
 * 微博信息Service
* @author ljw
* @version 1.0
* @date 2019-06-17
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class WeiboInfoServiceImpl extends BaseServiceImpl<WeiboInfo,WeiboInfoDao, Integer>  
			implements WeiboInfoService {

	@Autowired
	private WeiboUserService weiboUserService;
	
	@Override
	public ResponseInfo saveWeiboInfo(WeiboTokenVO vo, Integer siteId) throws GlobalException {
		if (!StringUtils.isNotBlank(vo.getAccessToken())) {
			return new ResponseInfo();
		}
		//根据uid 查询是否存在该账号，与站点ID做对比，站点相同则更新，反之提示错误信息
		WeiboInfo weibo = findWeiboInfo(vo.getUid());
		if (weibo != null && weibo.getSiteId().equals(siteId)) {
			weibo.setAccessToken(vo.getAccessToken());
			//设置过期时间，需减去5分钟
			Long sum = vo.getExpiresIn() - 300;
			Long expires = new Date().getTime() + sum * 1000;
			weibo.setAuthExpireTime(new Date(expires));
			super.update(weibo);
			return new ResponseInfo("200","系统检测到微博账号已授权，将自动做续期处理！",true);
		}
		if (weibo != null && !weibo.getSiteId().equals(siteId)) {
			return new ResponseInfo("500","微博账号已授权给站点，请更换账号再次进行授权!",false);
		}
		
		WeiboInfo info = new WeiboInfo();
		WeiboUserResponse response = weiboUserService.getWeiboUser(new WeiboUserRequest(vo.getAccessToken(),
				Long.valueOf(vo.getUid())));
		MyBeanUtils.copyProperties(response, info);
		info.setUid(response.getIdstr());
		info.setSiteId(siteId);
		info.setIsSetAdmin(false);
		//设置token
		info.setAccessToken(vo.getAccessToken());
		//设置过期时间，需减去5分钟
		Long sum = vo.getExpiresIn() - 300;
		Long expires = new Date().getTime() + sum * 1000;
		info.setAuthExpireTime(new Date(expires));
		super.save(info);
		return new ResponseInfo("200","授权成功",true);
	}

	@Override
	public WeiboInfo findWeiboInfo(String uid) throws GlobalException {
		return dao.findByUidAndHasDeleted(uid,false);
	}

	@Override
	public List<WeiboInfo> findList(Integer siteId) throws GlobalException {
		return dao.findBySiteIdAndHasDeleted(siteId,false);
	}

	@Override
	public void checkWeiboAuth(Integer userId, Integer id) throws GlobalException {
		WeiboInfo weiboInfo = super.findById(id);
		String code = null;
		String message = null;
		if (weiboInfo == null) {
			code = UserErrorCodeEnum.NO_OPERATE_WECHAT_PERMISSION.getCode();
			message = MessageResolver.getMessage(code,
					UserErrorCodeEnum.NO_OPERATE_WECHAT_PERMISSION.getDefaultMessage());
			throw new GlobalException(new WeChatExceptionInfo(code, message));
		}
		List<Integer> userIds = weiboInfo.getCoreUsers().stream()
				.map(CoreUser::getId).collect(Collectors.toList());
		// 判断公众号当前设置管理员及用户是否为管理员中一员
		if (weiboInfo.getIsSetAdmin() && !userIds.contains(userId)) {
			code = UserErrorCodeEnum.NO_OPERATE_WECHAT_PERMISSION.getCode();
			message = MessageResolver.getMessage(code,
					UserErrorCodeEnum.NO_OPERATE_WECHAT_PERMISSION.getDefaultMessage());
			throw new GlobalException(new WeChatExceptionInfo(code, message));
		}
	}
 
}