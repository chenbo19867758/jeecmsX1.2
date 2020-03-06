package com.jeecms.admin.controller.wechat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.jeecms.common.base.controller.BaseController;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.exception.UploadExceptionInfo;
import com.jeecms.common.exception.error.RPCErrorCodeEnum;
import com.jeecms.common.jsonfilter.annotation.MoreSerializeField;
import com.jeecms.common.jsonfilter.annotation.SerializeField;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.common.util.FileUtils;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.web.util.ResponseUtils;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.bean.MediaFile;
import com.jeecms.common.wechat.bean.request.mp.material.AddMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.AddVideoMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.BatchgetMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.UploadImgRequest;
import com.jeecms.common.wechat.bean.response.mp.material.dto.UpdateNewsDto;
import com.jeecms.common.wechat.util.client.HttpUtil;
import com.jeecms.common.wechat.util.client.httpclient.HttpClientDelegate;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.service.ResourcesSpaceDataService;
import com.jeecms.util.SystemContextUtils;
import com.jeecms.wechat.domain.WechatMaterial;
import com.jeecms.wechat.domain.WechatSend;
import com.jeecms.wechat.domain.dto.AddNewsRequestDto;
import com.jeecms.wechat.domain.dto.WechatDeleteDto;
import com.jeecms.wechat.service.AbstractWeChatInfoService;
import com.jeecms.wechat.service.WechatMaterialService;
import com.jeecms.wechat.service.WechatSendService;

/**
 * 微信永久素材管理Controller
 * @author: chenming
 * @date:   2019年6月5日 下午4:46:06
 */
@RequestMapping(value = "/wechatMaterial")
@RestController
public class WechatMaterialController extends BaseController<WechatMaterial, Integer> {
	
	private static final String SERVLET_IMG_PREFIX = "http";
	
	private static final int RESOURCE_SIZE = 1048576;

	@PostConstruct
	public void init() {
		String[] queryParams = {};
		super.setQueryParams(queryParams);
	}
	
	@Autowired
	private WechatSendService wechatSendService;
	@Autowired
	private WechatMaterialService service;
	@Autowired
	private ResourcesSpaceDataService spaceDataService;
	@Autowired
	private AbstractWeChatInfoService abstractWeChatInfoService;
	
