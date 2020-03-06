package com.jeecms.common.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;

/**
 * 文件辅助类
 * 
 * @author: tom
 * @date: 2018年7月10日 上午10:22:09
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class FileUtils {
	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
	static final String SPECIALS1 = "\0";
	static final String SPECIALS2 = "/";
	static final String SPECIALS3 = "\\";
	/**
	 * doc的后缀
	 */
	public static final String[] DOC_EXT = new String[] { "doc", "docx", "wps", "txt", "pdf" };
	/**
	 * excel后缀
	 */
	public static final String[] EXCEL_EXT = new String[] { "xlsx", "xlsm", "xltx", "xltm", "xlsb", "xlam" };
	/**
	 * ppt后缀
	 */
	public static final String[] PPT_EXT = new String[] { "ppt", "pptx", "pptm", "ppsx", "potx", "potm" };
	/**
	 * 视频后缀
	 */
	public static final String[] VIDEO_EXT = new String[] { "avi", "asf", "wmv", "avs", "flv", "mkv", "mov", "3gp",
		"mp4", "mpg", "mpeg", "dat", "ogm", "vob", "rmvb", "rm", "ts", "ifo" };
	/**
	 * 音频后缀
	 */
	public static final String[] AUDIO_EXT = new String[] { "wav", "aac", "mp3", "aif", "au", "ram", "wma", "amr" };
	/**
	 * 压缩包后缀
	 */
	public static final String[] ZIP_EXT = new String[] { "zip", "rar", "tar", "gzip" };

	private static Map<String, String> fileTypeMap = new HashMap<String, String>();
	private static Map<String, List<String>> whiteList = new HashMap<String, List<String>>();
	private static List<String> blackList = new ArrayList<String>();

	static {
		// 初始化文件类型信息
		initFileTypeHeadInfos();
	}

	/**
	 * 是否是文档
	 * 
	 * @param ext
	 *            后缀格式
	 */
	public static boolean isValidDocExt(String ext) {
		ext = ext.toLowerCase(Locale.ENGLISH);
		for (String s : DOC_EXT) {
			if (s.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否Excel格式
	 * 
	 * @Title: isValidExcelExt
	 * @param ext
	 *            格式
	 * @return: boolean true 是Excel
	 */
	public static boolean isValidExcelExt(String ext) {
		ext = ext.toLowerCase(Locale.ENGLISH);
		for (String s : EXCEL_EXT) {
			if (s.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否ppt格式
	 * 
	 * @Title: isValidPptExt
	 * @param ext
	 *            格式
	 * @return: boolean true 是ppt
	 */
	public static boolean isValidPptExt(String ext) {
		ext = ext.toLowerCase(Locale.ENGLISH);
		for (String s : PPT_EXT) {
			if (s.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否视频格式
	 * 
	 * @Title: isValidVideoExt
	 * @param ext
	 *            格式
	 * @return: boolean true 是视频
	 */
	public static boolean isValidVideoExt(String ext) {
		ext = ext.toLowerCase(Locale.ENGLISH);
		for (String s : VIDEO_EXT) {
			if (s.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否音频格式
	 * 
	 * @Title: isValidAudioExt
	 * @param ext
	 *            格式
	 * @return: boolean true 是音频
	 */
	public static boolean isValidAudioExt(String ext) {
		ext = ext.toLowerCase(Locale.ENGLISH);
		for (String s : AUDIO_EXT) {
			if (s.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否压缩包格式
	 * 
	 * @Title: isValidZipExt
	 * @param ext
	 *            格式
	 * @return: boolean true 是压缩包
	 */
	public static boolean isValidZipExt(String ext) {
		ext = ext.toLowerCase(Locale.ENGLISH);
		for (String s : ZIP_EXT) {
			if (s.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断该文件是否允许上传！
	 * 
	 * @Title: checkFileIsValid
	 * @param inputStream
	 *            检查输入流上传的文件是否合法
	 * @return: Boolean
	 */
	public static Boolean checkFileIsValid(InputStream inputStream) {
		String suffix = null;
		String fileHeaderCode = null;
		Boolean flag = false;
		if (inputStream == null) {
			return false;
		}
		fileHeaderCode = getFileHeaderCode(inputStream);
		suffix = getFileSuffix(fileHeaderCode);
		boolean isInWhite = false;
		if (StringUtils.isNotBlank(suffix)) {
			suffix = suffix.toLowerCase(Locale.ENGLISH);
			isInWhite = whiteList.containsKey(suffix);
		}
		/** 没在白名单文件头中定义的则检查黑名单 可能是txt html等 */
		if (isInWhite) {
			if (codeIsBelongToExt(suffix, fileHeaderCode)) {
				flag = true;
			} else {
				flag = false;
			}
		} else {
			for (String string : blackList) {
				if (fileHeaderCode.toLowerCase(Locale.ENGLISH)
						.startsWith(string.toLowerCase(Locale.ENGLISH))
						|| string.toLowerCase(Locale.ENGLISH)
						.startsWith(fileHeaderCode.toLowerCase(Locale.ENGLISH))) {
					flag = false;
				} else {
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * 将InputStream文件流转换File
	 * 
	 * @Title: convertInputStreamToFile
	 * @param: @param
	 *             input 文件流
	 * @param: @param
	 *             filePath 文件路径及名称
	 * @param: @return
	 * @return: File
	 */
	public static File convertInputStreamToFile(InputStream input, String filePath) {
		BufferedOutputStream bos = null;
		FileOutputStream fileOutputStream = null;
		try {
			filePath = java.text.Normalizer.normalize(filePath, java.text.Normalizer.Form.NFKD);
			File file = new File(filePath);
			bos = new BufferedOutputStream(fileOutputStream = new FileOutputStream(file));
			byte[] buf = new byte[1024];
			int length;
			while ((length = input.read(buf)) != -1) {
				bos.write(buf, 0, length);
			}
			input.close();
			bos.close();
			return file;
		} catch (IOException e) {
			logger.debug("文件流转换出错{}", e.getMessage());
			return null;
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	/**
	 * 输入流转字节输出流
	 * 
	 * @Title: convertInputStreamToByte
	 * @param inputStream
	 *            InputStream
	 * @return: ByteArrayOutputStream
	 */
	public static ByteArrayOutputStream convertInputStreamToByte(InputStream inputStream) {
		try {
			ByteArrayOutputStream outByte = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = inputStream.read(buffer)) > -1) {
				outByte.write(buffer, 0, len);
			}
			outByte.flush();
			return outByte;
		} catch (Exception e) {
			logger.debug("文件流转换出错{}", e.getMessage());
			return null;
		}
	}

	/**
	 * 是否非法文件名或者路径
	 * 
	 * @Title: isValidFilename
	 * @param basePath
	 *            限定路径（选择文件或者路径只能是在这下面） 可为空， 为空则不验证限定路径
	 * @param filename
	 *            文件名或者路径  不可为空 为空则抛出异常 非法请求
	 * @throws GlobalException GlobalException
	 */
	public static void isValidFilename(String basePath, String filename) throws GlobalException {
		if (org.apache.commons.lang3.StringUtils.isBlank(filename)) {
			throw new GlobalException(SysOtherErrorCodeEnum.PATH_OR_FILENAME_VALID);
		}
		String filenameType1 = "../";
		String filenameType2 = "..\\";
		if (filename.contains(filenameType1) || filename.contains(filenameType2) || (filename.indexOf(SPECIALS1) != -1)) {
			throw new GlobalException(SysOtherErrorCodeEnum.PATH_OR_FILENAME_VALID);
		} else {
			if (org.apache.commons.lang3.StringUtils.isNoneBlank(basePath)) {
				if (!filename.startsWith(basePath)) {
					throw new GlobalException(SysOtherErrorCodeEnum.PATH_OR_FILENAME_VALID);
				}
			}
		}
	}

	/**
	 * 格式化文件名或者路徑名
	 * 
	 * @Title: normalizeFilename
	 * @param filename
	 *            文件名或者路径名
	 * @return: String
	 */
	public static String normalizeFilename(String filename) {
		if (org.apache.commons.lang3.StringUtils.isBlank(filename)) {
			return filename;
		}
		return java.text.Normalizer.normalize(filename, java.text.Normalizer.Form.NFKD);
	}

	private static void initFileTypeHeadInfos() {
		List<String> jpgHeadList = new ArrayList<String>();
		jpgHeadList.add("ffd8ff");
		List<String> pngHeadList = new ArrayList<String>();
		pngHeadList.add("89504e");
		List<String> gifHeadList = new ArrayList<String>();
		gifHeadList.add("47494638");
		List<String> bmpHeadList = new ArrayList<String>();
		bmpHeadList.add("424d");
		List<String> aviHeadList = new ArrayList<String>();
		aviHeadList.add("41564920");
		aviHeadList.add("52494646");
		List<String> wmvHeadList = new ArrayList<String>();
		wmvHeadList.add("3026b2758e66cf11a6d9");
		List<String> mp4HeadList = new ArrayList<String>();
		mp4HeadList.add("000000");
		List<String> pdfHeadList = new ArrayList<String>();
		pdfHeadList.add("255044462d312e");
		List<String> docHeadList = new ArrayList<String>();
		docHeadList.add("d0cf11e0a1b11ae10000");
		List<String> docxHeadList = new ArrayList<String>();
		docxHeadList.add("504b03041400");
		List<String> htmlHeadList = new ArrayList<String>();
		htmlHeadList.add("68746D6C3E");
		htmlHeadList.add("3c21444f435459504520");
		List<String> flvHeadList = new ArrayList<String>();
		flvHeadList.add("464c56");
		List<String> tifHeadList = new ArrayList<String>();
		tifHeadList.add("49492A00");
		List<String> rarHeadList = new ArrayList<String>();
		rarHeadList.add("52617221");
		rarHeadList.add("1f8b0800");
		rarHeadList.add("504b0304140000000800");

		whiteList.put("jpg", jpgHeadList);
		whiteList.put("jpeg", jpgHeadList);
		whiteList.put("png", pngHeadList);
		whiteList.put("gif", gifHeadList);
		whiteList.put("bmp", bmpHeadList);
		whiteList.put("avi", aviHeadList);
		whiteList.put("wmv", wmvHeadList);
		whiteList.put("mp4", mp4HeadList);
		whiteList.put("pdf", pdfHeadList);
		whiteList.put("doc", docHeadList);
		whiteList.put("xls", docHeadList);
		whiteList.put("ppt", docHeadList);
		whiteList.put("flv", flvHeadList);
		whiteList.put("docx", docxHeadList);
		whiteList.put("xlsx", docxHeadList);
		whiteList.put("pptx", docxHeadList);
		whiteList.put("html", htmlHeadList);
		whiteList.put("tif", tifHeadList);
		whiteList.put("rar", rarHeadList);
		// txt 文件头过于多了
		whiteList.put("txt", null);
		whiteList.put("3gp", null);
		blackList.add("4d5a90");
		fileTypeMapInit();
	}
	
	public static void fileTypeMapInit() {
		fileTypeMap.put("ffd8ff", "jpg");
		fileTypeMap.put("89504e", "png");
		fileTypeMap.put("47494638", "gif");
		fileTypeMap.put("424d", "bmp");
		fileTypeMap.put("41564920", "avi");
		fileTypeMap.put("52494646", "avi");
		fileTypeMap.put("3026b2758e66cf11a6d9", "wmv");
		fileTypeMap.put("000000", "mp4");
		fileTypeMap.put("255044462d312e", "pdf");
		fileTypeMap.put("d0cf11e0a1b11ae10000", "doc");
		// docx|xlsx|pptx
		fileTypeMap.put("504b03041400", "docx");
		fileTypeMap.put("68746D6C3E", "html");
		fileTypeMap.put("3c21444f435459504520", "html");
		fileTypeMap.put("3c21646f637479706520", "htm");
		fileTypeMap.put("49492a", "tif");
		fileTypeMap.put("414331", "dwg");
		fileTypeMap.put("48544d", "css");
		fileTypeMap.put("696b2e", "js");
		fileTypeMap.put("7b5c72", "rtf");
		fileTypeMap.put("384250", "psd");
		fileTypeMap.put("46726f", "eml");
		fileTypeMap.put("537461", "mdb");
		fileTypeMap.put("252150", "ps");
		fileTypeMap.put("2e524d", "rmvb");
		// flv、f4v
		fileTypeMap.put("464c56", "flv");
		fileTypeMap.put("494433", "mp3");
		fileTypeMap.put("000001", "mpg");
		fileTypeMap.put("52494646e27807005741", "wav");
		// MIDI (mid)
		fileTypeMap.put("4d546864000000060001", "mid");
		fileTypeMap.put("504b0304140000000800", "zip");
		fileTypeMap.put("52617221", "rar");
		fileTypeMap.put("235468", "ini");
		fileTypeMap.put("504b03040a0000000000", "jar");
		fileTypeMap.put("4d5a90", "exe");
		fileTypeMap.put("3c2540", "jsp");
		fileTypeMap.put("4d616e", "mf");
		fileTypeMap.put("3C3F786D6C", "xml");
		fileTypeMap.put("494e53", "sql");
		fileTypeMap.put("706163", "java");
		fileTypeMap.put("406563", "bat");
		fileTypeMap.put("1f8b0800000000000000", "gz");
		fileTypeMap.put("6c6f67", "properties");
		fileTypeMap.put("cafeba", "class");
		fileTypeMap.put("495453", "chm");
		fileTypeMap.put("04000000010000001300", "mxp");
		fileTypeMap.put("6431303a637265617465", "torrent");
		fileTypeMap.put("6D6F6F76", "mov");
		fileTypeMap.put("FF575043", "wpd");
		fileTypeMap.put("CFAD12FEC5FD746F", "dbx");
		fileTypeMap.put("2142444E", "pst");
		fileTypeMap.put("AC9EBD8F", "qdf");
		fileTypeMap.put("E3828596", "pwl");
		fileTypeMap.put("2E7261FD", "ram");
		fileTypeMap.put("49492A00", "tif");
	}

	/**
	 * 得到上传文件的文件头
	 * 
	 * @param src
	 *            文件数组
	 * @return
	 */
	private static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (null == src || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 获取文件类型
	 * 
	 * @param fileHeaderCode
	 *            文件头
	 * @return
	 */
	private static String getFileSuffix(String fileHeaderCode) {
		String res = null;
		Iterator<String> keyIter = fileTypeMap.keySet().iterator();
		while (keyIter.hasNext()) {
			String key = keyIter.next();
			if (key.toLowerCase(Locale.ENGLISH).startsWith(fileHeaderCode.toLowerCase(Locale.ENGLISH))
					|| fileHeaderCode.toLowerCase(Locale.ENGLISH).startsWith(key)) {
				res = fileTypeMap.get(key);
				break;
			}
		}
		return res;
	}

	/**
	 * 获取文件头
	 * 
	 * @Title: getFileHeaderCode
	 * @param file
	 *            輸入流
	 * @return: String
	 */
	private static String getFileHeaderCode(InputStream file) {
		byte[] b = new byte[10];
		String fileCode = "";
		try {
			file.read(b, 0, b.length);
			fileCode = bytesToHexString(b);
		} catch (IOException e) {
			logger.error("IOException:", e);
		}
		return fileCode;
	}

	/**
	 * 白名单，判断文件头是否与文件后缀一致
	 * 
	 * @param ext
	 *            文件后缀格式
	 * @param code
	 *            文件头
	 * @return
	 */
	private static Boolean codeIsBelongToExt(String ext, String code) {
		Boolean flag = false;
		List<String> list = whiteList.get(ext);
		if (list != null && list.size() > 0) {
			for (String string : list) {
				if (code.toLowerCase(Locale.ENGLISH)
						.startsWith(string.toLowerCase(Locale.ENGLISH))
						|| string.toLowerCase(Locale.ENGLISH)
						.startsWith(code.toLowerCase(Locale.ENGLISH))) {
					flag = true;
				}
			}
		}

		return flag;
	}

}
