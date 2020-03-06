package com.jeecms.interact.domain.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.validator.constraints.Length;

/**
 * 评论审核修改Dto
 * @author: chenming
 * @date:   2019年5月7日 下午4:26:37
 */
public class UserCommentDto {
	/** 评论Id数组*/
	private Integer[] ids;
	/** 评论Id*/
	private Integer id;
	/**
	 * 审核状态：
	 * 	1. 审核通过
	 * 	2. 审核不通过
	 * 	3. 审核撤回
	 */
	private Short status;
	/** 回复信息*/
	private String replytText;
	/** 是否是Ip*/
	private Boolean isIp;
	
	private Boolean isUpdate;

	@NotNull(groups = { UpdateCheck.class})
	@Null(groups = { UpdateStop.class, UpdateReplyt.class})
	public Integer[] getIds() {
		return ids;
	}

	public void setIds(Integer[] ids) {
		this.ids = ids;
	}

	@NotNull(groups = { UpdateStop.class, UpdateReplyt.class})
	@Null(groups = { UpdateCheck.class})
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@NotNull(groups = { UpdateCheck.class})
	@Null(groups = { UpdateReplyt.class,UpdateStop.class})
	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@NotBlank(groups = { UpdateReplyt.class})
	@Null(groups = { UpdateCheck.class,UpdateStop.class})
	@Length(max = 150)
	public String getReplytText() {
		return replytText;
	}

	public void setReplytText(String replytText) {
		this.replytText = replytText;
	}
	
	@NotNull(groups = { UpdateStop.class})
	@Null(groups = { UpdateCheck.class,UpdateReplyt.class})
	public Boolean getIsIp() {
		return isIp;
	}

	public void setIsIp(Boolean isIp) {
		this.isIp = isIp;
	}

	@NotNull(groups = { UpdateReplyt.class})
	@Null(groups = { UpdateCheck.class,UpdateStop.class})
	public Boolean getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	/** 修改审核校验*/
	public interface UpdateCheck{
		
	}
	
	/** 修改回复校验*/
	public interface UpdateReplyt{
		
	}
	
	/** 禁止用户或Ip评论*/
	public interface UpdateStop{
		
	}
	
}
