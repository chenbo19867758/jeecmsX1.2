/*
 * * @Copyright:  江西金磊科技发展有限公司  All rights reserved.
 * Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.resource.domain.UploadFtp;

/**
 * FTP管理service层
 *
 * @author: wulongwei
 * @date: 2019年4月9日 下午2:26:44
 */
public interface FtpService extends IBaseService<UploadFtp, Integer> {

        /**
         * 添加ftp信息
         *
         * @param ftp
         * @return
         * @throws GlobalException
         * @Title: saveFtpInfo
         * @return: ResponseInfo
         */
        ResponseInfo saveFtpInfo(UploadFtp ftp) throws GlobalException;

        /**
         * 修改ftp信息
         *
         * @param ftp
         * @return
         * @throws GlobalException
         * @Title: updateFtpInfo
         * @return: ResponseInfo
         */
        ResponseInfo updateFtpInfo(UploadFtp ftp) throws GlobalException;
}
