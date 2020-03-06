/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.channel.domain.dto;

import java.util.List;

/**
 * 栏目可选内容模型Dto
 * 
 * @author: ljw
 * @date: 2019年12月12日 下午2:41:14
 */
public class ChannelSeleteDto {

	/** 栏目ID **/
	private Integer channelId;
	/** 可选内容模型集合 **/
	private List<Integer> modelIds;

	public ChannelSeleteDto() {
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public List<Integer> getModelIds() {
		return modelIds;
	}

	public void setModelIds(List<Integer> modelIds) {
		this.modelIds = modelIds;
	}

}
