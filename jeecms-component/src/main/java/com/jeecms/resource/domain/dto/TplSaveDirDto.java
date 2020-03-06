/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.domain.dto;

import javax.validation.constraints.NotBlank;

/**
 * 创建文件夹Dto
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019/5/7
 */

public class TplSaveDirDto {

        /**
         * 文件夹名称
         */
        private String dirName;

        /**
         * 文件夹路径
         */
        private String root;

        @NotBlank
        public String getDirName() {
                return dirName;
        }

        public void setDirName(String dirName) {
                this.dirName = dirName;
        }

        public String getRoot() {
                return root;
        }

        public void setRoot(String root) {
                this.root = root;
        }
}
