/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

/**
 * 批量操作Dto
 * 
 * @author: ljw
 * @date: 2019年4月30日 下午3:39:02
 */
public class BeatchDto {

	/** 实体IDs集合 **/
	private List<Integer> ids = new ArrayList<Integer>(10);
	/** 内容状态,取ContentConstant 里面字段 **/
	private Integer status;
	/**原因**/
	private String reason;
	/** 站点Id **/
	private Integer siteId;
	/**是否需要检查数据权限*/
	private boolean checkPerm;
	
	public static BeatchDto build(List<Integer> ids, Integer status, boolean checkPerm) {
	        return new BeatchDto(ids, status, checkPerm);
	}
	
	/**构造函数
	 * @param checkPerm TODO**/
	public BeatchDto(List<Integer> ids, Integer status, boolean checkPerm) {
                super();
                this.ids = ids;
                this.status = status;
        }

        public BeatchDto() {
	}

	@NotEmpty
	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

        public boolean isCheckPerm() {
                return checkPerm;
        }

        public void setCheckPerm(boolean checkPerm) {
                this.checkPerm = checkPerm;
        }
        
}
