/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.content.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.channel.domain.Channel;
import com.jeecms.common.base.domain.AbstractSortDomain;
import com.jeecms.common.base.domain.IBaseSite;
import com.jeecms.content.constants.CmsModelConstant;
import com.jeecms.system.domain.CmsSite;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.OrderBy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/***
 * 
 * @Description:模型表 domain The persistent class for the jc_model database table.
 * @author: tom
 * @date: 2018年11月10日 上午9:35:26
 */
@Entity
@Table(name = "jc_model")
@NamedQuery(name = "CmsModel.findAll", query = "SELECT c FROM CmsModel c")
public class CmsModel extends AbstractSortDomain<Integer> implements Serializable, IBaseSite {
 
        private static final long serialVersionUID = 1L;
        private Integer id;
        /** 名称 */
        private String modelName;
        /** 是否全局(1全局 0站点模型) */
        private Short isGlobal;
        /** 站点ID */
        private Integer siteId;
        /** 是否启用 */
        private Boolean isEnable;
        /** 模型类型（1-栏目模型 2-内容模型 3会员模型） */
        private Short tplType;
        /**站点*/
        private CmsSite site;
        /**排序权重*/
        private Integer sortWeight;
        /**未启用字段*/
        private String unEnableJsonStr;
        /**未启用字段json，返回jsonobject*/
        private JSONArray unEnableJson = new JSONArray();
        /**启用字段*/
        private JSONObject enableJson;
        /**模版集*/
        private Set<CmsModelTpl> tpls;
        /**字段集*/
        private Set<CmsModelItem> items;
        /**栏目*/
        private List<Channel> channels;
        /**内容*/
        private List<Content> contents;
        /** 全局站点模型 */
        public static final Short WHOLE_SITE_MODEL = 1;
        /** 本地站点模型 */
        public static final Short THIS_SITE_MODEL = 0;


        public CmsModel() {
        }

        /**
         * 根据字段名称获取模型项
         * 
         * @Title: getItem
         * @param field
         *                字段名
         * @return: CmsModelItem
         */
        @Transient
        public CmsModelItem getItem(String field) {
                for (CmsModelItem item : getItems()) {
                        if (item.getField().equals(field)) {
                                return item;
                        }
                }
                return null;
        }

        /**
         * 判断是否存在某字段
         * 
         * @Title: existItem
         * @param field
         *                字段名称
         * @return: boolean
         */
        @Transient
        public boolean existItem(String field) {
                if (StringUtils.isBlank(field)) {
                        return false;
                }
                CmsModelItem item = getItem(field);
                if (item != null) {
                        return true;
                }
                return false;
        }

        @Transient
        public  Set<CmsModelItem> getItemsForCollect() {
                Set<CmsModelItem> items = getItems().stream().filter(item->item!=null).collect(Collectors.toSet());
                Set<CmsModelItem> itemForCollect = new HashSet<>();
                for(CmsModelItem item:items){
                        /**系统默认字段指定字段 标题、副标题、摘要、作者、来源、发布时间、外部链接、单图上传、正文*/
                        if(!item.getHasDeleted()){
                                if(!item.getIsCustom()){
                                        boolean forCollect = CmsModelConstant.FIELD_SYS_TITLE.equals(item.getField())
                                                ||CmsModelConstant.FIELD_SYS_SHORT_TITLE.equals(item.getField())
                                                ||CmsModelConstant.FIELD_SYS_DESCRIPTION.equals(item.getField())
                                                ||CmsModelConstant.FIELD_SYS_AUTHOR.equals(item.getField())
                                                ||CmsModelConstant.FIELD_SYS_CONTENT_SOURCE.equals(item.getField())
                                                ||CmsModelConstant.FIELD_SYS_RELEASE_TIME.equals(item.getField())
                                                ||CmsModelConstant.FIELD_SYS_CONTENT_OUTLINK.equals(item.getField())
                                                ||CmsModelConstant.FIELD_SYS_CONTENT_RESOURCE.equals(item.getField())
                                                ||CmsModelConstant.FIELD_SYS_CONTENT_CONTXT.equals(item.getField());
                                        if(forCollect){
                                                itemForCollect.add(item);
                                        }
                                }else{
                                        /**目前 多图上传 视频上传   音频上传 附件上传  组织  所在地  城市 不支持采集*/
                                        boolean excludeField = CmsModelConstant.MANY_CHART_UPLOAD.equals(item.getDataType())
                                                ||CmsModelConstant.VIDEO_UPLOAD.equals(item.getDataType())
                                                ||CmsModelConstant.AUDIO_UPLOAD.equals(item.getDataType())
                                                ||CmsModelConstant.ANNEX_UPLOAD.equals(item.getDataType())
                                                ||CmsModelConstant.TISSUE.equals(item.getDataType())
                                                ||CmsModelConstant.ADDRESS.equals(item.getDataType())
                                                ||CmsModelConstant.CITY.equals(item.getDataType());
                                        if(!excludeField){
                                                itemForCollect.add(item);
                                        }
                                }
                        }
                }
                return itemForCollect;
        }

