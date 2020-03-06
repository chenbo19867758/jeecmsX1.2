package com.jeecms.content.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * The persistent class for the jc_sys_lucene_error database table.
 * 
 */
@Entity
@Table(name = "jc_sys_lucene_error")
@NamedQuery(name = "ContentLuceneError.findAll", query = "SELECT c FROM ContentLuceneError c")
public class ContentLuceneError extends com.jeecms.common.base.domain.AbstractIdDomain<Integer>
                implements Serializable {
        private static final long serialVersionUID = 1L;
        public static final Short OP_CREATE = 1;
        public static final Short OP_UPDATE = 2;
        public static final Short OP_DELETE = 3;
        private Integer id;

        private Integer contentId;

        private Date createTime;

        private Short luceneOp;

        public ContentLuceneError() {
        }

        @Id
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_sys_lucene_error", pkColumnValue = "jc_sys_lucene_error",
                initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_lucene_error")
        public Integer getId() {
                return this.id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        @Column(name = "content_id")
        public Integer getContentId() {
                return this.contentId;
        }

        public void setContentId(Integer contentId) {
                this.contentId = contentId;
        }

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "create_time")
        public Date getCreateTime() {
                return this.createTime;
        }

        public void setCreateTime(Date createTime) {
                this.createTime = createTime;
        }

        @Column(name = "lucene_op")
        public Short getLuceneOp() {
                return this.luceneOp;
        }

        public void setLuceneOp(Short luceneOp) {
                this.luceneOp = luceneOp;
        }

}