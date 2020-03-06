/*
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
   *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.wechat.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.wechat.bean.response.mp.user.UserInfoResponse;

/**
 * 微信粉丝实体类
 * 
 * @author ljw
 * @version 1.0
 * @date 2018-08-02
 */
@Entity
@Table(name = "jc_wechat_fans")
public class WechatFans extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 用户所属公众号的appId */
	private String appId;
	/** 会员ID */
	private Integer memberId;
	/** 会员账号 */
	private String username;
	/**
	 * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息，只有openid和UnionID（在该公众号绑定到了微信开放平台账号时才有）
	 */
	private Integer subscribe;
	/** 用户的标识 */
	private String openid;
	/** 用户的昵称 */
	private String nickname;
	/** 用户的性别 */
	private Integer sex;
	/** 用户所在城市 */
	private String city;
	/** 用户所在省份 */
	private String province;
	/** 用户所在国家 */
	private String country;
	/** 用户的语言，简体中文为zh_CN */
	private String language;
	/** 用户头像 */
	private String headimgurl;
	/** 用户关注时间 */
	private long subscribeTime;
	/** unionid */
	private String unionid;
	/** 粉丝的备注 */
	private String remark;
	/** 分组ID */
	private Integer groupid;
	/** 用户关注的渠道来源 */
	private String subscribeScene;
	/** 二维码扫码场景 */
	private String qrScene;
	/** 二维码扫码场景描述 */
	private String qrSceneStr;
	/** 是否是黑名单 **/
	private Boolean isBlackList;
	/** 标签列表 **/
	private String tagidList;
	/** 会员账号 **/
	private CoreUser user;

	/** 关联wechatInfo **/
	private AbstractWeChatInfo weChatInfo;
	/** 粉丝扩展 **/
	private WechatFansExt fansExt;

	public WechatFans() {
	}

	/**
	 * 构造函数
	 * 
	 * @param user 用户信息
	 */
	public WechatFans(UserInfoResponse user) {

		this.subscribe = user.getSubscribe();
		this.openid = user.getOpenid();
		this.nickname = StrUtils.escapeUnicode(user.getNickname());
		this.sex = user.getSex();
		this.city = user.getCity();
		this.province = user.getProvince();
		this.country = user.getCountry();
		this.language = user.getLanguage();
		this.headimgurl = user.getHeadimgurl();
		this.subscribeTime = user.getSubscribeTime();
		this.unionid = user.getUnionid();
		this.remark = user.getRemark();
		this.groupid = user.getGroupid();
		this.subscribeScene = user.getSubscribeScene();
		this.qrScene = user.getQrScene().toString();
		this.qrSceneStr = user.getQrSceneStr();
		List<Integer> list = user.getTagidList();
		StringBuilder stringBuilder = new StringBuilder();
		for (Integer integer : list) {
			stringBuilder.append(integer).append(",");
		}
		this.tagidList = stringBuilder.toString();
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_wechat_fans", pkColumnValue = "jc_wechat_fans", 
			initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_fans")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "app_id", nullable = false, length = 50)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "member_id", nullable = true, length = 11)
	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	@Column(name = "username", nullable = true, length = 150)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "subscribe", nullable = false, length = 11)
	public Integer getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(Integer subscribe) {
		this.subscribe = subscribe;
	}

	@Column(name = "openid", nullable = true, length = 50)
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Column(name = "nickname", nullable = true, length = 150)
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "sex", nullable = true, length = 11)
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(name = "city", nullable = true, length = 50)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "province", nullable = true, length = 50)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "country", nullable = true, length = 50)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "language", nullable = true, length = 150)
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Column(name = "headimgurl", nullable = true, length = 255)
	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	@Column(name = "subscribe_time", nullable = true, length = 11)
	public long getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(long subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	@Column(name = "unionid", nullable = true, length = 50)
	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	@Column(name = "remark", nullable = true, length = 150)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "groupid", nullable = true, length = 11)
	public Integer getGroupid() {
		return groupid;
	}

	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	@Column(name = "subscribe_scene", nullable = true, length = 150)
	public String getSubscribeScene() {
		return subscribeScene;
	}

	public void setSubscribeScene(String subscribeScene) {
		this.subscribeScene = subscribeScene;
	}

	@Column(name = "qr_scene", nullable = true, length = 255)
	public String getQrScene() {
		return qrScene;
	}

	public void setQrScene(String qrScene) {
		this.qrScene = qrScene;
	}

	@Column(name = "qr_scene_str", nullable = true, length = 255)
	public String getQrSceneStr() {
		return qrSceneStr;
	}

	public void setQrSceneStr(String qrSceneStr) {
		this.qrSceneStr = qrSceneStr;
	}

	@Column(name = "is_black_list", nullable = true, length = 255)
	public Boolean getIsBlackList() {
		return isBlackList;
	}

	public void setIsBlackList(Boolean isBlackList) {
		this.isBlackList = isBlackList;
	}

	@Column(name = "tagid_list", nullable = true, length = 150)
	public String getTagidList() {
		return tagidList;
	}

	public void setTagidList(String tagidList) {
		this.tagidList = tagidList;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", insertable = false, updatable = false)
	public CoreUser getUser() {
		return user;
	}

	public void setUser(CoreUser user) {
		this.user = user;
	}

	// 属性referencedColumnName标注的是所关联表中的字段名，若不指定则使用的所关联表的主键字段名作为外键。
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_id", referencedColumnName = "app_id", insertable = false, updatable = false)
	public AbstractWeChatInfo getWeChatInfo() {
		return weChatInfo;
	}

	public void setWeChatInfo(AbstractWeChatInfo weChatInfo) {
		this.weChatInfo = weChatInfo;
	}

	/**
	 * 获取公众号名称
	 * 
	 * @Title: getWechatName
	 * @return
	 */
	@Transient
	public String getWechatName() {
		if (weChatInfo != null) {
			return weChatInfo.getWechatName();
		}
		return "";
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "fans", 
			cascade = { CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.REFRESH })
	public WechatFansExt getFansExt() {
		return fansExt;
	}

	public void setFansExt(WechatFansExt fansExt) {
		this.fansExt = fansExt;
	}
	
	/**
	 * 新增EXT
	* @Title: addExt 
	* @param ext 粉丝扩展
	* @return
	 */
	public WechatFans addExt(WechatFansExt ext) {
		ext.setFans(this);
		this.setFansExt(ext);
		return this;
	}
	
	/**关注时间转换**/
	@Transient
	public String getSubscribeTimes() {
		String time = "";
		if (subscribeTime != 0L) {
			time = MyDateUtils.formatDate(new Date(subscribeTime * 1000));
		}
		return time;
	}

}