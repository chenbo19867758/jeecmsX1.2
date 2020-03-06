package com.jeecms.common.util.office;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.ContentErrorCodeEnum;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;
import com.jeecms.common.upload.UploadUtils;
import com.jeecms.common.util.SystemUtil;
import org.apache.commons.lang.StringUtils;
import org.jodconverter.OfficeDocumentConverter;
import org.jodconverter.office.LocalOfficeManager;
import org.jodconverter.office.LocalOfficeManager.Builder;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * JodConvert转换类
 * <b>该配置没有在项目启动时调用，原因在于</b>
 * <b>这个版本在springboot 启动的时候自动启动openoffice ,</b>
 * <b>没有安装openoffice 的机器项目都启动不了. </b>
 * <b>开发人员在操作时，需要手动调用startService()服务方法</b>
 * @author LJW
 */
@Component
public class OpenOfficeConverter {
	
	private static final Logger log = LoggerFactory.getLogger(OpenOfficeConverter.class);
	
	/**
	 * 开始服务(windows 2003可能启动不了会导致tomcat卡住)
	 * 
	 * @Title: startService
	 * @param officeHome 手动添加openOffice安装目录
	 */
	public void startService(String officeHome, String ports) {
		try {
			// 开始构建
			Builder builder = LocalOfficeManager.builder();
			//结束进程再开启会报已运行的异常，所以再次启动时，判断是否存在并允许，如果成立直接跳出不再启动服务, 另起一个线程即可
			if (officeManager != null && officeManager.isRunning()) {
				return;
			}
			// 设置OpenOffice.org安装目录
			builder.officeHome(officeHome);
			// 设置转换端口，默认为8100
			builder.portNumbers(StringUtils.isNotBlank(ports) ? Integer.valueOf(ports) : port);
			// 创建新的进程时，是否终止现有的Office进程
			builder.killExistingProcess(true);
			// 超时时间（默认）
			builder.processTimeout(120000);
			// 延迟时间（默认）
			builder.processRetryInterval(250);
			// 指定允许处理任务的最大时间（默认）
			builder.taskExecutionTimeout(1000 * 60 * 5L);
			// 最大任务数（默认）
			builder.maxTasksPerProcess(200);
			// 生存时间（默认）
			builder.taskQueueTimeout(30000);
			officeManager = builder.build();
			officeManager.start(); // 启动服务
			log.info("openoffice启动成功!");
		} catch (Exception ce) {
			log.error("openoffic 启动失败: " + ce.getMessage());
		} 
	}

	/**停止服务,销毁bean时停止服务**/
	@PreDestroy
	public void stopService() {
		if (officeManager != null) {
			try {
				officeManager.stop();
				log.info("openoffice关闭成功!");
			} catch (OfficeException e) {
				log.info("openoffice关闭失败!" + e.getMessage());
			}
		}
	}
	
	/**停止服务**/
	public void destroy() {
		this.stopService();
	}


	/**
	 * 转换格式
	 * 
	 * @param inputFile 需要转换的原文件路径
	 * @param outPath  输出目录
	 * @param fileType  要转换的目标文件类型 html,pdf
	 */
	public File convert(String inputFile, String outPath, String fileType)  throws GlobalException {
		// String outputFile = getFilePath()
		// +"/"+Calendar.getInstance().getTime().getTime()+fileType;
		String outputFile = UploadUtils.generateFilename(outPath, fileType);
		if (inputFile.endsWith(".txt")) {
			String odtFile = FileUtils.getFilePrefix(inputFile) + ".odt";
			if (new File(odtFile).exists()) {
				inputFile = odtFile;
			} else {
				try {
					FileUtils.copyFile(inputFile, odtFile);
					inputFile = odtFile;
				} catch (FileNotFoundException e) {
					log.error("openoffic convert fail " + e.getMessage());
					throw new GlobalException(SysOtherErrorCodeEnum.FILE_NOT_FIND);
				}
			}
		}
		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		File output = new File(outputFile);
		try {
			converter.convert(new File(inputFile), output);
		} catch (OfficeException e) {
			log.error("openoffic convert fail " + e.getMessage());
			throw new GlobalException(ContentErrorCodeEnum.OPEN_OFFICE_CONVERSION_ERROR);
		}
		return output;
	}

