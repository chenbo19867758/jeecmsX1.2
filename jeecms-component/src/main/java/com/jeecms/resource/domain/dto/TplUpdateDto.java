/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/5/8
 */

public class TplUpdateDto {
        private String root;
        private String filename;
        private String source;
        private Integer[] models;

        @NotNull
        public String getRoot() {
                return root;
        }

        public void setRoot(String root) {
                this.root = root;
        }

        @NotBlank
        public String getFilename() {
                return filename;
        }

        public void setFilename(String filename) {
                this.filename = filename;
        }

        @NotBlank
        public String getSource() {
                return source;
        }

        public void setSource(String source) {
                this.source = source;
        }

        public Integer[] getModels() {
                return models;
        }

        public void setModels(Integer[] models) {
                this.models = models;
        }

}
