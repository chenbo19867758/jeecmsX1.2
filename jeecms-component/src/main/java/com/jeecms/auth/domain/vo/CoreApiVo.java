/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.auth.domain.vo;

import java.io.Serializable;

import com.jeecms.auth.domain.CoreApi;

/**   
 * API vo
 * @author: tom
 * @date:   2019年8月13日 上午10:37:38     
 */
public class CoreApiVo implements Serializable {
        
        private static final long serialVersionUID = 2964803065806601883L;
        /** api地址 */
        private String apiUrl;
        /** 请求方式 1- get 2-post 3-update 4-put 5-delete */
        private String requestMethod;
        /**
         * 生成器
         * @Title: buildByApi
         * @param api CoreApi
         * @return: CoreApiVo
         */
        public static CoreApiVo buildByApi(CoreApi api){
                CoreApiVo vo= new CoreApiVo();
                vo.setApiUrl(api.getApiUrl());
                vo.setRequestMethod(CoreApi.getMethod(api.getRequestMethod()));
                return vo;       
        }
        /**
         * @return the apiUrl
         */
        public String getApiUrl() {
                return apiUrl;
        }
        /**
         * @return the requestMethod
         */
        public String getRequestMethod() {
                return requestMethod;
        }
        /**
         * @param apiUrl the apiUrl to set
         */
        public void setApiUrl(String apiUrl) {
                this.apiUrl = apiUrl;
        }
        /**
         * @param requestMethod the requestMethod to set
         */
        public void setRequestMethod(String requestMethod) {
                this.requestMethod = requestMethod;
        }
        
        
}