        @Id
        @TableGenerator(name = "jc_model", pkColumnValue = "jc_model", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_model")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "is_enable", nullable = false)
        @NotNull
        public Boolean getIsEnable() {
                return this.isEnable;
        }

        public void setIsEnable(Boolean isEnable) {
                this.isEnable = isEnable;
        }

        @Column(name = "tpl_type", nullable = false, length = 6)
        public Short getTplType() {
                return tplType;
        }

        public void setTplType(Short tplType) {
                this.tplType = tplType;
        }

        @Column(name = "model_name", length = 255)
        @NotBlank
        public String getModelName() {
                return this.modelName;
        }

        public void setModelName(String modelName) {
                this.modelName = modelName;
        }

        @Column(name = "is_global", nullable = false, length = 1)
        public Short getIsGlobal() {
                return isGlobal;
        }

        public void setIsGlobal(Short isGlobal) {
                this.isGlobal = isGlobal;
        }

        @Override
        @Column(name = "site_id", nullable = false, insertable = false, updatable = false)
        public Integer getSiteId() {
                return this.siteId;
        }

        public void setSiteId(Integer siteId) {
                this.siteId = siteId;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "site_id")
        public CmsSite getSite() {
                return site;
        }

        public void setSite(CmsSite site) {
                this.site = site;
        }

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "model", orphanRemoval = true)
        @OrderBy(clause = "id")
        public Set<CmsModelTpl> getTpls() {
                return tpls;
        }

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "model", orphanRemoval = true)
        public Set<CmsModelItem> getItems() {
                return items;
        }

        public void setTpls(Set<CmsModelTpl> tpls) {
                this.tpls = tpls;
        }

        public void setItems(Set<CmsModelItem> items) {
                this.items = items;
        }
        
        @Column(name = "un_enable_json", nullable = false, length = 6)
        public String getUnEnableJsonStr() {
			return unEnableJsonStr;
		}

		public void setUnEnableJsonStr(String unEnableJsonStr) {
			this.unEnableJsonStr = unEnableJsonStr;
		}

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "model", orphanRemoval = true)
        public List<Channel> getChannels() {
                return channels;
        }

        public void setChannels(List<Channel> channels) {
                this.channels = channels;
        }

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "model", orphanRemoval = true)
        public List<Content> getContents() {
                return contents;
        }

        public void setContents(List<Content> contents) {
                this.contents = contents;
        }

		@Transient
		public JSONObject getEnableJson() {
			return enableJson;
		}

		public void setEnableJson(JSONObject enableJson) {
			this.enableJson = enableJson;
		}
		
		@Transient
		public JSONArray getUnEnableJson() {
			if(StringUtils.isNoneBlank(this.getUnEnableJsonStr())) return JSON.parseArray(this.getUnEnableJsonStr());
			return unEnableJson;
		}

		public void setUnEnableJson(JSONArray unEnableJson) {
			this.unEnableJson = unEnableJson;
		}

		@Column(name = "sort_weight", length = 11)
		public Integer getSortWeight() {
			return sortWeight;
		}

		public void setSortWeight(Integer sortWeight) {
			this.sortWeight = sortWeight;
		}


		public static final Short CHANNEL_TYPE = 1;
        public static final Short CONTENT_TYPE = 2;
        public static final Short MEMBER_TYPE = 3;
}
