/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.audit.domain;

import com.jeecms.audit.constants.ContentAuditConstants;
import com.jeecms.audit.util.AuditUtil;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.base.domain.IBaseSite;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.system.domain.CmsSite;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 审核策略
 *
 * @author ljw
 * @version 1.0
 * @date 2019-12-16
 */
@Entity
@Table(name = "jc_audit_strategy")
public class AuditStrategy extends AbstractDomain<Integer> implements Serializable, IBaseSite {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/**
	 * 审核策略名称
	 */
	private String name;
	/**
	 * 文本审核，true开启，false关闭
	 */
	private Boolean isText;
	/**
	 * 图片审核，true开启，false关闭
	 */
	private Boolean isPicture;
	/**
	 * 文本审核场景
	 */
	private String textScene;
	/**
	 * 图片审核场景
	 */
	private String pictureScene;
	/**
	 * 是否开启，true开启，false不开启
	 */
	private Boolean status;
	/**
	 * 站点id
	 */
	private Integer siteId;
	/**
	 * 站点
	 */
	private CmsSite site;

	/**
	 * 关联栏目集合
	 **/
	private List<AuditChannelSet> channelSets;

	public AuditStrategy() {
	}

	public AuditStrategy(String name, Boolean status, Integer siteId, String textScene, String pictureScene) {
		this.name = name;
		this.isText = true;
		this.isPicture = true;
		this.status = status;
		this.siteId = siteId;
		this.textScene = textScene;
		this.pictureScene = pictureScene;
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_audit_strategy", pkColumnValue = "jc_audit_strategy",
		initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_audit_strategy")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 150)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "is_text", nullable = false, length = 1)
	public Boolean getIsText() {
		return isText;
	}

	public void setIsText(Boolean isText) {
		this.isText = isText;
	}

	@Column(name = "is_picture", nullable = false, length = 1)
	public Boolean getIsPicture() {
		return isPicture;
	}

	public void setIsPicture(Boolean isPicture) {
		this.isPicture = isPicture;
	}

	@Column(name = "text_scene", nullable = true, length = 500)
	public String getTextScene() {
		return textScene;
	}

	public void setTextScene(String textScene) {
		this.textScene = textScene;
	}

	@Column(name = "picture_scene", nullable = true, length = 500)
	public String getPictureScene() {
		return pictureScene;
	}

	public void setPictureScene(String pictureScene) {
		this.pictureScene = pictureScene;
	}

	@Column(name = "status", nullable = false, length = 1)
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	@OneToMany(mappedBy = "strategy")
	@Where(clause = " deleted_flag=0 ")
	public List<AuditChannelSet> getChannelSets() {
		return channelSets;
	}

	public void setChannelSets(List<AuditChannelSet> channelSets) {
		this.channelSets = channelSets;
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

	@Transient
	public String getTextAuditScene() {
		String textAuditScene = "无";
		String[] split = StringUtils.split(getTextScene(), WebConstants.ARRAY_SPT);
		List<String> list = new ArrayList<String>();
		for (String s : split) {
			list.add(ContentAuditConstants.textScene(Integer.parseInt(s)));
		}
		if (list.size() > 0) {
			textAuditScene = StringUtils.join(list, WebConstants.ARRAY_SPT);
		}
		return textAuditScene;
	}

	@Transient
	public String getPictureAuditScene() {
		String pictureAuditScene = "无";
		String[] split = StringUtils.split(getPictureScene(), WebConstants.ARRAY_SPT);
		List<String> list = new ArrayList<String>();
		for (String s : split) {
			list.add(ContentAuditConstants.pictureScene(Integer.parseInt(s)));
		}
		if (list.size() > 0) {
			pictureAuditScene = StringUtils.join(list, WebConstants.ARRAY_SPT);
		}
		return pictureAuditScene;
	}
	
	/**
	 * 审核策略获得文本审核类型集合
	 * @Title: getTextAuditScenes
	 * @return: List
	 */
	@Transient
	public List<Integer> getTextAuditScenes() {
		List<Integer> auditScenes = null;
		if (StringUtils.isNotBlank(getTextScene())) {
			auditScenes = AuditUtil.arrayToList(getTextScene());
		}
		return auditScenes;
	}
	
	/**
	 * 审核策略获得图片审核类型集合
	 * @Title: getPictureAuditScenes
	 * @return: List
	 */
	@Transient
	public List<Integer> getPictureAuditScenes() {
		List<Integer> auditScenes = null;
		if (StringUtils.isNotBlank(getPictureScene())) {
			auditScenes = AuditUtil.arrayToList(getPictureScene());
		}
		return auditScenes;
	}
	

}