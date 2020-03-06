/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.jeecms.common.base.domain.AbstractDomain;

import javax.persistence.*;
import java.io.Serializable;


/**
 * 敏感词实体类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-29
 */
@Entity
@Table(name = "jc_sys_sensitive_word")
public class SysSensitiveWord extends AbstractDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
        /**
         * 敏感词
         */
        @Excel(name = "敏感词", height = 20, width = 30, isImportField = "true_st")
        private String sensitiveWord;
        /**
         * 替换词
         */
        @Excel(name = "替换词", height = 20, width = 30, isImportField = "true_st")
        private String replaceWord;

        public SysSensitiveWord() {
        }

        @Id
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_sys_sensitive_word", pkColumnValue = "jc_sys_sensitive_word", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_sensitive_word")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "sensitive_word", nullable = false, length = 50)
        public String getSensitiveWord() {
                return sensitiveWord;
        }

        public void setSensitiveWord(String sensitiveWord) {
                this.sensitiveWord = sensitiveWord;
        }

        @Column(name = "replace_word", nullable = true, length = 150)
        public String getReplaceWord() {
                return replaceWord;
        }

        public void setReplaceWord(String replaceWord) {
                this.replaceWord = replaceWord;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((replaceWord == null) ? 0 : replaceWord.hashCode());
                result = prime * result + ((sensitiveWord == null) ? 0 : sensitiveWord.hashCode());
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
                SysSensitiveWord other = (SysSensitiveWord) obj;
                if (id == null) {
                        if (other.id != null) {
                                return false;
                        }
                } else if (!id.equals(other.id)) {
                        return false;
                }
                if (replaceWord == null) {
                        if (other.replaceWord != null) {
                                return false;
                        }
                } else if (!replaceWord.equals(other.replaceWord)) {
                        return false;
                }
                if (sensitiveWord == null) {
                        if (other.sensitiveWord != null) {
                                return false;
                        }
                } else if (!sensitiveWord.equals(other.sensitiveWord)) {
                        return false;
                }
                return true;
        }
}