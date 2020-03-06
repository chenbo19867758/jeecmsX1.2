package com.jeecms.system.domain;

import java.io.Serializable;

import com.jeecms.auth.domain.CoreRole;
import com.jeecms.auth.domain.CoreUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

/**
 * The persistent class for the jc_sys_perm_cfg database table.
 * @author: tom
 * @date:   2019年5月5日 上午11:42:22
 */
@Entity
@Table(name = "jc_sys_perm_cfg")
@NamedQuery(name = "CmsDataPermCfg.findAll", query = "SELECT c FROM CmsDataPermCfg c")
public class CmsDataPermCfg extends com.jeecms.common.base.domain.AbstractDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
        /** 新增栏目的栏目类数据权限(,逗号分隔操作) */
        private String newChannelOpe;
        /** 新增栏目的文档类数据权限(,逗号分隔操作) */
        private String newChannelOpeContent;
        /** 是否有新增菜单的权限 */
        private Boolean newMenuOwner;
        /** 新增站点的站点类数据权限(,逗号分隔操作) */
        private String newSiteOpe;
        /** 是否有新增站点的站群权限 */
        private Boolean newSiteOwner;
        private Integer orgId;
        private Integer roleId;
        private Integer userId;
        private CmsOrg org;
        private CoreUser user;
        private CoreRole role;
        private Integer siteId;
        private CmsSite site;

        public CmsDataPermCfg() {
        }

        @Id
        @Column(name = "id", unique = true, nullable = false)
        @TableGenerator(name = "jc_sys_perm_cfg", pkColumnValue = "jc_sys_perm_cfg", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_perm_cfg")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "new_channel_ope")
        @Length(max = 50)
        public String getNewChannelOpe() {
                return this.newChannelOpe;
        }

        public void setNewChannelOpe(String newChannelOpe) {
                this.newChannelOpe = newChannelOpe;
        }

        @Length(max = 50)
        @Column(name = "new_channel_ope_content")
        public String getNewChannelOpeContent() {
                return this.newChannelOpeContent;
        }

        public void setNewChannelOpeContent(String newChannelOpeContent) {
                this.newChannelOpeContent = newChannelOpeContent;
        }

        @Column(name = "new_menu_owner")
        public Boolean getNewMenuOwner() {
                return this.newMenuOwner;
        }

        public void setNewMenuOwner(Boolean newMenuOwner) {
                this.newMenuOwner = newMenuOwner;
        }

        @Length(max = 50)
        @Column(name = "new_site_ope")
        public String getNewSiteOpe() {
                return this.newSiteOpe;
        }

        public void setNewSiteOpe(String newSiteOpe) {
                this.newSiteOpe = newSiteOpe;
        }

        @Column(name = "new_site_owner")
        public Boolean getNewSiteOwner() {
                return this.newSiteOwner;
        }

        public void setNewSiteOwner(Boolean newSiteOwner) {
                this.newSiteOwner = newSiteOwner;
        }

        @Column(name = "org_id")
        public Integer getOrgId() {
                return this.orgId;
        }

        public void setOrgId(Integer orgId) {
                this.orgId = orgId;
        }

        @Column(name = "role_id", length = 11)
        public Integer getRoleId() {
                return this.roleId;
        }

        public void setRoleId(Integer roleId) {
                this.roleId = roleId;
        }

        @Column(name = "user_id")
        public Integer getUserId() {
                return this.userId;
        }

        public void setUserId(Integer userId) {
                this.userId = userId;
        }

        @Column(name = "site_id")
        public Integer getSiteId() {
                return siteId;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "site_id", insertable = false, updatable = false)
        public CmsSite getSite() {
                return site;
        }

        public void setSiteId(Integer siteId) {
                this.siteId = siteId;
        }

        public void setSite(CmsSite site) {
                this.site = site;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "org_id", insertable = false, updatable = false)
        public CmsOrg getOrg() {
                return org;
        }

        public void setOrg(CmsOrg org) {
                this.org = org;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", insertable = false, updatable = false)
        public CoreUser getUser() {
                return user;
        }

        public void setUser(CoreUser user) {
                this.user = user;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "role_id", insertable = false, updatable = false)
        public CoreRole getRole() {
                return role;
        }

        public void setRole(CoreRole role) {
                this.role = role;
        }

}