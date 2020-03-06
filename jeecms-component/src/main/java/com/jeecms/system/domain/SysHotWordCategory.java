/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.base.domain.IBaseSite;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 热词分类实体类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-28
 */
@Entity
@Table(name = "jc_sys_hot_word_category")
public class SysHotWordCategory extends AbstractDomain<Integer> implements IBaseSite, Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 应用范围全部
         */
        public static final Integer CATEGORY_RANGE_ALL = 1;

        /**
         * 应用范围指定栏目
         */
        public static final Integer CATEGORY_RANGE_CHANNEL = 2;

        private Integer id;
        /**
         * 站点id
         */
        private Integer siteId;
        /**
         * 分类名称
         */
        private String cateName;
        /**
         * 应用范围（1-全部  2-指定栏目）
         */
        private Integer applyScope;

        /**
         * 站点
         */
        private CmsSite site;

        /**
         * 栏目
         */
        private List<Channel> channels = new ArrayList<Channel>();

        /**
         * 应用范围
         */
        private String range;

        public SysHotWordCategory() {
        }

        @Id
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_sys_hot_word_category", pkColumnValue = "jc_sys_hot_word_category", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_hot_word_category")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Override
        @Column(name = "site_id", nullable = false, length = 11)
        public Integer getSiteId() {
                return siteId;
        }

        public void setSiteId(Integer siteId) {
                this.siteId = siteId;
        }

        @Column(name = "cate_name", nullable = false, length = 50)
        public String getCateName() {
                return cateName;
        }

        public void setCateName(String cateName) {
                this.cateName = cateName;
        }

        @Column(name = "apply_scope", nullable = false, length = 6)
        public Integer getApplyScope() {
                return applyScope;
        }

        public void setApplyScope(Integer applyScope) {
                this.applyScope = applyScope;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "site_id", insertable = false, updatable = false)
        public CmsSite getSite() {
                return site;
        }

        public void setSite(CmsSite site) {
                this.site = site;
        }

        @ManyToMany(cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_hotword_category_channel", joinColumns = @JoinColumn(name = "hot_word_category_id"),
                inverseJoinColumns = @JoinColumn(name = "channel_id"))
        public List<Channel> getChannels() {
                return channels;
        }

        public void setChannels(List<Channel> channels) {
                this.channels = channels;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((applyScope == null) ? 0 : applyScope.hashCode());
                result = prime * result + ((cateName == null) ? 0 : cateName.hashCode());
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
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
                SysHotWordCategory other = (SysHotWordCategory) obj;
                if (applyScope == null) {
                        if (other.applyScope != null) {
                                return false;
                        }
                } else if (!applyScope.equals(other.applyScope)) {
                        return false;
                }
                if (cateName == null) {
                        if (other.cateName != null) {
                                return false;
                        }
                } else if (!cateName.equals(other.cateName)) {
                        return false;
                }
                if (id == null) {
                        if (other.id != null) {
                                return false;
                        }
                } else if (!id.equals(other.id)) {
                        return false;
                }
                if (siteId == null) {
                        if (other.siteId != null) {
                                return false;
                        }
                } else if (!siteId.equals(other.siteId)) {
                        return false;
                }
                return true;
        }

        /**
         * 获取应用范围
         *
         * @return
         */
        @Transient
        public String getRange() {
                this.range = null;
                if (channels != null && !channels.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (Channel channel : channels) {
                                sb = sb.append(channel.getName() + "、");
                        }
                        if (sb.length() > 0) {
                                this.range = sb.toString().substring(0, sb.length() - 1);
                        }
                }
                return range;
        }
}