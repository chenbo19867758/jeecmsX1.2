/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.channel.domain;

import com.jeecms.common.base.domain.AbstractIdDomain;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.system.domain.Area;
import com.jeecms.system.domain.CmsOrg;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * 栏目自定义属性表
 * 
 * @author: chenming
 * @date: 2019年6月28日 下午3:15:17
 */
@Entity
@Table(name = "jc_channel_attr")
public class ChannelAttr extends AbstractIdDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;
        /** 主键值 */
        private Integer channelAttrId;
        /** 栏目ID */
        private Integer channelId;
        /** 扩展字段名 */
        private String attrName;
        /** 扩展字段值 */
        private String attrValue;
        /** 数据类型(对应模型字段数据类型) */
        private String attrType;
        /** 资源ID(单资源) */
        private Integer resId;
        /** 组织 */
        private Integer orgId;
        /** 省份编码 */
        private String provinceCode;
        /** 城市编码 */
        private String cityCode;
        /** 区县编码 */
        private String areaCode;

        /** 内容对象 */
        private Channel channel;
        /** 多资源对象集合 */
        private List<ChannelAttrRes> channelAttrRes;
        /** 资源对象 */
        private ResourcesSpaceData resourcesSpaceData;
        /** 组织对象 */
        private CmsOrg cmsOrg;
        /** 省级对象 */
        private Area province;
        /** 城市对象 */
        private Area city;
        /** 区级对象 */
        private Area area;

        public ChannelAttr() {
        }

        @Transient
        public String getOrgName() {
                if (getCmsOrg() != null) {
                        return getCmsOrg().getName();
                }
                return null;
        }

        /***
         * 获取地址拼接(省市县详细地址)
         * 
         * @Title: getAddress
         * @return: String
         */
        @Transient
        public String getAddress() {
                StringBuffer address = new StringBuffer("");
                if (getProvince() != null) {
                        address.append(getProvince().getAreaName());
                }
                if (getCity() != null) {
                        address.append(getCity().getAreaName());
                }
                if (getArea() != null) {
                        address.append(getArea().getAreaName());
                }
                address.append(getAttrValue());
                return address.toString();
        }

        /**
         * 获取省市拼接(省市)
         * 
         * @Title: getCityArea
         * @return: String
         */
        @Transient
        public String getCityArea() {
                StringBuffer address = new StringBuffer("");
                if (getProvince() != null) {
                        address.append(getProvince().getAreaName());
                }
                if (getCity() != null) {
                        address.append(getCity().getAreaName());
                }
                return address.toString();
        }

        /**
         * 获取资源地址
         * 
         * @Title: getResUrl
         * @return: String
         */
        @Transient
        public String getResUrl() {
                if (getResourcesSpaceData() != null) {
                        return getResourcesSpaceData().getUrl();
                }
                return null;
        }
        
        /**
         * 获取资源名称
         * 
         * @Title: getResAlias
         * @return: String
         */
        @Transient
        public String getResAlias() {
                if (getResourcesSpaceData() != null) {
                        return getResourcesSpaceData().getAlias();
                }
                return null;
        }
        

        /**
         * 获取资源对象类型（1图片 2视频 3音频 4附件)
         *
         * @return 资源对象类型
         */
        @Transient
        public Short getResType() {
                return getResourcesSpaceData() != null ? getResourcesSpaceData().getResourceType() : null;
        }

        /**
         * 获取资源对象大小
         *
         * @return 资源对象大小
         */
        @Transient
        public String getResSize() {
                return getResourcesSpaceData() != null ? getResourcesSpaceData().getSizeUnit() : "";
        }
        

        public ChannelAttr(String attrName, String attrType) {
                super();
                this.attrName = attrName;
                this.attrType = attrType;
        }

        @Id
        @Column(name = "channel_attr_id", nullable = false, length = 11)
        @TableGenerator(name = "jc_channel_attr", pkColumnValue = "jc_channel_attr", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_channel_attr")
        public Integer getChannelAttrId() {
                return this.channelAttrId;
        }

        public void setChannelAttrId(Integer channelAttrId) {
                this.channelAttrId = channelAttrId;
        }

        @Column(name = "channel_id", nullable = false, length = 11)
        public Integer getChannelId() {
                return channelId;
        }

        public void setChannelId(Integer channelId) {
                this.channelId = channelId;
        }

        @Column(name = "attr_name", nullable = false, length = 150)
        public String getAttrName() {
                return attrName;
        }

        public void setAttrName(String attrName) {
                this.attrName = attrName;
        }

        @Column(name = "attr_value", nullable = true, length = 715827882)
        public String getAttrValue() {
                return attrValue;
        }

        public void setAttrValue(String attrValue) {
                this.attrValue = attrValue;
        }

        @Column(name = "attr_type", nullable = false, length = 50)
        public String getAttrType() {
                return attrType;
        }

        public void setAttrType(String attrType) {
                this.attrType = attrType;
        }

        @Column(name = "res_id", nullable = true, length = 11)
        public Integer getResId() {
                return resId;
        }

        public void setResId(Integer resId) {
                this.resId = resId;
        }

        @Column(name = "org_id", nullable = true, length = 11)
        public Integer getOrgId() {
                return orgId;
        }

        public void setOrgId(Integer orgId) {
                this.orgId = orgId;
        }

        @Column(name = "province_code", nullable = true, length = 50)
        public String getProvinceCode() {
                return provinceCode;
        }

        public void setProvinceCode(String provinceCode) {
                this.provinceCode = provinceCode;
        }

        @Column(name = "city_code", nullable = true, length = 50)
        public String getCityCode() {
                return cityCode;
        }

        public void setCityCode(String cityCode) {
                this.cityCode = cityCode;
        }

        @Column(name = "area_code", nullable = true, length = 50)
        public String getAreaCode() {
                return areaCode;
        }

        public void setAreaCode(String areaCode) {
                this.areaCode = areaCode;
        }

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "channel_id", insertable = false, updatable = false)
        public Channel getChannel() {
                return channel;
        }

        public void setChannel(Channel channel) {
                this.channel = channel;
        }

        @OneToMany(mappedBy = "channelAttr")
        public List<ChannelAttrRes> getChannelAttrRes() {
                return channelAttrRes;
        }

        public void setChannelAttrRes(List<ChannelAttrRes> channelAttrRes) {
                this.channelAttrRes = channelAttrRes;
        }

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "res_id", referencedColumnName = "id", insertable = false, updatable = false)
        @NotFound(action = NotFoundAction.IGNORE)
        public ResourcesSpaceData getResourcesSpaceData() {
                return resourcesSpaceData;
        }

        public void setResourcesSpaceData(ResourcesSpaceData resourcesSpaceData) {
                this.resourcesSpaceData = resourcesSpaceData;
        }

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "org_id", referencedColumnName = "id", insertable = false, updatable = false)
        @NotFound(action = NotFoundAction.IGNORE)
        public CmsOrg getCmsOrg() {
                return cmsOrg;
        }

        public void setCmsOrg(CmsOrg cmsOrg) {
                this.cmsOrg = cmsOrg;
        }

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "province_code", referencedColumnName = "area_code", insertable = false, updatable = false)
        @NotFound(action = NotFoundAction.IGNORE)
        public Area getProvince() {
                return province;
        }

        public void setProvince(Area province) {
                this.province = province;
        }

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "city_code", referencedColumnName = "area_code", insertable = false, updatable = false)
        @NotFound(action = NotFoundAction.IGNORE)
        public Area getCity() {
                return city;
        }

        public void setCity(Area city) {
                this.city = city;
        }

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "area_code", referencedColumnName = "area_code", insertable = false, updatable = false)
        @NotFound(action = NotFoundAction.IGNORE)
        public Area getArea() {
                return area;
        }

        public void setArea(Area area) {
                this.area = area;
        }

}