/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.resource.domain.ResourcesSpaceData;

/**
 * 内容类型管理实体类
 * 
 * @author: wulongwei
 * @version 1.0
 * @date: 2019年5月5日 上午10:04:49
 */
@Entity
@Table(name = "jc_content_type")
public class ContentType extends AbstractDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
        /** 类型名称 */
        private String typeName;
        /** 类型图标 */
        private Integer logoId;

        /** logo资源 */
        private ResourcesSpaceData logoResource;

        public ContentType() {
        }

        /**
         * 获得所有的id的List集合
         */
        @Transient
        @JSONField(serialize = false)
        public static List<Integer> fetchIds(Collection<ContentType> types) {
                if (types == null) {
                        return null;
                }
                List<Integer> ids = new ArrayList<Integer>();
                for (ContentType s : types) {
                        ids.add(s.getId());
                }
                return ids;
        }
        
        @Id
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_content_type", pkColumnValue = "jc_content_type", initialValue = 1, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_content_type")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @NotBlank
        @Length(max = 150)
        @Column(name = "type_name", nullable = false, length = 150)
        public String getTypeName() {
                return typeName;
        }

        public void setTypeName(String typeName) {
                this.typeName = typeName;
        }

        @Column(name = "logo_id", nullable = false, length = 11)
        public Integer getLogoId() {
                return logoId;
        }

        public void setLogoId(Integer logoId) {
                this.logoId = logoId;
        }

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "logo_id", insertable = false, updatable = false)
        @NotFound(action = NotFoundAction.IGNORE)
        public ResourcesSpaceData getLogoResource() {
                return logoResource;
        }

        public void setLogoResource(ResourcesSpaceData logoResource) {
                this.logoResource = logoResource;
        }
}