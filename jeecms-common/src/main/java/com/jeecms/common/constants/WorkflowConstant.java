/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.common.constants;

import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;

import java.util.Arrays;
import java.util.List;

/**
 * 工作流常量
 * 
 * @author: tom
 * @date: 2019年7月1日 上午9:12:46
 */

public abstract class WorkflowConstant {
	/**
	 * 节点类型-开始节点
	 */
	public static final  String WORKFLOW_TYPE_START = "begin";
	/**
	 * 节点类型-结束节点
	 */
	public static final  String WORKFLOW_TYPE_END = "end";
	/**
	 * 节点类型-审核节点
	 */
	public static final  String WORKFLOW_TYPE_AUDIT = "audit";
	/**
	 * 工作流流转条件-动作
	 */
	public static final  String WORKFLOW_CONDITION_EXPRESSION_ACTION = "action";
	/**
	 * 工作流线条动作类型编号-通过
	 */
	public static final  String WORKFLOW_ACTION_TYPE_PASS = "agree";
	/**
	 * 工作流线条动作类型编号-不通过
	 */
	public static final  String WORKFLOW_ACTION_TYPE_REJECT = "disagree";
	/**
	 * 工作流线条动作类型编号-系统自动驳回 不通过
	 */
	public static final  String WORKFLOW_ACTION_TYPE_SYS_REJECT = "sysdisagree";
	/**
	 * 工作流线条动作类型编号-提交审核
	 */
	public static final  String WORKFLOW_ACTION_TYPE_SUBMIT = "submit";

	/**
	 * 工作流线条动作类型编号集合
	 */
	public static final  List<String> GLOBAL_ACTION_TYPE = Arrays.asList(WORKFLOW_ACTION_TYPE_PASS,
			WORKFLOW_ACTION_TYPE_REJECT, WORKFLOW_ACTION_TYPE_SUBMIT);

	/**
	 * 工作流线条 全局动作名称-通过
	 */
	public static final  String WORKFLOW_ACTION_TYPE_PASS_NAME = "通过";
	/**
	 * 工作流线条 全局动作名称-不通过
	 */
	public static final  String WORKFLOW_ACTION_TYPE_REJECT_NAME = "不通过";
	/**
	 * 工作流线条 全局动作名称-提交审核
	 */
	public static final  String WORKFLOW_ACTION_TYPE_SUBMIT_NAME = "提交审核";

	/**
	 * 工作流 通过 是系统
	 */
	public static final  String WORKFLOW_ACTION_PASS_SYSTEM = "系统";

	/**
	 * 工作流流转条件-通过人数
	 */
	public static final  String WORKFLOW_CONDITION_EXPRESSION_PASS_NUM = "passUserNum";
	/**
	 * 工作流流转条件-不通过人数
	 */
	public static final  String WORKFLOW_CONDITION_EXPRESSION_REJECT_NUM = "rejectUserNum";
	/**
	 * 工作流流转条件-总审核人数
	 */
	public static final  String WORKFLOW_CONDITION_EXPRESSION_TOTAL_USER_NUM = "totalUserNum";
	/**
	 * 工作流流转条件-通过会签比例
	 */
	public static final  String WORKFLOW_CONDITION_EXPRESSION_PASS_RATIO = "passRatio";
	/**
	 * 工作流流转条件-非通过会签比例
	 */
	public static final  String WORKFLOW_CONDITION_EXPRESSION_NO_PASS_RATIO = "noPassRatio";

	/**
	 * 工作流流转条件-是否流转下一步
	 */
	public static final  String WORKFLOW_CONDITION_EXPRESSION_TO_NEXT = "toNext";
	/**
	 * 工作流流转条件-会签流转结果
	 */
	public static final  String WORKFLOW_CONDITION_EXPRESSION_SIGN_RESULT = "signResult";

	/**
	 * 工作流 流转变量 最后处理任务定义KEY
	 */
	public static final  String WORKFLOW_OUTPUT_VAR_LAST_TASK_KEY = "lastTaskKey";

	/**
	 * flowable内置变量 -任务实例总数
	 */
	public static final  String FLOWABLE_VAR_TOTAL_INSTANCE = "nrOfInstances";
	/**
	 * flowable内置变量 -任务完成实例数
	 */
	public static final  String FLOWABLE_VAR_COMPLETE_INSTANCE = "nrOfCompletedInstances";
	/**
	 * flowable内置变量 -任务活跃总数
	 */
	public static final  String FLOWABLE_VAR_ACTIVE_INSTANCE = "nrOfActiveInstances";
	/**
	 * 工作流 办理人
	 */
	public static final  String WORKFLOW_ASSIGN_USER = "users";

	/**
	 * 会签类型 一票通过
	 */
	public static final  Short WORKFLOW_SIGN_JOINTLY_TYPE_ONE_PASS = 1;

	/**
	 * 会签类型 2一票否决
	 */
	public static final  Short WORKFLOW_SIGN_JOINTLY_TYPE_ONE_REJECT = 2;

	/**
	 * 会签类型 3比例通过
	 */
	public static final  Short WORKFLOW_SIGN_JOINTLY_TYPE_RATIO = 3;

	/**
	 * 数据类型-1 内容
	 */
	public static final  Short WORKFLOW_DATA_TYPE_CONTENT = 1;
	/**
	 * 数据类型-2 问卷调查
	 */
	public static final  Short WORKFLOW_DATA_TYPE_QUESTIONNAIRE = 2;
	/**
	 * 数据类型-3 领导信箱
	 */
	public static final  Short WORKFLOW_DATA_TYPE_LETTER = 3;

	/***
	 * 工作流规则提示类错误编号
	 */
	public static final  List<String> WORKFLOW_RULE_TIP_CODE = Arrays.asList(
			SysOtherErrorCodeEnum.WORKFLOW_RULE_AUDIT_NODE_PASS_OUT_LINE_TIP.getCode(),
			SysOtherErrorCodeEnum.WORKFLOW_RULE_AUDIT_NODE_REJECT_OUT_LINE_TIP.getCode());

	/** 工作流-节点动作类型 **/
	public enum WorkflowAction {
		/**
		 * 通过
		 */
		agree,
		/**
		 * 不通过
		 */
		disagree
	}

	/** 工作流-办理情况 **/
	public enum WorkflowProcess {
		/**
		 * 已办
		 */
		handle,
		/**
		 * 未办
		 */
		to
	}

	/** 工作流-排序 **/
	public enum WorkflowFlowOrder {
		/**
		 * 流转剩余时间升序
		 */
		remainTimeAsc,
		/**
		 * 已办时间降序
		 */
		flowTimeDesc
	}
}
