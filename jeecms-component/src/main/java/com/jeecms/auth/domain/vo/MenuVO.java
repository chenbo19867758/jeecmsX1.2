/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.auth.domain.vo;


import java.util.List;

/**
 * 菜单管理Vo
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/4 11:06
 */
public class MenuVO {

	private Boolean needChangePassword;
	private Boolean nextNeedCaptcha;
	private String lastLoginTime;
	private String userName;
	private String lastLoginIP;
	private List<Integer> roleIds;
	private Integer orgId;

	private List<SortMenuVO> menus;

	private List<CoreApiVo> perms;

	public MenuVO() {
		super();
	}

	public MenuVO(List<SortMenuVO> menus, List<CoreApiVo> perms) {
		this.menus = menus;
		this.perms = perms;
	}

	public MenuVO(Boolean needChangePassword, Boolean nextNeedCaptcha,
				  String lastLoginTime, String userName, String lastLoginIP,
				  List<SortMenuVO> menus, List<CoreApiVo> perms,
				  List<Integer>roleIds,Integer orgId) {
		this.needChangePassword = needChangePassword;
		this.nextNeedCaptcha = nextNeedCaptcha;
		this.lastLoginTime = lastLoginTime;
		this.userName = userName;
		this.lastLoginIP = lastLoginIP;
		this.menus = menus;
		this.perms = perms;
		this.roleIds = roleIds;
		this.orgId = orgId;
	}

	public Boolean getNeedChangePassword() {
		return needChangePassword;
	}

	public void setNeedChangePassword(Boolean needChangePassword) {
		this.needChangePassword = needChangePassword;
	}

	public Boolean getNextNeedCaptcha() {
		return nextNeedCaptcha;
	}

	public void setNextNeedCaptcha(Boolean nextNeedCaptcha) {
		this.nextNeedCaptcha = nextNeedCaptcha;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLastLoginIP() {
		return lastLoginIP;
	}

	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}

	public List<Integer> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public List<SortMenuVO> getMenus() {
		return menus;
	}

	public void setMenus(List<SortMenuVO> menus) {
		this.menus = menus;
	}

	public List<CoreApiVo> getPerms() {
		return perms;
	}

	public void setPerms(List<CoreApiVo> perms) {
		this.perms = perms;
	}
}
