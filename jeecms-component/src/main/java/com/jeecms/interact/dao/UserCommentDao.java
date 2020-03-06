/*   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */  

package com.jeecms.interact.dao;

import java.util.List;

import com.jeecms.common.base.dao.IBaseDao;
import com.jeecms.interact.dao.ext.UserCommentDaoExt;
import com.jeecms.interact.domain.UserComment;

/**
 * 用户评论实体类dao接口
 * @author: chenming
 * @date:   2019年5月6日 上午9:01:31
 */
public interface UserCommentDao extends IBaseDao<UserComment, Integer>,UserCommentDaoExt {
	
	/**
	 * 根据userId进行精准查询
	 * @Title: findByUserId  
	 * @param userId	用户Id
	 * @return: List
	 */
	List<UserComment> findByUserIdAndHasDeleted(Integer userId,Boolean hasDeleted);
	
	/**
	 * 根据Ip进行精准查询
	 * @Title: findByIp  
	 * @param ip	用户ip
	 * @return: List
	 */
	List<UserComment> findByIpAndHasDeleted(String ip,Boolean hasDeleted);
	
	/**
	 * 通过父级id节点集合进行检索查询
	 * @Title: findByParentIdInAndHasDeleted  
	 * @param ids			父级id节点集合
	 * @param hasDeleted	是否删除
	 * @return: List
	 */
	List<UserComment> findByParentIdInAndHasDeleted(List<Integer> ids,Boolean hasDeleted);
	
	/**
	 * 得到该评论下的全部回复
	 * @Title: findByReplyCommentIdInAndHasDeleted
	 * @param ids        评论ids
	 * @param hasDeleted 是否删除
	 * @return
	 */
	List<UserComment> findByReplyCommentIdInAndHasDeleted(List<Integer> ids, Boolean hasDeleted);
	
	/**
	 * 得到 地址为空、IP不为空而且未删除 的评论
	 * @Title: findByVisitorAreaIsNullAndIpIsNotNullAndHasDeleted  
	 * @param hasDeleted
	 * @return: List
	 */
	List<UserComment> findByVisitorAreaIsNullAndIpIsNotNullAndHasDeleted(Boolean hasDeleted);
}
