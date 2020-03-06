/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.audit.domain.vo;

import java.util.List;

/**
 * 栏目设置树形VO
 * @author: ljw
 * @date: 2019年12月17日 下午2:52:44
 */
public class AuditChannelTreeVo {

	/** 栏目ID **/
	private Integer channelId;
	/** 栏目名称 **/
	private String channelName;
	/** 是否勾选 **/
	private Boolean select;
	/** 是否置灰 **/
	private Boolean gray;
	/**父结点**/
	private Integer parentId;
	/** 子集合 **/
	List<AuditChannelTreeVo> children;

	public AuditChannelTreeVo() {
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Boolean getGray() {
		return gray;
	}

	public void setGray(Boolean gray) {
		this.gray = gray;
	}

	public Boolean getSelect() {
		return select;
	}

	public void setSelect(Boolean select) {
		this.select = select;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public List<AuditChannelTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<AuditChannelTreeVo> children) {
		this.children = children;
	}

}
