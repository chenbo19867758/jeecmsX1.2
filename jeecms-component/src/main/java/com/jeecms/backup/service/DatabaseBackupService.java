package com.jeecms.backup.service;

import com.jeecms.backup.domain.dto.CompleteDto;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;

import java.io.File;
import java.io.IOException;

/**
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/8/2 16:27
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface DatabaseBackupService {

    /**
     * 备份当前连接的数据库
     *
     * @param siteId 站点id
     * @return com.jeecms.common.response.ResponseInfo
     * @author Zhu Kaixiao
     * @date 2019/8/5 10:15
     */
    ResponseInfo backup(Integer siteId) throws GlobalException;

    /**
     * 备份当前连接的数据库
     *
     * @param siteId 站点id
     * @param remark 备注
     * @return com.jeecms.common.response.ResponseInfo
     * @author Zhu Kaixiao
     * @date 2019/8/5 10:15
     */
    ResponseInfo backup(Integer siteId, String remark) throws GlobalException;

    /**
     * 还原数据库
     *
     * @param backupId 备份id
     * @param siteId 站点id
     * @return com.jeecms.common.response.ResponseInfo
     * @author Zhu Kaixiao
     * @date 2019/8/5 10:15
     */
    ResponseInfo recovery(int backupId, Integer siteId) throws Exception;

    /**
     * 下载备份文件
     *
     * @param backupId 备份id
     * @return java.io.File
     * @author Zhu Kaixiao
     * @date 2019/8/5 15:40
     */
    File download(int backupId) throws IOException, GlobalException;

    /**
     * 删除备份记录和备份文件
     *
     * @param backupId 备份id
     * @author Zhu Kaixiao
     * @date 2019/8/6 11:58
     */
    void deleteBackup(int backupId) throws Exception;

    /**
     * 备份完成时调用
     * 备份失败或成功都属于备份完成
     *
     * @param dto dto
     * @author Zhu Kaixiao
     * @date 2019/8/6 11:58
     */
    void onBackupComplete(CompleteDto dto) throws Exception;

    /**
     * 还原完成时调用
     * 还原失败或成功都属于还原完成
     *
     * @param dto dto
     * @author Zhu Kaixiao
     * @date 2019/8/6 11:58
     */
    void onRecoveryComplete(CompleteDto dto) throws GlobalException;
}
