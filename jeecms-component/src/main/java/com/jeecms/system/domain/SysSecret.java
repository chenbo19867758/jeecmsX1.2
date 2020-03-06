/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractSortDomain;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 内容/附件密级实体类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-25
 */
@Entity
@Table(name = "jc_sys_secret")
public class SysSecret extends AbstractSortDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 内容密级
         */
        public static final Integer CONTENT_SECRET = 1;

        /**
         * 附件密级
         */
        public static final Integer ANNEX_SECRET = 2;

        private Integer id;
        /**
         * 名称
         */
        private String name;
        /**
         * 密级类型（1内容密级 2附件密级类型）
         */
        private Integer secretType;
        /**
         * 备注
         */
        private String remark;

        /**
         * 构造器
         */
        public SysSecret() {
        }

        @Id
        @Column(name = "secret_id", nullable = false, length = 11)
        @TableGenerator(name = "jc_sys_secret", pkColumnValue = "jc_sys_secret", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_secret")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @NotBlank
        @Column(name = "u_name", nullable = false, length = 50)
        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        @Range(min = 1, max = 2, message = "类型只有1或者2")
        public Integer getSecretType() {
                return secretType;
        }

        public void setSecretType(Integer secretType) {
                this.secretType = secretType;
        }

        @Column(name = "remark", nullable = true, length = 150)
        public String getRemark() {
                return remark;
        }

        public void setRemark(String remark) {
                this.remark = remark;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((name == null) ? 0 : name.hashCode());
                result = prime * result + ((remark == null) ? 0 : remark.hashCode());
                result = prime * result + ((secretType == null) ? 0 : secretType.hashCode());
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
                SysSecret other = (SysSecret) obj;
                if (id == null) {
                        if (other.id != null) {
                                return false;
                        }
                } else if (!id.equals(other.id)) {
                        return false;
                }
                if (name == null) {
                        if (other.name != null) {
                                return false;
                        }
                } else if (!name.equals(other.name)) {
                        return false;
                }
                if (remark == null) {
                        if (other.remark != null) {
                                return false;
                        }
                } else if (!remark.equals(other.remark)) {
                        return false;
                }
                if (secretType == null) {
                        if (other.secretType != null) {
                                return false;
                        }
                } else if (!secretType.equals(other.secretType)) {
                        return false;
                }
                return true;
        }
}