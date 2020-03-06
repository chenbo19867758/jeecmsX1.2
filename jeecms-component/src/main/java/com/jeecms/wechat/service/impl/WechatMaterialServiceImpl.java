package com.jeecms.wechat.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.Const;
import com.jeecms.common.wechat.api.mp.AddMaterialApiService;
import com.jeecms.common.wechat.api.mp.BatchgetMaterialApiService;
import com.jeecms.common.wechat.api.mp.DelMaterialApiService;
import com.jeecms.common.wechat.api.mp.GetMaterialApiService;
import com.jeecms.common.wechat.api.mp.UpdateNewsApiService;
import com.jeecms.common.wechat.bean.MediaFile;
import com.jeecms.common.wechat.bean.ValidateToken;
import com.jeecms.common.wechat.bean.request.mp.material.AddMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.AddNewsRequest;
import com.jeecms.common.wechat.bean.request.mp.material.AddVideoMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.BatchgetMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.DelMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.GetMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.UpdateNewsRequest;
import com.jeecms.common.wechat.bean.request.mp.material.UploadImgRequest;
import com.jeecms.common.wechat.bean.request.mp.material.common.GetArticles;
import com.jeecms.common.wechat.bean.request.mp.material.common.SaveArticles;
import com.jeecms.common.wechat.bean.response.mp.material.AddMeterialResponse;
import com.jeecms.common.wechat.bean.response.mp.material.AddNewsResponse;
import com.jeecms.common.wechat.bean.response.mp.material.BatchgetMaterialResponse;
import com.jeecms.common.wechat.bean.response.mp.material.BatchgetNewsResponse;
import com.jeecms.common.wechat.bean.response.mp.material.GetNewsResponse;
import com.jeecms.common.wechat.bean.response.mp.material.GetVideoResponse;
import com.jeecms.common.wechat.bean.response.mp.material.common.BatchgetNewsItem;
import com.jeecms.common.wechat.bean.response.mp.material.common.MaterialItems;
import com.jeecms.common.wechat.bean.response.mp.material.dto.UpdateNewsDto;
import com.jeecms.wechat.dao.WechatMaterialDao;
import com.jeecms.wechat.domain.WechatMaterial;
import com.jeecms.wechat.domain.dto.WechatDeleteDto;
import com.jeecms.wechat.service.WechatMaterialService;

