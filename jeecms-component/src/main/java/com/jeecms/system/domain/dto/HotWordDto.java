/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 热词Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/4/29
 */

public class HotWordDto {

        /**
         * 覆盖原有热词
         */
        public static final Integer DEAL_WITH_COVER = 1;

        /**
         * 跳过原有热词
         */
        public static final Integer DEAL_WITH_JUMP = 2;

        /**
         * 热词分类id
         */
        private Integer hotWordCategoryId;

        /**
         * 热词id
         */
        private Integer id;

        /**
         * 热词名称
         */
        private String hotWord;

        /**
         * 链接地址
         */
        private String linkUrl;

        /**
         * 是否新窗口打开
         */
        private Boolean isTargetBlank;

        /**
         * 备注
         */
        private String remark;

        /**
         * 处理类型
         */
        private Integer dealWithType = DEAL_WITH_COVER;

        @NotNull
        public Integer getHotWordCategoryId() {
                return hotWordCategoryId;
        }

        public void setHotWordCategoryId(Integer hotWordCategoryId) {
                this.hotWordCategoryId = hotWordCategoryId;
        }

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        @NotBlank
        public String getHotWord() {
                return hotWord;
        }

        public void setHotWord(String hotWord) {
                this.hotWord = hotWord;
        }

        @NotBlank
        public String getLinkUrl() {
                return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
                this.linkUrl = linkUrl;
        }

        @NotNull
        public Boolean getTargetBlank() {
                return isTargetBlank;
        }

        public void setTargetBlank(Boolean targetBlank) {
                isTargetBlank = targetBlank;
        }

        public String getRemark() {
                return remark;
        }

        public void setRemark(String remark) {
                this.remark = remark;
        }

        public Integer getDealWithType() {
                return dealWithType;
        }

        public void setDealWithType(Integer dealWithType) {
                this.dealWithType = dealWithType;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((dealWithType == null) ? 0 : dealWithType.hashCode());
                result = prime * result + ((hotWord == null) ? 0 : hotWord.hashCode());
                result = prime * result + ((hotWordCategoryId == null) ? 0 : hotWordCategoryId.hashCode());
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((isTargetBlank == null) ? 0 : isTargetBlank.hashCode());
                result = prime * result + ((linkUrl == null) ? 0 : linkUrl.hashCode());
                result = prime * result + ((remark == null) ? 0 : remark.hashCode());
                return result;
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj) {
                        return true;
                }
                if (obj == null) {
                        return false;
                }
                if (getClass() != obj.getClass()) {
                        return false;
                }
                HotWordDto other = (HotWordDto) obj;
                if (dealWithType == null) {
                        if (other.dealWithType != null) {
                                return false;
                        }
                } else if (!dealWithType.equals(other.dealWithType)) {
                        return false;
                }
                if (hotWord == null) {
                        if (other.hotWord != null) {
                                return false;
                        }
                } else if (!hotWord.equals(other.hotWord)) {
                        return false;
                }
                if (hotWordCategoryId == null) {
                        if (other.hotWordCategoryId != null) {
                                return false;
                        }
                } else if (!hotWordCategoryId.equals(other.hotWordCategoryId)) {
                        return false;
                }
                if (id == null) {
                        if (other.id != null) {
                                return false;
                        }
                } else if (!id.equals(other.id)) {
                        return false;
                }
                if (isTargetBlank == null) {
                        if (other.isTargetBlank != null) {
                                return false;
                        }
                } else if (!isTargetBlank.equals(other.isTargetBlank)) {
                        return false;
                }
                if (linkUrl == null) {
                        if (other.linkUrl != null) {
                                return false;
                        }
                } else if (!linkUrl.equals(other.linkUrl)) {
                        return false;
                }
                if (remark == null) {
                        if (other.remark != null) {
                                return false;
                        }
                } else if (!remark.equals(other.remark)) {
                        return false;
                }
                return true;
        }


}
