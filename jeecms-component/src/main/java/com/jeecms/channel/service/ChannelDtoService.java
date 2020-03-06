package com.jeecms.channel.service;

import java.util.List;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.channel.domain.dto.ChannelSaveAllDto;
import com.jeecms.channel.domain.dto.ChannelSaveMultipleDto;
import com.jeecms.common.exception.GlobalException;

/**
 * 栏目扩展service的dto接口
 * @author: chenming
 * @date:   2019年6月28日 下午2:09:07
 */
public interface ChannelDtoService {
	
	/**
	 * 将前台传入dto转换成channelAttrDto的list集合
	 * @Title: initChannelSaveAllDto  
	 * @param dto	前台传入dto
	 * @return: List
	 */
	List<ChannelSaveAllDto> initChannelSaveAllDto(ChannelSaveMultipleDto dto);
	
	/**
	 * 中断工作流流程
	 * @Title: workflowDelete  
	 * @param ids	工作流ID值
	 * @param user	用户user(使用子线程操作，所以user只能从主线程传送过来)
	 * @throws GlobalException    全局异常
	 * @return: void
	 */
	void workflowDelete(Integer[] ids,CoreUser user) throws GlobalException;
}
