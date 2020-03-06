/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.admin.controller.system;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.constants.WebConstants;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.system.constants.LogConstants;
import com.jeecms.system.domain.SysLog;
import com.jeecms.system.domain.dto.SearchLogDto;
import com.jeecms.system.domain.vo.RealTimeLogVo;
import com.jeecms.system.domain.vo.SysLogExportVo;
import com.jeecms.system.service.SysLogService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 日志Controller
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-29 17:00:15
 */
@RequestMapping("/logs")
@RestController
public class SysLogController extends BaseController<SysLog, Integer> {
	private static final Logger log = LoggerFactory.getLogger(SysLogController.class);

	private static final int LIST_SIZE = 10000;

	@Autowired
	private SysLogService sysLogService;
	/**
	 * 文件长度
	 */
	private long length = 0;

	@PostConstruct
	public void init() {
		String[] queryParams = {"username_LIKE", "clientIp_LIKE", "subEventType_LIKE", "logLevel_EQ_Integer",
				"operateType_EQ_Integer", "requestResult_EQ_Integer", "[beginDate,createTime]_GTE_Timestamp",
				"[endDate,createTime]_LTE_Timestamp", "logCategory_EQ_Integer"};
		super.setQueryParams(queryParams);
	}

	/**
	 * 列表分页
	 *
	 * @Title: 列表分页
	 * @Description: TODO
	 * @param: @param request
	 * @param: @param pageable
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/page")
	@SerializeField(clazz = SysLog.class, includes = {"id", "logCategory", "eventType", "subEventType", "logLevel",
			"logType", "operateType", "requestResult", "username", "clientIp", "timeConsuming",
			"createTime", "method", "uri", "responseTime"})
	public ResponseInfo page(HttpServletRequest request, @PageableDefault(sort = "createTime",
			direction = Direction.DESC) Pageable pageable) throws GlobalException {
		return super.getPage(request, pageable, true);
	}

	/**
	 * 告警日志分页
	 *
	 * @param request  HttpServletRequest
	 * @param pageable 分页组件
	 * @return ResponseInfo
	 */
	@GetMapping("/alarm/page")
	@SerializeField(clazz = SysLog.class, includes = {"id", "subEventType", "logType", "logLevel", "createTime", "responseTime"})
	public ResponseInfo alarmPage(HttpServletRequest request, @PageableDefault(sort = "createTime", direction =
			Direction.DESC) Pageable pageable) {
		Map<String, String[]> params = new HashMap<String, String[]>(4);
		params.put("EQ_logCategory_Integer", new String[]{String.valueOf(LogConstants.LOG_CATEGORY_ALARM)});
		params.put("GTE_createTime_Timestamp", new String[]{request.getParameter("beginDate")});
		params.put("LTE_createTime_Timestamp", new String[]{request.getParameter("endDate")});
		params.put("LIKE_subEventType_String", new String[]{request.getParameter("subEventType")});
		return new ResponseInfo(sysLogService.getPage(params, pageable, true));
	}

	/**
	 * 获取详情
	 *
	 * @Title: 获取详情
	 * @Description: TODO
	 * @param: @param id
	 * @param: @throws GlobalException
	 * @return: ResponseInfo
	 */
	@GetMapping(value = "/{id}")
	@Override
	@SerializeField(clazz = SysLog.class, includes = {"browser", "clientIp", "createTime", "eventType",
			"httpStatusCode", "logCategory", "logLevel", "logType", "method", "operateType", "os", "responseTime",
			"paramData", "requestResult", "subEventType", "timeConsuming", "uri", "userAgent", "username", "remark"})
	public ResponseInfo get(@PathVariable("id") Integer id) throws GlobalException {
		return super.get(id);
	}

