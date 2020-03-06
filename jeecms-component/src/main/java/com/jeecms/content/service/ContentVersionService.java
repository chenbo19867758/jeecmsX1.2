/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.content.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.content.domain.Content;
import com.jeecms.content.domain.ContentVersion;
import com.jeecms.content.domain.dto.VersionCompareDto;
import com.jeecms.content.domain.vo.ContentVersionVo;

import java.util.Map;

/**
 * 内容版本service接口
 * @author: chenming
 * @date:   2019年5月15日 下午5:25:21
 */
public interface ContentVersionService extends IBaseService<ContentVersion, Integer> {

	/**
	 * 新增一个内容版本
	 * @Title: save  
	 * @param contentTxtMap	内容文本内容map
	 * @param contentId	内容id
	 * @throws GlobalException	全局异常      
	 * @return: void
	 */
	void save(Map<String,String> contentTxtMap,Integer contentId,String remark) throws GlobalException;
	
	/**
	 * 新增一个内容版本
	 * @Title: save  
	 * @param contentId	内容id
	 * @throws GlobalException      
	 * @return: void
	 */
	void save(Content content,String remark) throws GlobalException;


	ContentVersionVo controstVersion(VersionCompareDto dto) throws GlobalException;


	/**
	 * 版本对比
	 * <p>
	 * 历史版本保存在ContentVersion中
	 * 当前版本在ContentTxt中
	 *
	 * @param dto      内容id, 版本id
	 * @return Map
	 * @author Zhu Kaixiao
	 * @date 2019/7/22 10:15
	 **/
	Map<String, Object> versionCompare(VersionCompareDto dto);



}
