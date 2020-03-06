package com.jeecms.common.exception.error;

import com.jeecms.common.exception.ExceptionInfo;

/**
 * 其他错误枚举  号段范围 15000~15500
 *
 * @author: tom
 * @date: 2020年1月4日 下午2:51:36
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public enum InteractErrorCodeEnum implements ExceptionInfo {
        /**
         * 名称错误、默认提示信息。
         */
        NAME_EXIST_ERROR("15001", "name exist error !"),
        /**默认类型不允许删除*/
        DEF_LETTER_TYPE_CANNOT_DEL_ERROR("15002", "Default type cannot be deleted!"),
        /**用户未登录*/
        USER_NOT_LOGIN("15003", "you are not login!"),
        /**提交信件超过限制*/
        WRITER_LETTER_EXCEED_COUNT("15004", "you are  exceed count!"),
        /**
         * 选项必填
         */
        WRITER_LETTER_REQUIRED("15005", "item required"),
        /**文件类型不允许*/
        WRITER_LETTER_FILE_TYPE_NOT_ALLOW("15006", "FILE type not allowed"),
        /**文件超过大小*/
        WRITER_LETTER_FILE_SIZE_NOT_ALLOW("15007", "FILE size not allowed"),
        /**文件数量超过限定*/
        WRITER_LETTER_FILE_NUMBER_NOT_ALLOW("15008", "FILE number not allowed"),
        /**该类型未设置表单*/
        LETTER_TYPE_NOT_SET_FORM("15009", "type not set form"),
        /**该类型未找到*/
        LETTER_TYPE_NOT_FIND("15010", "type not find"),
        /**该信件未找到*/
        LETTER_NOT_FIND("15011", "letter not find"),
        /**信件评分错误*/
        LETTER_SCORE_ERROR("15012", "信件评分错误，必须是2-10之间"),
        /**信件未开启评价错误*/
        LETTER_SCORE_CLOSE_ERROR("15013", "未开启评价！"),
        /**非自己信件不可评价错误*/
        LETTER_SCORE_NOT_SELF_ERROR("15014", "非自己信件不可评价！"),
        /**未回复不可评价错误*/
        LETTER_SCORE_NOT_REPLY_ERROR("15015", "未回复不可评价！"),
        /**不可重复评价*/
        LETTER_SCORE_REPEAT_ERROR("15016", "不可重复评价！"),
        /**不能转到当前信件停留信箱*/
        LETTER_FORWARD_TARGET_SAME_ERROR("15017", "不能转到当前信件停留信箱！"),
        /**信件回复正在审核中，不可再修改错误*/
        LETTER_REPLY_EDIT_ERROR("15018", "信件回复正在审核中，不可再修改！"),
        ;

        /**
         * 异常代码。
         */
        private String code;

        /**
         * 异常对应的默认提示信息。
         */
        private String defaultMessage;

        /**
         * 异常对应的原始提示信息。
         */
        private String originalMessage;

        /**
         * 当前请求的URL。
         */
        private String requestUrl;

        /**
         * 需转向（重定向）的URL，默认为空。
         */
        private String defaultRedirectUrl = "";

        /**
         * 异常对应的响应数据。
         */
        private Object data = new Object();

        /**
         * Description: 根据异常的代码、默认提示信息构建一个异常信息对象。
         *
         * @param code           异常的代码。
         * @param defaultMessage 异常的默认提示信息。
         */
        InteractErrorCodeEnum(String code, String defaultMessage) {
                this.code = code;
                this.defaultMessage = defaultMessage;
        }

        @Override
        public String getCode() {
                return code;
        }

        @Override
        public String getDefaultMessage() {
                return defaultMessage;
        }

        @Override
        public String getOriginalMessage() {
                return originalMessage;
        }

        @Override
        public void setOriginalMessage(String originalMessage) {
                this.originalMessage = originalMessage;
        }

        @Override
        public String getRequestUrl() {
                return requestUrl;
        }

        @Override
        public void setRequestUrl(String requestUrl) {
                this.requestUrl = requestUrl;
        }

        @Override
        public String getDefaultRedirectUrl() {
                return defaultRedirectUrl;
        }

        @Override
        public void setDefaultRedirectUrl(String defaultRedirectUrl) {
                this.defaultRedirectUrl = defaultRedirectUrl;
        }

        @Override
        public Object getData() {
                return data;
        }

        @Override
        public void setData(Object data) {
                this.data = data;
        }

}
