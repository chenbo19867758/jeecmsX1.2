package com.jeecms.common.image;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import magick.Magick;
import magick.MagickException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.UploadExceptionInfo;
import com.jeecms.common.exception.error.SysOtherErrorCodeEnum;

/**
 * 图片缩小类 根据环境情况选择java图片缩小方式或专业的magick图片缩小方式
 * 
 * @author: tom
 * @date: 2018年12月25日 上午10:01:51
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class ImageScaleServiceImpl implements ImageScaleService {
	private static final Logger log = LoggerFactory.getLogger(ImageScaleServiceImpl.class);

	@Override
	public void resizeFix(File srcFile, File destFile,  String srcFileName,int boxWidth, int boxHeight) throws Exception {
		if (isMagick) {
			MagickImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight);
		} else {
			AverageImageScale.resizeFix(srcFile, destFile,srcFileName, boxWidth, boxHeight);
		}
	}

	@Override
	public void resizeFix(File srcFile, File destFile,  String srcFileName,int boxWidth, int boxHeight, int cutTop, int cutLeft,
			int cutWidth, int catHeight) throws Exception {
		if (isMagick) {
			MagickImageScale.resizeFix(srcFile, destFile, boxWidth, boxHeight, cutTop, cutLeft, cutWidth, catHeight);
		} else {
			AverageImageScale.resizeFix(srcFile, destFile,srcFileName, boxWidth, boxHeight, cutTop, cutLeft, cutWidth, catHeight);
		}
	}

	@Override
	public void imageMark(File srcFile, File destFile, String srcFileName,int minWidth, int minHeight, int pos, String text, Color color,
			int size, int alpha) throws Exception {
		if (isMagick) {
			MagickImageScale.imageMark(srcFile, destFile, minWidth, minHeight, pos, text, color, size, alpha);
		} else {
			AverageImageScale.imageMark(srcFile, destFile,srcFileName, minWidth, minHeight, pos, text, color, size, alpha);
		}
	}

	@Override
	public void imageMark(File srcFile, File destFile, String srcFileName,int minWidth, int minHeight, int pos, File markFile) throws Exception{
		/** 水印文件不存在抛出异常 */
		if (markFile == null || !markFile.exists()) {
			throw new GlobalException(
					new UploadExceptionInfo(SysOtherErrorCodeEnum.UPLOAD_MARK_FILE_ERROR.getDefaultMessage(),
							SysOtherErrorCodeEnum.UPLOAD_MARK_FILE_ERROR.getCode()));
		}
		if (isMagick) {
			MagickImageScale.imageMark(srcFile, destFile, minWidth, minHeight, pos, markFile);
		} else {
			AverageImageScale.imageMark(srcFile, destFile, srcFileName,minWidth, minHeight, pos, markFile);
		}
	}

	/**
	 * 检查是否安装magick
	 */
	public void init() {
		if (tryMagick) {
			try {
				System.setProperty("jmagick.systemclassloader", "no");
				new Magick();
				log.info("using jmagick");
				isMagick = true;
			} catch (Throwable e) {
				log.warn("load jmagick fail, use java image scale. message:{}", e.getMessage());
				isMagick = false;
			}
		} else {
			log.info("jmagick is disabled.");
			isMagick = false;
		}
	}

	private boolean isMagick = false;
	private boolean tryMagick = true;

	public void setTryMagick(boolean tryMagick) {
		this.tryMagick = tryMagick;
	}
}
