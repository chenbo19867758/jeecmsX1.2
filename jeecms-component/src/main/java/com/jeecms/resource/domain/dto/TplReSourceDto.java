/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.resource.domain.dto;

import java.io.Serializable;

/**
 * @Description:模板资源保存Dto
 * @author: tom
 * @date: 2018年11月9日 下午2:05:51
 */
public class TplReSourceDto implements Serializable {
        private static final long serialVersionUID = -3640968260012730653L;

        private String root;
        private String filename;
        private String source;
        private Integer[] models;

        public String getRoot() {
                return root;
        }

        public void setRoot(String root) {
                this.root = root;
        }

        public String getFilename() {
                return filename;
        }

        public void setFilename(String filename) {
                this.filename = filename;
        }

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
