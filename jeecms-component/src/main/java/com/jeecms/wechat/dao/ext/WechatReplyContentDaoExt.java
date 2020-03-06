package com.jeecms.wechat.dao.ext;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.wechat.domain.WechatReplyContent;

public interface WechatReplyContentDaoExt {
	
	Page<WechatReplyContent> getPage(String appId,List<Integer> ids,Pageable pageable,String searchStr);
}
