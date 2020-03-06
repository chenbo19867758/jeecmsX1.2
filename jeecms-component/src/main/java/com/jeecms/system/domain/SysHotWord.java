/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.base.domain.IBaseSite;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 热词实体类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-28
 */
@Entity
@Table(name = "jc_sys_hot_word")
public class SysHotWord extends AbstractDomain<Integer> implements IBaseSite, Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
        /**
         * 热词分类
         */
        private Integer hotWordCategoryId;
        /**
         * 热词名称
         */
        private String hotWord;

        /**
         * 使用次数
         */
        private Integer useCount;

        /**
         * 点击次数
         */
        private Integer clickCount;
        /**
         * 链接地址
         */
        private String linkUrl;
        /**
         * 是否新窗口打开(0-否  1-是)
         */
        private Boolean isTargetBlank;
        /**
         * 备注
         */
        private String remark;
        /**
         * 站点id
         */
        private Integer siteId;

        /**
         * 站点
         */
        private CmsSite site;

        /**
         * 热词分类
         */
        private SysHotWordCategory sysHotWordCategory;

        /**
         * 热词分类名称
         */
        private String cateName;

        public SysHotWord() {
        }

        @Id
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_sys_hot_word", pkColumnValue = "jc_sys_hot_word", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_hot_word")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "hot_word_category_id", nullable = false, length = 11)
        public Integer getHotWordCategoryId() {
                return hotWordCategoryId;
        }

        public void setHotWordCategoryId(Integer hotWordCategoryId) {
                this.hotWordCategoryId = hotWordCategoryId;
        }

        @NotBlank
        @Column(name = "hot_word", nullable = false, length = 50)
        public String getHotWord() {
                return hotWord;
        }

        public void setHotWord(String hotWord) {
                this.hotWord = hotWord;
        }

        @Column(name = "use_count", nullable = false, length = 11)
        public Integer getUseCount() {
                return useCount;
        }

        public void setUseCount(Integer useCount) {
                this.useCount = useCount;
        }

        @Column(name = "click_count", nullable = false, length = 11)
        public Integer getClickCount() {
                return clickCount;
        }

        public void setClickCount(Integer clickCount) {
                this.clickCount = clickCount;
        }

        @NotBlank
        @Column(name = "link_url", nullable = false, length = 500)
        public String getLinkUrl() {
                return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
                this.linkUrl = linkUrl;
        }

        @NotNull
        @Column(name = "is_target_blank", nullable = false, length = 1)
        public Boolean getIsTargetBlank() {
                return isTargetBlank;
        }

        public void setIsTargetBlank(Boolean isTargetBlank) {
                this.isTargetBlank = isTargetBlank;
        }

        @Column(name = "remark", nullable = true, length = 150)
        public String getRemark() {
                return remark;
        }

        public void setRemark(String remark) {
                this.remark = remark;
        }

        @Column(name = "site_id", nullable = false, length = 11)
        @Override
        public Integer getSiteId() {
                return siteId;
        }

        public void setSiteId(Integer siteId) {
                this.siteId = siteId;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "site_id", insertable = false, updatable = false)
        public CmsSite getSite() {
                return site;
        }

        public void setSite(CmsSite site) {
                this.site = site;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "hot_word_category_id", insertable = false, updatable = false)
        public SysHotWordCategory getSysHotWordCategory() {
                return sysHotWordCategory;
        }

        public void setSysHotWordCategory(SysHotWordCategory sysHotWordCategory) {
                this.sysHotWordCategory = sysHotWordCategory;
        }

        @Transient
        public String getCateName() {
                return getSysHotWordCategory() != null ? getSysHotWordCategory().getCateName() : "";
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((clickCount == null) ? 0 : clickCount.hashCode());
                result = prime * result + ((hotWord == null) ? 0 : hotWord.hashCode());
                result = prime * result + ((hotWordCategoryId == null) ? 0 : hotWordCategoryId.hashCode());
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((isTargetBlank == null) ? 0 : isTargetBlank.hashCode());
                result = prime * result + ((linkUrl == null) ? 0 : linkUrl.hashCode());
                result = prime * result + ((remark == null) ? 0 : remark.hashCode());
                result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
                result = prime * result + ((useCount == null) ? 0 : useCount.hashCode());
                return result;
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj) {
                        return true;
                }
                if (obj == null) {
                        return false;
                }
                if (getClass() != obj.getClass()) {
                        return false;
                }
                SysHotWord other = (SysHotWord) obj;
                if (clickCount == null) {
                        if (other.clickCount != null) {
                                return false;
                        }
                } else if (!clickCount.equals(other.clickCount)) {
                        return false;
                }
                if (hotWord == null) {
                        if (other.hotWord != null) {
                                return false;
                        }
                } else if (!hotWord.equals(other.hotWord)) {
                        return false;
                }
                if (hotWordCategoryId == null) {
                        if (other.hotWordCategoryId != null) {
                                return false;
                        }
                } else if (!hotWordCategoryId.equals(other.hotWordCategoryId)) {
                        return false;
                }
                if (id == null) {
                        if (other.id != null) {
                                return false;
                        }
                } else if (!id.equals(other.id)) {
                        return false;
                }
                if (isTargetBlank == null) {
                        if (other.isTargetBlank != null) {
                                return false;
                        }
                } else if (!isTargetBlank.equals(other.isTargetBlank)) {
                        return false;
                }
                if (linkUrl == null) {
                        if (other.linkUrl != null) {
                                return false;
                        }
                } else if (!linkUrl.equals(other.linkUrl)) {
                        return false;
                }
                if (remark == null) {
                        if (other.remark != null) {
                                return false;
                        }
                } else if (!remark.equals(other.remark)) {
                        return false;
                }
                if (siteId == null) {
                        if (other.siteId != null) {
                                return false;
                        }
                } else if (!siteId.equals(other.siteId)) {
                        return false;
                }
                if (useCount == null) {
                        if (other.useCount != null) {
                                return false;
                        }
                } else if (!useCount.equals(other.useCount)) {
                        return false;
                }
                return true;
        }

}