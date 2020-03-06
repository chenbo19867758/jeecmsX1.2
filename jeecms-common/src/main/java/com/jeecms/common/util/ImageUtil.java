package com.jeecms.common.util;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

/**
 *
 * @author Zhu Kaixiao
 * @version 1.0
 * @date 2019/7/12 9:12
 * @copyright 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class ImageUtil {

    /**
     * 判断图片的真实类型
     *
     * @param imgFile 图片文件
     * @return java.lang.String 图片格式
     * @author Zhu Kaixiao
     * @date 2019/7/12 8:50
     **/
    public static String realImgFormat(File imgFile) throws IOException {
        String format;

        if (!imgFile.exists()) {
            throw new FileNotFoundException(String.format("文件不存在: [%s]", imgFile.getCanonicalPath()));
        }
        ImageInputStream iis = ImageIO.createImageInputStream(imgFile);
        try  {
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                throw new IllegalArgumentException("无法识别的文件!");
            }
            ImageReader reader = iter.next();
            format = reader.getFormatName();
        } finally {
            iis.close();
        }
        format =  format.toLowerCase();
        return format;
    }

    /**
     * {@link ImageUtil#realImgFormat(File)}
     *
     * @param imgFilePath 图片路径
     * @return java.lang.String 图片格式
     * @author Zhu Kaixiao
     * @date 2019/7/12 9:13
     **/
    public static String realImgFormat(String imgFilePath) throws IOException {
        return realImgFormat(new File(imgFilePath));
    }

}
