package com.jeecms.common.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import magick.CompositeOperator;
import magick.DrawInfo;
import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;
import magick.PixelPacket;

import org.apache.commons.io.FileUtils;

import com.jeecms.common.image.ImageUtils.Position;

/**
 * MagickImageScale 工具类
 * @author: tom
 * @date:   2018年12月25日 上午10:02:08     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class MagickImageScale {
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
	 * @throws IOException IOException
	 * @throws MagickException MagickException
	 */
	public static void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight) throws IOException, MagickException {
		ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
		MagickImage image = new MagickImage(info);
		// 计算缩小宽高
		Dimension dim = image.getDimension();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		int zoomWidth;
		int zoomHeight;
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		} else {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		}
		// 缩小
		MagickImage scaled = image.scaleImage(zoomWidth, zoomHeight);
		// 输出
		scaled.setFileName(destFile.getAbsolutePath());
		scaled.writeImage(info);
		scaled.destroyImages();
	}

	/**
	 * 裁剪并缩小
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
	 * @throws IOException  IOException
	 * @throws MagickException MagickException
	 */
	public static void resizeFix(File srcFile, File destFile, int boxWidth,
			int boxHeight, int cutTop, int cutLeft, int cutWidth, int catHeight)
			throws IOException, MagickException {
		ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
		MagickImage image = new MagickImage(info);
		// 剪切
		Rectangle rect = new Rectangle(cutTop, cutLeft, cutWidth, catHeight);
		// 计算压缩宽高
		MagickImage cropped = image.cropImage(rect);
		Dimension dim = cropped.getDimension();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		int zoomWidth;
		int zoomHeight;
		if ((float) width / height > (float) boxWidth / boxHeight) {
			zoomWidth = boxWidth;
			zoomHeight = Math.round((float) boxWidth * height / width);
		} else {
			zoomWidth = Math.round((float) boxHeight * width / height);
			zoomHeight = boxHeight;
		}
		// 缩小
		MagickImage scaled = cropped.scaleImage(zoomWidth, zoomHeight);
		// 输出
		scaled.setFileName(destFile.getAbsolutePath());
		scaled.writeImage(info);
		scaled.destroyImages();
	}

	/**
	 * 给图片加水印
	 * @Title: imageMark  
	 * @param srcFile 源文件
	 * @param destFile 目标文件
	 * @param minWidth 最小宽度
	 * @param minHeight 最小高度
	 * @param pos 位置
	 * @throws IOException  Exception MagickException
	 * @return: void
	 */
	public static void imageMark(File srcFile, File destFile, int minWidth,
			int minHeight, int pos, String markContent, Color markColor, int markSize, int alpha)
			throws IOException, MagickException {
		ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
		MagickImage image = new MagickImage(info);

		Dimension dim = image.getDimension();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		
		/**暂且用Graphics2D 获取文字高度、宽度*/
		BufferedImage imgBuff = ImageIO.read(srcFile);
		Graphics2D g = imgBuff.createGraphics();
		java.awt.FontMetrics fm = g.getFontMetrics();
		int textHeight = fm.getHeight(); // 计算字体高度
		int textWidth = fm.stringWidth(markContent);// 计算字体宽度
		
		if (width < minWidth || height < minHeight) {
			image.destroyImages();
			if (!srcFile.equals(destFile)) {
				FileUtils.copyFile(srcFile, destFile);
			}
		} else {
			imageMark(image, info, width, height, pos,
					markContent,textWidth,textHeight,markColor, markSize, alpha);
			image.setFileName(destFile.getAbsolutePath());
			image.writeImage(info);
			image.destroyImages();
		}
	}

	/**
	 * 给图片加水印
	 * @Title: imageMark  
	 * @param srcFile 源文件
	 * @param destFile 目标文件
	 * @param minWidth 最小宽度
	 * @param minHeight 最小高度
	 * @param pos 位置
	 * @param markFile 水印图片文件
	 * @throws IOException  Exception MagickException
	 * @return: void
	 */
	public static void imageMark(File srcFile, File destFile, int minWidth,
			int minHeight, int pos, File markFile)
			throws IOException, MagickException {
		ImageInfo info = new ImageInfo(srcFile.getAbsolutePath());
		MagickImage image = new MagickImage(info);

		Dimension dim = image.getDimension();
		int width = (int) dim.getWidth();
		int height = (int) dim.getHeight();
		if (width < minWidth || height < minHeight) {
			image.destroyImages();
			if (!srcFile.equals(destFile)) {
				FileUtils.copyFile(srcFile, destFile);
			}
		} else {
			imageMark(image, info, width, height, pos,markFile);
			image.setFileName(destFile.getAbsolutePath());
			image.writeImage(info);
			image.destroyImages();
		}
	}

	private static void imageMark(MagickImage image, ImageInfo info, int width,
			int height, int pos,  String text,int txtWidth,int txtHeight,
			Color color, int size, int alpha) throws MagickException {
		DrawInfo draw = new DrawInfo(info);
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		draw.setFill(new PixelPacket(r * r, g * g, b * b,
				65535 - (alpha * 65535 / 100)));
		draw.setPointsize(size);
		draw.setTextAntialias(true);
		draw.setText(text);
		Position p = ImageUtils.markPosition(width, height, pos, txtWidth,
				txtHeight);
		draw.setGeometry("+" + p.getX() + "+" + p.getY());
		image.annotateImage(draw);
	}

	private static void imageMark(MagickImage image, ImageInfo info, int width,
			int height, int pos, File markFile)
			throws MagickException, IOException {
		BufferedImage markImgBuff = ImageIO.read(markFile);
		Position p = ImageUtils.markPosition(width, height, pos, markImgBuff.getWidth(),
				markImgBuff.getHeight());
		MagickImage mark = new MagickImage(new ImageInfo(markFile
				.getAbsolutePath()));
		image.compositeImage(CompositeOperator.AtopCompositeOp, mark, p.getX(),
				p.getY());
		mark.destroyImages();
	}
}
