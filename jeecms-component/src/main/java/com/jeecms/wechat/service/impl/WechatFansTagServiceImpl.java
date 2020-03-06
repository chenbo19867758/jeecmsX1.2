/**
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.wechat.api.mp.UserManageService;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.user.TagsAddRequest;
import com.jeecms.common.wechat.bean.request.mp.user.TagsDeleteRequest;
import com.jeecms.common.wechat.bean.request.mp.user.TagsUpdateRequest;
import com.jeecms.common.wechat.bean.request.mp.user.UserTagsAddRequest;
import com.jeecms.common.wechat.bean.response.mp.user.TagsAddResponse;
import com.jeecms.common.wechat.bean.response.mp.user.TagsResponse;
import com.jeecms.common.wechat.bean.response.mp.user.TagsResponse.Tag;
import com.jeecms.wechat.dao.WechatFansTagDao;
import com.jeecms.wechat.domain.WechatFans;
import com.jeecms.wechat.domain.WechatFansTag;
import com.jeecms.wechat.domain.dto.WechatTagDto;
import com.jeecms.wechat.service.WechatFansService;
import com.jeecms.wechat.service.WechatFansTagService;

/**
 * 微信粉丝标签实现类
* @author ljw
* @version 1.0
* @date 2019-05-28
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatFansTagServiceImpl extends BaseServiceImpl<WechatFansTag,WechatFansTagDao, Integer>  
			implements WechatFansTagService {
	
	@Autowired
	private UserManageService userManageService;
	@Autowired
	private WechatFansService wechatFansService;
	
	@Override
	public ResponseInfo addTags(WechatTagDto dto) throws GlobalException {
		Boolean flag = check(dto.getAppId(),dto.getTagname());
		if (!flag) {
			return new ResponseInfo(RPCErrorCodeEnum.WECHAT_TAGNAME_ALREADY_EXISTED.getCode(),
					RPCErrorCodeEnum.WECHAT_TAGNAME_ALREADY_EXISTED.getDefaultMessage(), false);
		}
		TagsAddRequest tagsAddRequest = new TagsAddRequest();
		TagsAddRequest.Tag tags = tagsAddRequest.new Tag();
		tags.setName(dto.getTagname());
		tagsAddRequest.setTag(tags);
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(dto.getAppId());
		TagsAddResponse tag = userManageService.addTags(tagsAddRequest, validToken);
		WechatFansTag wechatFanTag = new WechatFansTag();
		wechatFanTag.setId(tag.getTag().getId());
		wechatFanTag.setTagName(tag.getTag().getName());
		wechatFanTag.setAppId(dto.getAppId());
		wechatFanTag.setUserCount(0);
		wechatFanTag = super.save(wechatFanTag);
		return new ResponseInfo(wechatFanTag);
	}

	@Override
	public ResponseInfo setTags(WechatTagDto dto) throws GlobalException {
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(dto.getAppId());
		List<Integer> integers = dto.getIds();
		List<WechatFans> fans = wechatFansService.findAllById(integers);
		List<String> openlist = new ArrayList<>();
		for (WechatFans wechatFans : fans) {
			openlist.add(wechatFans.getOpenid());
		}
		List<Integer> tagids = dto.getTagids();
		//遍历标签，给微信发请求
		StringBuilder sb = new StringBuilder();
		for (Integer integer : tagids) {
			sb.append(integer + ",");
			UserTagsAddRequest userTagsAddRequest = new UserTagsAddRequest();
			userTagsAddRequest.setOpenidList(openlist);
			userTagsAddRequest.setTagid(integer);
			userManageService.addUserTags(userTagsAddRequest, validToken);			
		}
		//修改粉丝的标签列表
		for (WechatFans wechatFans : fans) {
			LinkedList<Integer> cwa = new LinkedList<Integer>(tagids);
			String listString = wechatFans.getTagidList();
			//判断粉丝标签列表是否为空
			if (StringUtils.isNotBlank(listString)) {
				String oldString = wechatFans.getTagidList().substring(0, listString.length() - 1);
				String[] shStrings = oldString.split(",");
				List<String> list = Arrays.asList(shStrings);
				for (String old : list) {
					Integer rm = Integer.parseInt(old);
					//判断之前的粉丝标签列表是否包含现在的标签，不包含则将标签人数减1
					if (!cwa.contains(rm)) {
						WechatFansTag tag = super.findById(rm);	
						tag.setUserCount(tag.getUserCount() - 1);
						super.update(tag);
					} else {
						cwa.remove(rm);
					}
				}
			}
			//更新标签人数
			if (cwa != null) {
				List<WechatFansTag> tags = super.findAllById(cwa);
				for (WechatFansTag tag : tags) {
					tag.setUserCount(tag.getUserCount() + 1);
					super.update(tag);
				}
			}
			wechatFans.setTagidList(sb.toString());
			wechatFansService.update(wechatFans);
		}	
		return new ResponseInfo();						
	}	

	@Override
	public ResponseInfo syncTags(String appid) throws GlobalException {
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(appid);
		TagsResponse tagsResponse = userManageService.tags(validToken);
		//同步之前先删除改公众号下的全部的标签
		deleteTags(appid);
		List<Tag> tags = tagsResponse.getTags();
		List<WechatFansTag> list = new ArrayList<WechatFansTag>();
		for (Tag tag : tags) {
			WechatFansTag fanTag = new WechatFansTag();	
			fanTag.setAppId(appid);
			fanTag.setId(tag.getId());
			fanTag.setTagName(tag.getName());
			fanTag.setUserCount(tag.getCount());
			list.add(fanTag);
		}
		super.saveAll(list);
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo tags(String appid) throws GlobalException {
		JSONObject jsonObject = new JSONObject();	
		List<WechatFansTag> list = selectTags(appid);	
		Map<String, String[]> params = new HashMap<String, String[]>(16);
		params.put("EQ_appId_String", new String[]{appid});		
		long count = wechatFansService.count(params);	
		count(params);
		//分别把标签列表和粉丝数放入jsonObject中
		jsonObject.put("tagList", list);
		jsonObject.put("fansSize", count);	
		return new ResponseInfo(jsonObject);
	}
	
	@Override
	public ResponseInfo deleteTags(String appid, Integer tagid) throws GlobalException {
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(appid);
		TagsDeleteRequest tagsDeleteRequest = new TagsDeleteRequest();
		TagsDeleteRequest.Tag tags = tagsDeleteRequest.new Tag();
		tags.setId(tagid);
		tagsDeleteRequest.setTag(tags);
		userManageService.deleteTags(tagsDeleteRequest, validToken);
		dao.deleteTags(appid, tagid);
		return new ResponseInfo();
	}
	
	@Override
	public void deleteTags(String appid) {
		dao.deleteTags(appid);
	}
	
	@Override
	public ResponseInfo updateTags(WechatTagDto dto) throws GlobalException {
		Boolean flag = check(dto.getAppId(),dto.getTagname());
		if (!flag) {
			return new ResponseInfo(RPCErrorCodeEnum.WECHAT_TAGNAME_ALREADY_EXISTED.getCode(),
					RPCErrorCodeEnum.WECHAT_TAGNAME_ALREADY_EXISTED.getDefaultMessage(), false);
		}
		ValidateToken validToken = new ValidateToken();
		validToken.setAppId(dto.getAppId());		
		TagsUpdateRequest tagsUpdateRequest = new TagsUpdateRequest();		
		TagsUpdateRequest.Tag tags = tagsUpdateRequest.new Tag();
		tags.setName(dto.getTagname());
		tags.setId(dto.getTagid());	
		tagsUpdateRequest.setTag(tags);
		userManageService.updateTags(tagsUpdateRequest, validToken);
		WechatFansTag wechatFanTag = selectTags(dto.getAppId(),dto.getTagid());
		wechatFanTag.setTagName(dto.getTagname());
		wechatFanTag = super.update(wechatFanTag);
		return new ResponseInfo(wechatFanTag);
	}
	
	@Override
	public List<WechatFansTag> selectTags(String appid) {
		return dao.selectTags(appid);
	}

	@Override
	public WechatFansTag selectTags(String appid, Integer id) {
		return dao.selectTags(appid, id);
	}

	@Override
	public Boolean check(String appid, String tagName) {
		Map<String, String[]> params = new HashMap<String, String[]>(2);
		tagName = java.text.Normalizer.normalize(tagName, java.text.Normalizer.Form.NFKD);
		params.put("EQ_tagName_String", new String[] { tagName });
		params.put("EQ_appId_String", new String[] { appid });
		List<WechatFansTag> tags = super.getList(params, null, false);
		if (tags != null && !tags.isEmpty()) {
			return false;
		}
		return true;
	}
	
}