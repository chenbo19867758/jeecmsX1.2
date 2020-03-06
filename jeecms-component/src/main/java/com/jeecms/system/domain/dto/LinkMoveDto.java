/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * 友情链接移动Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/6/11 16:19
 */

public class LinkMoveDto {
        /**
         * 移动的友情链接id
         */
        private Integer[] ids;
        /**
         * 友情链接类别id
         */
        private Integer linkTypeId;

        @NotEmpty
        public Integer[] getIds() {
                return ids;
        }

        public void setIds(Integer[] ids) {
                this.ids = ids;
        }

        @NotNull
        public Integer getLinkTypeId() {
                return linkTypeId;
        }

        public void setLinkTypeId(Integer linkTypeId) {
                this.linkTypeId = linkTypeId;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) {
                        return true;
                }
                if (!(o instanceof LinkMoveDto)) {
                        return false;
                }

                LinkMoveDto that = (LinkMoveDto) o;

                // Probably incorrect - comparing Object[] arrays with Arrays.equals
                if (!Arrays.equals(getIds(), that.getIds())) {
                        return false;
                }
                return linkTypeId != null ? linkTypeId.equals(that.linkTypeId) : that.linkTypeId == null;
        }

        @Override
        public int hashCode() {
                int result = Arrays.hashCode(getIds());
                result = 31 * result + (linkTypeId != null ? linkTypeId.hashCode() : 0);
                return result;
        }
}
