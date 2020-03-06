package com.jeecms.resource.service;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.resource.domain.ResourcesSpace;
import com.jeecms.resource.domain.ResourcesSpaceData;
import com.jeecms.resource.domain.UploadFtp;
import com.jeecms.resource.domain.UploadOss;
import com.jeecms.resource.domain.dto.ResourcesSpaceShareDto;
import com.jeecms.system.domain.CmsSite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @Description:ResourcesSpaceData service接口
 * @author: tom
 * @date: 2018年4月16日 上午11:10:01
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

public interface ResourcesSpaceDataService extends IBaseService<ResourcesSpaceData, Integer> {

	/**
	 * 获取资源
	 *
	 * @param alias                 别名
	 * @param userId                用户id
	 * @param storeResourcesSpaceId 资源空间
	 * @param resourceType          资源类型
	 * @param shareStatus           分享状态
	 * @param beginCreateTime       开始时间
	 * @param endCreateTime         结束时间
	 * @param pageable              分页
	 * @return Page
	 */
	Page<ResourcesSpaceData> getPage(String alias, Integer userId, Integer storeResourcesSpaceId, Short resourceType,
									 Short shareStatus, Date beginCreateTime, Date endCreateTime, Pageable pageable);

	/**
	 * 获取分享资源
	 *
	 * @param alias                 别名
	 * @param shareUser             分享用户
	 * @param resourceType          资源类型
	 * @param storeResourcesSpaceId 资源空间
	 * @param userId                用户id
	 * @param pageable              分页
	 * @return Page
	 */
	Page<ResourcesSpaceData> getPage(String alias, String shareUser, Short resourceType, Integer storeResourcesSpaceId,
									 Integer userId, Pageable pageable);

	/**
	 * 根据用户获取资源列表
	 *
	 * @param userId 用户id
	 * @return list
	 */
	List<ResourcesSpaceData> getByUserId(Integer userId);

	/**
	 * 保存资源
	 *
	 * @param coreUserId   管理员ID
	 * @param spaceId      资源文件ID
	 * @param fileName     文件名
	 * @param size         文件大小
	 * @param url          文件上传URL
	 * @param dimensions   尺寸
	 * @param resourceType 文件类型
	 * @param display      是否展示在资源库中
	 * @param siteId       站点Id
	 * @param duration     视频或音频的时长(单位: 秒)
	 * @param uploadFtp    上传FTP
	 * @param uploadOss    上传OSS
	 * @throws GlobalException 异常
	 * @Title: save
	 * @return: ResourcesSpaceData
	 */
	ResourcesSpaceData save(Integer coreUserId, Integer spaceId, String fileName, Integer size, String url,
							String dimensions, Short resourceType, boolean display,
							Integer siteId, Integer duration, UploadFtp uploadFtp,
							UploadOss uploadOss) throws GlobalException;

	/**
	 * selectUrlsByFlag
	 *
	 * @Title:
	 * @Description: 根据资源ID返回URL List
	 * @param: @param targetIds 资源ID
	 * @param: @return
	 * @param: @throws GlobalException
	 * @return: List<String>
	 */
	List<String> selectUrlsByIds(Iterable<Integer> targetIds) throws GlobalException;

	/**
	 * 通过ID数组查询图片资源
	 *
	 * @param ids
	 * @return
	 * @throws GlobalException 异常
	 * @Title: findByIds
	 * @return: List<ResourcesSpaceData>
	 */
	List<ResourcesSpaceData> findByIds(Integer[] ids) throws GlobalException;

	/**
	 * 通过id查询资源对象
	 *
	 * @param id 资源id
	 * @return ResourcesSpaceData
	 * @throws GlobalException 异常
	 * @Title: findByIdAndHasDeleted
	 * @return: ResourcesSpaceData
	 */
	ResourcesSpaceData findByIdAndHasDeleted(Integer id) throws GlobalException;

	/**
	 * 本地保存(测试用的)
	 *
	 * @param data 测试数据
	 * @return ResourcesSpaceData
	 * @throws GlobalException 程序异常
	 * @Title: saveTest
	 * @return: ResourcesSpaceData
	 */
	ResourcesSpaceData saveTest(ResourcesSpaceData data) throws GlobalException;

