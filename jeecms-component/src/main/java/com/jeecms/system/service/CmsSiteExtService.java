/**
 ** @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 
 */

package com.jeecms.system.service;

import java.io.IOException;

import com.jeecms.common.base.service.IBaseService;
import com.jeecms.system.domain.CmsSiteExt;

/**
 * 站点扩展Service
* @author ljw
* @version 1.0
* @date 2019-04-11

*/
public interface CmsSiteExtService extends IBaseService<CmsSiteExt, Integer> {

	/**
	 * 创建目录以及复制父站点的目录下的全部文件
	 * 
	 * @Title: saveDir
	 * @param from 来源站点ID
	 * @param to 目标站点ID
	 * @throws IOException 异常
	 */
	void saveDir(Integer from, Integer to) throws IOException;
}
