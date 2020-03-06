package com.jeecms.common.upload;

import java.io.File;
import java.util.Calendar;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.jeecms.common.util.Num62;

/**
 * 上传工具类
 * @author: tom
 * @date:   2018年12月26日 上午11:00:22     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class UploadUtils {
	public static final String MONTH_FORMAT = "/yyyyMM/ddHHmmss";
	public static final String YEAR_MONTH_FORMAT = "yyyyMM";

	public static FastDateFormat getFastDateFormatInstance(String pattern) {
		return FastDateFormat.getInstance(pattern);
	}

	public static String generateFilename(String path, String ext) {
		return path + getFastDateFormatInstance(MONTH_FORMAT).format(Calendar.getInstance().getTime())
				+ RandomStringUtils.random(4, Num62.N36_CHARS) + "." + ext;
	}

	public static String generateRamdonFilename(String ext) {
		return getFastDateFormatInstance(MONTH_FORMAT).format(Calendar.getInstance().getTime())
				+ RandomStringUtils.random(4, Num62.N36_CHARS) + "." + ext;
	}

	public static String generateMonthname() {
		return getFastDateFormatInstance(YEAR_MONTH_FORMAT).format(Calendar.getInstance().getTime());
	}

	public static String generateByFilename(String path, String fileName, String ext) {
		return path + fileName + "." + ext;
	}

	protected static final Pattern ILLEGAL_CURRENT_FOLDER_PATTERN = Pattern
			.compile("^[^/]|[^/]$|/\\.{1,2}|\\\\|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}");

	/**
	 * Sanitizes a filename from certain chars.<br />
	 * This method enforces the <code>forceSingleExtension</code> property and
	 * then replaces all occurrences of \, /, |, :, ?, *, &quot;, &lt;, &gt;,
	 * control chars by _ (underscore).
	 * 
	 * @param filename
	 *            a potentially 'malicious' filename
	 * @return sanitized filename
	 */
	public static String sanitizeFileName(final String filename) {

		if (StringUtils.isBlank(filename)) {
			return filename;
		}

		String name = forceSingleExtension(filename);

		// Remove \ / | : ? * " < > 'Control Chars' with _
		return name.replaceAll("\\\\|/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}", "_");
	}

	/**
	 * Sanitizes a folder name from certain chars.<br />
	 * This method replaces all occurrences of \, /, |, :, ?, *, &quot;, &lt;,
	 * &gt;, control chars by _ (underscore).
	 * 
	 * @param folderName
	 *            a potentially 'malicious' folder name
	 * @return sanitized folder name
	 */
	public static String sanitizeFolderName(final String folderName) {

		if (StringUtils.isBlank(folderName)) {
			return folderName;
		}

		// Remove . \ / | : ? * " < > 'Control Chars' with _
		return folderName.replaceAll("\\.|\\\\|/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}", "_");
	}

	/**
	 * Checks whether a path complies with the FCKeditor File Browser <a href=
	 * "http://docs.fckeditor.net/FCKeditor_2.x/Developers_Guide/Server_Side_Integration#File_Browser_Requests"
	 * target="_blank">rules</a>.
	 * 
	 * @param path
	 *            a potentially 'malicious' path
	 * @return <code>true</code> if path complies with the rules, else
	 *         <code>false</code>
	 */
	public static boolean isValidPath(final String path) {
		if (StringUtils.isBlank(path)) {
			return false;
		}

		if (ILLEGAL_CURRENT_FOLDER_PATTERN.matcher(path).find()) {
			return false;
		}

		return true;
	}

	/**
	 * Replaces all dots in a filename with underscores except the last one.
	 * 
	 * @param filename
	 *            filename to sanitize
	 * @return string with a single dot only
	 */
	public static String forceSingleExtension(final String filename) {
		return filename.replaceAll("\\.(?![^.]+$)", "_");
	}

	/**
	 * Checks if a filename contains more than one dot.
	 * 
	 * @param filename
	 *            filename to check
	 * @return <code>true</code> if filename contains severals dots, else
	 *         <code>false</code>
	 */
	public static boolean isSingleExtension(final String filename) {
		return filename.matches("[^\\.]+\\.[^\\.]+");
	}

	/**
	 * Checks a directory for existence and creates it if non-existent.
	 * 
	 * @param dir
	 *            directory to check/create
	 */
	public static void checkDirAndCreate(File dir) {
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * Iterates over a base name and returns the first non-existent file.<br />
	 * This method extracts a file's base name, iterates over it until the first
	 * non-existent appearance with <code>basename(n).ext</code>. Where n is a
	 * positive integer starting from one.
	 * 
	 * @param file
	 *            base file
	 * @return first non-existent file
	 */
	public static File getUniqueFile(final File file) {
		if (!file.exists()) {
			return file;
		}

		File tmpFile = new File(file.getAbsolutePath());
		File parentDir = tmpFile.getParentFile();
		int count = 1;
		String extension = FilenameUtils.getExtension(tmpFile.getName());
		String baseName = FilenameUtils.getBaseName(tmpFile.getName());
		do {
			tmpFile = new File(parentDir, baseName + "(" + count++ + ")." + extension);
		} while (tmpFile.exists());
		return tmpFile;
	}

	public static void main(String[] args) {
		System.out.println(generateFilename("/base", "gif"));
	}

}
