/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.domain.vo;

import java.io.Serializable;

/**
 * 静态页面生成进度结果
 * 
 * @author: tom
 * @date: 2019年6月13日 上午10:09:10
 */
public class PageProcessResult implements Serializable {

        private static final long serialVersionUID = 850201015994611456L;
        int totalPage;
        int currPage;

        public synchronized int getTotalPage() {
                return totalPage;
        }

        public synchronized int getCurrPage() {
                return currPage;
        }

        public synchronized void setTotalPage(int totalPage) {
                this.totalPage = totalPage;
        }

        public synchronized void setCurrPage(int currPage) {
                if (currPage >= getTotalPage()) {
                        this.currPage = getTotalPage();
                        return;
                }
                this.currPage = currPage;
        }

        /**
         * 构造器
         * 
         * @param totalPage
         *                总页数
         * @param currPage
         *                当前生成页数
         */
        public PageProcessResult(int totalPage, int currPage) {
                super();
                this.totalPage = totalPage;
                this.currPage = currPage;
        }
}
