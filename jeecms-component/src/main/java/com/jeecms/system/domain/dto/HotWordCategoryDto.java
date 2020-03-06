/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;

/**
 * 热词分类Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/4/28
 */

public class HotWordCategoryDto {

        /**
         * 热词分类id
         */
        private Integer id;
        /**
         * 热词分类名称
         */
        private String cateName;
        /**
         * 指定栏目id
         */
        private Integer[] channelId;
        /**
         * 应用范围（1所有栏目，2指定栏目）
         */
        private Integer applyScope = 1;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        @NotBlank
        public String getCateName() {
                return cateName;
        }

        public void setCateName(String cateName) {
                this.cateName = cateName;
        }

        public Integer[] getChannelId() {
                return channelId;
        }

        public void setChannelId(Integer[] channelId) {
                this.channelId = channelId;
        }

        public Integer getApplyScope() {
                return applyScope;
        }

        public void setApplyScope(Integer applyScope) {
                this.applyScope = applyScope;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((applyScope == null) ? 0 : applyScope.hashCode());
                result = prime * result + ((cateName == null) ? 0 : cateName.hashCode());
                result = prime * result + Arrays.hashCode(channelId);
                result = prime * result + ((id == null) ? 0 : id.hashCode());
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
                HotWordCategoryDto other = (HotWordCategoryDto) obj;
                if (applyScope == null) {
                        if (other.applyScope != null) {
                                return false;
                        }
                } else if (!applyScope.equals(other.applyScope)) {
                        return false;
                }
                if (cateName == null) {
                        if (other.cateName != null) {
                                return false;
                        }
                } else if (!cateName.equals(other.cateName)) {
                        return false;
                }
                if (!Arrays.equals(channelId, other.channelId)) {
                        return false;
                }
                if (id == null) {
                        if (other.id != null) {
                                return false;
                        }
                } else if (!id.equals(other.id)) {
                        return false;
                }
                return true;
        }


}
