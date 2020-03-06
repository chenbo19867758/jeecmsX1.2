package com.jeecms.wechat.domain.dto;

import java.util.Map;

import com.jeecms.wechat.domain.AbstractWeChatInfo;
import com.jeecms.wechat.domain.WechatFansStatistics;

/**
 * 粉丝表数据  和  公众号信息数据
 * <p>Title:FansAndInfo</p>
 * @author wulongwei
 * @date 2018年8月7日
 */
public class FansAndInfo {

	private Map<String, WechatFansStatistics> fansStatistics;
	private AbstractWeChatInfo abstractWeChatInfo;

	public Map<String, WechatFansStatistics> getFansStatistics() {
		return fansStatistics;
	}

	public void setFansStatistics(Map<String, WechatFansStatistics> fansStatistics) {
		this.fansStatistics = fansStatistics;
	}

	public AbstractWeChatInfo getAbstractWeChatInfo() {
		return abstractWeChatInfo;
	}

	public void setAbstractWeChatInfo(AbstractWeChatInfo abstractWeChatInfo) {
		this.abstractWeChatInfo = abstractWeChatInfo;
	}

}
