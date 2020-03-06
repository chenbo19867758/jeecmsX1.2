package com.jeecms.common.image;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import com.jeecms.common.exception.GlobalException;

import magick.MagickException;

/**
 * 图片缩小接口
 * @author: tom
 * @date:   2018年12月25日 上午10:01:38     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public interface ImageScaleService {
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
	 * @throws Exception Exception
	 */
	public void resizeFix(File srcFile, File destFile, String srcFileName,int boxWidth,
			int boxHeight) throws Exception;

	/**
	 * 缩小并裁剪图片
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
	 * @throws Exception Exception
	 */
	public void resizeFix(File srcFile, File destFile,  String srcFileName,int boxWidth,
			int boxHeight, int cutTop, int cutLeft, int cutWidth, int catHeight)
			throws Exception;

	/**
	 * 给图片加水印
	 * @Title: imageMark  
	 * @param srcFile 源文件
	 * @param destFile 目标文件
	 * @param minWidth 最小宽度
	 * @param minHeight 最小高度
	 * @param pos 位置
	 * @param text 水印文本内容
	 * @param color 水印颜色
	 * @param size 水印文本大小
	 * @param alpha 水印透明度
	 * @throws GlobalException  GlobalException
	 * @throws IOException  IOException
	 * @throws Exception  Exception
	 * @return: void
	 */
	public void imageMark(File srcFile, File destFile,String srcFileName, int minWidth,
			int minHeight, int pos,  String text,
			Color color, int size, int alpha) throws Exception;

	/**
	 * 给图片加水印
	 * @Title: imageMark  
	 * @param srcFile 源文件
	 * @param destFile 目标文件
	 * @param minWidth 最小宽度
	 * @param minHeight 最小高度
	 * @param pos 位置
	 * @param markFile 水印图片文件
	 * @throws GlobalException  GlobalException
	 * @throws IOException  IOException
	 * @throws Exception  Exception
	 * @return: void
	 */
	public void imageMark(File srcFile, File destFile, String srcFileName,int minWidth,
			int minHeight, int pos, File markFile)
					throws Exception;
}