	/**
	 * 共享资源
	 *
	 * @param dto 分享资源空间Dto
	 * @return list
	 * @throws GlobalException 异常
	 */
	List<ResourcesSpaceData> share(ResourcesSpaceShareDto dto) throws GlobalException;

	/**
	 * 取消分享资源
	 *
	 * @param ids 资源id数组
	 * @return List
	 * @throws GlobalException 异常
	 */
	List<ResourcesSpaceData> unShare(Integer[] ids) throws GlobalException;

	/**
	 * 取消分享资源
	 *
	 * @param list 资源数组
	 * @throws GlobalException 异常
	 */
	void unShare(List<ResourcesSpaceData> list) throws GlobalException;

	/**
	 * 批量共享资源
	 *
	 * @param set      用户集合
	 * @param dataList 资源集合
	 * @return List
	 * @throws GlobalException 异常
	 */
	List<ResourcesSpaceData> shareAll(List<CoreUser> set, List<ResourcesSpaceData> dataList) throws GlobalException;

	/**
	 * 获取资源的用户列表
	 *
	 * @param shareStatus 分享状态
	 * @return list
	 */
	List<CoreUser> findUserByShareStatus(Short shareStatus);

	/**
	 * 通过资源空间获取资源
	 *
	 * @param spaceId 资源空间id
	 * @return list
	 */
	List<ResourcesSpaceData> findBySpaceId(Integer spaceId);

	/**
	 * 修改资源空间的资源是否在资源库显示
	 *
	 * @param spaces  资源空间集合
	 * @param display 是否显示
	 * @throws GlobalException 异常
	 */
	void updateToDisplay(List<ResourcesSpace> spaces, Boolean display) throws GlobalException;

	/**
	 * 保存到我的资源
	 *
	 * @param ids     资源id数组
	 * @param userId  用户id
	 * @param spaceId 资源空间id
	 * @param site    站点
	 * @throws GlobalException 全局异常
	 */
	void copyBatch(Integer[] ids, Integer userId, Integer spaceId, CmsSite site) throws GlobalException;

	/**
	 * 删除我的资源数据和文件
	 *
	 * @param ids 资源Ids
	 * @return List
	 * @throws GlobalException 异常
	 */
	List<ResourcesSpaceData> deleteFile(Integer[] ids) throws GlobalException;

	/**
	 * 重名处理, 如果别名已存在, 则文件名后的数字递增, 步长为1
	 *
	 * @param alias                 文件名
	 * @param id                    资源id
	 * @param userId                用户id
	 * @param i                     序号
	 * @param storeResourcesSpaceId 资源空间id
	 * @return 返回结果 文件名(i)
	 */
	String filename(String alias, Integer id, Integer userId, int i, Integer storeResourcesSpaceId);

	/**
	 * 查询别名前缀数量
	 * @param alias 别名
	 * @param userId 用户id
	 * @param storeResourcesSpaceId 文件夹id
	 * @return
	 */
	Long countByAliasPrefix(String alias,Integer userId, Integer storeResourcesSpaceId);

	/**
	 * 校验资源别名是否存在
	 *
	 * @param alias                 资源别名
	 * @param id                    资源别名id
	 * @param userId                用户id
	 * @param storeResourcesSpaceId 资源空间id
	 * @return true（不存在） false（存在）
	 */
	boolean checkByAlias(String alias, Integer id, Integer userId, Integer storeResourcesSpaceId);

	/**
	 * 返回资源对应的临时文件(如果是FTP OSS上资源需要调用方在用完临时文件的上级文件删除，因为随机的是文件夹，文件名是资源别名)
	 *
	 * @param data
	 * @Title: getFile
	 * @return: getFile
	 */
	File getFile(ResourcesSpaceData data);

	/**
	 * 查找问卷页眉图和背景图
	 *
	 * @return List
	 */
	List<ResourcesSpaceData> findByQuestionnairePic();

}
