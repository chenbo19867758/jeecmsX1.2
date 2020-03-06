/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.domain.vo;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 推送记录素材VO
 * 
 * @author: ljw
 * @date: 2019年6月10日 下午6:21:36
 */
public class MaterialVO {

	/** 素材ID **/
	private Integer id;
	/** 微信素材ID **/
	private String materialId;
	/** 素材类型 **/
	private String mediaType;
	/** 素材名称 **/
	private String mediaName;
	/** 素材url **/
	private String mediaUrl;
	/** 图文素材 **/
	private List<JSONObject> objects;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public List<JSONObject> getObjects() {
		return objects;
	}

	public void setObjects(List<JSONObject> objects) {
		this.objects = objects;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

}
