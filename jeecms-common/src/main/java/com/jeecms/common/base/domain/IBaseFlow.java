/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.common.base.domain;

/**
 * 流程接口
 * 
 * @author: tom
 * @date: 2019年8月3日 下午6:14:05
 */
public interface IBaseFlow {
	/**
	 * 设置流程id
	 * 
	 * @Title: setFlowProcessId
	 * @param flowProcessId
	 *            流程ID
	 * @return: void
	 */
	public void setFlowProcessId(String flowProcessId);

	/**
	 * 设置流程启动用户id
	 * 
	 * @Title: setFlowStartUserId
	 * @param userId
	 *            用户ID
	 * @return: Integer
	 */
	public void setFlowStartUserId(Integer flowStartUserId);

	/**
	 * 获取启动流程用户id
	 * 
	 * @Title: getFlowStartUserId
	 * @return: Integer
	 */
	public Integer getFlowStartUserId();

	/**
	 * 获取流程运行id
	 * 
	 * @Title: getFlowProcessId
	 * @return
	 * @return: String
	 */
	public String getFlowProcessId();

	/**
	 * 获取工作流ID
	 * 
	 * @Title: getFlowId
	 * @return: Integer
	 */
	public Integer getFlowId();
	
	
	/**
	 * 设置工作流ID
	 * @Title: setFlowId
	 * @param workflowId
	 */
	public void setFlowId(Integer workflowId);

	/**
	 * 设置数据状态
	 * 
	 * @Title: setStatus
	 * @param status
	 * @return: void
	 */
	public void setStatus(Integer status);
	
	/**
	 * 联动设置数据状态
	 * 
	 * @Title: setStatus
	 * @param status
	 * @return: void
	 */
	public void setStatusByChannel(Integer status);

	/**
	 * 获取数据ID
	 * 
	 * @Title: getId
	 * @return: Integer
	 */
	public Integer getId();
	
	/**
	 * 获取当前审核节点id
	 * @Title: getCurrNodeId
	 * @return: Integer
	 */
	public Integer getCurrNodeId();
	
	/**
	 * 设置当前审核节点id
	 * @Title: setCurrNodeId
	 * @param nodeId
	 * @return: Integer
	 */
	public void setCurrNodeId(Integer nodeId);
	
	/**
	 * 获取消息站位字符串
	 * @return
	 */
	public String getMsgPlace();
}
