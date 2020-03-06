package com.jeecms.system.dao.ext;

import java.util.List;

import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.Area;

/**
 * Area设置dao扩展接口
 * @author: chenming
 * @date:   2019年4月13日 下午2:48:07
 */
public interface AreaDaoExt {
	
	/**
	 * 查找区域列表
	 * @Title: findByParams  
	 * @param parentId 父节点ID
	 * @param hasDeleted 是否删除
	 * @throws GlobalException 程序异常     
	 * @return: List<Area>
	 */
	List<Area> findByParams(Integer parentId,Boolean hasDeleted) throws GlobalException;
}
