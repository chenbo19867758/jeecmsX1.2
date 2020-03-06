/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.content.domain.CmsModel;

/**
 * 站点模型模板配置
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-04-23
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Entity
@Table(name = "jc_site_model_tpl")
public class SiteModelTpl extends AbstractDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
        /** 模型id */
        private Integer modelId;
        /** 站点id */
        private Integer siteId;
        /** pc端模板 */
        private String pcTplPath;
        /** 移动端模板 */
        private String mobileTplPath;
        /** 站点id */
        private CmsSite site;

        /** 模型id */
        private CmsModel model;

        public SiteModelTpl() {
        }

        /**
         * 有参构造
         * 
         * @param modelId
         *                模型ID
         * @param site
         *                站点
         * @param pcTplPath
         *                pc模板路径
         * @param mobileTplPath
         *                手机模板路径
         */
        public SiteModelTpl(Integer modelId, CmsSite site, Integer siteId, String pcTplPath, String mobileTplPath) {
                this.modelId = modelId;
                this.site = site;
                this.siteId = siteId;
                this.pcTplPath = pcTplPath;
                this.mobileTplPath = mobileTplPath;
        }

        @Id
        @Override
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_site_model_tpl", pkColumnValue = "jc_site_model_tpl", initialValue = 1, allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_site_model_tpl")
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "model_id", nullable = false, length = 11)
        public Integer getModelId() {
                return modelId;
        }

        public void setModelId(Integer modelId) {
                this.modelId = modelId;
        }

        @Column(name = "site_id", nullable = false, length = 11, insertable = false, updatable = false)
        public Integer getSiteId() {
                return siteId;
        }

        public void setSiteId(Integer siteId) {
                this.siteId = siteId;
        }

        @Column(name = "pc_tpl_path", nullable = true, length = 150)
        public String getPcTplPath() {
                return pcTplPath;
        }

        public void setPcTplPath(String pcTplPath) {
                this.pcTplPath = pcTplPath;
        }

        @Column(name = "mobile_tpl_path", nullable = true, length = 150)
        public String getMobileTplPath() {
                return mobileTplPath;
        }

        public void setMobileTplPath(String mobileTplPath) {
                this.mobileTplPath = mobileTplPath;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "site_id")
        public CmsSite getSite() {
                return site;
        }

        public void setSite(CmsSite site) {
                this.site = site;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "model_id", insertable = false, updatable = false)
        public CmsModel getModel() {
                return model;
        }

        public void setModel(CmsModel model) {
                this.model = model;
        }

}