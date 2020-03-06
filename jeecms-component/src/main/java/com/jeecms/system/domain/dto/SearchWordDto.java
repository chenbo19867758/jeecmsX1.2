/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.domain.dto;

import java.io.Serializable;

/**
 * 搜索词dto
 * @author: tom
 * @date: 2019年5月30日 下午1:40:53
 */
public class SearchWordDto implements Serializable {

        private static final long serialVersionUID = -2492379263022313798L;
        String word;
        Integer siteId;
        Integer searchCount;

        /**
         * 构造器
         * @param word 搜索词
         * @param siteId 站点ID
         * @param searchCount 搜索次数
         */
        public SearchWordDto(String word, Integer siteId, Integer searchCount) {
                super();
                this.word = word;
                this.siteId = siteId;
                this.searchCount = searchCount;
        }

        public String getWord() {
                return word;
        }

        public Integer getSiteId() {
                return siteId;
        }

        public Integer getSearchCount() {
                return searchCount;
        }

        public void setWord(String word) {
                this.word = word;
        }

        public void setSiteId(Integer siteId) {
                this.siteId = siteId;
        }

        public void setSearchCount(Integer searchCount) {
                this.searchCount = searchCount;
        }

}
