/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 模版信息实体类
 * @author ljw
 * @date 2019年8月14日 上午9:49:25
 */
@Entity
@Table(name = "jc_sys_message_tpl")
public class MessageTpl extends AbstractDomain<Integer> {
	private static final long serialVersionUID = 1L;

	private Integer id;
	/** 所属站点 */
	private Integer siteId;
	/** 模板标题 */
	private String mesTitle;
	/** 模板唯一标识 */
	private String mesCode;
	/** 备注 */
	private String remark;
	/** 模板类型（1.会员验证 2.用户消息） */
	private Integer tplType;

	private List<MessageTplDetails> details = new ArrayList<MessageTplDetails>();

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_sys_message_tpl", pkColumnValue = "jc_sys_message_tpl", 
			initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_message_tpl")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "site_id", nullable = false, length = 11)
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	@NotBlank
	@Length(max = 150)
	@Column(name = "mes_title", nullable = false, length = 150)
	public String getMesTitle() {
		return mesTitle;
	}

	public void setMesTitle(String mesTitle) {
		this.mesTitle = mesTitle;
	}

	@NotBlank
	@Length(max = 150)
	@Column(name = "mes_code", nullable = false, length = 150)
	public String getMesCode() {
		return mesCode;
	}

	public void setMesCode(String mesCode) {
		this.mesCode = mesCode;
	}

	@Column(name = "remark", nullable = true, length = 500)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "messageTpl")
	public List<MessageTplDetails> getDetails() {
		return details;
	}

	public void setDetails(List<MessageTplDetails> details) {
		this.details = details;
	}

	@Column(name = "tpl_type", nullable = false, length = 6)
	public Integer getTplType() {
		return tplType;
	}

	public void setTplType(Integer tplType) {
		this.tplType = tplType;
	}
	
	/**得到第一个模板详情**/
	@Transient
	public MessageTplDetails getFirstTplDetail() {
		if (details != null && details.size() > 0) {
			return details.get(0);
		} else {
			return null;
		}
	}
	
	/** 只返回错误信息 */
	public static final Integer RETURN_ERROR = 1;
	/** 删除已保存的模板，并返回错误信息 */
	public static final Integer DELETE_RETURN_ERROR = 2;

}
