/**
*@Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.member.service.impl;

import static com.jeecms.member.domain.MemberScoreDetails.COMMENT_SCORE_TYPE;
import static com.jeecms.member.domain.MemberScoreDetails.CONTRIBUTOR_SCORE_TYPE;
import static com.jeecms.member.domain.MemberScoreDetails.MESSAGE_SCORE_TYPE;
import static com.jeecms.member.domain.MemberScoreDetails.REGISTER_SCORE_TYPE;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.jeecms.auth.domain.CoreUser;
import com.jeecms.auth.service.CoreUserService;
import com.jeecms.common.base.service.BaseServiceImpl;
import com.jeecms.common.exception.GlobalException;
import com.jeecms.common.util.MyDateUtils;
import com.jeecms.content.domain.CmsModelItem;
import com.jeecms.content.service.CmsModelService;
import com.jeecms.interact.domain.UserComment;
import com.jeecms.interact.service.UserCommentService;
import com.jeecms.member.dao.MemberScoreDetailsDao;
import com.jeecms.member.domain.MemberAttr;
import com.jeecms.member.domain.MemberLevel;
import com.jeecms.member.domain.MemberScoreDetails;
import com.jeecms.member.domain.UserTotal;
import com.jeecms.member.service.MemberLevelService;
import com.jeecms.member.service.MemberScoreDetailsService;
import com.jeecms.member.service.UserTotalService;
import com.jeecms.system.domain.CmsSite;
import com.jeecms.system.domain.CmsSiteConfig;
import com.jeecms.system.service.CmsSiteService;

/**
 * 会员积分详情Service
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-09-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberScoreDetailsServiceImpl extends BaseServiceImpl<MemberScoreDetails, MemberScoreDetailsDao, Integer>
		implements MemberScoreDetailsService {

	@Autowired
	private CmsSiteService cmsSiteService;
	@Autowired
	private CoreUserService coreUserService;
	@Autowired
	private UserTotalService userTotalService;
	@Autowired
	private MemberLevelService levelService;
	@Autowired
	private CmsModelService cmsModelService;
	@Autowired
	private UserCommentService userCommentService;
	
	/**记录初始化
	 * @throws GlobalException **/
	public UserTotal init(Integer userId) throws GlobalException {
		//判断是否有用户记录
		UserTotal total = userTotalService.findByUserId(userId);
		if (total == null) {
			UserTotal newtotal = new UserTotal(userId, 0, 0);
			//立即刷新
			newtotal = userTotalService.save(newtotal);
			userTotalService.flush();
			return newtotal;
		}
		return total;
	}

	@Override
	public void addMemberScore(int type, Integer userId, Integer siteId, Integer commentId) 
			throws GlobalException {
		Assert.notNull(userId, "UserId must not be null");
		Assert.notNull(siteId, "SiteId must not be null");
		// 得到积分配置相关信息
		CmsSite site = cmsSiteService.findById(siteId);
		CmsSiteConfig config = site.getCmsSiteCfg();
		//得到用户累计数
		UserTotal total = init(userId);
		// 得到用户信息
		CoreUser user = coreUserService.findById(userId);
		switch (type) {
			case CONTRIBUTOR_SCORE_TYPE:
				contributor(config, user, total);
				break;
			case COMMENT_SCORE_TYPE:
				Assert.notNull(commentId, "CommentId must not be null");
				UserComment comment = userCommentService.findById(commentId);
				comment.setCommentFlag(true);
				userCommentService.update(comment);
				comment(config, user, total);
				break;
			case REGISTER_SCORE_TYPE:
				register(config, user);
				break;
			case MESSAGE_SCORE_TYPE:
				message(config, user, total);
				break;
			default:
				break;
		}
	}

	/** 投稿积分 
	 * @throws GlobalException **/
	protected void contributor(CmsSiteConfig config, CoreUser user, UserTotal total) 
			throws GlobalException {
		// 判断是否开启
		if (TRUE_STRING.equals(config.getSubmitStatus())) {
			//累计投稿次数,需要根据次数判断是否得分
			total.setContributorNum(total.getContributorNum() + 1);
			//用户当前积分
			Integer sum = user.getIntegral() != null ? user.getIntegral() : 0;
			//每成功投稿篇数
			Integer success = config.getSubmitSuccessNumber();
			//得到积分
			Integer secore = config.getSubmitSuccessScore();
			//判断是否满足条件
			if (total.getContributorNum() >= success) {
				//得到倍率
				Integer rate = total.getContributorNum() / success;
				//需要减去的数目
				Integer sub = rate * success;
				//判断积分限制是否开启
				if (TRUE_STRING.equals(config.getSubmitScoreLimit())) {
					//判断积分今日是否超过限制
					Integer sumDay = dao.scoreSum(user.getId(), 
							MyDateUtils.getStartDate(new Date()), 
							MyDateUtils.getFinallyDate(new Date()));
					//如果不大于,就可以得分
					if (config.getSubmitOnedayMaxScore() > sumDay) {
						//判断等级
						levelNum(sum + secore * rate, user);
						//新增积分详情记录
						MemberScoreDetails bean = new MemberScoreDetails(user.getId(), 
								CONTRIBUTOR_SCORE_TYPE, secore);
						super.save(bean);
					}
					//去除投稿数据
					total.setContributorNum(total.getContributorNum() - sub);
				} else {
					//判断等级
					levelNum(sum + secore * rate, user);
					//新增积分详情记录
					MemberScoreDetails bean = new MemberScoreDetails(user.getId(), 
							CONTRIBUTOR_SCORE_TYPE, secore);
					super.save(bean);
					//去除投稿数据
					total.setContributorNum(total.getContributorNum() - sub);
				}
			}
		}
		userTotalService.update(total);
	}

	/** 评论积分 
	 * @throws GlobalException **/
	protected void comment(CmsSiteConfig config, CoreUser user, UserTotal total) 
			throws GlobalException {
		// 判断是否开启
		if (TRUE_STRING.equals(config.getCommentStatus())) {
			//累计评论次数,需要根据次数判断是否得分
			total.setCommentNum(total.getCommentNum() + 1);
			Integer sum = user.getIntegral() != null ? user.getIntegral() : 0;
			//每成功投稿篇数
			Integer success = config.getCommentSuccessNumber();
			//得到积分
			Integer secore = config.getCommentSuccessScore();
			//判断是否满足条件
			if (total.getCommentNum() >= success) {
				//得到倍率
				Integer rate = total.getCommentNum() / success;
				//需要减去的数目
				Integer sub = rate * success;
				//判断积分限制是否开启
				if (TRUE_STRING.equals(config.getCommentScoreLimit())) {
					//判断积分今日是否超过限制
					Integer sumDay = dao.scoreSum(user.getId(), 
							MyDateUtils.getStartDate(new Date()), 
							MyDateUtils.getFinallyDate(new Date()));
					//如果不大于,就可以得分
					if (config.getCommentOnedayMaxScore() > sumDay) {
						//判断等级
						levelNum(sum + secore * rate, user);
						//新增积分详情记录
						MemberScoreDetails bean = new MemberScoreDetails(user.getId(), 
								COMMENT_SCORE_TYPE, secore);
						super.save(bean);
					}
					//去除评论数据
					total.setCommentNum(total.getCommentNum() - sub);
				} else {
					//判断等级
					levelNum(sum + secore * rate, user);
					//新增积分详情记录
					MemberScoreDetails bean = new MemberScoreDetails(user.getId(), 
							COMMENT_SCORE_TYPE, secore);
					super.save(bean);
					//去除评论数据
					total.setCommentNum(total.getCommentNum() - sub);
				}
			}
		}
		userTotalService.update(total);
	}

	/** 注册积分 
	 * @throws GlobalException **/
	protected void register(CmsSiteConfig config, CoreUser user) throws GlobalException {
		// 判断是否开启
		if (TRUE_STRING.equals(config.getRegisterStatus())) {
			Integer sum = user.getIntegral() != null ? user.getIntegral() : 0;
			Integer result = sum + config.getRegisterSuccessScore();
			levelNum(result, user);
			//新增积分详情记录
			MemberScoreDetails bean = new MemberScoreDetails(user.getId(), 
					REGISTER_SCORE_TYPE, result);
			super.save(bean);
		}
	}

	/** 完善信息积分 
	 * @throws GlobalException **/
	protected void message(CmsSiteConfig config, CoreUser user, UserTotal total) throws GlobalException {
		// 判断是否开启
		if (TRUE_STRING.equals(config.getPerfectMessageStatus())) {
			Integer sum = user.getIntegral() != null ? user.getIntegral() : 0;
			//判断用户信息是否完善
			if (perfect(user) && !total.getUserPerfect()) {
				Integer result = sum + config.getPerfectMessageSuccessScore();
				levelNum(result, user);
				total.setUserPerfect(true);
				userTotalService.update(total);
				//新增积分详情记录
				MemberScoreDetails bean = new MemberScoreDetails(user.getId(), 
						MESSAGE_SCORE_TYPE, result);
				super.save(bean);
			}
		}

	}
	
	/**判断会员字段是否有值
	 * 包含邮箱，手机号，个性签名
	 * @throws GlobalException **/
	protected Boolean perfect(CoreUser user) throws GlobalException {
		//得到会员模型
		Set<CmsModelItem> modelItems = cmsModelService.getInfo(null).getItems();
		//过滤会员组，会员等级，积分，密码，状态
		Boolean flag = StringUtils.isNotBlank(user.getEmail()) 
				&& StringUtils.isNotBlank(user.getUserExt().getRealname())
				&& StringUtils.isNotBlank(user.getTelephone());
		//判断自定义字段
		Boolean customFlag = true;
		Set<CmsModelItem> customModelItems = modelItems.stream().filter(modelItem -> modelItem.getIsCustom())
				.collect(Collectors.toSet());
		//得到会员自定义信息
		List<MemberAttr> attrs = user.getMemberAttrs();
		for (CmsModelItem cmsModelItem : customModelItems) {
			//得到字段名
			String filed = cmsModelItem.getField();
			if (!attrs.isEmpty()) {
				for (MemberAttr memberAttr : attrs) {
					//判断字段名是否相同
					if (memberAttr.getAttrName().equals(filed)) {
						if (!StringUtils.isNotEmpty(memberAttr.getAttrValue())) {
							return customFlag = false;
						}
					}
				}
			}
		}
		return flag && customFlag;
	}
	
	/**判断分数属于哪一会员等级
	 * @throws GlobalException **/
	protected void levelNum(Integer secore, CoreUser user) throws GlobalException {
		user.setIntegral(secore);
		MemberLevel level = levelService.findBySecore(secore);
		if (level != null) {
			user.setLevelId(level.getId());
			user.setUserLevel(level);
		}
		coreUserService.update(user);
		coreUserService.flush();
	}

	public static final String TRUE_STRING = "1";
	public static final String FALSE_STRING = "0";
}