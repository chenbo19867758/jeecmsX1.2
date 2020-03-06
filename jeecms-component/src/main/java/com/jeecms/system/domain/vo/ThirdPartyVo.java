package com.jeecms.system.domain.vo;

import java.io.Serializable;

import com.jeecms.common.exception.SystemExceptionEnum;

/**
 * 第三方结果Vo
 * 
 * @author: ztx
 * @date: 2018年9月7日 下午2:13:12
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ThirdPartyVo implements Serializable {
        private static final long serialVersionUID = -1226358319803511849L;

        /** 结果状态码 */
        private String code;
        /** 结果消息 */
        private String message;
        /** 数据 */
        private Object data;

        public ThirdPartyVo() {
                super();
                this.code = SystemExceptionEnum.SUCCESSFUL.getCode();
                this.message = SystemExceptionEnum.SUCCESSFUL.getDefaultMessage();
        }

        public ThirdPartyVo(String message) {
                super();
                this.code = SystemExceptionEnum.SUCCESSFUL.getCode();
                this.message = message;
        }

        public ThirdPartyVo(Object data) {
                super();
                this.code = SystemExceptionEnum.SUCCESSFUL.getCode();
                this.message = SystemExceptionEnum.SUCCESSFUL.getDefaultMessage();
                this.data = data;
        }

        public ThirdPartyVo(String code, String message) {
                super();
                this.code = code;
                this.message = message;
        }

        public ThirdPartyVo(String code, String message, Object data) {
                super();
                this.code = code;
                this.message = message;
                this.data = data;
        }

        public String getCode() {
                return code;
        }

        public void setCode(String code) {
                this.code = code;
        }

        public String getMessage() {
                return message;
        }

        public void setMessage(String message) {
                this.message = message;
        }

        public Object getData() {
                return data;
        }

        public void setData(Object data) {
                this.data = data;
        }

        @Override
        public String toString() {
                return "ThirdPartyResultDTO [code=" + code + ", message=" + message + ", data=" + data + "]";
        }

}
