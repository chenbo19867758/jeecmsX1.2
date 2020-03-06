/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.service;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.response.ResponseInfo;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentExt;
import com.jeecms.content.domain.ContentVersion;
import com.jeecms.content.domain.dto.*;
import com.jeecms.content.domain.vo.ContentFindVo;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.GlobalConfig;
import com.jeecms.system.domain.dto.BeatchDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 内容主体service接口
 * 
 * @author: chenming
 * @date: 2019年5月6日 下午2:35:08
 */
public interface ContentService extends IBaseService<Content, Integer> {

	/**
	 * 改变内容状态
	 * 
	 * @Title: changeStatus
	 * @Description:
	 * @param dto 批量操作Dto
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	ResponseInfo changeStatus(BeatchDto dto,CoreUser user) throws GlobalException;

	/**
	 * 发布内容（无权限限制）
	 * 
	 * @Title: publish
	 * @param ids 内容ID集合
	 * @throws GlobalException GlobalException
	 * @return: void
	 */
	void publish(List<Integer> ids) throws GlobalException;

	/**
	 * 内容类型操作
	 * 
	 * @Title: operation
	 * @Description:
	 * @param dto 批量操作Dto
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	ResponseInfo operation(OperationDto dto) throws GlobalException;

	/**
	 * 置顶操作
	 * 
	 * @Title: top
	 * @Description:
	 * @param dto 批量操作Dto
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	ResponseInfo top(OperationDto dto) throws GlobalException;

	/**
	 * 取消置顶操作
	 * 
	 * @Title: notop
	 * @Description:
	 * @param dto 批量操作Dto
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	ResponseInfo notop(OperationDto dto) throws GlobalException;

	/**
	 * 移动操作
	 * 
	 * @Title: move
	 * @Description:
	 * @param dto 批量操作Dto
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	ResponseInfo move(OperationDto dto) throws GlobalException;

	/**
	 * 排序操作
	 * 
	 * @Title: sort
	 * @Description:
	 * @param dto 批量操作Dto
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	ResponseInfo sort(OperationDto dto) throws GlobalException;

	/**
	 * 删除操作(加入回收站)
	 * 
	 * @Title: rubbish
	 * @Description:
	 * @param dto 批量操作Dto
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	ResponseInfo rubbish(OperationDto dto) throws GlobalException;

	/**
	 * 引用操作
	 * 
	 * @Title: quote
	 * @Description:
	 * @param dto 批量操作Dto
	 * @return ResponseInfo
	 * @throws GlobalException 异常
	 */
	ResponseInfo quote(OperationDto dto) throws GlobalException;

	/**
	 * 取消引用操作
	 * 
	 * @Title: quote
	 * @Description:
	 * @param dto 批量操作Dto
	 * @return ResponseInfo 返回对象
	 * @throws GlobalException 异常
	 */
	ResponseInfo noquote(OperationDto dto) throws GlobalException;

	/**
	 * 新增内容
	 * 
	 * @Title: save
	 * @param dto 新增内容扩展dto
	 * @return TODO
	 * @throws GlobalException 全局异常
	 * @return: void
	 */
	Content save(ContentSaveDto dto, CmsSite site) throws GlobalException;
	
	/**
	 * 提交内容
	 * @Title: submit
	 * @param dto ContentUpdateDto
	 * @param request HttpServletRequest
	 * @throws GlobalException GlobalException
	 * @return: void
	 */
	void submit(ContentUpdateDto dto, HttpServletRequest request,boolean contribute,Content content) throws GlobalException;

	/**
	 * 查询单个内容
	 * 
	 * @Title: findContent
	 * @param id           内容id
	 * @param globalConfig 全局配置
	 * @throws GlobalException 全局异常
	 * @return: ContentFindVo
	 */
	ContentFindVo findContent(Integer id, GlobalConfig globalConfig) throws GlobalException;

	/**
	 * 修改单个内容
	 * 
	 * @Title: update
	 * @param dto     接受修改dto
	 * @param request 前台传入request请求
	 * @return TODO
	 * @throws GlobalException 异常
	 * @return: void
	 */
	Content update(ContentUpdateDto dto, HttpServletRequest request) throws GlobalException;

	/**
	 * 初始化组装update的dto
	 * 
	 * @Title: initSpliceCheckUpdateDto
	 * @param content 内容对象
	 * @return: SpliceCheckUpdateDto
	 */
	SpliceCheckUpdateDto initSpliceCheckUpdateDto(Content content);

