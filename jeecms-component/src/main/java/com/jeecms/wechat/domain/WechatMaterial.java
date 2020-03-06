package com.jeecms.wechat.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.wechat.Const;

/**
 * 微信素材实体类
 * @author: chenming
 * @date:   2019年6月3日 下午4:48:57
 */
@Entity
@Table(name = "jc_wechat_material")
public class WechatMaterial extends AbstractDomain<Integer> {
	private static final long serialVersionUID = 1L;
	
	public static final String CHECK_VIDEO_URL = "http://mp.weixin.qq.com";
	
	public static final String IMAGE_NAME = "image";
	public static final String VOICE_NAME = "voice";
	public static final String VIDEO_NAME = "video";
	public static final List<String> IMAGE_TYPE = Arrays.asList("bmp","png","jpeg","jpg","gif");
	public static final List<String> VOICE_TYPE = Arrays.asList("mp3","wma","wav","amr");
	public static final List<String> VIDEO_TYPE = Arrays.asList("mp4","avi","wmv");
	public static final List<String> UPLOAD_IMAGE_TYPE = Arrays.asList("png","jpg");
	public static final List<String> TYPES = Arrays.asList("image","video","voice");
	
	/** 主键值 */
	private Integer id;
	/** 所属公众号appId */
	private String appId;
	/** 素材类型图片（image）、视频（video）、语音 （voice）、图文（news） */
	private String mediaType;
	/** 微信服务器标识的素材ID */
	private String mediaId;
	/** 素材json数据包，微信服务器返回 */
	private String materialJson;
	/** 如果查询的素材类型是非news类型，则会将materialJson转换成Json类型且set入该属性 */
	private JSONObject request = new JSONObject();
	/** 如果查询的素材类型是news类型，则会将materialJson转换成Json类型且set入该属性 */
	private JSONArray requestArray = new JSONArray();
	/** 微信服务器返回的修改时间 */
	private Long wechatUpdateTime;
	/** 图文素材标题集*/
	private String mediaTitles;
	
	private String materialName;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@TableGenerator(name = "jc_wechat_material", pkColumnValue = "jc_wechat_material")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_material")
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "app_id")
	@NotBlank
	@Length(max = 50)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "media_type")
	@Length(max = 50)
	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	@Column(name = "media_id")
	@NotBlank
	@Length(max = 150)
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@Column(name = "material_json")
	public String getMaterialJson() {
		return materialJson;
	}

	public void setMaterialJson(String materialJson) {
		this.materialJson = materialJson;
	}

	/** 如果消息是图文则要将json转成JSONArray*/
	@Transient
	public JSONArray getRequestArray() {
		if (Const.Mssage.REQ_MESSAGE_TYPE_NEWS.equals(getMediaType())) {
			requestArray = JSONObject.parseArray(getMaterialJson());
		}
		return requestArray;
	}

	public void setRequestArray(JSONArray requestArray) {
		this.requestArray = requestArray;
	}

	/** 如果消息是图文则要将json转成JSONArray*/
	@Transient
	public JSONObject getRequest() {
		if (!Const.Mssage.REQ_MESSAGE_TYPE_NEWS.equals(getMediaType())) {
			request = JSONObject.parseObject(getMaterialJson());
		}
		return request;
	}

	public void setRequest(JSONObject request) {
		this.request = request;
	}

	@Column(name = "wechat_update_time", length = 20, nullable = false)
	public Long getWechatUpdateTime() {
		return wechatUpdateTime;
	}

	public void setWechatUpdateTime(Long wechatUpdateTime) {
		this.wechatUpdateTime = wechatUpdateTime;
	}

	@Column(name = "media_titles", length = 1500, nullable = false)
	public String getMediaTitles() {
		return mediaTitles;
	}

	public void setMediaTitles(String mediaTitles) {
		this.mediaTitles = mediaTitles;
	}
	
	/**微信服务器返回的修改时间转换**/
	@Transient
	public String getWechatUpdateTimes() {
		String time = "";
		if (wechatUpdateTime != null) {
			time = MyDateUtils.formatDate(new Date(wechatUpdateTime * 1000));
		}
		return time;
	}

	@Column(name = "material_name", length = 120, nullable = false)
	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
}
