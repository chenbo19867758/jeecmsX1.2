package com.jeecms.common.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

/**
 * 二维码工具类
 * 
 * @author: tom
 * @date: 2018年12月26日 下午3:19:47
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

public class QrCodeUtil {
	private static final Logger logger = LoggerFactory.getLogger(QrCodeUtil.class);

	/**
	 * 生成包含字符串信息的二维码图片
	 * 
	 * @param content
	 *            二维码携带信息
	 * @param qrCodeSize
	 *            二维码图片大小
	 * @throws WriterException
	 *             WriterException
	 * @throws IOException
	 *             IOException
	 */
	public static BufferedImage createQrCode(String content, int qrCodeSize) throws WriterException, IOException {
		// 设置二维码纠错级别ＭＡＰ
		Hashtable<EncodeHintType, Object> hintMap = new
				Hashtable<EncodeHintType, Object>();
		// 矫错级别
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hintMap.put(EncodeHintType.MARGIN, 0);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		// 创建比特矩阵(位矩阵)的QR码编码的字符串
		BitMatrix byteMatrix = qrCodeWriter.encode(content, 
				BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
		// 使BufferedImage勾画QRCode (matrixWidth 是行二维码像素点)
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth,
				matrixWidth, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// 使用比特矩阵画并保存图像
		graphics.setColor(Color.BLACK);
		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		return image;
	}

	/**
	 * 读二维码并输出携带的信息
	 */
	public static void readQrCode(InputStream inputStream) throws IOException {
		// 从输入流中获取字符串信息
		BufferedImage image = ImageIO.read(inputStream);
		// 将图像转换为二进制位图源
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			reader.decode(bitmap);
		} catch (ReaderException e) {
			logger.error(e.getMessage());
		}
	}
}
