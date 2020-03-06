/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractSortDomain;
import com.jeecms.common.base.domain.IBaseSite;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 友情链接分类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-06-10
 */
@Entity
@Table(name = "jc_sys_link_type")
public class SysLinkType extends AbstractSortDomain<Integer> implements Serializable, IBaseSite {
        private static final long serialVersionUID = 1L;
        private Integer id;
        /**
         * 所属站点
         */
        private Integer siteId;
        /**
         * 名称
         */
        private String typeName;

        public SysLinkType() {
        }

        @Id
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_sys_link_type", pkColumnValue = "jc_sys_link_type", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_link_type")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "site_id", nullable = false, length = 11)
        @Override
        public Integer getSiteId() {
                return siteId;
        }

        public void setSiteId(Integer siteId) {
                this.siteId = siteId;
        }

        @NotNull
        @Length(max = 150)
        @Column(name = "type_name", nullable = false, length = 150)
        public String getTypeName() {
                return typeName;
        }

        public void setTypeName(String typeName) {
                this.typeName = typeName;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) {
                        return true;
                }
                if (!(o instanceof SysLinkType)) {
                        return false;
                }

                SysLinkType that = (SysLinkType) o;

                if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
                        return false;
                }
                if (getSiteId() != null ? !getSiteId().equals(that.getSiteId()) : that.getSiteId() != null) {
                        return false;
                }
                return getTypeName() != null ? getTypeName().equals(that.getTypeName()) : that.getTypeName() == null;
        }

        @Override
        public int hashCode() {
                int result = getId() != null ? getId().hashCode() : 0;
                result = 31 * result + (getSiteId() != null ? getSiteId().hashCode() : 0);
                result = 31 * result + (getTypeName() != null ? getTypeName().hashCode() : 0);
                return result;
        }
}