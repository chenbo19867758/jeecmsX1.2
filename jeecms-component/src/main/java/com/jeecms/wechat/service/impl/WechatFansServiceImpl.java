/**
 *  * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.wechat.api.mp.UserManageService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.message.MessageRequest;
import com.jeecms.common.wechat.bean.request.mp.user.BatchblackListRequest;
import com.jeecms.common.wechat.bean.request.mp.user.BlackFansListRequest;
import com.jeecms.common.wechat.bean.request.mp.user.UserInfoRequest;
import com.jeecms.common.wechat.bean.request.mp.user.UserListRequest;
import com.jeecms.common.wechat.bean.response.mp.BaseResponse;
import com.jeecms.common.wechat.bean.response.mp.user.MessageResponse;
import com.jeecms.common.wechat.bean.response.mp.user.UserInfoListResponse;
import com.jeecms.common.wechat.bean.response.mp.user.UserInfoResponse;
import com.jeecms.common.wechat.bean.response.mp.user.UserListResponse;
import com.jeecms.wechat.constants.WechatConstants;
import com.jeecms.wechat.dao.WechatFansDao;
import com.jeecms.wechat.domain.WechatComment;
import com.jeecms.wechat.domain.WechatFans;
import com.jeecms.wechat.domain.WechatFansExt;
import com.jeecms.wechat.domain.WechatFansSendLog;
import com.jeecms.wechat.domain.WechatMaterial;
import com.jeecms.wechat.domain.vo.WechatFansVO;
import com.jeecms.wechat.service.WechatCommentService;
import com.jeecms.wechat.service.WechatFansExtService;
import com.jeecms.wechat.service.WechatFansSendLogService;
import com.jeecms.wechat.service.WechatFansService;
import com.jeecms.wechat.service.WechatMaterialService;

/**
 * 微信粉丝实现类
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-05-28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatFansServiceImpl extends BaseServiceImpl<WechatFans, WechatFansDao, Integer>
		implements WechatFansService {

	@Autowired
	private WechatFansSendLogService logService;
	@Autowired
	private WechatCommentService commentService;
	@Autowired
	private UserManageService userManageService;
	@Autowired
	private WechatFansSendLogService wechatFansSendLogService;
	@Autowired
	private WechatMaterialService wechatMaterialService;
	@Autowired
	private WechatFansExtService wechatFansExtService;

	@Override
	public ResponseInfo sync(String appid) throws GlobalException {
		UserListRequest userListRequest = new UserListRequest();
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(appid);
		// 获取该公众号下全部粉丝
		List<UserInfoListResponse> list = userManageService.getUserList(userListRequest, validToken);
		//删除全部的粉丝扩展
		List<WechatFans> fans = getListByAppid(Arrays.asList(appid));
		if (!fans.isEmpty()) {
			List<String> openids = fans.stream().map(WechatFans::getOpenid).collect(Collectors.toList());
			wechatFansExtService.deleteAllFansExt(openids);
		}
		// 同步之前删除之前全部的粉丝
		dao.deleteAllFans(appid);
		if (list != null) {
			for (UserInfoListResponse userInfoListResponse : list) {
				List<UserInfoResponse> list2 = userInfoListResponse.getUserInfoList();
				List<WechatFans> wechatFansList = new ArrayList<>();
				for (UserInfoResponse user : list2) {
					// 这里微信会将公众号返回，公众号不存在一些粉丝信息，也不是粉丝，所以此处将其拦截
					if (user.getSubscribeTime() == null) {
						continue;
					}
					WechatFansExt ext = new WechatFansExt();
					ext.setCommentCount(0);
					ext.setMesCount(0);
					ext.setTopCommentCount(0);
					ext.setOpenid(user.getOpenid());
					
					WechatFans wechatFans = new WechatFans(user);
					wechatFans.addExt(ext);
					// 是否是黑名单
					wechatFans.setIsBlackList(false);
					wechatFans.setAppId(appid);
					wechatFansList.add(wechatFans);
				}
				super.saveAll(wechatFansList);
			}
		}
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo black(String appid, Integer[] ids) throws GlobalException {
		List<WechatFans> list = super.findAllById(Arrays.asList(ids));
		List<String> stringList = new ArrayList<>();
		for (WechatFans wechatFans : list) {
			stringList.add(wechatFans.getOpenid());
		}
		BatchblackListRequest batchblackListRequest = new BatchblackListRequest();
		batchblackListRequest.setOpenidList(stringList);
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(appid);
		BaseResponse response = userManageService.batchBlack(batchblackListRequest, validToken);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			for (WechatFans wechatFans : list) {
				wechatFans.setIsBlackList(true);
				super.update(wechatFans);
			}
		}
		return new ResponseInfo();
	}
	
	@Override
	public ResponseInfo syncblack(String appid) throws GlobalException {
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(appid);
		BlackFansListRequest blackFansListRequest = new BlackFansListRequest();
		UserListResponse response = userManageService.batchBlackList(blackFansListRequest, validToken);
		List<String> openIds = response.getData().getOpenid();
		if (!openIds.isEmpty()) {
			List<WechatFans> fans = dao.findByOpenidIn(openIds);
			for (WechatFans wechatFans : fans) {
				wechatFans.setIsBlackList(true);
			}
			super.batchUpdate(fans);
		}
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo blackList(String appid, String nickname,Pageable pageable) throws GlobalException {
		Map<String, String[]> params = new HashMap<String, String[]>(16);
		params.put("EQ_appId_String", new String[] { appid });
		params.put("EQ_isBlackList_Boolean", new String[] { "true" });
		if (StringUtils.isNotBlank(nickname)) {
			params.put("LIKE_nickname_String", new String[] { nickname });
		}
		Page<WechatFans> fans = getPage(params, pageable, true);
		return new ResponseInfo(fans);
	}


	@Override
	public ResponseInfo send(String appid, MessageRequest messageRequest) throws GlobalException {
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(appid);
		WechatFansSendLog log = new WechatFansSendLog();
		log.setSendType(WechatConstants.SEND_TYPE_WECHAT);
		log.setReply(false);
		log.setCollect(false);
		WechatMaterial material = null;
		String text = "text";
		String mpnews = "mpnews";
		if (!messageRequest.getMsgtype().equalsIgnoreCase(text)) {
			material = wechatMaterialService.getMediaId(messageRequest.getMediaId());
			if (messageRequest.getMsgtype().equalsIgnoreCase(mpnews)) {
				StringBuilder builder = new StringBuilder();
				builder.append("{\"articles\":").append(material.getMaterialJson()).append("}");
				log.setMediaJson(builder.toString());
			} else {
				log.setMediaJson(material.getMaterialJson());
			}
		} else {
			JSONObject object = new JSONObject();
			object.put("msgType", text);
			object.put("content", StrUtils.escapeUnicode(messageRequest.getContent()));
			log.setMediaJson(object.toJSONString());
		}
		MessageResponse response = userManageService.send(messageRequest, validToken);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			log.setAppId(appid);
			log.setOpenId(messageRequest.getTouser());
			// 发送成功为1，失败为2
			log.setSendStatus(WechatConstants.SEND_MESSAGE_SUCCESS);
			log.setMsgType(messageRequest.getMsgtype());
			wechatFansSendLogService.save(log);
		} else {
			log.setAppId(appid);
			log.setOpenId(messageRequest.getTouser());
			// 发送成功为1，失败为2
			log.setSendStatus(WechatConstants.SEND_MESSAGE_FAILED);
			log.setMsgType(messageRequest.getMsgtype());
			wechatFansSendLogService.save(log);
		}
		return new ResponseInfo();
	}

	@Override
	public Integer timeCount(Long startTime, Long endTime, String appId) throws GlobalException {
		return dao.selectFans(appId, startTime, endTime);
	}

	@Override
	public Long fansCount(String appId) throws GlobalException {
		Map<String, String[]> map = new HashMap<>(16);
		map.put("LIKE_appId_String", new String[] { appId });
		return super.count(map);
	}

	@Override
	public ResponseInfo syncFans(String appid, Integer[] ids) throws GlobalException {
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(appid);
		List<WechatFans> fans = super.findAllById(Arrays.asList(ids));
		List<String> list = new ArrayList<String>();
		for (WechatFans wechatFans : fans) {
			list.add(wechatFans.getOpenid());
		}
		UserInfoRequest userInfoRequest = new UserInfoRequest(list);
		UserInfoListResponse response = userManageService.fansMore(userInfoRequest, validToken);
		// 获取全部第三方会员信息
		// List<MemberThird> thirds = memberThirdService.getThirds(appid);
		// 删除要同步的粉丝ID
		super.physicalDeleteInBatch(fans);
		List<UserInfoResponse> userInfoResponses = response.getUserInfoList();
		List<WechatFans> wechatFansList = new ArrayList<>();
		for (UserInfoResponse user : userInfoResponses) {
			WechatFans wechatFans = new WechatFans(user);
			// 是否是黑名单
			wechatFans.setIsBlackList(false);
			wechatFans.setAppId(appid);
			wechatFansList.add(wechatFans);
		}
		super.saveAll(wechatFansList);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo getFansPage(List<String> appids, Pageable pageable, String nickname, 
			String tagid, Boolean black)
			throws GlobalException {
		Page<WechatFans> page = dao.selectFans(appids, nickname, tagid, black, pageable);
		return new ResponseInfo(page);
	}

	@Override
	public List<String> selectFansSex(String appId) throws GlobalException {
		return dao.selectFansSex(appId);
	}

	@Override
	public List<String> selectFansSubscribe(String appId) throws GlobalException {
		return dao.selectFansSubscribe(appId);
	}

	@Override
	public List<String> selectFansCountry(String appId) throws GlobalException {
		return dao.selectFansCountry(appId);
	}

	@Override
	public List<String> selectFansProvince(String appId) throws GlobalException {
		return dao.selectFansProvince(appId);
	}

	@Override
	public Integer selectFansCount(String appId) {
		return dao.selectFansCount(appId);
	}

	@Override
	public ResponseInfo cancelblack(String appid, Integer[] ids) throws GlobalException {
		List<WechatFans> list = super.findAllById(Arrays.asList(ids));
		List<String> stringList = new ArrayList<>();
		for (WechatFans wechatFans : list) {
			stringList.add(wechatFans.getOpenid());
		}
		BatchblackListRequest batchblackListRequest = new BatchblackListRequest();
		batchblackListRequest.setOpenidList(stringList);
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(appid);
		BaseResponse response = userManageService.cancelbatchBlack(batchblackListRequest, validToken);
		if (response.SUCCESS_CODE.equals(response.getErrcode())) {
			for (WechatFans wechatFans : list) {
				wechatFans.setIsBlackList(false);
				super.update(wechatFans);
			}
		} 
		return new ResponseInfo();
	}

	@Override
	public List<WechatFans> getListByAppid(List<String> appids) {
		return dao.findByAppIdIn(appids);
	}

	@Override
	public ResponseInfo getFans(String openId) throws GlobalException {
		WechatFans fans = dao.findByOpenid(openId);
		WechatFansExt ext = fans.getFansExt();
		//查询留言数
		List<WechatComment> comments = commentService.findByOpenId(openId);
		if (!comments.isEmpty()) {
			//精选留言数
			ext.setCommentCount(comments.size());
			Long top = comments.stream().filter(x -> x.getCommentType().equals(true)).count();
			ext.setTopCommentCount(top.intValue());
		}
		//查询消息数
		List<WechatFansSendLog> logs = logService.findByOpenId(openId);
		if (!logs.isEmpty()) {
			ext.setMesCount(logs.size());
		}
		return new ResponseInfo(fans);
	}

	@Override
	public List<WechatFansVO> fansVOs(List<String> appids, Integer type, String province) {
		return dao.fansVOs(appids, type, province);
	}


}