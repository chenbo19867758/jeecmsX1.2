/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractSortDomain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 人员密级实体类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-25
 */
@Entity
@Table(name = "jc_sys_user_secret")
public class SysUserSecret extends AbstractSortDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
        /**
         * 名称
         */
        private String name;
        /**
         * 备注
         */
        private Integer remark;

        /**
         * 内容密级名称
         */
        private String contentSecretNames;

        /**
         * 附件密级名称
         */
        private String annexSecretNames;

        /**
         * 密级（内容和附件）
         */
        private List<SysSecret> sysSecrets = new ArrayList<SysSecret>();

        public SysUserSecret() {
        }

        @Id
        @Column(name = "user_secret_id", nullable = false, length = 11)
        @TableGenerator(name = "jc_sys_user_secret", pkColumnValue = "jc_sys_user_secret", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_user_secret")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "u_name", nullable = true, length = 50)
        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        @Column(name = "remark", nullable = true, length = 11)
        public Integer getRemark() {
                return remark;
        }

        public void setRemark(Integer remark) {
                this.remark = remark;
        }

        @ManyToMany(cascade = CascadeType.MERGE)
        @JoinTable(name = "jc_tr_user_sys_secret", joinColumns = @JoinColumn(name = "user_secret_id"), inverseJoinColumns = @JoinColumn(name = "secret_id"))
        public List<SysSecret> getSysSecrets() {
                return sysSecrets;
        }

        public void setSysSecrets(List<SysSecret> sysSecrets) {
                this.sysSecrets = sysSecrets;
        }

        /**
         * 获得内容密级
         *
         * @return
         */
        @Transient
        public String getContentSecretNames() {
                List<SysSecret> sysSecrets = getSysSecrets();
                StringBuilder contentSb = new StringBuilder();
                if (sysSecrets != null && !sysSecrets.isEmpty()) {
                        for (SysSecret sysSecret : sysSecrets) {
                                //获得内容密级添加到sb中
                                if (sysSecret.getSecretType().equals(SysSecret.CONTENT_SECRET)) {
                                        contentSb = contentSb.append(sysSecret.getName() + "、");
                                }
                        }
                        if (contentSb.length() > 0) {
                                this.contentSecretNames = contentSb.toString().substring(0, contentSb.length() - 1);
                        }
                }
                return contentSecretNames;
        }
        
        /**
         * 获取内容密级ID集合
         * @Title: getContentSecretIds
         * @return: Set
         */
        @Transient
        public Set<Integer> getContentSecretIds() {
                List<SysSecret> sysSecrets = getSysSecrets();
                Set<Integer> contentSecretIds=new HashSet<Integer>();
                if (sysSecrets != null && !sysSecrets.isEmpty()) {
                        for (SysSecret sysSecret : sysSecrets) {
                                //获得内容密级添加到sb中
                                if (sysSecret.getSecretType().equals(SysSecret.CONTENT_SECRET)) {
                                        contentSecretIds.add(sysSecret.getId());
                                }
                        }
                }
                return contentSecretIds;
        }

        /**
         * 获得附件密级
         *
         * @return
         */
        @Transient
        public String getAnnexSecretNames() {
                if (sysSecrets != null && !sysSecrets.isEmpty()) {
                        StringBuilder annexSb = new StringBuilder();
                        for (SysSecret sysSecret : sysSecrets) {
                                //获得附件密级添加到sb中
                                if (sysSecret.getSecretType().equals(SysSecret.ANNEX_SECRET)) {
                                        annexSb = annexSb.append(sysSecret.getName() + "、");
                                }
                        }
                        if (annexSb.length() > 0) {
                                this.annexSecretNames = annexSb.toString().substring(0, annexSb.length() - 1);
                        }
                }
                return annexSecretNames;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((name == null) ? 0 : name.hashCode());
                result = prime * result + ((remark == null) ? 0 : remark.hashCode());
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
                SysUserSecret other = (SysUserSecret) obj;
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
                return true;
        }
}