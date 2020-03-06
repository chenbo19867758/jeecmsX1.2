/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.base.domain.IBaseSite;
import com.jeecms.content.domain.Content;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * tag词实体类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-27
 */
@Entity
@Table(name = "jc_content_tag")
public class ContentTag extends AbstractDomain<Integer> implements IBaseSite, Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
        /**
         * tag词
         */
        private String tagName;
        /**
         * 引用内容数
         */
        private Integer refCounter;

        /**
         * 站点id
         */
        private Integer siteId;

        /**
         * 站点
         */
        private CmsSite site;

        private List<Content> contentList;

        public ContentTag() {
        }

        /**
         * 获得所有的id的List集合
         */
        @Transient
        @JSONField(serialize = false)
        public static List<Integer> fetchIds(Collection<ContentTag> tags) {
                if (tags == null) {
                        return null;
                }
                List<Integer> ids = new ArrayList<Integer>();
                for (ContentTag s : tags) {
                        ids.add(s.getId());
                }
                return ids;
        }

        @Id
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_content_tag", pkColumnValue = "jc_content_tag", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_content_tag")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @NotBlank
        @Column(name = "tag_name", nullable = false, length = 50)
        public String getTagName() {
                return tagName;
        }

        public void setTagName(String tagName) {
                this.tagName = tagName;
        }

        @Column(name = "ref_counter", nullable = false, length = 11)
        public Integer getRefCounter() {
                return refCounter;
        }

        public void setRefCounter(Integer refCounter) {
                this.refCounter = refCounter;
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

        @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
        @JoinTable(name = "jc_tr_content_tag", joinColumns = @JoinColumn(name = "tag_id"),
                inverseJoinColumns = @JoinColumn(name = "content_id"))
        public List<Content> getContentList() {
                return contentList;
        }

        public void setContentList(List<Content> contentList) {
                this.contentList = contentList;
        }

        /**
         * 重写 hashCode
         *
         * @return int
         * @Title: hashCode
         */
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((refCounter == null) ? 0 : refCounter.hashCode());
                result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
                return result;
        }

        /**
         * 重写 equals
         *
         * @param obj
         *                Object
         * @return boolean
         * @Title: equals
         */
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
                ContentTag other = (ContentTag) obj;
                if (tagName == null) {
                        if (other.tagName != null) {
                                return false;
                        }
                } else if (!tagName.equals(other.tagName)) {
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
                if (refCounter == null) {
                        if (other.refCounter != null) {
                                return false;
                        }
                } else if (!refCounter.equals(other.refCounter)) {
                        return false;
                }
                return true;
        }

}