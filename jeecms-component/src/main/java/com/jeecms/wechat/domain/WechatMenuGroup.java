package com.jeecms.wechat.domain;

import java.io.Serializable;
import java.util.List;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.system.domain.Area;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

/**
 * 微信菜单组实体
 * @author: chenming
 * @date: 2018年8月10日 下午7:36:56
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_wechat_menu_group")
public class WechatMenuGroup extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 默认菜单*/
	public static final Integer DEFAULT_MENU = 1;
	/** 个性化菜单*/
	public static final Integer PERSONALISE_MENU = 2;
	/** 生效*/
	public static final Integer ACTIVE_STATUS = 1;
	/** 未生效*/
	public static final Integer NOT_ACTIVE_STATUS = 2;
	/** 主键值 */
	private Integer id;

	private Integer menuId;
	/** appId */
	private String appId;
	/** 菜单组类型1-默认菜单 2-个性化菜单 */
	private Integer menuGroupType;
	/** 菜单组状态（默认菜单组类型中，只允许一组生效菜单） 1- 生效 2-未生效 */
	private Integer status;
	/** 菜单组名称 */
	private String menuGroupName;
	/** 显示对象国家，默认为中国 */
	private String countryName;
	/** 区域对象用于级联操作 */
	private Area area;
	/** 显示对象地理区域编码 */
	private String areaCode;
	/** 显示对象性别 1-男 2-女( 字典类型sex_type) */
	private String sexDictCode;
	/** 显示对象粉丝标签ID */
	private Integer tagId;
	/** 显示对象客户端版本 1-IOS, 2-Android,3-Others( 字典类型client_type) */
	private String clientDictCode;
	/** 显示对象语言 ( 字典类型language_type) */
	private String languageDictCode;
	/** 微信菜单集合 */
	private List<WechatMenu> menuList;
	/** 微信粉丝标签对象 */
	private WechatFansTag wTag;

	public WechatMenuGroup() {

	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_wechat_menu_group", pkColumnValue = "jc_wechat_menu_group", 
					initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_wechat_menu_group")
	@Override
	@NotNull(groups = {UpdateWechatMenuGroup.class})
	@Null(groups = {SaveWechatMenuGroup.class})
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "app_id", length = 50)
	@Length(max = 50)
	@NotBlank(groups = {SaveWechatMenuGroup.class})
	@Null(groups = {UpdateWechatMenuGroup.class})
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "menu_group_type", nullable = false, length = 6)
	@NotNull(groups = {SaveWechatMenuGroup.class})
	@Null(groups = {UpdateWechatMenuGroup.class})
	@Min(value = 1, message = "菜单类型必须大于等于1",groups = {SaveWechatMenuGroup.class})
	@Max(value = 2, message = "菜单类型必须小于等于2",groups = {SaveWechatMenuGroup.class})
	public Integer getMenuGroupType() {
		return menuGroupType;
	}

	public void setMenuGroupType(Integer menuGroupType) {
		this.menuGroupType = menuGroupType;
	}

	@Column(name = "status", nullable = false, length = 6)
	@NotNull(groups = {SaveWechatMenuGroup.class,UpdateWechatMenuGroup.class})
	@Min(value = 1, message = "菜单类型必须大于等于1",groups = {SaveWechatMenuGroup.class,UpdateWechatMenuGroup.class})
	@Max(value = 2, message = "菜单类型必须小于等于2",groups = {SaveWechatMenuGroup.class,UpdateWechatMenuGroup.class})
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "menu_group_name", nullable = false, length = 150)
	@NotBlank(groups = {SaveWechatMenuGroup.class,UpdateWechatMenuGroup.class})
	@Length(max = 150)
	public String getMenuGroupName() {
		return menuGroupName;
	}

	public void setMenuGroupName(String menuGroupName) {
		this.menuGroupName = menuGroupName;
	}

	@Column(name = "country_name", nullable = true, length = 50)
	@Length(max = 50)
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@Column(name = "area_code", nullable = true, length = 50)
	@Length(max = 50)
	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_code", referencedColumnName = "area_code", insertable = false, updatable = false)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@Column(name = "sex_dict_code", nullable = true, length = 50)
	@Length(max = 50)
	public String getSexDictCode() {
		return sexDictCode;
	}

	public void setSexDictCode(String sexDictCode) {
		this.sexDictCode = sexDictCode;
	}

	@Column(name = "tag_id", nullable = true, length = 11)
	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	@Column(name = "client_dict_code", nullable = true, length = 50)
	@Length(max = 50)
	public String getClientDictCode() {
		return clientDictCode;
	}

	public void setClientDictCode(String clientDictCode) {
		this.clientDictCode = clientDictCode;
	}

	@Column(name = "language_dict_code", nullable = true, length = 50)
	@Length(max = 50)
	public String getLanguageDictCode() {
		return languageDictCode;
	}

	public void setLanguageDictCode(String languageDictCode) {
		this.languageDictCode = languageDictCode;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "menuGroup")
	@NotFound(action = NotFoundAction.IGNORE)
	public List<WechatMenu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<WechatMenu> menuList) {
		this.menuList = menuList;
	}

	@Column(name = "menu_id", nullable = true, length = 20)
	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id", referencedColumnName = "id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public WechatFansTag getwTag() {
		return wTag;
	}

	public void setwTag(WechatFansTag wTag) {
		this.wTag = wTag;
	}

	public interface SaveWechatMenuGroup{
		
	}
	
	public interface UpdateWechatMenuGroup{
		
	}
}