package com.jeecms.common.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.jeecms.common.image.ImageUtils.Position;

/**
 * 图片缩小类。使用方型区域颜色平均算法
 * 
 * @author: tom
 * @date: 2018年12月25日 上午10:01:25
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class AverageImageScale {
	/**
	 * 缩小图片
	 * 
	 * @param srcFile
	 *            原图片
	 * @param destFile
	 *            目标图片
	 * @param boxWidth
	 *            缩略图最大宽度
	 * @param boxHeight
	 *            缩略图最大高度
	 * @throws IOException
	 *             IOException
	 */
	public static void resizeFix(File srcFile, File destFile, String srcFileName, int boxWidth, int boxHeight)
			throws IOException {
		BufferedImage srcImgBuff = ImageIO.read(srcFile);
		int width = srcImgBuff.getWidth();
		int height = srcImgBuff.getHeight();
		String ext = FilenameUtils.getExtension(srcFileName).toLowerCase(Locale.ENGLISH);
		if (width <= boxWidth && height <= boxHeight) {
			FileUtils.copyFile(srcFile, destFile);
			return;
		}
		int zoomWidth;
		int zoomHeight;
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		} else {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		}
		BufferedImage imgBuff = scaleImage(srcImgBuff, width, height, zoomWidth, zoomHeight);
		writeFile(imgBuff, ext, destFile);
	}

	/**
	 * 裁剪并压缩
	 * 
	 * @param srcFile
	 *            原文件
	 * @param destFile
	 *            目标文件
	 * @param boxWidth
	 *            缩略图最大宽度
	 * @param boxHeight
	 *            缩略图最大高度
	 * @param cutTop
	 *            裁剪TOP
	 * @param cutLeft
	 *            裁剪LEFT
	 * @param cutWidth
	 *            裁剪宽度
	 * @param catHeight
	 *            裁剪高度
	 * @throws IOException
	 *             IOException
	 */
	public static void resizeFix(File srcFile, File destFile, String srcFileName, int boxWidth, int boxHeight,
			int cutTop, int cutLeft, int cutWidth, int catHeight) throws IOException {
		BufferedImage srcImgBuff = ImageIO.read(srcFile);
		srcImgBuff = srcImgBuff.getSubimage(cutTop, cutLeft, cutWidth, catHeight);
		int width = srcImgBuff.getWidth();
		int height = srcImgBuff.getHeight();
		String ext = FilenameUtils.getExtension(srcFileName).toLowerCase(Locale.ENGLISH);
		if (width <= boxWidth && height <= boxHeight) {
			writeFile(srcImgBuff, ext, destFile);
			return;
		}
		int zoomWidth;
		int zoomHeight;
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		} else {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		}
		BufferedImage imgBuff = scaleImage(srcImgBuff, width, height, zoomWidth, zoomHeight);
		writeFile(imgBuff, ext, destFile);
	}

	/**
	 * 生成目标图片文件
	 * 
	 * @Title: writeFile
	 * @param imgBuf
	 *            BufferedImage
	 * @param destFile
	 *            File
	 * @throws IOException
	 *             IOException
	 * @return: void
	 */
	public static void writeFile(BufferedImage imgBuf, String originImgExt, File destFile) throws IOException {
		File parent = destFile.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		if (StringUtils.isBlank(originImgExt)) {
			originImgExt = "jpeg";
		}
		ImageIO.write(imgBuf, originImgExt, destFile);
	}

	/**
	 * 添加文字水印
	 * 
	 * @param srcFile
	 *            源图片文件。需要加水印的图片文件。
	 * @param destFile
	 *            目标图片。加水印后保存的文件。如果和源图片文件一致，则覆盖源图片文件。
	 * @param minWidth
	 *            需要加水印的最小宽度，如果源图片宽度小于该宽度，则不加水印。
	 * @param minHeight
	 *            需要加水印的最小高度，如果源图片高度小于该高度，则不加水印。
	 * @param pos
	 *            加水印的位置。
	 * @param text
	 *            水印文字
	 * @param color
	 *            水印颜色
	 * @param size
	 *            水印字体大小
	 * @param alpha
	 *            透明度
	 * @throws IOException
	 *             IOException
	 */
	public static void imageMark(File srcFile, File destFile, String srcFileName, int minWidth, int minHeight, int pos,
			String text, Color color, int size, int alpha) throws IOException {
		BufferedImage imgBuff = ImageIO.read(new FileInputStream(srcFile));
		String ext = FilenameUtils.getExtension(srcFileName).toLowerCase(Locale.ENGLISH);
		int width = imgBuff.getWidth();
		int height = imgBuff.getHeight();
		if (width <= minWidth || height <= minHeight) {
			imgBuff = null;
			if (!srcFile.equals(destFile)) {
				FileUtils.copyFile(srcFile, destFile);
			}
		} else {
			imageMark(imgBuff, width, height, pos, text, color, size, alpha);
			writeFile(imgBuff, ext, destFile);
			imgBuff = null;
		}
	}

	/**
	 * 添加图片水印
	 * 
	 * @param srcFile
	 *            源图片文件。需要加水印的图片文件。
	 * @param destFile
	 *            目标图片。加水印后保存的文件。如果和源图片文件一致，则覆盖源图片文件。
	 * @param minWidth
	 *            需要加水印的最小宽度，如果源图片宽度小于该宽度，则不加水印。
	 * @param minHeight
	 *            需要加水印的最小高度，如果源图片高度小于该高度，则不加水印。
	 * @param pos
	 *            加水印的位置。
	 * @param markFile
	 *            水印图片
	 * @throws IOException
	 *             IOException
	 */
	public static void imageMark(File srcFile, File destFile, String srcFileName, int minWidth, int minHeight, int pos,
			File markFile) throws IOException {
		BufferedImage imgBuff = ImageIO.read(srcFile);
		int width = imgBuff.getWidth();
		int height = imgBuff.getHeight();
		BufferedImage imgBuff2 = ImageIO.read(markFile);
		int width2 = imgBuff2.getWidth();
		int height2 = imgBuff2.getHeight();
		String ext = FilenameUtils.getExtension(srcFileName).toLowerCase(Locale.ENGLISH);
		if (width <= minWidth || height <= minHeight) {
			imgBuff = null;
			if (!srcFile.equals(destFile)) {
				FileUtils.copyFile(srcFile, destFile);
			}
		} else {
			imageMark(imgBuff, width, height, pos, markFile);
			writeFile(imgBuff, ext, destFile);
			imgBuff = null;
		}

	}

	/**
	 * 添加文字水印
	 * 
	 * @param imgBuff
	 *            原图片
	 * @param width
	 *            原图宽度
	 * @param height
	 *            原图高度
	 * @param pos
	 *            位置。1：左上；2：右上；3左下；4右下；5：中央；0或其他：随机。
	 * @param offsetX
	 *            水平偏移量。
	 * @param offsetY
	 *            垂直偏移量。
	 * @param text
	 *            水印内容
	 * @param color
	 *            水印颜色
	 * @param size
	 *            文字大小
	 * @param alpha
	 *            透明度。0-100。越小越透明。
	 * @throws IOException
	 *             IOException
	 */
	private static void imageMark(BufferedImage imgBuff, int width, int height, int pos, String text, Color color,
			int size, int alpha) throws IOException {
		Graphics2D g = imgBuff.createGraphics();
		g.setColor(color);
		g.setFont(new Font("宋体", Font.PLAIN, size));
		AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) alpha / 100);
		g.setComposite(a);
		java.awt.FontMetrics fm = g.getFontMetrics();
		int textHeight = fm.getHeight(); // 计算字体高度
		int textWidth = fm.stringWidth(text);// 计算字体宽度
		Position p = ImageUtils.markPosition(width, height, pos, textWidth, textHeight);
		g.drawString(text, p.getX(), p.getY());
		g.dispose();
	}

	private static void imageMark(BufferedImage imgBuff, int width, int height, int pos, File markFile)
			throws IOException {
		Graphics2D g = imgBuff.createGraphics();
		AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP);
		g.setComposite(a);
		BufferedImage markImgBuff = ImageIO.read(markFile);
		Position p = ImageUtils.markPosition(width, height, pos, markImgBuff.getWidth(), markImgBuff.getHeight());
		g.drawImage(ImageIO.read(markFile), p.getX(), p.getY(), null);
		g.dispose();
	}

	public static void main(String[] args) {
		File srcFile = new File("D:\\test\\xjp.png");
		File markFile = new File("D:\\test\\watermark.png");
		File destFile2 = new File("D:\\test\\2.png");
		File destFile3 = new File("D:\\test\\3.png");
		File destFile4 = new File("D:\\test\\4.png");
		File destFile5 = new File("D:\\test\\5.png");
		File destFile6 = new File("D:\\test\\6.png");
		File destFile7 = new File("D:\\test\\7.png");
		File destFile8 = new File("D:\\test\\8.png");
		File destFile9 = new File("D:\\test\\9.png");
		File destFile10 = new File("D:\\test\\10.png");

		try {
			BufferedImage imgBuff = ImageIO.read(srcFile);
			String oriname = "png";
			/**
			 * imageMark(imgBuff, imgBuff.getWidth(), imgBuff.getHeight(), 1,
			 * "jeecms", Color.red, 30, 100); writeFile(imgBuff, destFile2);
			 * 
			 * imgBuff = ImageIO.read(srcFile); imageMark(imgBuff,
			 * imgBuff.getWidth(), imgBuff.getHeight(), 2,"jeecms", Color.red,
			 * 30, 100); writeFile(imgBuff, destFile3);
			 * 
			 * imgBuff = ImageIO.read(srcFile); imageMark(imgBuff,
			 * imgBuff.getWidth(), imgBuff.getHeight(), 3, "jeecms", Color.red,
			 * 30, 100); writeFile(imgBuff, destFile4);
			 * 
			 * imgBuff = ImageIO.read(srcFile); imageMark(imgBuff,
			 * imgBuff.getWidth(), imgBuff.getHeight(), 4, "jeecms", Color.red,
			 * 30, 100); writeFile(imgBuff, destFile5);
			 * 
			 * imgBuff = ImageIO.read(srcFile); imageMark(imgBuff,
			 * imgBuff.getWidth(), imgBuff.getHeight(), 5, "jeecms", Color.red,
			 * 30, 100); writeFile(imgBuff, destFile6);
			 * 
			 * imgBuff = ImageIO.read(srcFile); imageMark(imgBuff,
			 * imgBuff.getWidth(), imgBuff.getHeight(), 6, "jeecms", Color.red,
			 * 30, 100); writeFile(imgBuff, destFile7);
			 * 
			 * imgBuff = ImageIO.read(srcFile); imageMark(imgBuff,
			 * imgBuff.getWidth(), imgBuff.getHeight(), 7, "jeecms", Color.red,
			 * 30, 100); writeFile(imgBuff, destFile8);
			 * 
			 * imgBuff = ImageIO.read(srcFile); imageMark(imgBuff,
			 * imgBuff.getWidth(), imgBuff.getHeight(), 8, "jeecms", Color.red,
			 * 30, 100); writeFile(imgBuff, destFile9);
			 * 
			 * imgBuff = ImageIO.read(srcFile); imageMark(imgBuff,
			 * imgBuff.getWidth(), imgBuff.getHeight(), 9, "jeecms", Color.red,
			 * 30, 100); writeFile(imgBuff, destFile10);
			 */

			imgBuff = ImageIO.read(srcFile);
			// imageMark(srcFile, destFile2, oriname,0, 0, 1, markFile);
			imageMark(imgBuff, imgBuff.getWidth(), imgBuff.getHeight(), 1, "jeecms", Color.red, 30, 100);
			writeFile(imgBuff, oriname, destFile2);

			// imageMark(srcFile, destFile3,oriname, 0, 0, 2, markFile);
			//
			// imageMark(srcFile, destFile4,oriname, 0, 0, 3, markFile);
			//
			// imageMark(srcFile, destFile5,oriname, 0, 0, 4, markFile);
			//
			// imageMark(srcFile, destFile6,oriname, 0, 0, 5, markFile);
			//
			// imageMark(srcFile, destFile7,oriname, 0, 0, 6, markFile);
			//
			// imageMark(srcFile, destFile8,oriname, 0, 0, 7, markFile);
			//
			// imageMark(srcFile, destFile9, oriname,0, 0, 8, markFile);
			//
			// imageMark(srcFile, destFile10,oriname, 0, 0, 9, markFile);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * private static void imageMark(BufferedImage imgBuff, int width, int
	 * height, int pos, int offsetX, int offsetY, File markFile) throws
	 * IOException { Position p = ImageUtils.markPosition(width, height, pos,
	 * offsetX, offsetY); Graphics2D g = imgBuff.createGraphics();
	 * AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP);
	 * g.setComposite(a); g.drawImage(ImageIO.read(markFile), p.getX(),
	 * p.getY(), null); g.dispose(); }
	 */

	private static BufferedImage scaleImage(BufferedImage srcImgBuff, int width, int height, int zoomWidth,
			int zoomHeight) {
		int[] colorArray = srcImgBuff.getRGB(0, 0, width, height, null, 0, width);
		BufferedImage outBuff = new BufferedImage(zoomWidth, zoomHeight, BufferedImage.TYPE_INT_RGB);
		// 宽缩小的倍数
		float wScale = (float) width / zoomWidth;
		int wScaleInt = (int) (wScale + 0.5);
		// 高缩小的倍数
		float hScale = (float) height / zoomHeight;
		int hScaleInt = (int) (hScale + 0.5);
		int area = wScaleInt * hScaleInt;
		int color;
		long red;
		long green;
		long blue;
		int x0;
		int x1;
		int y0;
		int y1;
		int x;
		int y;
		int i;
		int j;
		for (y = 0; y < zoomHeight; y++) {
			// 得到原图高的Y坐标
			y0 = (int) (y * hScale);
			y1 = y0 + hScaleInt;
			for (x = 0; x < zoomWidth; x++) {
				x0 = (int) (x * wScale);
				x1 = x0 + wScaleInt;
				red = green = blue = 0;
				for (i = x0; i < x1; i++) {
					for (j = y0; j < y1; j++) {
						color = colorArray[width * j + i];
						red += getRedValue(color);
						green += getGreenValue(color);
						blue += getBlueValue(color);
					}
				}
				outBuff.setRGB(x, y, comRGB((int) (red / area), (int) (green / area), (int) (blue / area)));
			}
		}
		return outBuff;
	}

	private static int getRedValue(int rgbValue) {
		return (rgbValue & 0x00ff0000) >> 16;
	}

	private static int getGreenValue(int rgbValue) {
		return (rgbValue & 0x0000ff00) >> 8;
	}

	private static int getBlueValue(int rgbValue) {
		return rgbValue & 0x000000ff;
	}

	private static int comRGB(int redValue, int greenValue, int blueValue) {
		return (redValue << 16) + (greenValue << 8) + blueValue;
	}

}