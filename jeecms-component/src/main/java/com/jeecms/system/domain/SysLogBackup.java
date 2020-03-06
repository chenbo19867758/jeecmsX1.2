/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.jeecms.common.base.domain.AbstractDomain;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 日志备份实体类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-31 14:10:32
 */
@Entity
@Table(name = "jc_sys_log_backup")
public class SysLogBackup extends AbstractDomain<Integer> implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer id;
        /**
         * 备份名称
         */
        private String backupName;
        /**
         * 起始时间
         */
        private Date startTime;
        /**
         * 截止时间
         */
        private Date endTime;
        /**
         * 备注
         */
        private String remark;
        /**
         * 数据量
         */
        private Integer dataCount;
        /**
         * 文件路径
         */
        private String backupFileUrl;

        public SysLogBackup() {
        }

        @Id
        @Column(name = "id", nullable = false, length = 11)
        @TableGenerator(name = "jc_sys_log_backup", pkColumnValue = "jc_sys_log_backup", initialValue = 0, allocationSize = 10)
        @GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_log_backup")
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
        @Column(name = "backup_name", nullable = true, length = 150)
        public String getBackupName() {
                return backupName;
        }

        public void setBackupName(String backupName) {
                this.backupName = backupName;
        }

        @NotNull
        @Temporal(TemporalType.DATE)
        @JSONField(format = "yyyy-MM-dd")
        @Column(name = "start_time", nullable = true, length = 19)
        public Date getStartTime() {
                return startTime;
        }

        public void setStartTime(Date startTime) {
                this.startTime = startTime;
        }

        @NotNull
        @Temporal(TemporalType.DATE)
        @JSONField(format = "yyyy-MM-dd")
        @Column(name = "end_time", nullable = true, length = 19)
        public Date getEndTime() {
                return endTime;
        }

        public void setEndTime(Date endTime) {
                this.endTime = endTime;
        }

        @Length(max = 1500)
        @Column(name = "remark", nullable = true, length = 1500)
        public String getRemark() {
                return remark;
        }

        public void setRemark(String remark) {
                this.remark = remark;
        }

        @Column(name = "data_count", nullable = true, length = 11)
        public Integer getDataCount() {
                return dataCount;
        }

        public void setDataCount(Integer dataCount) {
                this.dataCount = dataCount;
        }

        @Length(max = 255)
        @Column(name = "backup_file_url", nullable = false, length = 255)
        public String getBackupFileUrl() {
                return backupFileUrl;
        }

        public void setBackupFileUrl(String backupFileUrl) {
                this.backupFileUrl = backupFileUrl;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) {
                        return true;
                }
                if (!(o instanceof SysLogBackup)) {
                        return false;
                }

                SysLogBackup that = (SysLogBackup) o;

                if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
                        return false;
                }
                if (getBackupName() != null ? !getBackupName().equals(that.getBackupName()) : that.getBackupName() != null) {
                        return false;
                }
                if (getStartTime() != null ? !getStartTime().equals(that.getStartTime()) : that.getStartTime() != null) {
                        return false;
                }
                if (getEndTime() != null ? !getEndTime().equals(that.getEndTime()) : that.getEndTime() != null) {
                        return false;
                }
                if (getRemark() != null ? !getRemark().equals(that.getRemark()) : that.getRemark() != null) {
                        return false;
                }
                if (getDataCount() != null ? !getDataCount().equals(that.getDataCount()) : that.getDataCount() != null) {
                        return false;
                }
                return getBackupFileUrl() != null ? getBackupFileUrl().equals(that.getBackupFileUrl()) : that.getBackupFileUrl() == null;
        }

        @Override
        public int hashCode() {
                int result = getId() != null ? getId().hashCode() : 0;
                result = 31 * result + (getBackupName() != null ? getBackupName().hashCode() : 0);
                result = 31 * result + (getStartTime() != null ? getStartTime().hashCode() : 0);
                result = 31 * result + (getEndTime() != null ? getEndTime().hashCode() : 0);
                result = 31 * result + (getRemark() != null ? getRemark().hashCode() : 0);
                result = 31 * result + (getDataCount() != null ? getDataCount().hashCode() : 0);
                result = 31 * result + (getBackupFileUrl() != null ? getBackupFileUrl().hashCode() : 0);
                return result;
        }
}