/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.domain.DragSortDto;
import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.ContentMark;
import com.jeecms.system.domain.dto.ContentMarkBatchDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 发文字号管理Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-05-21
 */
public interface ContentMarkService extends IBaseService<ContentMark, Integer> {

	/**
	 * 批量添加
	 *
	 * @param markNames 机关代字/年号(可用逗号隔开)
	 * @param markType  标记词种类(1机关代字 2年份)
	 * @return List
	 * @throws GlobalException 异常
	 */
	List<ContentMark> saveBatch(@NotBlank String markNames, Integer markType) throws GlobalException;

	/**
	 * 校验markName是否存在
	 *
	 * @param markName 机关代字/年号
	 * @param markType 标记词种类(1机关代字 2年份)
	 * @param id       发文字号管理id
	 * @return boolean true不存在 false存在
	 */
	boolean checkByMarkName(String markName, Integer markType, Integer id);

	/**
	 * 逻辑删除
	 *
	 * @param ids      发文字号管理id数组
	 * @param markType 标记词种类(1机关代字 2年份)
	 */
	void deleteByIdAndMarkType(Integer[] ids, Integer markType);

	/**
	 * 通过id和标记词种类查询
	 *
	 * @param id       发文字号管理id
	 * @param markType 标记词种类(1机关代字 2年份)
	 * @return ContentMark
	 */
	ContentMark findByIdAndMarkType(Integer id, Integer markType);

	/**
	 * 排序
	 *
	 * @param sorts    拖拽排序dto
	 * @param markType 标记词种类(1机关代字 2年份)
	 * @return List
	 * @throws GlobalException 异常
	 */
	List<ContentMark> sort(DragSortDto sorts, Integer markType) throws GlobalException;

	/**
	 * 年号批量录入
	 *
	 * @param dto 年号批量录入Dto
	 * @return list
	 * @throws GlobalException 异常
	 */
	List<ContentMark> entryBatch(ContentMarkBatchDto dto) throws GlobalException;

	/**
	 * 根据标记词种类查询出list集合
	 * 
	 * @Title: getList
	 * @param markType 标记词种类(1机关代字 2年份)
	 * @return: List
	 */
	List<ContentMark> getList(Integer markType);
}