/**
 * 微信素材管理
 * @author: chenming
 * @date: 2019年6月3日 下午5:01:41
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WechatMaterialServiceImpl extends BaseServiceImpl<WechatMaterial, WechatMaterialDao, Integer>
		implements WechatMaterialService {

	@Autowired
	private AddMaterialApiService addApiService;
	@Autowired
	private DelMaterialApiService delApiService;
	@Autowired
	private BatchgetMaterialApiService batchgetMaterialApiService;
	@Autowired
	private GetMaterialApiService gMaterialApiService;
	@Autowired
	private WechatMaterialDao dao;
	@Autowired
	private UpdateNewsApiService uApiService;

	private ValidateToken getToken(String appId) {
		ValidateToken validateToken = new ValidateToken();
		validateToken.setAppId(appId);
		// validateToken.setAccessToken(tokenService.findByAppId(appId).getAuthorizerAccessToken());
		return validateToken;
	}

	/**
	 * 新增永久图文素材
	 */
	@Override
	public WechatMaterial saveNews(AddNewsRequest addNewsRequest, String appId) throws GlobalException {
		ValidateToken validateToken = this.getToken(appId);
		WechatMaterial wMaterial = new WechatMaterial();
		AddNewsResponse addNewsResponse = addApiService.addNews(addNewsRequest, validateToken);
		GetMaterialRequest getMaterialRequest = new GetMaterialRequest(addNewsResponse.getMediaId());
		final GetNewsResponse getNewsResponse = gMaterialApiService.getNews(getMaterialRequest, validateToken);
		wMaterial.setAppId(appId);
		wMaterial.setMediaId(addNewsResponse.getMediaId());
		wMaterial.setMediaType("news");
		List<String> urList = new ArrayList<>();
		for (SaveArticles saveArticles : addNewsRequest.getArticles()) {
			urList.add(saveArticles.getThumbMediaUrl());
		}
		int i = 0;
		for (GetArticles art : getNewsResponse.getNewsItem()) {
			// 微信服务端针对内容中的图片进行了过滤，所有<img src="*"/> 替换成成<img
			// data-src="*"/>，导致本地页面无法显示对应图片
			if (StringUtils.isNoneBlank(art.getContent())) {
				art.setContent(art.getContent().replace("data-src=\"", "src=\""));
			}
			art.getContent();
			String thumbMediaUrl = urList.get(i);
			// 判断图文略缩图ID在图文素材中是否存在，存在获取对应的URL
			if (StringUtils.isNotBlank(thumbMediaUrl)) {
				art.setThumbMediaUrl(thumbMediaUrl);
			}
			i++;
		}
		wMaterial.setWechatUpdateTime(System.currentTimeMillis() / 1000);
		wMaterial.setMaterialJson(JSON.toJSONString(getNewsResponse.getNewsItem()));
		String titles = getNewsResponse.getNewsItem().stream().map(GetArticles::getTitle)
				.collect(Collectors.joining(","));
		wMaterial.setMediaTitles(titles);
		return super.save(wMaterial);
	}

	/**
	 * 新增其它素材(视频素材、图文素材除外)
	 */
	@Override
	public WechatMaterial saveMaterial(AddMaterialRequest addMaterialRequest, String appId, File file)
			throws GlobalException {
		ValidateToken validateToken = this.getToken(appId);
		WechatMaterial wMaterial = new WechatMaterial();
		AddMeterialResponse addMaterialResponse = addApiService.addMaterial(addMaterialRequest, validateToken, file);
		String name = addMaterialRequest.getFileName().substring(addMaterialRequest.getFileName().lastIndexOf("/") + 1,
				addMaterialRequest.getFileName().length());
		MaterialItems mItems = new MaterialItems(addMaterialResponse.getMediaId(), name,
				String.valueOf(System.currentTimeMillis()), addMaterialResponse.getUrl());
		wMaterial.setAppId(appId);
		wMaterial.setMediaType(addMaterialRequest.getType());
		wMaterial.setMediaId(addMaterialResponse.getMediaId());
		wMaterial.setMaterialJson(JSON.toJSONString(mItems));
		wMaterial.setRequest(JSON.parseObject(wMaterial.getMaterialJson()));
		wMaterial.setWechatUpdateTime(System.currentTimeMillis() / 1000);
		wMaterial.setMaterialName(name);
		return super.save(wMaterial);
	}

	/**
	 * 将图文素材中的图片上传，并获取url
	 */
	@Override
	public String uploadImg(UploadImgRequest uploadImgRequest, String appId) throws GlobalException {
		ValidateToken validateToken = this.getToken(appId);
		String url = addApiService.uploadImg(uploadImgRequest, validateToken, uploadImgRequest.getFile()).getUrl();
		return url;
	}

	/**
	 * 新增视频素材
	 */
	@Override
	public WechatMaterial saveVideo(AddVideoMaterialRequest addVideoMaterialRequest, String appId, File file)
			throws GlobalException {
		ValidateToken validateToken = this.getToken(appId);
		WechatMaterial wMaterial = new WechatMaterial();
		AddMeterialResponse addMeterialResponse = addApiService.addVideoMaterial(addVideoMaterialRequest, validateToken,
				file);
		MaterialItems mItems = new MaterialItems(addMeterialResponse.getMediaId(), addVideoMaterialRequest.getTitle(),
				String.valueOf(System.currentTimeMillis()), null);
		wMaterial.setAppId(appId);
		wMaterial.setMediaType("video");
		wMaterial.setMediaId(addMeterialResponse.getMediaId());
		wMaterial.setMaterialJson(JSON.toJSONString(mItems));
		wMaterial.setWechatUpdateTime(System.currentTimeMillis() / 1000);
		wMaterial.setMaterialName(addVideoMaterialRequest.getTitle());
		return super.save(wMaterial);
	}

	/**
	 * 修改永久图文素材
	 */
	@Override
	public WechatMaterial updateNews(UpdateNewsDto dto,WechatMaterial wMaterial) throws GlobalException {
		/*
		 * 此处的修改永久图文素材，因为微信不提供整片图文进行修改，所以此处的修改为将选中的这片微信图文素材删除 而后新建一个新的前端传过来的图文素材就可以了
		 * 数据库中的该条图文信息进行修改操作
		 */
		
		final ValidateToken validateToken = this.getToken(wMaterial.getAppId());
		List<UpdateNewsRequest> news = dto.getNews();
		int size = 0;
		if (news != null) {
			size = dto.getNews().size();
		}
		List<String> urList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			UpdateNewsRequest request = news.get(i);
			urList.add(request.getThumbMediaUrl());
			request.setMediaId(wMaterial.getMediaId());
			uApiService.updateNews(request, validateToken);
		}
		GetMaterialRequest getMaterialRequest = new GetMaterialRequest(wMaterial.getMediaId());
		GetNewsResponse getNewsResponse = gMaterialApiService.getNews(getMaterialRequest, validateToken);
		int i = 0;
		for (GetArticles art : getNewsResponse.getNewsItem()) {
			// 微信服务端针对内容中的图片进行了过滤，所有<img src="*"/> 替换成成<img
			// data-src="*"/>，导致本地页面无法显示对应图片
			if (StringUtils.isNoneBlank(art.getContent())) {
				art.setContent(art.getContent().replace("data-src=\"", "src=\""));
			}
			art.getContent();
			String thumbMediaUrl = urList.get(i);
			// 判断图文略缩图ID在图文素材中是否存在，存在获取对应的URL
			if (StringUtils.isNotBlank(thumbMediaUrl)) {
				art.setThumbMediaUrl(thumbMediaUrl);
			}
			i++;
		}
		String titles = getNewsResponse.getNewsItem().stream().map(GetArticles::getTitle)
				.collect(Collectors.joining(","));
		wMaterial.setMediaTitles(titles);
		wMaterial.setWechatUpdateTime(System.currentTimeMillis() / 1000);
		wMaterial.setMaterialJson(JSON.toJSONString(getNewsResponse.getNewsItem()));
		return super.update(wMaterial);
	}

	/**
	 * 删除永久素材
	 */
	@Override
	public void deleteMaterial(WechatDeleteDto dto) throws GlobalException {
		for (DelMaterialRequest request : dto.getRequests()) {
			WechatMaterial material = super.findById(request.getId());
			ValidateToken validateToken = this.getToken(material.getAppId());
			
			delApiService.delMaterial(request, validateToken);
			super.delete(material.getId());
		}
	}

	/**
	 * 同步图文素材
	 */
	@Override
	public void synchNews(BatchgetMaterialRequest batchgetMaterialRequest, String appId) throws GlobalException {
		ValidateToken validateToken = this.getToken(appId);
		// 由于同步图文素材时，微信接口只返回素材ID，为了页面显示略缩图，先进行同步图片素材
		BatchgetMaterialRequest pictureMaterialRequest = new BatchgetMaterialRequest(
				Const.Mssage.REQ_MESSAGE_TYPE_IMAGE, batchgetMaterialRequest.getOffset(),
				batchgetMaterialRequest.getCount());
		// 同步所有图片素材
		Map<String, String> pictureMaterials = synchMaterial(pictureMaterialRequest, appId);

		List<WechatMaterial> addList = new ArrayList<WechatMaterial>();
		List<WechatMaterial> updateList = new ArrayList<WechatMaterial>();
		// 请求微信接口次数
		int time = 1;
		// 当前请求第几次微信接口次数
		int index = 0;
		List<WechatMaterial> oldMaterials = dao.findByAppIdAndMediaTypeAndHasDeleted(appId,
				batchgetMaterialRequest.getType(), false);
		Map<String, WechatMaterial> oldMaterialMap = oldMaterials.stream()
				.collect(Collectors.toMap(WechatMaterial::getMediaId, a -> a, (k1, k2) -> k2));
		do {
			// 设置获取的起始位置
			batchgetMaterialRequest.setOffset(index * batchgetMaterialRequest.getCount());
			BatchgetNewsResponse bNewsResponse = batchgetMaterialApiService.batchgetNews(batchgetMaterialRequest,
					validateToken);
			if (time == 1) {
				// 计算当前公众号所有图文数量需要进行几次获取
				time = (bNewsResponse.getTotalCount() / batchgetMaterialRequest.getCount()) + 1;
			}
			// 转换微信返回数据本地库的实体类
			for (BatchgetNewsItem newsItem : bNewsResponse.getItem()) {
				WechatMaterial wMaterial = oldMaterialMap.get(newsItem.getMediaId());
				// 当前素材是否已同步过 false-否 true-是
				boolean operateFlag = true;
				// 判断当前同步微信服务器上的素材是否在本地库中已存在
				if (wMaterial == null) {
					operateFlag = false;
					wMaterial = new WechatMaterial();
					wMaterial.setAppId(appId);
					wMaterial.setMediaType(batchgetMaterialRequest.getType());
					wMaterial.setMediaId(newsItem.getMediaId());
				}

				List<GetArticles> articles = newsItem.getContent().getNewsItem();
				if (articles != null) {
					for (GetArticles art : articles) {
						// 微信服务端针对内容中的图片进行了过滤，所有<img src="*"/> 替换成成<img
						// data-src="*"/>，导致本地页面无法显示对应图片
						if (StringUtils.isNoneBlank(art.getContent())) {
							art.setContent(art.getContent().replace("data-src=\"", "src=\""));
						}

						String thumbMediaUrl = pictureMaterials.get(art.getThumbMediaId());
						// 判断图文略缩图ID在图文素材中是否存在，存在获取对应的URL
						if (StringUtils.isNotBlank(thumbMediaUrl)) {
							art.setThumbMediaUrl(thumbMediaUrl);
						}
					}
				}
				String titles = articles.stream().map(GetArticles::getTitle).collect(Collectors.joining(","));
				wMaterial.setMediaTitles(titles);
				wMaterial.setMaterialJson(JSON.toJSONString(articles));
				wMaterial.setWechatUpdateTime(Long.parseLong(newsItem.getUpdateTime()));
				if (operateFlag) {
					updateList.add(wMaterial);
				} else {
					addList.add(wMaterial);
				}
			}
			index++;
		} while (index < time);
		// 保存所有新增素材
		if (addList.size() > 0) {
			super.saveAll(addList);
		}
		// 更新所有之前已通不过的素材
		if (updateList.size() > 0) {
			super.batchUpdate(updateList);
		}
	}

	/**
	 * 同步其它素材(图文素材除外)
	 */
	@Override
	public Map<String, String> synchMaterial(BatchgetMaterialRequest batchgetMaterialRequest, String appId)
			throws GlobalException {
		ValidateToken validateToken = this.getToken(appId);
		List<WechatMaterial> addList = new ArrayList<WechatMaterial>();
		List<WechatMaterial> updateList = new ArrayList<WechatMaterial>();
		Map<String, String> maps = new HashMap<String, String>(16);
		// 请求微信接口次数
		int time = 1;
		// 当前请求第几次微信接口次数
		int index = 0;
		List<WechatMaterial> oldMaterials = dao.findByAppIdAndMediaTypeAndHasDeleted(appId,
				batchgetMaterialRequest.getType(), false);
		Map<String, WechatMaterial> oldMaterialMap = oldMaterials.stream()
				.collect(Collectors.toMap(WechatMaterial::getMediaId, a -> a, (k1, k2) -> k2));
		do {
			// 设置获取的起始位置
			batchgetMaterialRequest.setOffset(index * batchgetMaterialRequest.getCount());
			BatchgetMaterialResponse bMaterialResponse = batchgetMaterialApiService
					.batchgetMaterial(batchgetMaterialRequest, validateToken);
			if (time == 1) {
				// 计算当前公众号所有图文数量需要进行几次获取
				time = (bMaterialResponse.getTotalCount() / batchgetMaterialRequest.getCount()) + 1;
			}
			// 转换微信返回数据本地库的实体类，之前已同步素材进行更新，新增素材进行保存
			for (MaterialItems materialItem : bMaterialResponse.getItem()) {
				WechatMaterial wMaterial = oldMaterialMap.get(materialItem.getMediaId());
				// 判断当前同步微信服务器上的素材是否在本地库中已存在
				if (wMaterial == null) {
					wMaterial = new WechatMaterial();
					wMaterial.setAppId(appId);
					wMaterial.setMediaType(batchgetMaterialRequest.getType());
					wMaterial.setMediaId(materialItem.getMediaId());
					wMaterial.setMaterialJson(JSON.toJSONString(materialItem));
					wMaterial.setWechatUpdateTime(Long.parseLong(materialItem.getUpdateTime()));
					wMaterial.setMaterialName(materialItem.getName());
					addList.add(wMaterial);
				} else {
					wMaterial.setMaterialJson(JSON.toJSONString(materialItem));
					wMaterial.setWechatUpdateTime(Long.parseLong(materialItem.getUpdateTime()));
					wMaterial.setMaterialName(materialItem.getName());
					updateList.add(wMaterial);
				}
				maps.put(wMaterial.getMediaId(), materialItem.getUrl());
			}
			index++;
		} while (index < time);
		// 保存所有新增素材
		if (addList.size() > 0) {
			super.saveAll(addList);
		}
		// 更新所有之前已通不过的素材
		if (updateList.size() > 0) {
			super.batchUpdate(updateList);
		}
		return maps;
	}

	@Override
	public String dowloadVideo(Integer id) throws GlobalException {
		WechatMaterial wMaterial = super.findById(id);
		ValidateToken validateToken = this.getToken(wMaterial.getAppId());
		GetMaterialRequest gMaterialRequest = new GetMaterialRequest(wMaterial.getMediaId());
		GetVideoResponse gVideoResponse = gMaterialApiService.getVideo(gMaterialRequest, validateToken);
		return gVideoResponse.getDownUrl();
	}

	@Override
	public MediaFile dowloadMatererial(Integer id, String appId, HttpServletRequest request,
			HttpServletResponse response) throws GlobalException, IOException {
		ValidateToken validateToken = this.getToken(appId);
		GetMaterialRequest gMaterialRequest = new GetMaterialRequest(super.get(id).getMediaId());
		MediaFile mediaFile = gMaterialApiService.getMaterial(gMaterialRequest, validateToken);
		return mediaFile;
	}

	@Override
	public WechatMaterial getMediaId(String mediaId) {
		List<WechatMaterial> materials = dao.findByMediaIdAndHasDeleted(mediaId, false);
		if (materials != null && materials.size() > 0) {
			return materials.get(0);
		}
		return null;
	}

}
