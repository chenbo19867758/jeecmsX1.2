package com.jeecms.wechat.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.wechat.bean.MediaFile;
import com.jeecms.common.wechat.bean.request.mp.material.AddMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.AddNewsRequest;
import com.jeecms.common.wechat.bean.request.mp.material.AddVideoMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.BatchgetMaterialRequest;
import com.jeecms.common.wechat.bean.request.mp.material.UploadImgRequest;
import com.jeecms.common.wechat.bean.response.mp.material.dto.UpdateNewsDto;
import com.jeecms.wechat.domain.WechatMaterial;
import com.jeecms.wechat.domain.dto.WechatDeleteDto;

/**
 * 素材管理service接口
 * @author: chenming
 * @date:   2019年5月31日 下午1:53:37
 */
public interface WechatMaterialService extends IBaseService<WechatMaterial,Integer>{
	
	/**
	 * 新增永久图文素材
	 * @Title: saveNews  
	 * @param addNewsRequest
	 * @param appId
	 * @return
	 * @throws GlobalException      
	 * @return: WechatMaterial
	 */
	WechatMaterial saveNews(AddNewsRequest addNewsRequest,String appId) throws GlobalException;
	
	/**
	 * 新增其它永久素材(图文、视频除外)
	 * @Title: saveMaterial  
	 * @param addMaterialRequest
	 * @param appId
	 * @param file
	 * @return
	 * @throws GlobalException      
	 * @return: WechatMaterial
	 */
	WechatMaterial saveMaterial(AddMaterialRequest addMaterialRequest,String appId,File file)throws GlobalException;
	
	/**
	 * 上传图文素材中的图片获取URL
	 * @Title: uploadImg  
	 * @param uploadImgRequest
	 * @param appId
	 * @return
	 * @throws GlobalException      
	 * @return: String
	 */
	String uploadImg(UploadImgRequest uploadImgRequest,String appId)throws GlobalException;
	
	/**
	 * 新增永久视频素材
	 * @Title: saveVideo  
	 * @param addVideoMaterialRequest
	 * @param appId
	 * @param file
	 * @return
	 * @throws GlobalException      
	 * @return: WechatMaterial
	 */
	WechatMaterial saveVideo(AddVideoMaterialRequest addVideoMaterialRequest,String appId,File file)throws GlobalException;
	
	/**
	 * 修改永久图文素材
	 * @Title: updateNews  
	 * @param updateNewsRequestDto
	 * @param id
	 * @param appId
	 * @return
	 * @throws GlobalException      
	 * @return: WechatMaterial
	 */
	WechatMaterial updateNews(UpdateNewsDto dto,WechatMaterial wMaterial)throws GlobalException;
	
	/**
	 * 删除永久素材
	 * @Title: deleteMaterial  
	 * @param delMaterialRequest
	 * @param id
	 * @param appId
	 * @return
	 * @throws GlobalException      
	 * @return: WechatMaterial
	 */
	void deleteMaterial(WechatDeleteDto dto)throws GlobalException;
	
	
	/**
	 * 同步图文素材
	 * @Title: synchNews  
	 * @param batchgetMaterialRequest
	 * @param appId
	 * @param paginable
	 * @return
	 * @throws GlobalException      
	 */
	void synchNews(BatchgetMaterialRequest batchgetMaterialRequest,String appId)throws GlobalException;
	
	/**
	 * 同步其它素材
	 * @Title: synchMaterial  
	 * @param batchgetMaterialRequest
	 * @param appId
	 * @param paginable
	 * @return
	 * @throws GlobalException      
	 * @return: Map<String,String>
	 */
	Map<String, String> synchMaterial(BatchgetMaterialRequest batchgetMaterialRequest,String appId)throws GlobalException;
	
	/**
	 * 下载永久视频素材
	 * @Title: dowloadVideo  
	 * @param id
	 * @param appId
	 * @return
	 * @throws GlobalException      
	 * @return: String
	 */
	String dowloadVideo(Integer id)throws GlobalException;
	
	/**
	 * 下载其它永久素材(图文、视频除外)
	 * @Title: dowloadMatererial  
	 * @param id
	 * @param appId
	 * @param request
	 * @return
	 * @throws GlobalException      
	 * @return: String
	 */
	MediaFile dowloadMatererial(Integer id,String appId,HttpServletRequest request,HttpServletResponse response)throws GlobalException, IOException;

	WechatMaterial getMediaId(String mediaId);
}
