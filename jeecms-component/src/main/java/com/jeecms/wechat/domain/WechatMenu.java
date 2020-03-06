package com.jeecms.wechat.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 微信菜单实体
 * @Description: 微信菜单实体
 * @author: chenming
 * @date: 2018年8月10日 下午2:36:15
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_wechat_menu")
public class WechatMenu extends AbstractDomain<Integer> {
	
	public static final String TYPE_MEDIAID = "media_id";
	
	private static final long serialVersionUID = 1L;
	/** 主键值 */
	private Integer id;
	/** 菜单组对象 */
	private WechatMenuGroup menuGroup;
	/** 菜单组ID */
	private Integer menuGroupId;
	/** 菜单名称 */
	private String menuName;
	/** 菜单类型 view-网页类型 click-点击类型 miniprogram-小程序类型 */
	private String menuType;
	/** 菜单选择类型：1. 发送消息，2.跳转网页，3.扫码，4. 扫码(等待消息)，5.立即拍照，6.拍照/相册，7.相册，8.地址位置，9.关联小程序 */
	private Integer choiceType;
	/** 菜单点击触发关键词 */
	private Integer menuKey;
	/** 关键词回复内容对象 */
	private WechatReplyContent wechatReplyContent;
	/** 菜单跳转url(含小程序跳转url) */
	private String linkUrl;
	/** 小程序ID */
	private String miniprogramAppid;
	/** 小程序的页面路径 */
	private String miniprogramPagepath;
	/** 素材id */
	private Integer mediaId;
	/** 微信素材对象 */
	private WechatMaterial material;
	/** 父级ID */
	protected Integer parentId;
	/** 父级对象 */
	protected WechatMenu parent;
	/** 子集 */
	protected List<WechatMenu> childs = new ArrayList<WechatMenu>(0);

	public WechatMenu() {

	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_wechat_menu", pkColumnValue = "jc_wechat_menu", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_menu")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_group_id", insertable = false, updatable = false)
	public WechatMenuGroup getMenuGroup() {
		return menuGroup;
	}

	public void setMenuGroup(WechatMenuGroup menuGroup) {
		this.menuGroup = menuGroup;
	}

	@Column(name = "menu_group_id")
	public Integer getMenuGroupId() {
		return menuGroupId;
	}

	public void setMenuGroupId(Integer menuGroupId) {
		this.menuGroupId = menuGroupId;
	}

	@Column(name = "menu_name", length = 150)
	@NotBlank
	@Length(max = 150)
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	@Column(name = "menu_type", nullable = true, length = 50)
	@Length(max = 50)
	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	@Column(name = "menu_key", nullable = true, length = 11)
	public Integer getMenuKey() {
		return menuKey;
	}

	public void setMenuKey(Integer menuKey) {
		this.menuKey = menuKey;
	}

	@Column(name = "choice_type", nullable = true, length = 6)
	public Integer getChoiceType() {
		return choiceType;
	}

	public void setChoiceType(Integer choiceType) {
		this.choiceType = choiceType;
	}

	@Column(name = "link_url", nullable = true, length = 255)
	@Length(max = 255)
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	@Column(name = "miniprogram_appid", nullable = true, length = 50)
	@Length(max = 50)
	public String getMiniprogramAppid() {
		return miniprogramAppid;
	}

	public void setMiniprogramAppid(String miniprogramAppid) {
		this.miniprogramAppid = miniprogramAppid;
	}

	@Column(name = "miniprogram_pagepath", nullable = true, length = 255)
	@Length(max = 255)
	public String getMiniprogramPagepath() {
		return miniprogramPagepath;
	}

	public void setMiniprogramPagepath(String miniprogramPagepath) {
		this.miniprogramPagepath = miniprogramPagepath;
	}

	@Column(name = "media_id")
	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false)
	public WechatMenu getParent() {
		return parent;
	}

	public void setParent(WechatMenu parent) {
		this.parent = parent;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	public List<WechatMenu> getChilds() {
		return childs;
	}

	public void setChilds(List<WechatMenu> childs) {
		this.childs = childs;
	}

	@Column(name = "parent_id", precision = 20, scale = 0, nullable = true)
	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_key", referencedColumnName = "id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public WechatReplyContent getWechatReplyContent() {
		return wechatReplyContent;
	}

	public void setWechatReplyContent(WechatReplyContent wechatReplyContent) {
		this.wechatReplyContent = wechatReplyContent;
	}

	/**
	 * 此级联配置的是关联素材表的media_id字段，如需配置其它，若其id值，需修改referencedColumnName字段
	 */
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "media_id", referencedColumnName = "id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public WechatMaterial getMaterial() {
		return material;
	}

	public void setMaterial(WechatMaterial material) {
		this.material = material;
	}

}