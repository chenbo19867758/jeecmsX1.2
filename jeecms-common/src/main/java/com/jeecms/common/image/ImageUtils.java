package com.jeecms.common.image;

import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 图片辅助类
 * 
 * @author: tom
 * @date: 2018年4月16日 下午2:40:26
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ImageUtils {
	/**
	 * 图片的后缀
	 */
	public static final String[] IMAGE_EXT = new String[] { "jpg", "jpeg", "gif", "png", "bmp", "ico" };

	/** 1左上 2上 3右上 4左 5中 6右 7左下 8下 9右下 */
	public static final int POS_LEFT_TOP = 1;
	public static final int POS_TOP = 2;
	public static final int POS_RIGHT_TOP = 3;
	public static final int POS_LEFT = 4;
	public static final int POS_CENTER = 5;

	public static final int POS_RIGHT = 6;
	public static final int POS_LEFT_BOTTOM = 7;
	public static final int POS_BOTTOM = 8;
	public static final int POS_RIGHT_BOTTOM = 9;

	/**
	 * 是否是图片
	 * 
	 * @param ext
	 *            文件格式
	 * @return "jpg", "jpeg", "gif", "png", "bmp", "ico" 为文件后缀名者为图片
	 */
	public static boolean isValidImageExt(String ext) {
		ext = ext.toLowerCase(Locale.ENGLISH);
		for (String s : IMAGE_EXT) {
			if (s.equalsIgnoreCase(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否图片url
	 * 
	 * @Title: isValidImageUrl
	 * @param url
	 *            url
	 * @return: boolean
	 */
	public static boolean isValidImageUrl(String url) {
		url = url.toLowerCase(Locale.ENGLISH);
		for (String s : IMAGE_EXT) {
			if (url.endsWith(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the underlying input stream contains an image.
	 * 
	 * @param in
	 *            input stream of an image
	 * @return <code>true</code> if the underlying input stream contains an
	 *         image, else <code>false</code>
	 */
	public static boolean isImage(final InputStream in) {
		ImageInfo ii = new ImageInfo();
		ii.setInput(in);
		return ii.check();
	}

	/**
	 * 获得水印位置
	 * 
	 * @param width
	 *            原图宽度
	 * @param height
	 *            原图高度
	 * @param markWidth
	 *            水印宽度
	 * @param markHeight
	 *            水印高度
	 * @param p
	 *            水印位置 1-5，其他值为随机。1：左上；2：右上；3：左下；4：右下；5：中央。
	 * 
	 *            1左上 2上 3右上 4左 5中 6右 7左下 8下 9右下
	 * @return 水印位置
	 */
	public static Position markPosition(int width, int height, int p, int markWidth, int markHeight) {
		if (p < POS_LEFT_TOP || p > POS_RIGHT_BOTTOM) {
			Random ram = new SecureRandom();
			p = (int) (ram.nextFloat() * 5) + 1;
		}
		int x;
		int y;
		int offsetx = 0;
		int offsety = 0;
		/** 1左上 2上 3右上 4左 5中 6右 7左下 8下 9右下 */
		switch (p) {
			// 左上
			case 1:
				x = offsetx;
				y = markHeight;
				break;
			// 上中
			case 2:
				x = (width / 2) - (markWidth / 2);
				y = markHeight;
				break;
			// 右上
			case 3:
				x = width - markWidth;
				y = markHeight;
				break;
			// 左中
			case 4:
				x = offsetx;
				y = (height / 2) - (markHeight / 2);
				break;
			// 中
			case 5:
				x = (width / 2) - (markWidth / 2);
				y = (height / 2) - (markHeight / 2);
				break;
			// 右中
			case 6:
				x = width - markWidth;
				y = (height / 2) - (markHeight / 2);
				break;
			// 左下
			case 7:
				x = offsetx;
				y = height - markHeight;
				break;
			// 下
			case 8:
				x = (width / 2) - (markWidth / 2);
				y = height - markHeight;
				break;
			// 右下
			case 9:
				x = width - markWidth;
				y = height - markHeight;
				break;
			default:
				throw new RuntimeException("never reach ...");
		}
		return new Position(x, y);
	}

	/**
	 * 水印位置包含左边偏移量，右边偏移量。
	 */
	public static class Position {
		private int x;
		private int y;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}
	}

	/**
	 * 获取图片输入流的图片尺寸
	 * 
	 * @Title: getImageDimensions
	 * @param in
	 *            InputStream
	 * @return: String
	 */
	public static String getImageDimensions(final ImageInputStream in) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int width = 0;
		int height = 0;
		if (bi != null) {
			width = bi.getWidth();
			height = bi.getHeight();
		}
		return width + "*" + height;
	}

	/**
	 * 获取图片输入流的图片尺寸
	 *
	 * @Title: getImageDimensions
	 * @param in
	 *            InputStream
	 * @return: String
	 */
	public static String getImageDimensions(final File imgFile) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(imgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int width = 0;
		int height = 0;
		if (bi != null) {
			width = bi.getWidth();
			height = bi.getHeight();
		}
		return width + "*" + height;
	}

	/**
	 * 根据html代码获取代码内图片URL
	 * 
	 * @Title: getImageSrc
	 * @param htmlCode
	 *            htmlCode
	 * @return: List
	 */
	public static List<String> getImageSrc(String htmlCode) {
		List<String> imageSrcList = new ArrayList<String>();
		String regular = "<img(.*?)src=\"(.*?)\"";
		String imgPre = "(?i)<img(.*?)src=\"";
		String imgSub = "\"";
		Pattern p = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
		if (StringUtils.isNotBlank(htmlCode)) {
			Matcher m = p.matcher(htmlCode);
			String src = null;
			while (m.find()) {
				src = m.group();
				src = src.replaceAll(imgPre, "").replaceAll(imgSub, "").trim();
				imageSrcList.add(src);
			}
		}
		return imageSrcList;
	}

}
