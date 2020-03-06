/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 内容版本实体类
 * @author: chenming
 * @date:   2019年5月15日 下午5:00:36
 */
@Entity
@Table(name = "jc_content_version")
public class ContentVersion extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 版本号 */
	private String versionCode;
	/** 内容ID */
	private Integer contentId;
	/** 内容，存储json格式，json中以正文字段为key */
	private String txt;
	/** 备注 */
	private String remark;
	
	private Content content;
	
	private JSONObject jsonTxt = new JSONObject();

	public ContentVersion() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_content_version", pkColumnValue = "jc_content_version", 
		initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_content_version")
	@Override
	@NotNull(groups = {UpdateVersion.class,RecoveryVersion.class})
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "version_code", nullable = false, length = 50)
	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	@Column(name = "content_id", nullable = false, length = 11)
	@NotNull(groups = {SaveVersion.class})
	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	@Column(name = "txt", nullable = true, length = 715827882)
	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	@Column(name = "remark", nullable = true, length = 255)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", insertable = false, updatable = false)
	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}
	
	@Transient
	public JSONObject getJsonTxt() {
		jsonTxt = JSONObject.parseObject(getTxt());
		return jsonTxt;
	}

	public void setJsonTxt(JSONObject jsonTxt) {
		this.jsonTxt = jsonTxt;
	}

	public interface SaveVersion {
		
	}
	
	public interface UpdateVersion {
		
	}
	
	public interface RecoveryVersion {
		
	}
}