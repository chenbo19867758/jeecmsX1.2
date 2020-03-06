package com.jeecms.resource.ueditor.hunter;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import com.jeecms.resource.service.ImageService;
import com.jeecms.resource.ueditor.PathFormat;
import com.jeecms.resource.ueditor.define.BaseState;
import com.jeecms.resource.ueditor.define.MultiState;
import com.jeecms.resource.ueditor.define.State;

/**
 * 图片抓取器
 * @author: tom
 * @date:   2019年3月5日 下午4:48:37
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SuppressWarnings("unused")
public class ImageHunter {

	private String filename = null;
	private String savePath = null;
	private String rootPath = null;
	private List<String> allowTypes = null;
	private long maxSize = -1;

	private List<String> filters = null;

	private ImageService imgSvc;

	public ImageHunter(ImageService imgSvc) {
		super();
		this.imgSvc = imgSvc;
	}

	public State capture(String[] list) {

		MultiState state = new MultiState(true);
		if (list != null && list.length > 0) {
			for (String source : list) {
				state.addState(captureRemoteData(source));
			}
		}

		return state;

	}

	public State captureByApi(String urlPrefix, String[] list) {

		MultiState state = new MultiState(true);
		if (list != null && list.length > 0) {
			for (String source : list) {
				state.addState(captureRemoteDataByApi(urlPrefix, source));
			}
		}

		return state;

	}

	public State captureRemoteData(String urlStr) {
		String imgUrl = imgSvc.crawlImg(urlStr);
		State state = new BaseState();
		state.putInfo("url", imgUrl);
		state.putInfo("source", urlStr);
		return state;
	}

	public State captureRemoteDataByApi(String urlPrefix, String urlStr) {
		String imgUrl = imgSvc.crawlImg(urlStr);
		State state = new BaseState();
		state.putInfo("url", urlPrefix + imgUrl);
		state.putInfo("source", urlStr);
		return state;
	}

	private String getPath(String savePath, String filename, String suffix) {

		return PathFormat.parse(savePath + suffix, filename);

	}

	private boolean validContentState(int code) {

		return HttpURLConnection.HTTP_OK == code;

	}

	private boolean validFileType(String type) {

		return this.allowTypes.contains(type);

	}

	private boolean validFileSize(int size) {
		return size < this.maxSize;
	}

}