	/**
	 * 校验修改的内容，并返回数据
	 * 
	 * @Title: checkUpdate
	 * @param oldUpdateDto 老的校验dto
	 * @param newUpdateDto 新的校验dto
	 * @param globalConfig 全局配置
	 * @throws GlobalException 全局异常
	 * @return: String
	 */
	void checkUpdate(SpliceCheckUpdateDto oldUpdateDto, SpliceCheckUpdateDto newUpdateDto, 
			GlobalConfig globalConfig,Content bean) throws GlobalException;

	/**
	 * copy内容
	 * 
	 * @Title: copy
	 * @param dto    copy接收dto
	 * @param rquest 前台出传入request请求
	 * @throws GlobalException 异常
	 * @return: void
	 */
	void copy(ContentCopyDto dto, HttpServletRequest rquest) throws GlobalException;

	/**
	 * 校验内容标题，true->该内容存在，false->内容不存在
	 * 
	 * @Title: checkTitle
	 * @param title     内容标题
	 * @param channelId 栏目id
	 * @return: boolean
	 */
	boolean checkTitle(String title, Integer channelId);

	/**
	 * 回复版本
	 * 
	 * @Title: recoveryVersion
	 * @param version   版本对象
	 * @param contentId 内容id
	 * @throws GlobalException 全局异常
	 * @return: void
	 */
	ResponseInfo recoveryVersion(ContentVersion version, Integer contentId) throws GlobalException;

	/**
	 * 校验权限
	 * 
	 * @Title: validType
	 * @param opration 权限标识符
	 * @param contents 内容集合
	 * @param content  内容对象
	 * @return: Boolean
	 */
	Boolean validType(Short opration, List<Content> contents, Content content);

	/**
	 * 通过栏目id校验权限
	 * 
	 * @Title: validType
	 * @param opration 权限标识
	 * @param channId  栏目id
	 * @return: Boolean
	 */
	Boolean validType(Short opration, Integer channId);

	/**
	 * 通过栏目id数组查询该栏目id数组下的内容集合
	 * 
	 * @Title: findByChannels
	 * @param channelIds 栏目数组
	 * @return: List
	 */
	List<Content> findByChannels(Integer[] channelIds);

	/**
	 * 推送到站群
	 * 
	 * @Title: pushSites
	 * @param dto     接受dto
	 * @param request request请求
	 * @throws GlobalException 全局异常
	 * @return: void
	 */
	void pushSites(ContentPushSitesDto dto, HttpServletRequest request) throws GlobalException;

	/**
	 * 导出
	 * 
	 * @Title: exportContent
	 * @param channelId        栏目ID
	 * @param modelId          模型ID
	 * @param collectContentId 收藏ID
	 * @throws GlobalException 异常
	 */
	void exportContent(Integer channelId, Integer modelId, Integer collectContentId, Integer userId)
			throws GlobalException;

	/**
	 * 微信预览
	 * 
	 * @Title: preview
	 * @param dto 传输DTO
	 * @throws GlobalException 异常
	 */
	ResponseInfo preview(WechatViewDto dto) throws GlobalException;

	/**
	 * 推送到微信
	 * 
	 * @Title: push
	 * @param dto 传输DTO
	 * @throws Exception 异常
	 */
	ResponseInfo push(WechatPushDto dto) throws Exception;

	/**
	 * 内容导入
	 * 
	 * @Title: docImport
	 * @param file 文件
	 * @param type 类型
	 * @throws Exception 异常
	 */
	String docImport(MultipartFile file, Integer type) throws Exception;

	/**
	 * 还原内容
	 * 
	 * @Title: restore
	 * @param contentIds 内容ID数组
	 * @param siteId 站点ID
	 * @param channelIds	栏目id集合
	 * @throws GlobalException 异常
	 */
	void restore(List<Integer> contentIds, Integer siteId,List<Integer> channelIds) throws GlobalException;
	
	/**
	 *删除内容
	 * @Title: restore
	 * @param contentIds 内容ID数组
	 * @throws GlobalException 异常
	 */
	void deleteContent(List<Integer> contentIds) throws GlobalException;
	
	/**
	 * 新增修改之后初始化内容content关联的对象
	 * @Title: initContentObject  
	 * @param content	需要初始化的内容
	 * @throws GlobalException      
	 * @return: Content
	 */
	Content initContentObject(Content content) throws GlobalException;
	
	/**
	 * 新增修改之后初始化内容扩展contentExt关联的对象
	 * @Title: initContentExtObject  
	 * @param contentExt	内容扩展
	 * @throws GlobalException      
	 * @return: ContentExt
	 */
	ContentExt initContentExtObject(ContentExt contentExt) throws GlobalException;
	
}
