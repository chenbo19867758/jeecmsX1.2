/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.weibo.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.constants.SysConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.exception.error.UserErrorCodeEnum;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.common.weibo.api.article.WeiboArticleService;
import com.jeecms.common.weibo.bean.request.article.ArticlePushRequest;
import com.jeecms.common.weibo.bean.response.article.ArticlePushResponse;
import com.jeecms.content.constants.ContentConstant;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentTxt;
import com.jeecms.content.service.ContentService;
import com.jeecms.weibo.constants.WeiboConstants;
import com.jeecms.weibo.dao.WeiboArticlePushDao;
import com.jeecms.weibo.domain.WeiboArticlePush;
import com.jeecms.weibo.domain.WeiboInfo;
import com.jeecms.weibo.domain.dto.PreviewDto;
import com.jeecms.weibo.domain.dto.PushDto;
import com.jeecms.weibo.domain.vo.WeiboArticleVo;
import com.jeecms.weibo.service.WeiboArticlePushService;
import com.jeecms.weibo.service.WeiboInfoService;

/**
 * 微博推送Service
* @author ljw
* @version 1.0
* @date 2019-06-18
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class WeiboArticlePushServiceImpl extends BaseServiceImpl<WeiboArticlePush,WeiboArticlePushDao, Integer>  
			implements WeiboArticlePushService {

	@Autowired
	private WeiboArticleService weiboArticleService;
	@Autowired
	private ContentService contentService;
	@Autowired
	private WeiboInfoService weiboInfoService;
	
	@Override
	public Page<WeiboArticlePush> page(Long uid, String title,Integer status, Pageable pageable) 
				throws GlobalException {
		Map<String, String[]> params = new HashMap<String, String[]>(16);
		if (uid != null) {
			params.put("EQ_uid_String", new String[] { uid.toString() });
		}
		if (StringUtils.isNotBlank(title)) {
			params.put("LIKE_title_String", new String[] { title });
		}
		if (status != null) {
			params.put("EQ_pushResult_Integer", new String[] { status.toString() });
		}
		return super.getPage(params, pageable, false);
	}
	
	@Override
	public ResponseInfo push(PushDto dto) throws GlobalException {
		for (Integer contentId : dto.getContentIds()) {
			StringBuilder builder = new StringBuilder();
			//查询内容
			Content content = contentService.findById(contentId);
			if (!content.getWeiboPushContentAble()) {
				return new ResponseInfo(UserErrorCodeEnum
						.ALREADY_DATA_NOT_OPERATION.getCode(),
						UserErrorCodeEnum
						.ALREADY_DATA_NOT_OPERATION.getDefaultMessage(), false);
			}
			//判断是否发布状态
			if (content.getStatus() != ContentConstant.STATUS_PUBLISH) {
				return new ResponseInfo(RPCErrorCodeEnum.PUSH_ERROR_CONTENT_UNPUBLISH.getCode(),
					RPCErrorCodeEnum.PUSH_ERROR_CONTENT_UNPUBLISH.getDefaultMessage(), false);
			}
			List<ContentTxt> txts = content.getContentTxts();
			if (txts.isEmpty()) {
				return new ResponseInfo(RPCErrorCodeEnum.PUSH_ERROR_MAINBODY_NOTNULL.getCode(),
					RPCErrorCodeEnum.PUSH_ERROR_MAINBODY_NOTNULL.getDefaultMessage(), false);
			}
			for (ContentTxt contentTxt : txts) {
				builder.append(contentTxt.getAttrTxt());
			}
			//获取当前域名
			String uri = RequestUtils.getServerUrl(RequestUtils.getHttpServletRequest());
			//如果不存在内容图片地址，默认给图片
			if (content.getContentExt().getReData() != null 
					&& StringUtils.isNotBlank(content.getContentExt().getReData().getUrl())) {
				uri += content.getContentExt().getReData().getUrl();
			} else {
				uri += SysConstants.TPL_BASE + "/weibo/tc.service.png";
			}
			for (Integer weiboId : dto.getWeiboIds()) {
				//查询微博账户
				WeiboInfo info = weiboInfoService.findById(weiboId);
				ArticlePushRequest request = new ArticlePushRequest();
				request.setContent(builder.toString());
				request.setTitle(content.getTitle());
				request.setCover(uri);
				request.setText("发表了头条文章：《" + content.getTitle() + "》");
				request.setAccessToken(info.getAccessToken());
				ArticlePushResponse response = weiboArticleService.push(request);
				//新增推送记录
				WeiboArticlePush push = new WeiboArticlePush();
				//判断推送是否成功
				if (null != response.getCode() && response.getCode().equals(100000)) {
					push.setPushResult(WeiboConstants.PUSH_ARTICLE_SUCCESS);
				} else {
					push.setPushResult(WeiboConstants.PUSH_ARTICLE_FAILED);
				}
				push.setArticleWeiboUrl(response.getData() != null ? response.getData().getUrl() : "");
				push.setArticleSourceUrl(content.getUrlWhole());
				push.setContentId(content.getId());
				push.setUid(info.getUid());
				push.setTitle(content.getTitle());
				push.setSiteId(dto.getSiteId());
				super.save(push);	
			}
		}
		return new ResponseInfo();
	}

	@Override
	public ResponseInfo preview(PreviewDto dto) throws GlobalException {
		StringBuilder builder = new StringBuilder();
		//查询内容
		Content content = contentService.findById(dto.getContentId());
		List<ContentTxt> txts = content.getContentTxts();
		for (ContentTxt contentTxt : txts) {
			builder.append(contentTxt.getAttrTxt());
		}
		//获取当前域名
		String uri = RequestUtils.getServerUrl(RequestUtils.getHttpServletRequest());
		//如果不存在内容图片地址，默认给图片
		if (content.getContentExt() != null 
				&& content.getContentExt().getReData() != null 
				&& StringUtils.isNotBlank(content.getContentExt().getReData().getUrl())) {
			uri += content.getContentExt().getReData().getUrl();
		} else {
			uri += SysConstants.TPL_BASE + "/weibo/tc.service.png";
		}
		//查询微博账户
		WeiboInfo info = weiboInfoService.findById(dto.getWeiboId());
		WeiboArticleVo vo = new WeiboArticleVo();
		vo.setContentId(dto.getContentId());
		vo.setWeiboId(info.getId());
		vo.setContent(builder.toString());
		vo.setCover(uri);
		vo.setProfileImageUrl(info.getProfileImageUrl());
		vo.setPushDate(new Date());
		vo.setScreenName(info.getScreenName());
		vo.setSummary("");
		vo.setText("发表了头条文章：《" + content.getTitle() + "》");
		vo.setTitle(content.getTitle());
		return new ResponseInfo(vo);
	}
	
	@Override
	public ResponseInfo updatePush(PreviewDto dto) throws GlobalException {
		//查询微博账户
		WeiboInfo info = weiboInfoService.findById(dto.getWeiboId());
		ArticlePushRequest request = new ArticlePushRequest();
		request.setContent(dto.getContent());
		request.setTitle(dto.getTitle());
		request.setCover(dto.getCover());
		request.setText(dto.getText());
		request.setSummary(dto.getSummary());
		request.setAccessToken(info.getAccessToken());
		ArticlePushResponse response = weiboArticleService.push(request);
		//新增推送记录
		WeiboArticlePush push = new WeiboArticlePush();
		//判断推送是否成功
		if (null != response.getCode() && response.getCode().equals(100000)) {
			push.setPushResult(WeiboConstants.PUSH_ARTICLE_SUCCESS);
		} else {
			push.setPushResult(WeiboConstants.PUSH_ARTICLE_FAILED);
		}
		Content content = contentService.findById(dto.getContentId());
		push.setArticleWeiboUrl(response.getData().getUrl());
		push.setArticleSourceUrl(content.getUrlWhole());
		push.setContentId(dto.getContentId());
		push.setUid(info.getUid());
		push.setTitle(dto.getTitle());
		push.setSiteId(dto.getSiteId());
		super.save(push);	
		return new ResponseInfo();
	}

 
}