package com.jeecms.common.wechat.bean.request.mp.material;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 
 * @Description: 删除永久素材：request
 * @author: chenming
 * @date:   2018年7月30日 下午2:55:08     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class DelMaterialRequest {
	/** 要获取的素材的media_id*/
	private String mediaId;
	/** id*/ 
	private Integer id;

	@NotBlank
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public DelMaterialRequest(String mediaId, Integer id) {
		super();
		this.mediaId = mediaId;
		this.id = id;
	}

	public DelMaterialRequest() {
		super();
	}

	@NotNull
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
