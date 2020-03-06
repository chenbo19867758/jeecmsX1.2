package com.jeecms.resource.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeecms.common.file.FileNameUtils;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.upload.UploadUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.common.web.util.RequestUtils;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.domain.UploadOss;
import com.jeecms.resource.service.ImageService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.domain.GlobalConfigAttr;
import com.jeecms.util.SystemContextUtils;

/***
 * 
 * @Description:图片service实现
 * @author: tom
 * @date: 2018年12月17日 下午3:32:06
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class ImageServiceImpl implements ImageService {
	@Override
	public String crawlImg(String imgUrl) {
//		
//		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//		CloseableHttpClient client = httpClientBuilder.build();
//		HttpServletRequest request = RequestUtils.getHttpServletRequest();
//		CmsSite site = SystemContextUtils.getSite(request);
//		String outFileName = "";
//		String fileUrl = "";
//		InputStream is = null;
//		OutputStream os = null;
//		try {
//			if (validUrl(imgUrl)) {
//				GlobalConfig config = SystemContextUtils.getGlobalConfig(request);
//				GlobalConfigAttr configAttr = config.getConfigAttr();
//				URI uri = new URI(imgUrl);
//				HttpGet httpget = new HttpGet(uri);
//				HttpResponse response = client.execute(httpget);
//				HttpEntity entity = null;
//				int resCode = response.getStatusLine().getStatusCode();
//				if (resCode == HttpServletResponse.SC_OK) {
//					entity = response.getEntity();
//					is = entity.getContent();
//					String ctx = request.getContextPath();
//					String ext = FileNameUtils.getFileSufix(imgUrl);
//					if (GlobalConfigAttr.UPLOAD_WAY_FTP == configAttr.getUploadWay() && site.getUploadFtp() != null) {
//						UploadFtp ftp = site.getUploadFtp();
//						String ftpUrl = ftp.getUrl();
//						fileUrl = ftp.storeByExt(config.getUploadPath(), ext, is);
//						// 加上url前缀
//						fileUrl = ftpUrl + fileUrl;
//					} else if (site.getUploadOss() != null) {
//						UploadOss oss = site.getUploadOss();
//						fileUrl = oss.storeByExt(config.getUploadPath(), ext, is);
//					} else {
//						String fileName = UploadUtils.generateRamdonFilename(FileNameUtils.getFileSufix(imgUrl));
//						fileName = java.text.Normalizer.normalize(fileName, java.text.Normalizer.Form.NFKD);
//						File outFile = new File(realPathResolver.get(config.getUploadPath()), fileName);
//						UploadUtils.checkDirAndCreate(outFile.getParentFile());
//						outFileName = config.getUploadPath() + fileName;
//						os = new FileOutputStream(outFile);
//						IOUtils.copy(is, os);
//						// 加上部署路径
//						if (ctx != null && StringUtils.isNotBlank(ctx)) {
//							fileUrl = ctx + outFileName;
//						} else {
//							fileUrl = outFileName;
//						}
//
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (is != null) {
//					is.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			try {
//				if (os != null) {
//					os.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			try {
//				if (client != null) {
//					client.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		return null;
	}

	@Override
	public String crawlImg(String imgUrl, String ctx, UploadFtp ftp, UploadOss oss, String uploadPath) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient client = httpClientBuilder.build();
		String outFileName = "";
		String fileUrl = "";
		InputStream is = null;
		OutputStream os = null;
		try {
			if (validUrl(imgUrl)) {
				HttpGet httpget = new HttpGet(new URI(imgUrl));
				HttpResponse response = client.execute(httpget);
				HttpEntity entity = null;
				entity = response.getEntity();
				is = entity.getContent();
				String ext = FileNameUtils.getFileSufix(imgUrl);
				if (ftp != null) {
					String ftpUrl = ftp.getUrl();
					fileUrl = ftp.storeByExt(uploadPath, ext, is);
					// 加上url前缀
					fileUrl = ftpUrl + fileUrl;
				} else if (oss != null) {
					fileUrl = oss.storeByExt(uploadPath, ext, is);
				} else {
					outFileName = UploadUtils.generateFilename(uploadPath, FileNameUtils.getFileSufix(imgUrl));
					File outFile = new File(realPathResolver.get(outFileName));
					UploadUtils.checkDirAndCreate(outFile.getParentFile());
					os = new FileOutputStream(outFile);
					IOUtils.copy(is, os);
					// 加上部署路径
					if (ctx != null && StringUtils.isNotBlank(ctx)) {
						fileUrl = ctx + outFileName;
					} else {
						fileUrl = outFileName;
					}
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (client != null) {
					client.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileUrl;
	}

	private boolean validUrl(String imgUrl) {
		URL url;
		boolean isImage = true;
		try {
			url = new URL(imgUrl);
			URLConnection urlConnection = url.openConnection();
			InputStream inputStream = urlConnection.getInputStream();
			isImage = ImageUtils.isImage(inputStream);
			inputStream.close();
		} catch (Exception e) {
			isImage = false;
		}
		return isImage;
	}

	@Autowired
	protected FileRepository fileRepository;
	@Autowired
	private RealPathResolver realPathResolver;
}
