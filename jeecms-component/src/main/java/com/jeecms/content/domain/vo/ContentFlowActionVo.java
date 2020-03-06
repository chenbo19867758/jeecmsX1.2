/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.content.domain.vo;

import java.io.Serializable;

/**
 * 内容审核流程支持的动作
 * 
 * @author: tom
 * @date: 2019年8月24日 下午4:09:50
 */
public class ContentFlowActionVo implements Serializable {

        private static final long serialVersionUID = -6150928833805331862L;
        /** 动作ID */
        Integer actionId;
        /** 动作名 */
        String actionName;
        /** 是否需要输入意见 */
        Boolean reasonNeed;
        /** 意见是否必填 */
        Boolean reasonMust;

        public ContentFlowActionVo(Integer actionId, String actionName, Boolean reasonNeed,
                        Boolean reasonMust) {
                super();
                this.actionId = actionId;
                this.actionName = actionName;
                this.reasonNeed = reasonNeed;
                this.reasonMust = reasonMust;
        }

        /**
         * @return the reasonNeed
         */
        public Boolean getReasonNeed() {
                return reasonNeed;
        }

        /**
         * @return the reasonMust
         */
        public Boolean getReasonMust() {
                return reasonMust;
        }

        /**
         * @param reasonNeed
         *                the reasonNeed to set
         */
        public void setReasonNeed(Boolean reasonNeed) {
                this.reasonNeed = reasonNeed;
        }

        /**
         * @param reasonMust
         *                the reasonMust to set
         */
        public void setReasonMust(Boolean reasonMust) {
                this.reasonMust = reasonMust;
        }

        /**
         * @return the actionId
         */
        public Integer getActionId() {
                return actionId;
        }

        /**
         * @return the actionName
         */
        public String getActionName() {
                return actionName;
        }

        /**
         * @param actionId
         *                the actionId to set
         */
        public void setActionId(Integer actionId) {
                this.actionId = actionId;
        }

        /**
         * @param actionName
         *                the actionName to set
         */
        public void setActionName(String actionName) {
                this.actionName = actionName;
        }

}