	/**
	 * 导出日志
	 *
	 * @param dto      查询日志Dto
	 * @param response {@link HttpServletResponse}
	 * @throws GlobalException 异常
	 */
	@PostMapping("/export")
	public void export(@RequestBody SearchLogDto dto, HttpServletResponse response) {
		ExportParams exportParams = new ExportParams();
		Integer logCategory = dto.getLogCategory() == null ? 1 : dto.getLogCategory();
		exportParams.setSheetName(LogConstants.category(logCategory));
		Map<String, String[]> params = new HashMap<String, String[]>(9);
		params.put("LIKE_subEventType_String", new String[]{dto.getSubEventType()});
		if (dto.getBeginDate() != null) {
			params.put("GTE_createTime_Timestamp", new String[]{MyDateUtils.formatDate(dto.getBeginDate(),
					MyDateUtils.COM_Y_M_D_H_M_S_PATTERN)});
		}
		if (dto.getEndDate() != null) {
			params.put("LTE_createTime_Timestamp", new String[]{MyDateUtils.formatDate(dto.getEndDate(),
					MyDateUtils.COM_Y_M_D_H_M_S_PATTERN)});
		}
		params.put("LIKE_clientIp_String", new String[]{dto.getClientIp()});
		params.put("LIKE_username_String", new String[]{dto.getUsername()});
		params.put("EQ_logLevel_Integer", new String[]{dto.getLogLevel() != null ? String.valueOf(dto.getLogLevel()) : ""});
		params.put("EQ_operateType_Integer", new String[]{dto.getOperateType() != null
				? String.valueOf(dto.getOperateType()) : ""});
		params.put("EQ_requestResult_Integer", new String[]{dto.getRequestResult() != null
				? String.valueOf(dto.getRequestResult()) : ""});
		params.put("EQ_logCategory_Integer", new String[]{String.valueOf(logCategory)});
		List<SysLog> list = service.getList(params, null, false);
		Workbook workbook = null;
		int listSize = list.size();
		//子集合的长度
		int toIndex = 10000;
		for (int i = 0; i < listSize; i += LIST_SIZE) {
			if (i + LIST_SIZE > listSize) {
				toIndex = listSize - i;
			}
			List<SysLog> newList = list.subList(i, i + toIndex);
			workbook = ExcelExportUtil.exportBigExcel(exportParams, SysLog.class, newList);
		}
		if (workbook == null) {
			workbook = ExcelExportUtil.exportBigExcel(exportParams, SysLog.class, new ArrayList<SysLog>(0));
		}
		ExcelExportUtil.closeExportBigExcel();
		response.setContentType("application/x-download;charset=UTF-8");
		String category = LogConstants.category(logCategory);
		try {
			RequestUtils.setDownloadHeader(response, new String(category.getBytes(), "ISO8859-1") + ".xlsx");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
		FileOutputStream fos = null;
		InputStream input = null;
		OutputStream output = null;
		File file = null;
		try {
			fos = new FileOutputStream(WebConstants.SPT + category + ".xlsx");
			workbook.write(fos);
			file = new File(WebConstants.SPT + category + ".xlsx");
			input = new FileInputStream(file);
			output = response.getOutputStream();
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = input.read(buff)) > -1) {
				output.write(buff, 0, len);
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
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());

			}
		}
		if (file != null) {
			file.delete();
		}
	}

	/**
	 * 导出告警日志
	 *
	 * @param subEventType 事件子类型
	 * @param beginDate    开始时间
	 * @param endDate      结束时间
	 * @param response     {@link HttpServletResponse}
	 */
	@PostMapping("/alarm/export")
	public void exportAlarm(String subEventType, String beginDate, String endDate,
							HttpServletResponse response) {

		Map<String, String[]> params = new HashMap<String, String[]>(1);
		params.put("LIKE_subEventType_String", new String[]{subEventType});
		params.put("EQ_beginDate_Timestamp", new String[]{beginDate});
		params.put("EQ_endDate_Timestamp", new String[]{endDate});
		params.put("EQ_logCategory_Integer", new String[]{String.valueOf(LogConstants.LOG_CATEGORY_ALARM)});
		List<SysLog> logs = service.getList(params, null, false);
		List<SysLogExportVo> logExportVos = new ArrayList<SysLogExportVo>(logs.size());
		for (SysLog sysLog : logs) {
			logExportVos.add(new SysLogExportVo(sysLog.getSubEventType(), sysLog.getLogType(),
					sysLog.getCreateTime(), sysLog.getLogLevel()));
		}
		String name = "告警日志";
		Workbook workbook = null;
		ExportParams exportParams = new ExportParams();
		exportParams.setSheetName(name);
		int listSize = logExportVos.size();
		//子集合的长度
		int toIndex = 10000;
		for (int i = 0; i < listSize; i += LIST_SIZE) {
			if (i + LIST_SIZE > listSize) {
				toIndex = listSize - i;
			}
			List<SysLogExportVo> newList = logExportVos.subList(i, i + toIndex);
			workbook = ExcelExportUtil.exportBigExcel(exportParams, SysLogExportVo.class, newList);
		}
		if (workbook == null) {
			workbook = ExcelExportUtil.exportExcel(exportParams, SysLogExportVo.class, new ArrayList<SysLogExportVo>(0));
		}
		ExcelExportUtil.closeExportBigExcel();
		response.setContentType("application/x-download;charset=UTF-8");
		try {
			RequestUtils.setDownloadHeader(response, new String(name.getBytes(), "ISO8859-1") + ".xlsx");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
		FileOutputStream fos = null;
		InputStream input = null;
		OutputStream output = null;
		File file = null;
		try {
			fos = new FileOutputStream("/" + name + ".xlsx");
			workbook.write(fos);
			file = new File("/" + name + ".xlsx");
			input = new FileInputStream(file);
			output = response.getOutputStream();
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = input.read(buff)) > -1) {
				output.write(buff, 0, len);
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
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());

			}
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());

			}
		}
		if (file != null) {
			file.delete();
		}
	}

	/**
	 * @param lastTimeFileSize 开始大小
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("resource")
	@GetMapping("/realTime")
	public ResponseInfo realTimeLog(Long lastTimeFileSize) {
		String url = logAddress();
		File tmpLogFile = new File(url);
		//File tmpLogFile = new File("./logs/log." + MyDateUtils.formatDate(Calendar.getInstance().getTime()) + ".log");
		//指定文件可读可写
		RandomAccessFile randomFile;
		try {
			randomFile = new RandomAccessFile(tmpLogFile, "rw");
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			return new ResponseInfo(SysOtherErrorCodeEnum.FILE_NOT_FIND.getCode(),
					SysOtherErrorCodeEnum.FILE_NOT_FIND.getDefaultMessage());
		}
		List<String> list = new ArrayList<String>();
		try {

			if (lastTimeFileSize == null || lastTimeFileSize == 0) {
				list = readLastNLine(tmpLogFile, 10);
				Collections.reverse(list);
			} else {

				//获得变化部分的
				randomFile.seek(lastTimeFileSize);
				String tmp;
				while ((tmp = randomFile.readLine()) != null) {
					list.add(new String(tmp.getBytes("ISO8859-1")));
				}
			}
			lastTimeFileSize = randomFile.length();
			length = randomFile.length();
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
		RealTimeLogVo realTimeLogVo = new RealTimeLogVo();
		realTimeLogVo.setContents(list);
		realTimeLogVo.setLastTimeFileSize(lastTimeFileSize);
		return new ResponseInfo(realTimeLogVo);
	}

	/**
	 * 获取新日志行数
	 *
	 * @param lastTimeFileSize 最后文件大小
	 * @return ResponseInfo 新日志行数
	 */
	@GetMapping("/getNewLog")
	public ResponseInfo getNewLog(Long lastTimeFileSize) {
		//初始化游标
		long pos = length - 1;
		String url = logAddress();
		File tmpLogFile = new File(url);
		RandomAccessFile fileRead = null;
		try {
			fileRead = new RandomAccessFile(tmpLogFile, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int count = 0;
		lastTimeFileSize = lastTimeFileSize != null ? lastTimeFileSize : 1;
		while (pos >= lastTimeFileSize) {
			pos--;
			//开始读取
			try {
				fileRead.seek(pos);
				//如果读取到\n代表是读取到一行
				if (fileRead.readByte() == '\n') {
					//行数统计
					count++;
				}
			} catch (IOException e) {
				log.error(e.getMessage());
				return new ResponseInfo(SysOtherErrorCodeEnum.IO_ERROR.getCode(),
						SysOtherErrorCodeEnum.IO_ERROR.getDefaultMessage());
			}
		}
		log.info("获取新增日志数量成功", count);
		return new ResponseInfo(count);
	}

	/**
	 * 读取文件最后N行
	 * 根据换行符判断当前的行数，
	 * 使用统计来判断当前读取第N行
	 * PS:输出的List是倒叙，需要对List反转输出
	 *
	 * @param file    待文件
	 * @param numRead 读取的行数
	 * @return 日志内容
	 */
	private List<String> readLastNLine(File file, int numRead) {
		// 定义结果集
		List<String> result = new ArrayList<String>(numRead);
		//行数统计
		int count = 0;
		// 排除不可读状态
		if (!file.exists() || file.isDirectory() || !file.canRead()) {
			return null;
		}
		// 使用随机读取
		RandomAccessFile fileRead = null;
		try {
			//使用读模式
			fileRead = new RandomAccessFile(file, "r");
			//读取文件长度
			length = fileRead.length();
			//如果是0，代表是空文件，直接返回空结果
			if (length == 0L) {
				return result;
			} else {
				//初始化游标
				long pos = length - 1;
				while (pos > 0) {
					pos--;
					//开始读取
					fileRead.seek(pos);
					//如果读取到\n代表是读取到一行
					if (fileRead.readByte() == '\n') {
						//使用readLine获取当前行
						String line = fileRead.readLine();
						//保存结果
						result.add(new String(line.getBytes("ISO8859-1")));
						//行数统计，如果到达了numRead指定的行数，就跳出循环
						count++;
						if (count == numRead) {
							break;
						}
					}
				}
				if (pos == 0) {
					fileRead.seek(0);
					result.add(new String(fileRead.readLine().getBytes("ISO8859-1")));
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			if (fileRead != null) {
				try {
					//关闭资源
					fileRead.close();
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}
		return result;
	}

	/**
	 * 获取日志文件路径
	 *
	 * @return String
	 */
	private String logAddress() {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		String canonicalPath = "./logs/log." + MyDateUtils.formatDate(Calendar.getInstance().getTime()) + ".log";
		for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
			for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext(); ) {
				Appender<ILoggingEvent> appender = index.next();
				if (appender instanceof FileAppender) {
					FileAppender fileAppender = (FileAppender) appender;
					File file = new File(fileAppender.getFile());
					try {
						canonicalPath = file.getCanonicalPath();
					} catch (IOException e) {
						log.error("获取日志文件路径错误");
					}
				}
			}
		}
		return canonicalPath;
	}

}



