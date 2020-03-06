/**
 * * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。 JpaRepository Repository
 */

package com.jeecms.wechat.dao.ext;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jeecms.wechat.domain.WechatFans;
import com.jeecms.wechat.domain.vo.WechatFansVO;

/** 微信粉丝DAO扩展
 * @Description:WechatFansDaoExt
 * @author: ljw
 * @date: 2019年06月03日 
 */
public interface WechatFansDaoExt {

	/**
	 * 根据nickname, tagid 查询粉丝列表
	 * @Title: selectFans  
	 * @param appids 公众号IDs
	 * @param nickname 昵称
	 * @param tagid 标签ID
	 * @param black 是否黑名单
	 * @param pageable 分页参数
	 * @return: Page
	 */
	Page<WechatFans> selectFans(List<String> appids, String nickname, String tagid, Boolean black, 
			Pageable pageable);

	/** 
	 * 根据分组查询
	* @Title: fansVOs 
	* @param appids 公众号APPIDS
	* @param type 分组类型
	* @param province 省级名称
	* @return  
	*/ 
	List<WechatFansVO> fansVOs(List<String> appids, Integer type, String province);
}
