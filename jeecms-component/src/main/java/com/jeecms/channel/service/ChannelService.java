/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.channel.service;

import com.jeecms.channel.domain.Channel;
import com.jeecms.channel.domain.dto.*;
import com.jeecms.channel.domain.vo.ChannelModelTplVo;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.CmsSite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * 栏目service接口
 *
 * @author: tom
 * @date: 2019年3月19日 下午6:31:04
 */
public interface ChannelService extends IBaseService<Channel, Integer> {
	/**
	 * 查询顶层栏目列表
	 *
	 * @param siteId            站点id
	 * @param containNotDisplay false则不包含不显示的栏目 true包含未发布的栏目。
	 * @return List
	 * @Title: findTopList
	 */
	List<Channel> findTopList(Integer siteId, boolean containNotDisplay);

	/**
	 * 查询子栏目列表
	 *
	 * @param siteId            站点id
	 * @param parentId          父栏目id
	 * @param containNotPublish false则不包含未发布的栏目 true包含未发布的栏目。
	 * @param containNotDisplay false则不包含不显示的栏目 true包含未发布的栏目。
	 * @return List
	 * @Title: findListByParentId
	 */
	List<Channel> findListByParentId(Integer siteId, Integer parentId, boolean containNotPublish,
									 boolean containNotDisplay);


	/**
	 * 查询子栏目分页
	 *
	 * @param siteId            站点id
	 * @param parentId          父栏目id
	 * @param containNotPublish false则不包含未发布的栏目 true包含未发布的栏目。
	 * @param containNotDisplay false则不包含不显示的栏目 true包含未发布的栏目。
	 * @return page
	 * @Title: findListByParentId
	 */
	Page<Channel> findPageByParentId(Integer siteId, Integer parentId, boolean containNotPublish,
									 boolean containNotDisplay, Pageable pageable);

	/**
	 * 查询栏目列表(递归含子栏目)
	 *
	 * @param siteId            站点id
	 * @param containNotDisplay false则不包含不显示的栏目 true包含不显示的栏目。
	 * @return List
	 * @Title: findList
	 */
	List<Channel> findList(Integer siteId, boolean containNotDisplay);

	List<Channel> findListBySiteId(Integer siteId);

	/**
	 * 新增
	 *
	 * @param dto 新增栏目扩展Dto
	 * @throws GlobalException 全局异常
	 * @Title: save
	 * @return: void
	 */
	Channel save(ChannelSaveDto dto) throws GlobalException;

	/**
	 * 批量新增
	 *
	 * @param dtos 批量新增扩展Dto
	 * @param site 站点对象
	 * @throws GlobalException 全局异常
	 * @Title: saveAll
	 * @return: void
	 */
	List<Channel> saveAll(List<ChannelSaveAllDto> dtos, CmsSite site) throws GlobalException;

	/**
	 * 校验栏目名称或者栏目路径
	 * @Title: checkElement  
	 * @param channel	栏目对象
	 * @param name		栏目名称
	 * @param path		栏目路径
	 * @param siteId	站点ID
	 * @throws GlobalException	全局异常      
	 * @return: boolean
	 */
	boolean checkElement(Channel channel, String name, String path, Integer siteId) throws GlobalException;

	/**
	 * 查询栏目详情
	 *
	 * @param id   栏目名称
	 * @param site 站点对象
	 * @throws GlobalException 全局异常
	 * @Title: findById
	 * @return: Map
	 */
	ChannelFindVo findById(Integer id, CmsSite site) throws GlobalException;

	/**
	 * 根据栏目路径找栏目对象
	 *
	 * @param path   栏目路径
	 * @param siteId 站点ID
	 * @Title: findByPath
	 * @return: Channel
	 */
	Channel findByPath(String path, Integer siteId);

	/**
	 * 根据栏目路径找栏目对象集合
	 *
	 * @param paths   栏目路径s
	 * @param siteId 站点ID
	 * @Title: findByPath
	 * @return: Channel
	 */
	List<Channel> findByPath(String[] paths, Integer siteId);

