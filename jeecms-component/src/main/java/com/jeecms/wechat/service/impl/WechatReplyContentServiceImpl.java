package com.jeecms.wechat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.wechat.dao.WechatReplyContentDao;
import com.jeecms.wechat.domain.WechatReplyContent;
import com.jeecms.wechat.domain.WechatReplyKeyword;
import com.jeecms.wechat.service.WechatReplyClickService;
import com.jeecms.wechat.service.WechatReplyContentService;
import com.jeecms.wechat.service.WechatReplyKeywordService;

/**
 * 关键词回复内容service实现类
 * @author: chenming
 * @date:   2019年6月3日 上午9:43:52
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatReplyContentServiceImpl extends BaseServiceImpl<WechatReplyContent, WechatReplyContentDao, Integer>
		implements WechatReplyContentService {

	@Autowired
	WechatReplyKeywordService wechatReplyKeywordService;
	@Autowired
	WechatReplyClickService wechatReplyClickService;
	

	@Override
	public WechatReplyContent getId(Integer id) {
		return dao.findByIdAndHasDeleted(id, false);
	}

	/**
	 * 是否禁用
	 */
	@Override
	public ResponseInfo isEnable(Integer id, Boolean isEnable) throws GlobalException {
		WechatReplyContent content = super.findById(id);
		content.setIsEnable(isEnable);
		return new ResponseInfo(super.update(content));
	}

	/**
	 * 修改关键词
	 */
	@Override
	public ResponseInfo updateKeyWord(WechatReplyContent content) throws GlobalException {
		List<WechatReplyKeyword> wechatReplyKeyword = content.getWechatReplyKeywordList();
		for (WechatReplyKeyword keyword : wechatReplyKeyword) {
			// ID为空 则为添加数据 否则为修改数据
			if (keyword.getId() != null) {
				wechatReplyKeywordService.update(keyword);
			} else {
				WechatReplyKeyword replyKeyword = new WechatReplyKeyword();
				replyKeyword.setAppId(content.getAppId());
				replyKeyword.setReplyContentId(content.getId());
				if (keyword.getWordkeyEq() != null) {
					replyKeyword.setWordkeyEq(keyword.getWordkeyEq());
				} else {
					replyKeyword.setWordkeyLike(keyword.getWordkeyLike());
				}
				wechatReplyKeywordService.save(replyKeyword);
			}

		}
		content.getWechatReplyKeywordList().clear();
		super.update(content);
		return new ResponseInfo();
	}

	/**
	 * 删除关键词
	 */
	@Override
	public ResponseInfo deleteKeyWork(Integer[] ids) throws GlobalException {
		for (Integer id : ids) {
			WechatReplyContent content = super.findById(id);
			if (content != null && content.getWechatReplyKeywordList() != null) {
				List<WechatReplyKeyword> list = content.getWechatReplyKeywordList();
				for (WechatReplyKeyword keyword : list) {
					wechatReplyKeywordService.delete(keyword.getId());
				}
			}
			super.delete(id);
		}
		return new ResponseInfo();
	}

	@Override
	public Page<WechatReplyContent> getPage(String appId,Pageable pageable,String searchStr) throws GlobalException {
		List<Integer> ids = wechatReplyClickService.findByAppId(appId);
		return dao.getPage(appId,ids,pageable,searchStr);
	}

}