	/**
	 * 转换格式
	 * 
	 * @param inputFile 需要转换的原文件路径
	 * @param fileType  要转换的目标文件类型 html,pdf
	 * @param delOldFile  是否删除之前的文件
	 */
	public File convert(String inputFile, String fileType, boolean delOldFile) {
		File tempFile = null;
		String outputFile = UploadUtils.generateFilename(getFilePath(), fileType);
		if (inputFile.endsWith(".txt")) {
			String filecode = "";
			filecode = ConverEncoding.getFilecharset(inputFile);
			if (SystemUtil.isOSLinux()) {
				if (!filecode.equals("UTF-8")) {
					try {
						filecode = java.text.Normalizer
								.normalize(filecode, java.text.Normalizer.Form.NFKD);
						inputFile = java.text.Normalizer
								.normalize(inputFile, java.text.Normalizer.Form.NFKD);
						Runtime.getRuntime()
								.exec("iconv -f " + filecode + "  -t utf-8 " 
										+ inputFile + " -c -s -o " + inputFile);
					} catch (IOException e) {
						e.printStackTrace();
						log.error("openoffic convert fail " + e.getMessage());
					}
				}
			} else {
				// UTF8编码的文本不需要转odt格式
				if (!filecode.equals("UTF-8")) {
					String odtFile = FileUtils.getFilePrefix(inputFile) + ".odt";
					tempFile = new File(odtFile);
					if (tempFile.exists()) {
						inputFile = odtFile;
					} else {
						try {
							FileUtils.copyFile(inputFile, odtFile);
							tempFile = new File(odtFile);
							inputFile = odtFile;
						} catch (FileNotFoundException e) {
							log.error("openoffic convert fail " + e.getMessage());
							e.printStackTrace();
						}
					}
				}
			}
		}
		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		File output = new File(outputFile);
		File oldFile = new File(inputFile);
		try {
			converter.convert(oldFile, output);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (delOldFile && oldFile.exists()) {
			oldFile.delete();
		}
		//删除ODT文件
		if (tempFile != null && tempFile.exists()) {
			tempFile.delete();
		}
		return output;
	}

	/**转换PDF
	 * @throws GlobalException **/
	public File convertToPdf(String inputFile, String outPath, String fileName) 
			throws GlobalException {
		String outputFile = UploadUtils.generateByFilename(outPath, fileName, PDF);
		File tempFile = null;
		if (inputFile.endsWith(".txt")) {
			String filecode = "";
			filecode = ConverEncoding.getFilecharset(inputFile);
			if (SystemUtil.isOSLinux()) {
				if (!filecode.equals("UTF-8")) {
					try {
						Runtime.getRuntime()
								.exec("iconv -f " + filecode + "  -t utf-8 " 
										+ inputFile + " -c -s -o " + inputFile);
					} catch (IOException e) {
						log.error("openoffic convert fail " + e.getMessage());
						e.printStackTrace();
					}
				}
			} else {
				// UTF8编码的文本不需要转odt格式
				if (!filecode.equals("UTF-8")) {
					String odtFile = FileUtils.getFilePrefix(inputFile) + ".odt";
					tempFile = new File(odtFile);
					if (tempFile.exists()) {
						inputFile = odtFile;
					} else {
						try {
							FileUtils.copyFile(inputFile, odtFile);
							inputFile = odtFile;
						} catch (FileNotFoundException e) {
							log.error("openoffic convert fail " + e.getMessage());
							e.printStackTrace();
						}
					}
				}
			}
		}
		if (officeManager == null) {
			throw new GlobalException(ContentErrorCodeEnum.OPEN_OFFICE_SERVER_UNINSTALL);
		}
		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		File output = new File(outputFile);
		try {
			converter.convert(new File(inputFile), output);
		} catch (Exception e) {
			log.error("openoffic convert fail " + e.getMessage());
		}
		if (tempFile != null && tempFile.exists()) {
			try {
				org.apache.commons.io.FileUtils.deleteDirectory(tempFile.getParentFile());
			} catch (IOException e) {
				log.error("删除odt文件失败：" + e.getMessage());
			}
		}
		return output;
	}
	
	/**
	 * 转换HTML
	* @Title: convertToHtml 
	* @param docFile 源文件
	* @param htmlFile html文件
	* @return
	 */
	public File convertToHtml(File docFile, File htmlFile) {
		OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
		// 转换文档为html
		try {
			converter.convert(docFile, htmlFile);
		} catch (OfficeException e) {
			log.error("openoffic convert fail " + e.getMessage());
		}
		return htmlFile;
	}
	
	/**
	 * 转换HTML字符串
	* @Title: convertToHtmlString 
	* @param docFile 源文件
	* @param htmlFile html文件
	* @return
	 */
	public String convertToHtmlString(File docFile, File htmlFile) {
		htmlFile = convertToHtml(docFile, htmlFile);
		// 获取html文件流
		StringBuffer htmlSb = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(htmlFile),
					"gb2312")); 
			while (br.ready()) {
				String line = br.readLine();
				htmlSb.append(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// HTML文件字符串
		String htmlStr = htmlSb.toString();
		return htmlStr.toLowerCase();
	}
	
	private static OfficeManager officeManager;
	//安装目录
	private String officeHome;
	//默认8100
	private Integer port = 8100;
	private String filePath;

	/**构造函数**/
	public OpenOfficeConverter(String officeHome, int port, String filePath) {
		super();
		this.officeHome = officeHome;
		this.port = port;
		this.filePath = filePath;
	}

	/**构造函数**/
	public OpenOfficeConverter(String officeHome, int port) {
		super();
		this.officeHome = officeHome;
		this.port = port;
	}

	public OpenOfficeConverter() {
		super();
	}

	public String getOfficeHome() {
		return officeHome;
	}

	public void setOfficeHome(String officeHome) {
		this.officeHome = officeHome;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public static final String HTML = "html";
	public static final String PDF = "pdf";
	public static final String TXT = "txt";
	public static final String DOC = "doc";
	public static final String DOCX = "docx";
	public static final String XLS = "xls";
	public static final String XLSX = "xlsx";
	public static final String PPT = "ppt";
	public static final String PPTX = "pptx";
	public static final String WPS = "wps";
	
}
