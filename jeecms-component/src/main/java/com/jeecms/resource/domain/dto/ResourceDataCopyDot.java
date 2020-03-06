/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;

/**
 * 保存到我的资源Dto
 * @author xiaohui
 * @version 1.0
 * @date 2019/5/27 11:20
 */

public class ResourceDataCopyDot {

        /** id */
        @JSONField(serialize = true)
        private Integer[] ids;

        /**
         * 资源空间id
         */
        private Integer spaceId;

        @NotNull
        @Size(min = 1, max = 10000)
        public Integer[] getIds() {
                return ids;
        }

        public void setIds(Integer[] ids) {
                this.ids = ids;
        }

        @NotNull
        public Integer getSpaceId() {
                return spaceId;
        }

        public void setSpaceId(Integer spaceId) {
                this.spaceId = spaceId;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) {
                        return true;
                }
                if (!(o instanceof ResourceDataCopyDot)) {
                        return false;
                }

                ResourceDataCopyDot that = (ResourceDataCopyDot) o;

                // Probably incorrect - comparing Object[] arrays with Arrays.equals
                if (!Arrays.equals(getIds(), that.getIds())) {
                        return false;
                }
                return getSpaceId() != null ? getSpaceId().equals(that.getSpaceId()) : that.getSpaceId() == null;
        }

        @Override
        public int hashCode() {
                int result = Arrays.hashCode(getIds());
                result = 31 * result + (getSpaceId() != null ? getSpaceId().hashCode() : 0);
                return result;
        }
}
