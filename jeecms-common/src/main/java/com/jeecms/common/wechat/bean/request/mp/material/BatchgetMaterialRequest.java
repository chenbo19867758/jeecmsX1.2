package com.jeecms.common.wechat.bean.request.mp.material;

/**
 * 
 * @Description: 获取素材列表
 * @author: chenming
 * @date:   2018年7月30日 下午3:01:10     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class BatchgetMaterialRequest {
	/** 素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）*/
	private String type;
	/** 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回*/
	private Integer offset;
	/** 返回素材的数量，取值在1到20之间*/ 	
	private Integer count;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	public BatchgetMaterialRequest(String type, Integer offset, Integer count) {
		super();
		this.type = type;
		this.offset = offset;
		this.count = count;
	}
	public BatchgetMaterialRequest() {
		super();
	}
	
}
