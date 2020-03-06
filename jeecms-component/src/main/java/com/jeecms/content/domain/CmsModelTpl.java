package com.jeecms.content.domain;

import com.jeecms.common.base.domain.IBaseSite;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 模板对应模型关系表 The persistent class for the jc_model_tpl database table.
 * @author: tom
 * @date: 2018年11月12日 上午11:31:41
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_model_tpl")
@NamedQuery(name = "CmsModelTpl.findAll", query = "SELECT c FROM CmsModelTpl c")
public class CmsModelTpl extends com.jeecms.common.base.domain.AbstractIdDomain<Integer>
                implements Serializable, IBaseSite {
        public static final Short TPL_TYPE_CONTENT = 2;
        public static final Short TPL_TYPE_CHANNEL = 1;
        public static final String TPL_PATH_CONTENT = "content";
        public static final String TPL_PATH_CHANNEL = "channel";
        private static final long serialVersionUID = 1L;
        private Integer id;
        /** 模型ID **/
        private Integer modelId;
        /** 模板路径 **/
        private String tplPath;
        /** 模板类型(1栏目模板 2内容模板) **/
        private Short tplType;
        /** 模板方案名 **/
        private String tplSolution;
        /** 站点ID **/
        private Integer siteId;

        private CmsModel model;

        public CmsModelTpl() {
        }

        @Id
        @TableGenerator(name = "jc_model_tpl", pkColumnValue = "jc_model_tpl", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_model_tpl")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "model_id", nullable = false)
        public Integer getModelId() {
                return this.modelId;
        }

        public void setModelId(Integer modelId) {
                this.modelId = modelId;
        }

        @Column(name = "tpl_path", nullable = false, length = 150)
        public String getTplPath() {
                return this.tplPath;
        }

        public void setTplPath(String tplPath) {
                this.tplPath = tplPath;
        }

        @Column(name = "tpl_type", nullable = false)
        public Short getTplType() {
                return tplType;
        }

        public void setTplType(Short tplType) {
                this.tplType = tplType;
        }

        @Column(name = "tpl_solution", nullable = false)
        public String getTplSolution() {
                return tplSolution;
        }

        public void setTplSolution(String tplSolution) {
                this.tplSolution = tplSolution;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "model_id", insertable = false, updatable = false)
        @NotFound(action = NotFoundAction.IGNORE)
        public CmsModel getModel() {
                return model;
        }

        public void setModel(CmsModel model) {
                this.model = model;
        }

        @Column(name = "site_id", nullable = false, length = 11)
        @Override
        public Integer getSiteId() {
                return siteId;
        }

        public void setSiteId(Integer siteId) {
                this.siteId = siteId;
        }

        
        /**
         * 重写hashCode  
         * @Title: hashCode
         * @return int
         */
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((modelId == null) ? 0 : modelId.hashCode());
                result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
                result = prime * result + ((tplPath == null) ? 0 : tplPath.hashCode());
                result = prime * result + ((tplSolution == null) ? 0 : tplSolution.hashCode());
                result = prime * result + ((tplType == null) ? 0 : tplType.hashCode());
                return result;
        }

        /**
         * 重写  equals
         * @Title: equals
         * @param obj Object
         * @return boolean
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
                CmsModelTpl other = (CmsModelTpl) obj;
                if (id == null) {
                        if (other.id != null) {
                                return false;
                        }
                } else if (!id.equals(other.id)) {
                        return false;
                }
                if (modelId == null) {
                        if (other.modelId != null) {
                                return false;
                        }
                } else if (!modelId.equals(other.modelId)) {
                        return false;
                }
                if (siteId == null) {
                        if (other.siteId != null) {
                                return false;
                        }
                } else if (!siteId.equals(other.siteId)) {
                        return false;
                }
                if (tplPath == null) {
                        if (other.tplPath != null) {
                                return false;
                        }
                } else if (!tplPath.equals(other.tplPath)) {
                        return false;
                }
                if (tplSolution == null) {
                        if (other.tplSolution != null) {
                                return false;
                        }
                } else if (!tplSolution.equals(other.tplSolution)) {
                        return false;
                }
                if (tplType == null) {
                        if (other.tplType != null) {
                                return false;
                        }
                } else if (!tplType.equals(other.tplType)) {
                        return false;
                }
                return true;
        }

}