	/**
	 * 新增永久图文素材
	 */
	@RequestMapping(value = "/news", method = RequestMethod.POST)
	public ResponseInfo saveNews(@RequestBody @Valid AddNewsRequestDto dto, HttpServletRequest request,
			BindingResult result) throws GlobalException {
		JSONObject object = new JSONObject();
		Integer number = 1;
		super.validateBindingResult(result);
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, dto.getAppId());
		WechatMaterial wMaterial = service.saveNews(dto.getAddNewsRequest(), dto.getAppId());
		if (wMaterial != null) {
			//判断今日群发次数
			List<WechatSend> send = wechatSendService.listWechatSend(Arrays.asList(dto.getAppId()),
					MyDateUtils.getStartDate(new Date()), MyDateUtils.getFinallyDate(new Date()));
			//判断月群发次数
			Boolean flag = wechatSendService.service(dto.getAppId(), new Date());
			if (!send.isEmpty() || !flag) {
				number = 0;
			}
			object.put("id", wMaterial.getId());
			object.put("number", number);
			return new ResponseInfo(object);
		}
		return new ResponseInfo(false);
	}

	/**
	 * 新增其它永久素材(图文素材、视频素材除外)
	 */
	@RequestMapping(value = "/material", method = RequestMethod.POST)
	@MoreSerializeField({
		@SerializeField(clazz = WechatMaterial.class, includes = {"request"}),
	})
	public ResponseInfo saveMaterial(@RequestParam(value = "uploadFile") MultipartFile mfile,
			@RequestParam(value = "type") String type, @RequestParam(value = "appId") String appId,
			HttpServletRequest request) throws GlobalException, IOException {
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, appId);
		validMultipartFile(mfile);
		// 获取文件名
		String originalFileName = mfile.getOriginalFilename();
		// 获取文件后缀
		String prefix = originalFileName.substring(originalFileName.lastIndexOf("."));
		String suffix = prefix.substring(1);
		List<String> materialTypes = Arrays.asList(WechatMaterial.IMAGE_NAME,WechatMaterial.VOICE_NAME);
		if (!materialTypes.contains(type)) {
			return new ResponseInfo();
		}
		if (WechatMaterial.IMAGE_NAME.equals(type)) {
			if (!WechatMaterial.IMAGE_TYPE.contains(suffix)) {
				return new ResponseInfo(RPCErrorCodeEnum.UPLOAD_IMAGE_FORMAT_ERROR.getCode(),
						RPCErrorCodeEnum.UPLOAD_IMAGE_FORMAT_ERROR.getDefaultMessage());
			}
		}
		if (WechatMaterial.VOICE_NAME.equals(type)) {
			if (!WechatMaterial.VOICE_TYPE.contains(suffix)) {
				return new ResponseInfo(RPCErrorCodeEnum.UPLOAD_VOICE_FORMAT_ERROR.getCode(),
						RPCErrorCodeEnum.UPLOAD_VOICE_FORMAT_ERROR.getDefaultMessage());
			}
		}
		// 获取文件名
		String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
		// 1 由于系统未下载微信语音及视频文件至本地，导致无法播放，
		// 2 便于系统方便辨识文件的内容且防止生成临时文件出现重名，因此采用原始文件名字加随机6位数组合新的文件名，
		prefix = java.text.Normalizer.normalize(prefix, java.text.Normalizer.Form.NFKD);
		fileName = java.text.Normalizer.normalize(fileName, java.text.Normalizer.Form.NFKD);
		final File file = File.createTempFile(fileName + StrUtils.getRandStr(6), prefix);
		mfile.transferTo(file);
		AddMaterialRequest addMaterialRequest = new AddMaterialRequest();
		addMaterialRequest.setFileName(originalFileName);
		addMaterialRequest.setType(type);
		WechatMaterial material = service.saveMaterial(addMaterialRequest, appId, file);
		// 执行业务之后删除临时文件
		file.delete();
		return new ResponseInfo(material);
	}

	/**
	 * 新增视频素材
	 */
	@RequestMapping(value = "/video", method = RequestMethod.POST)
	public ResponseInfo saveVideo(@RequestParam(value = "uploadFile") MultipartFile mfile,
			@RequestParam(value = "title") String title, 
			@RequestParam(value = "introduction") String introduction,
			@RequestParam(value = "appId") String appId,
			HttpServletRequest request) throws GlobalException, IOException {
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, appId);
		validMultipartFile(mfile);
		// 获取文件名
		String originalFileName = mfile.getOriginalFilename();
		// 获取文件后缀
		String prefix = originalFileName.substring(originalFileName.lastIndexOf("."));
		String suffix = prefix.substring(1);
		if (!WechatMaterial.VIDEO_TYPE.contains(suffix)) {
			return new ResponseInfo(RPCErrorCodeEnum.UPLOAD_VIDEO_FORMAT_ERROR.getCode(),
					RPCErrorCodeEnum.UPLOAD_VIDEO_FORMAT_ERROR.getDefaultMessage());
		}
		// 获取文件名
		String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
		// 1 由于系统未下载微信语音及视频文件至本地，导致无法播放，
		// 2 便于系统方便辨识文件的内容且防止生成临时文件出现重名，因此采用原始文件名字加随机6位数组合新的文件名，
		prefix = java.text.Normalizer.normalize(prefix, java.text.Normalizer.Form.NFKD);
		fileName = java.text.Normalizer.normalize(fileName, java.text.Normalizer.Form.NFKD);
		final File file = File.createTempFile(fileName + StrUtils.getRandStr(6), prefix);
		mfile.transferTo(file);
		AddVideoMaterialRequest addVideoMaterialRequest = new AddVideoMaterialRequest(title, introduction);
		WechatMaterial wMaterial = service.saveVideo(addVideoMaterialRequest, appId, file);
		// 执行业务之后删除临时文件
		file.delete();
		if (wMaterial != null) {
			return new ResponseInfo(true);
		}
		return new ResponseInfo(false);
	}

	/**
	 * 上传图文素材中的图片，并返回url给前台
	 */
	@RequestMapping(value = "/upload/img", method = RequestMethod.POST)
	public ResponseInfo uploadImg(@RequestParam(value = "uploadFile", required = false) MultipartFile mfile,
			@RequestParam(value = "appId") String appId,
			HttpServletRequest request) throws GlobalException, IOException {
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, appId);
		validMultipartFile(mfile);
		// 获取文件名
		String originalFileName = mfile.getOriginalFilename();
		// 获取文件后缀
		String prefix = originalFileName.substring(originalFileName.lastIndexOf("."));
		String suffix = prefix.substring(1);
		Long size = mfile.getSize();
		if (!(WechatMaterial.UPLOAD_IMAGE_TYPE.contains(suffix)) || size > WechatMaterialController.RESOURCE_SIZE) {
			return new ResponseInfo(RPCErrorCodeEnum.UPLOAD_NEWS_IMAGE_FORMAT_ERROR.getCode(),
					RPCErrorCodeEnum.UPLOAD_NEWS_IMAGE_FORMAT_ERROR.getDefaultMessage());
		}	
		// 获取文件名
		String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
		// 1 由于系统未下载微信语音及视频文件至本地，导致无法播放，
		// 2 便于系统方便辨识文件的内容且防止生成临时文件出现重名，因此采用原始文件名字加随机6位数组合新的文件名，
		fileName = java.text.Normalizer.normalize(fileName, java.text.Normalizer.Form.NFKD);
		prefix = java.text.Normalizer.normalize(prefix, java.text.Normalizer.Form.NFKD);
		final File file = File.createTempFile(fileName + StrUtils.getRandStr(6), prefix);
		mfile.transferTo(file);
		UploadImgRequest uploadImgRequest = new UploadImgRequest();
		uploadImgRequest.setFile(file);
		String url = service.uploadImg(uploadImgRequest, appId);
		// 执行业务之后删除临时文件
		file.delete();
		return new ResponseInfo(url);
	}

	/**
	 * 修改图文素材
	 */
	@RequestMapping(value = "/news", method = RequestMethod.PUT)
	public ResponseInfo updateNews(@RequestBody @Valid UpdateNewsDto dto,
			HttpServletRequest request, BindingResult result) throws GlobalException {
		super.validateBindingResult(result);
		WechatMaterial wMaterial = service.findById(dto.getId());
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, wMaterial.getAppId());
		if (wMaterial.getRequestArray() != null) {
			if (wMaterial.getRequestArray().size() != dto.getNews().size()) {
				// 此处属于特殊情况，及微信端返回的状态码，所以此处不写入到RPCError中，直接写字符串，与微信端一致，此处情况特殊
				return new ResponseInfo("40114", "发送的数据格式出错，请检查数据索引是否正确？");
			}
		}
		WechatMaterial newMaterial = service.updateNews(dto,wMaterial);
		if (newMaterial != null) {
			return new ResponseInfo(true);
		}
		return new ResponseInfo(false);
	}

	/**
	 * 删除永久素材
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseInfo delete(@RequestBody @Valid WechatDeleteDto dto, 
			HttpServletRequest request) throws GlobalException {
		service.deleteMaterial(dto);
		return new ResponseInfo(true);
	}

	/**
	 * 同步图文素材
	 */
	@RequestMapping(value = "/synch/news",method = RequestMethod.GET)
	public ResponseInfo synchNews(HttpServletRequest request, @RequestParam String appId) throws GlobalException {
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, appId);
		BatchgetMaterialRequest batchgetMaterialRequest = new BatchgetMaterialRequest(
				Const.Mssage.REQ_MESSAGE_TYPE_NEWS, 0, 20);
		service.synchNews(batchgetMaterialRequest, appId);
		return new ResponseInfo(true);
	}

	/**
	 * 同步其它素材(图文素材除外)
	 */
	@RequestMapping(value = "/synch/material",method = RequestMethod.GET)
	public ResponseInfo synchMaterial(@RequestParam("type") String type, @RequestParam String appId, 
			HttpServletRequest request) throws GlobalException {
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, appId);
		if (!WechatMaterial.TYPES.contains(type)) {
			return new ResponseInfo(RPCErrorCodeEnum.REQUEST_TYPE_ERROR.getCode(),
					RPCErrorCodeEnum.REQUEST_TYPE_ERROR.getDefaultMessage());
		}
		BatchgetMaterialRequest batchgetMaterialRequest = new BatchgetMaterialRequest(type, 0, 20);
		service.synchMaterial(batchgetMaterialRequest, appId);
		return new ResponseInfo(true);
	}

	/**
	 * 分页查询所有素材
	 */
	@RequestMapping(value = "/page",method = RequestMethod.GET)
	@MoreSerializeField({
		@SerializeField(clazz = WechatMaterial.class, includes = {"id","mediaType","mediaId","request",
				"requestArray","wechatUpdateTime", "wechatUpdateTimes"}),
	})
	public ResponseInfo pageMaterial(HttpServletRequest request,
			@RequestParam String appId,
			@RequestParam String type,
			@RequestParam(name = "title",required = false) String title,
			@PageableDefault(sort = "wechatUpdateTime", direction = Direction.DESC) Pageable pageable)
			throws GlobalException {
		Map<String,String[]> params = super.getCommonParams(request);
		params.put("EQ_appId_String", new String[] {appId});
		params.put("EQ_mediaType_String", new String[] {type});
		if (Const.Mssage.REQ_MESSAGE_TYPE_NEWS.equals(type)) {
			params.put("LIKE_mediaTitles_String", new String[] {title});
		} else {
			params.put("LIKE_materialName_String", new String[] {title});
		}
		Page<WechatMaterial> materials = service.getPage(params, pageable, false);
		return new ResponseInfo(materials);
	}

	/**
	 * 通过查询mediaId得到素材对象
	 */
	@MoreSerializeField({
		@SerializeField(clazz = WechatMaterial.class, includes = {"id","mediaType","mediaId","request",
				"requestArray","wechatUpdateTime"}),
	})
	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
	public ResponseInfo getMediaId(@PathVariable(name = "id") Integer id) 
			throws GlobalException {
		return new ResponseInfo(service.findById(id));
	}

	/**
	 * 同步服务器的图片
	 */
	@RequestMapping(value = "/synch/img",method = RequestMethod.GET)
	@MoreSerializeField({
		@SerializeField(clazz = WechatMaterial.class, includes = {"request"}),
	})
	public ResponseInfo synchServerImg(@RequestParam("id") Integer id, @RequestParam("appId")String appId,
			HttpServletRequest request) throws Exception {
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, appId);
		File file;
		ResourcesSpaceData spaceData = spaceDataService.findById(id);
		if (spaceData.getUrl().startsWith(WechatMaterialController.SERVLET_IMG_PREFIX)) {
			HttpClientDelegate hDelegate = new HttpClientDelegate();
			file = File.createTempFile(spaceData.getAlias(),
					spaceData.getUrl().substring(
						spaceData.getUrl().lastIndexOf("/") + 1,spaceData.getUrl().length()));
			byte[] imgBytes = hDelegate.readURLImage(spaceData.getUrl());
			try (OutputStream output = new FileOutputStream(file);
					BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);) {
				bufferedOutput.write(imgBytes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			String path = request.getSession().getServletContext().getRealPath("/");
			file = new File(path + spaceData.getUrl());
		}
		AddMaterialRequest addMaterialRequest = new AddMaterialRequest();
		addMaterialRequest.setFileName(spaceData.getUrl());
		addMaterialRequest.setType(Const.Mssage.REQ_MESSAGE_TYPE_IMAGE);
		WechatMaterial wMaterial = service.saveMaterial(addMaterialRequest, appId, file);
		// 执行业务之后删除临时文件
		file.delete();
		return new ResponseInfo(wMaterial);
	}

	/**
	 * 下载永久视频素材
	 */
	@RequestMapping(value = "/dowload/video",method = RequestMethod.GET)
	public ResponseInfo dowloadVideo(@RequestParam(name = "id") Integer id,HttpServletRequest request)
			throws GlobalException {
		WechatMaterial wMaterial = service.findById(id);
		if (wMaterial == null) {
			return new ResponseInfo();
		}
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, wMaterial.getAppId());
		if (!WechatMaterial.VIDEO_NAME.equals(service.findById(id).getMediaType())) {
			return new ResponseInfo(RPCErrorCodeEnum.REQUEST_TYPE_ERROR.getCode(),
						RPCErrorCodeEnum.REQUEST_TYPE_ERROR.getDefaultMessage());
		}
		return new ResponseInfo(service.dowloadVideo(id));
	}

	/**
	 * 下载其它永久素材(图文、视频除外)
	 */
	@RequestMapping(value = "/dowload/material",method = RequestMethod.GET)
	public void dowloadMaterail(@RequestParam("id") Integer id, HttpServletRequest request,
			HttpServletResponse response) throws GlobalException, IOException, Exception {
		WechatMaterial wMaterial = service.findById(id);
		if (wMaterial == null) {
			return;
		}
		abstractWeChatInfoService.checkWeChatAuth(SystemContextUtils.getUserId(request), null, wMaterial.getAppId());
		if (WechatMaterial.VIDEO_NAME.equals(wMaterial.getMediaType())) {
			String videoUrl = service.dowloadVideo(id);
			String checkUrl = videoUrl.substring(0, 23);
			if (WechatMaterial.CHECK_VIDEO_URL.equals(checkUrl)) {
				ResponseUtils.downloadVideo(
						request, response, null, null, null,videoUrl);
			} else {
				String fileName = JSONObject.parseObject(wMaterial.getMaterialJson()).getString("name");
				byte[] fileBytes = HttpUtil.readURLVideo(videoUrl);
				ResponseUtils.downloadVideo(
						request, response, fileName, "mp4", fileBytes,videoUrl);
			}
		} else {
			MediaFile mediaFile = service.dowloadMatererial(id, wMaterial.getAppId(), request, null);
			byte[] fileBytes = mediaFile.getFileStream().toByteArray();
			ResponseUtils.downloadMaterial(
					request, response, mediaFile.getFileName(), mediaFile.getSuffix(), fileBytes);
		}
		
	}

	private void validMultipartFile(MultipartFile mfile) throws GlobalException, IOException {
		/** 文件头格式检查 */
		InputStream ins = mfile.getInputStream();
		try {
			if (mfile != null && !FileUtils.checkFileIsValid(ins)) {
				throw new GlobalException(
						new UploadExceptionInfo(
								RPCErrorCodeEnum.UPLOAD_FILE_ERROR.getDefaultMessage(),
								RPCErrorCodeEnum.UPLOAD_FILE_ERROR.getCode()));
			}
		} catch (Exception e) {
			throw new GlobalException(new UploadExceptionInfo(
					RPCErrorCodeEnum.UPLOAD_FILE_ERROR.getDefaultMessage(),
					RPCErrorCodeEnum.UPLOAD_FILE_ERROR.getCode()));
		} finally {
			try {
				if (ins != null) {
					safeClose(ins);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭流
	 */
	public static void safeClose(InputStream fis) {
		if (fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
