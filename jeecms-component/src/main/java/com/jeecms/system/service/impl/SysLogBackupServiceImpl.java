/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.constants.TplConstants;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.SystemExceptionInfo;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.service.impl.FtpTpl;
import com.jeecms.system.dao.SysLogBackupDao;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.SysLog;
import com.jeecms.system.domain.SysLogBackup;
import com.jeecms.system.domain.vo.SysLogVo;
import com.jeecms.system.service.SysLogBackupService;
import com.jeecms.system.service.SysLogService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志备份Service实现类
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-31 14:12:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysLogBackupServiceImpl extends BaseServiceImpl<SysLogBackup, SysLogBackupDao, Integer> implements SysLogBackupService {

	private static final Logger log = LoggerFactory.getLogger(SysLogBackupServiceImpl.class);

	private static final String FTP = "ftp";

	private static final int LIST_SIZE = 10000;

	@Value("${freemarker.resources.type}")
	private String type;
	@Autowired
	private SysLogService logService;
	@Autowired
	private RealPathResolver realPathResolver;

	@Override
	public SysLogBackup backup(SysLogBackup logBackup, CmsSite site) throws GlobalException {
		String fileUrl = TplConstants.LOG_BACKUP + WebConstants.SPT + logBackup.getBackupName() + ".xls";
		Map<String, String[]> map = new HashMap<String, String[]>(2);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		map.put("GTE_createTime_Date", new String[]{format.format(logBackup.getStartTime())});
		map.put("LTE_createTime_Date", new String[]{format.format(logBackup.getEndTime())});
		List<SysLog> list = logService.getList(map, null, false);
		logBackup.setBackupFileUrl(fileUrl);
		logBackup.setDataCount(list.size());
		export(list, fileUrl, logBackup.getBackupName(), site);
		return super.save(logBackup);
	}

	private void export(List<SysLog> list, String fileUrl, String backupName, CmsSite site) throws GlobalException {
		List<SysLogVo> logVos = new ArrayList<SysLogVo>(list.size());
		for (SysLog sysLog : list) {
			logVos.add(new SysLogVo(sysLog));
		}
		Workbook workbook = null;
		ExportParams exportParams = new ExportParams();
		exportParams.setSheetName(backupName);
		int listSize = logVos.size();
		//子集合的长度
		int toIndex = 10000;
		for (int i = 0; i < listSize; i += LIST_SIZE) {
			if (i + LIST_SIZE > listSize) {
				toIndex = listSize - i;
			}
			List<SysLogVo> newList = logVos.subList(i, i + toIndex);
			workbook = ExcelExportUtil.exportBigExcel(exportParams, SysLogVo.class, newList);
		}
		if (workbook == null) {
			workbook = ExcelExportUtil.exportExcel(exportParams, SysLogVo.class, new ArrayList<SysLogVo>(0));
		}
		FileOutputStream fos = null;
		try {
			File file = new File(realPathResolver.get(TplConstants.LOG_BACKUP));
			if (!file.exists()) {
				file.mkdirs();
			}
			fos = new FileOutputStream(realPathResolver.get(fileUrl));
			workbook.write(fos);
			if (WebConstants.FREEMARKER_RES_TYPE.equalsIgnoreCase(type)) {
				UploadFtp uploadFtp = site.getUploadFtp();
				FtpTpl ftpTpl = new FtpTpl(uploadFtp);
				InputStream inputStream = new FileInputStream(new File(realPathResolver.get(fileUrl)));
				ftpTpl.storeByFilename(fileUrl, inputStream);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}

	@Override
	public boolean checkByBackupName(String backupName, Integer id) {
		if (StringUtils.isBlank(backupName)) {
			return true;
		}
		SysLogBackup logBackup = dao.findByBackupName(backupName);
		if (logBackup == null) {
			return true;
		} else {
			if (id == null) {
				return false;
			} else {
				return logBackup.getId().equals(id);
			}
		}
	}

	@Override
	public List<SysLog> restore(Integer id, CmsSite site) throws GlobalException {
		SysLogBackup logBackup = findById(id);
		FileInputStream fis = null;
		String fileUrl = logBackup.getBackupFileUrl();
		try {
			if (FTP.equalsIgnoreCase(type)) {
				UploadFtp uploadFtp = site.getUploadFtp();
				FtpTpl ftpTpl = new FtpTpl(uploadFtp);
				fis = (FileInputStream) ftpTpl.getInputStream(fileUrl);
			} else {
				fis = new FileInputStream(realPathResolver.get(logBackup.getBackupFileUrl()));
			}
			ImportParams params = new ImportParams();
			List<SysLogVo> list = ExcelImportUtil.importExcel(fis, SysLogVo.class, params);
			Map<String, String[]> map = new HashMap<String, String[]>(2);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			map.put("GTE_createTime_Date", new String[]{format.format(logBackup.getStartTime())});
			map.put("LTE_createTime_Date", new String[]{format.format(logBackup.getEndTime())});
			List<SysLog> sysLogs = logService.getList(map, null, false);
			List<SysLog> logs = new ArrayList<SysLog>(list.size());
			for (SysLogVo sysLogVo : list) {
				SysLog log = voToBean(sysLogVo);
				if (!sysLogs.contains(log)) {
					logs.add(log);
				}
			}
			logService.saveAll(logs);
			return logs;
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			throw new GlobalException(new SystemExceptionInfo(
					SysOtherErrorCodeEnum.FILE_NOT_FIND.getDefaultMessage(),
					SysOtherErrorCodeEnum.FILE_NOT_FIND.getCode()));
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new GlobalException(new SystemExceptionInfo(
					SysOtherErrorCodeEnum.IO_ERROR.getDefaultMessage(),
					SysOtherErrorCodeEnum.IO_ERROR.getCode()));
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GlobalException(new SystemExceptionInfo(
					SysOtherErrorCodeEnum.IMPORT_EXCEL_ERROR.getDefaultMessage(),
					SysOtherErrorCodeEnum.IMPORT_EXCEL_ERROR.getCode()));
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}

	private SysLog voToBean(SysLogVo vo) {
		SysLog log = new SysLog();
		log.setId(vo.getId());
		log.setLogCategory(vo.getLogCategory());
		log.setLogType(vo.getLogType());
		log.setEventType(vo.getEventType());
		log.setLogLevel(vo.getLogLevel());
		log.setOperateType(vo.getOperateType());
		log.setSubEventType(vo.getSubEventType());
		log.setUsername(vo.getUsername());
		log.setClientIp(vo.getClientIp());
		log.setUri(vo.getUri());
		log.setMethod(vo.getMethod());
		log.setParamData(vo.getParamData());
		log.setSessionId(vo.getSessionId());
		log.setReturmTime(vo.getReturmTime());
		log.setReturnData(vo.getReturnData());
		log.setHttpStatusCode(vo.getHttpStatusCode());
		log.setTimeConsuming(vo.getTimeConsuming());
		log.setRequestResult(vo.getRequestResult());
		log.setBrowser(vo.getBrowser());
		log.setOs(vo.getOs());
		log.setUserAgent(vo.getUserAgent());
		log.setCreateTime(vo.getCreateTime());
		log.setCreateUser(vo.getCreateUser());
		log.setUpdateTime(vo.getUpdateTime());
		log.setUpdateUser(vo.getUpdateUser());
		log.setHasDeleted(vo.getHasDeleted());
		return log;
	}

}