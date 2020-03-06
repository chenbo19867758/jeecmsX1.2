package com.jeecms.wechat.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.jeecms.common.base.domain.AbstractDomain;

/**
 * 小程序成员管理实体类
 * @author: chenming
 * @date:   2019年6月13日 上午11:08:38
 */
@Entity
@Table(name = "jc_miniprogram_member")
public class MiniprogramMember extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** appId */
	private String appId;
	/** 微信号 */
	private String wechatId;

	/** 微信号标识 */
	public static final String WECHATID = "wechatId";

	public MiniprogramMember() {

	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_miniprogram_member", pkColumnValue = "jc_miniprogram_member", 
					initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_miniprogram_member")
	@Override
	@NotNull(groups = {DeleteMember.class})
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "app_id", nullable = false, length = 50)
	@Length(max = 50)
	@NotBlank(groups = {SaveMember.class,DeleteMember.class})
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "wechat_id", nullable = false, length = 50)
	@Length(max = 50)
	@NotBlank(groups = {SaveMember.class,DeleteMember.class})
	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}
	
	public interface SaveMember {
		
	}
	
	public interface DeleteMember {
		
	}
}