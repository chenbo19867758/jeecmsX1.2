/**
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.system.service;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.system.domain.SysUserSecret;
import com.jeecms.system.domain.dto.UserSecretDto;

/**
 * 人员密级Service
 *
 * @author xiaohui
 * @version 1.0
 * @date 2019-04-25
 */
public interface SysUserSecretService extends IBaseService<SysUserSecret, Integer> {

	/**
	 * 添加人员密级信息
	 *
	 * @param userSecretDto 人员密级dto
	 * @return SysUserSecret
	 * @throws GlobalException 异常
	 */
	SysUserSecret save(UserSecretDto userSecretDto) throws GlobalException;

	/**
	 * 修改人员密级信息
	 *
	 * @param userSecretDto 人员密级dto
	 * @return SysUserSecret
	 * @throws GlobalException 异常
	 */
	SysUserSecret update(UserSecretDto userSecretDto) throws GlobalException;

	/**
	 * 校验密集名称是否可用
	 *
	 * @param name 密级名称
	 * @param id   密级id
	 * @return boolean true 可用 false 不可用
	 */
	boolean checkByName(String name, Integer id);
}
