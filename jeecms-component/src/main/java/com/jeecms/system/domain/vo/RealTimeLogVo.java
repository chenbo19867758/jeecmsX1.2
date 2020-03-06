/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain.vo;

import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/5/30 16:38
 */

public class RealTimeLogVo {

        /**
         * 最后文件大小
         */
        private long lastTimeFileSize;
        /**
         * 文件内容
         */
        private List<String> contents;
        /**
         * 新增行数
         */
        private Integer newRow;

        public long getLastTimeFileSize() {
                return lastTimeFileSize;
        }

        public void setLastTimeFileSize(long lastTimeFileSize) {
                this.lastTimeFileSize = lastTimeFileSize;
        }

        public List<String> getContents() {
                return contents;
        }

        public void setContents(List<String> contents) {
                this.contents = contents;
        }

        public Integer getNewRow() {
                return newRow;
        }

        public void setNewRow(Integer newRow) {
                this.newRow = newRow;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) {
                        return true;
                }
                if (!(o instanceof RealTimeLogVo)) {
                        return false;
                }

                RealTimeLogVo that = (RealTimeLogVo) o;

                if (getLastTimeFileSize() != that.getLastTimeFileSize()) {
                        return false;
                }
                if (getContents() != null ? !getContents().equals(that.getContents()) : that.getContents() != null) {
                        return false;
                }
                return getNewRow() != null ? getNewRow().equals(that.getNewRow()) : that.getNewRow() == null;
        }

        @Override
        public int hashCode() {
                int result = (int) (getLastTimeFileSize() ^ (getLastTimeFileSize() >>> 32));
                result = 31 * result + (getContents() != null ? getContents().hashCode() : 0);
                result = 31 * result + (getNewRow() != null ? getNewRow().hashCode() : 0);
                return result;
        }
}
