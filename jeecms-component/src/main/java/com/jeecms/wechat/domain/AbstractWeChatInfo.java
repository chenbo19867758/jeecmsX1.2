package com.jeecms.wechat.domain;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 微信公众号信息
 * @author: qqwang
 * @date: 2018年6月11日 上午10:28:00
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_wechat_info")
public class AbstractWeChatInfo extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 授权方类型：公众号*/
	public static final Short TYPE_WECHAT = 1;
	/** 授权方类型：小程序*/
	public static final Short TYPE_MINIPROGRAM = 2;
	
	private Integer id;
	/** 授权方类型 1-公众号 2-小程序 */
	private Short type;
	/** 站点id */
	private Integer siteId;
	/** 头像 */
	private String headImg;
	/** 公众号名称 */
	private String wechatName;
	/** 公众号的主体名称 */
	private String principalName;
	/** 公众号类型 1-普通订阅号（含历史老账号升级后的订阅号） 2-服务号 */
	private Short wechatType;
	/** 认证状态 1-未认证 2-已认证 */
	private Short verifyStatus;
	/** 原始ID */
	private String globalId;
	/** app_id */
	private String appId;
	/** 接入方式 1-授权接入 */
	private Short grantType;
	/** 二维码图片地址 */
	private String qrcodeUrl;
	/**
	 * 权限集，多个使用逗号分隔 1.消息管理权限 2.用户管理权限 3.帐号服务权限 4.网页服务权限 5.微信小店权限 6.微信多客服权限 7.群发与通知权限
	 * 8.微信卡券权限 9.微信扫一扫权限 10.微信连WIFI权限 11.素材管理权限 12.微信摇周边权限 13.微信门店权限 14.微信支付权限
	 * 15.自定义菜单权限 17.帐号管理权限 18.开发管理权限 19.客服消息管理权限
	 */
	private String funcInfo;
	/** 微信号 */
	private String alias;
	/** 小程序使用服务域名列表 ， */
	private String network;
	/** 小程序服务类目 */
	private String categories;
	/** 是否设置管理员*/
	private Boolean isSetAdmin;
	/** 是否是默认快捷登录账号*/
	private Boolean isDefaultAuth;
	/** 管理员列表 */
	private List<CoreUser> users;
	

	public AbstractWeChatInfo() {
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@TableGenerator(name = "jc_wechat_info", pkColumnValue = "jc_wechat_info", 
							initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_info")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@NotNull
	@Column(name = "type", length = 6, nullable = false)
	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	@NotBlank
	@Column(name = "wechat_name", length = 150, nullable = false)
	@Length(max = 150)
	public String getWechatName() {
		return wechatName;
	}

	public void setWechatName(String wechatName) {
		this.wechatName = wechatName;
	}

	@NotBlank
	@Column(name = "principal_name", length = 255, nullable = false)
	@Length(max = 255)
	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	@NotNull
	@Column(name = "wechat_type", length = 6, nullable = false)
	public Short getWechatType() {
		return wechatType;
	}

	public void setWechatType(Short wechatType) {
		this.wechatType = wechatType;
	}

	@NotNull
	@Column(name = "verify_status", length = 6, nullable = false)
	public Short getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(Short verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	@NotBlank
	@Column(name = "global_id", length = 50, nullable = false)
	@Length(max = 50)
	public String getGlobalId() {
		return globalId;
	}

	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}

	@NotBlank
	@Column(name = "app_id", length = 50, nullable = false)
	@Length(max = 50)
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@NotNull
	@Column(name = "grant_type", length = 6, nullable = false)
	public Short getGrantType() {
		return grantType;
	}

	public void setGrantType(Short grantType) {
		this.grantType = grantType;
	}

	@Column(name = "qrcode_url", length = 255)
	@Length(max = 255)
	public String getQrcodeUrl() {
		return qrcodeUrl;
	}

	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}

	@Column(name = "func_info", length = 50)
	@Length(max = 150)
	public String getFuncInfo() {
		return funcInfo;
	}

	public void setFuncInfo(String funcInfo) {
		this.funcInfo = funcInfo;
	}

	@Column(name = "alias", length = 50)
	@Length(max = 50)
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(name = "network", length = 3000)
	@Length(max = 3000)
	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	@Column(name = "categories", length = 500)
	@Length(max = 500)
	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	@Column(name = "head_img", length = 255)
	@Length(max = 255)
	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	@Column(name = "site_id")
	@NotNull
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinTable(name = "jc_tr_wechat_user", joinColumns = @JoinColumn(name = "wehcat_info_id"), 
				inverseJoinColumns = @JoinColumn(name = "user_id"))
	public List<CoreUser> getUsers() {
		return users;
	}

	public void setUsers(List<CoreUser> users) {
		this.users = users;
	}

	@Column(name = "is_set_admin", nullable = false)
	public Boolean getIsSetAdmin() {
		return isSetAdmin;
	}

	public void setIsSetAdmin(Boolean isSetAdmin) {
		this.isSetAdmin = isSetAdmin;
	}

	@Column(name = "is_default_auth", nullable = false)
	public Boolean getIsDefaultAuth() {
		return isDefaultAuth;
	}
	
	public void setIsDefaultAuth(Boolean isDefaultAuth) {
		this.isDefaultAuth = isDefaultAuth;
	}
	
	@Transient
	public String getUserNames() {
		String userNames = "";
		if (getUsers() != null && getUsers().size() > 0) {
			userNames = getUsers().stream().map(CoreUser::getUsername)
					.collect(Collectors.joining(","));
		}
		return userNames;
	}
	
}