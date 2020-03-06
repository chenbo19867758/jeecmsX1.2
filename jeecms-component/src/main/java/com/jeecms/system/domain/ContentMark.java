/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.jeecms.common.base.domain.AbstractSortDomain;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 发文字号管理实体类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-21
 */
@Entity
@Table(name = "jc_content_mark")
public class ContentMark extends AbstractSortDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;
        /**
         * 机关代字
         */
        public static final Integer MARK_TYPE_AGENCY = 1;
        /**
         * 年号A
         */
        public static final Integer MARK_TYPE_YEAR = 2;

        private Integer id;
        /**
         * 标记词名称
         */
        private String markName;
        /**
         * 标记词种类(1机关代字 2年份)
         */
        private Integer markType;

        public ContentMark() {
        }

        @Id
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_content_mark", pkColumnValue = "jc_content_mark", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_content_mark")
        @Override
        public Integer getId() {
                return this.id;
        }

        @Override
        public void setId(Integer id) {
                this.id = id;
        }

        @NotBlank
        @Length(max = 150)
        @Column(name = "mark_name", nullable = false, length = 150)
        public String getMarkName() {
                return markName;
        }

        public void setMarkName(String markName) {
                this.markName = markName;
        }

        @Column(name = "mark_type", nullable = false, length = 6)
        public Integer getMarkType() {
                return markType;
        }

        public void setMarkType(Integer markType) {
                this.markType = markType;
        }

        /**
         * 重写 hashCode
         *
         * @return int
         * @Title: hashCode
         */
        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((markName == null) ? 0 : markName.hashCode());
                result = prime * result + ((id == null) ? 0 : id.hashCode());
                result = prime * result + ((markType == null) ? 0 : markType.hashCode());
                return result;
        }


        /**
         * 重写  equals
         *
         * @param obj Object
         * @return boolean
         * @Title: equals
         */
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
                ContentMark other = (ContentMark) obj;
                if (markName == null) {
                        if (other.markName != null) {
                                return false;
                        }
                } else if (!markType.equals(other.markType)) {
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