	/**
	 * 修改栏目主体
	 *
	 * @param channelDto 修改栏目dto
	 * @throws GlobalException 全局异常
	 * @Title: updateChannel
	 * @return: void
	 */
	void updateChannel(ChannelDto channelDto) throws GlobalException;

	/**
	 * 删除栏目
	 *
	 * @param ids        栏目Id数组
	 * @param isThorough 行为:true->逻辑删除，false->加入回收站
	 * @throws GlobalException 全局异常
	 * @Title: delete
	 * @return: void
	 */
	void delete(Integer[] ids, boolean isThorough) throws GlobalException;

	/**
	 * 还原加入回收站的栏目
	 * @Title: reduction  
	 * @param dto	还原栏目扩展dto
	 * @throws GlobalException   全局异常   
	 * @return: void
	 */
	void reduction(ChannelReductionDto dto) throws GlobalException;

	/**
	 * 设置栏目是否开启查询索引
	 *
	 * @param channelOpenSet 栏目设置数据
	 * @throws GlobalException 全局异常
	 * @Title: reduction
	 * @return: void
	 */
	void setOpenIndex(ChannelSetIndexDto channelOpenSet) throws GlobalException;

	/**
	 * 将工作流应用到其它栏目
	 *
	 * @param dto 栏目操作工作流dto
	 * @throws GlobalException 全局异常
	 * @Title: claimWorkflow
	 * @return: void
	 */
	void claimWorkflow(ChannelWorkflowDto dto) throws GlobalException;

	/**
	 * 移动或将栏目移动到对应的位置
	 *
	 * @param dto         Id数据
	 * @param channelList 前台展示的栏目List集合
	 * @throws GlobalException 全局异常
	 * @Title: ChannelSort
	 * @return: void
	 */
	void channelSort(ChannelSortDto dto, List<Channel> channelList) throws GlobalException;

	/**
	 * 合并栏目
	 *
	 * @param channel 目标栏目
	 * @param ids     需要合并的栏目id数组
	 * @throws GlobalException 全局异常
	 * @Title: mergeChannel
	 * @return: void
	 */
	void mergeChannel(Channel channel, Integer[] ids) throws GlobalException;

	/**
	 * 查询内容模型模板
	 * @param siteId 站点id
	 * @param channelId 栏目ID值
	 * @throws GlobalException 全局异常
	 * @Title: findModelTplVo
	 * @return: List
	 */
	List<ChannelModelTplVo> findModelTplVo(Integer siteId,Integer channelId) throws GlobalException;

	/**
	 * 前台获取栏目详情
	 *
	 * @param id     栏目id
	 * @param siteId 站点id
	 * @param path   地址
	 * @return Channel
	 */
	Channel get(Integer id, Integer siteId, String path);

	/**
	 * 根据parentId查找栏目
	 *
	 * @param parentId 栏目id
	 * @param siteId   站点id
	 * @return List 集合
	 */
	List<Channel> getChannelByParentId(Integer siteId, Integer parentId);

	/**
	 * 模型删除工作流字段进行数据处理
	 * @param modelId 模型id值
	 * @Title: deleteWorkflow
	 * @throws GlobalException 全局异常
	 * @return: void
	 */
	void deleteWorkflow(Integer modelId) throws GlobalException;
	
	/**
	 * 根据删除进行检索查询栏目集合
	 * @Title: getChanelByRecycle  
	 * @param siteId	站点ID
	 * @param recycle	删除标识
	 * @return: List
	 */
	List<Channel> getChanelByRecycle(Integer siteId, Boolean recycle);

	/**
	 * 根据模型ID进行检索查询栏目集合
	 * @Title: getContentByModel  
	 * @param modelId
	 * @return: List
	 */
	List<Integer> getContentByModel(Integer modelId);

	List<Channel>  findByIds(Collection<Integer> ids);
}
