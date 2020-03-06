/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain.vo;

import java.util.List;
import java.util.Map;

import com.jeecms.content.domain.Content;

/**
 * 内容Vo
 * 
 * @author: ljw
 * @date: 2019年5月17日 上午10:26:10
 */
public class ContentVo {

	/** 内容对象 **/
	private Content cmsContent;
	/** 权限数据 **/
	private Map<String, Boolean> operations;
	/** 内容类型权限 **/
	private List<TypeOperations> types;
	/** 内容栏目id**/
	private Integer channelId;
	/** 引用内容状态 **/
	private Integer status;
	/** 引用内容创建方式 **/
	private Integer createType;
	/** 是否引用数据 **/
	private Boolean quote;
	/** 引用栏目名称 **/
	private String quoteChannelName;
	/** 当前审核节点名称*/
	private String currentNodeName;

	public Content getCmsContent() {
		return cmsContent;
	}

	public void setCmsContent(Content cmsContent) {
		this.cmsContent = cmsContent;
	}

	public Map<String, Boolean> getOperations() {
		return operations;
	}

	public void setOperations(Map<String, Boolean> operations) {
		this.operations = operations;
	}

	public List<TypeOperations> getTypes() {
		return types;
	}

	public void setTypes(List<TypeOperations> types) {
		this.types = types;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getQuote() {
		return quote;
	}

	public void setQuote(Boolean quote) {
		this.quote = quote;
	}

	public String getQuoteChannelName() {
		return quoteChannelName;
	}

	public void setQuoteChannelName(String quoteChannelName) {
		this.quoteChannelName = quoteChannelName;
	}
	
        public String getCurrentNodeName() {
                return currentNodeName;
        }

        public void setCurrentNodeName(String currentNodeName) {
                this.currentNodeName = currentNodeName;
        }

        public Integer getCreateType() {
		return createType;
	}

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}

	

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}



	public class TypeOperations {
		// 类型ID
		private Integer id;
		// 类型名称
		private String typeName;
		// 是否有权限
		private Boolean operation;
		// logo 图标
		private String logo;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public Boolean getOperation() {
			return operation;
		}

		public void setOperation(Boolean operation) {
			this.operation = operation;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}
	}
}
