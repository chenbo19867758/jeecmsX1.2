/**   
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.content.constants;

import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 内容常量
 * 
 * @author: ljw
 * @date: 2019年5月16日 上午9:40:07
 */
public class ContentConstant {

	/**
	 * 数据类型-1 内容
	 */
	public final static Short WORKFLOW_DATA_TYPE_CONTENT = 1;

	/** 允许游客评论 */
	public static final int COMMENT_ALLOW_VISITORS = 1;
	/** 登录后评论 */
	public static final int COMMENT_AFTER_LOGIN = 2;
	/** 不允许评论 */
	public static final int COMMENT_NOT_ALLOWED = 3;

	/** 内容状态名称 */
	public static final String CONTENT_STATUS_NAME = "status";
	/** 内容类型关联的集合 */
	public static final String CONTENT_TYPES_NAME = "contentTypes";

	/** 内容默认的标题颜色 */
	public static final String TITLE_DEFAULT_COLOR = "#666666";

	/** 搜索关键字类型1.标题2.作者3.来源4.描述5.创建人 **/
	public static final int KEY_TYPE_TITLE = 1;
	public static final int KEY_TYPE_AUTHOR = 2;
	public static final int KEY_TYPE_SOURCE = 3;
	public static final int KEY_TYPE_DESCRIBE = 4;
	public static final int KEY_TYPE_USER = 5;

	/** 排序类型 **/
	/**
	 * 相关度排序
	 */
	public static final int ORDER_TYPE_RELATE = 0;
	/** 创建时间降序 **/
	public static final int ORDER_TYPE_CREATETIME_DESC = 1;
	/** 创建时间升序 **/
	public static final int ORDER_TYPE_CREATETIME_ASC = 2;

	/** 总访问数降序 **/
	public static final int ORDER_TYPE_VIEWS_DESC = 3;
	/** 总访问数升序 **/
	public static final int ORDER_TYPE_VIEWS_ASC = 4;
	/** 月访问数降序 **/
	public static final int ORDER_TYPE_VIEWS_MONTH_DESC = 5;
	/** 月访问数升序 **/
	public static final int ORDER_TYPE_VIEWS_MONTH_ASC = 6;
	/** 周访问数降序 **/
	public static final int ORDER_TYPE_VIEWS_WEEK_DESC = 7;
	/** 周访问数升序 **/
	public static final int ORDER_TYPE_VIEWS_WEEK_ASC = 8;
	/** 日访问数降序 **/
	public static final int ORDER_TYPE_VIEWS_DAY_DESC = 9;
	/** 日访问数升序 **/
	public static final int ORDER_TYPE_VIEWS_DAY_ASC = 10;

	/** 总评论数降序 **/
	public static final int ORDER_TYPE_COMMENTS_DESC = 11;
	/** 总评论数升序 **/
	public static final int ORDER_TYPE_COMMENTS_ASC = 12;
	/** 月评论数降序 **/
	public static final int ORDER_TYPE_COMMENTS_MONTH_DESC = 13;
	/** 月评论数升序 **/
	public static final int ORDER_TYPE_COMMENTS_MONTH_ASC = 14;
	/** 周评论数降序 **/
	public static final int ORDER_TYPE_COMMENTS_WEEK_DESC = 15;
	/** 周评论数升序 **/
	public static final int ORDER_TYPE_COMMENTS_WEEK_ASC = 16;
	/** 日评论数降序 **/
	public static final int ORDER_TYPE_COMMENTS_DAY_DESC = 17;
	/** 日评论数升序 **/
	public static final int ORDER_TYPE_COMMENTS_DAY_ASC = 18;

	/** 总点赞数降序 **/
	public static final int ORDER_TYPE_UPS_DESC = 19;
	/** 总点赞数升序 **/
	public static final int ORDER_TYPE_UPS_ASC = 20;
	/** 月点赞数降序 **/
	public static final int ORDER_TYPE_UPS_MONTH_DESC = 21;
	/** 月点赞数升序 **/
	public static final int ORDER_TYPE_UPS_MONTH_ASC = 22;
	/** 周点赞数降序 **/
	public static final int ORDER_TYPE_UPS_WEEK_DESC = 23;
	/** 周点赞数升序 **/
	public static final int ORDER_TYPE_UPS_WEEK_ASC = 24;
	/** 日点赞数降序 **/
	public static final int ORDER_TYPE_UPS_DAY_DESC = 25;
	/** 日点赞数升序 **/
	public static final int ORDER_TYPE_UPS_DAY_ASC = 26;
	/** 发布时间降序 **/
	public static final int ORDER_TYPE_RELEASE_TIME_DESC = 27;
	/** 发布时间升序 **/
	public static final int ORDER_TYPE_RELEASE_TIME_ASC = 28;
	/** 置顶降序 */
	public static final int ORDER_TYPE_SORT_NUM_DESC = 29;
	/** id升序 */
	public static final int ORDER_TYPE_ID_ASC = 30;
	/** id降序 */
	public static final int ORDER_TYPE_ID_DESC = 31;
	/** 日下载降序 */
	public static final int ORDER_TYPE_DAY_DOWNLOAD_DESC = 32;
	/** 周下载降序 */
	public static final int ORDER_TYPE_WEEK_DOWNLOAD_DESC = 33;
	/** 月下载降序 */
	public static final int ORDER_TYPE_MONTH_DOWNLOAD_DESC = 34;
	/** 总下载降序 */
	public static final int ORDER_TYPE_DOWNLOAD_DESC = 35;
	/** 流转到期时间升序 **/
	public static final int ORDER_TYPE_FLOW_AUTO_REVOKETIME_ASC = 29;
	/** 修改时间倒序用于：作为审核时间倒序*/
	public static final int ORDER_TYPE_UPDATETIME_DESC = 36;

	/** 排序 **/
	public static Map<String, String> order() {
		Map<String, String> a = ImmutableMap.of("0", "默认排序", "27", "发布时间降序", "28", "发布时间升序", "1", "创建时间降序", "2",
				"创建时间升序");
		Map<String, String> b = ImmutableMap.of("3", "总访问数降序", "4", "总访问数升序", "5", "月访问数降序", "6", "月访问数升序", "7",
				"周访问数降序");
		Map<String, String> c = ImmutableMap.of("8", "周访问数升序", "9", "日访问数降序", "10", "日访问数升序", "11", "总评论数降序", "12",
				"总评论数升序");
		Map<String, String> d = ImmutableMap.of("13", "月评论数降序", "14", "月评论数升序", "15", "周评论数降序", "16", "周评论数升序");
		Map<String, String> e = ImmutableMap.of("17", "日评论数降序", "18", "日评论数升序", "19", "总点赞数降序", "20", "总点赞数升序");
		Map<String, String> f = ImmutableMap.of("21", "月点赞数降序", "22", "月点赞数升序", "23", "周点赞数降序", "24", "周点赞数升序");
		Map<String, String> g = ImmutableMap.of("25", "日点赞数降序", "26", "日点赞数升序");
		Map<String, String> map = new LinkedHashMap<>(29);
		map.putAll(a);
		map.putAll(b);
		map.putAll(c);
		map.putAll(d);
		map.putAll(e);
		map.putAll(f);
		map.putAll(g);
		return map;
	}

	/** 待审核 */
	public static final int CONTRIBUTE_PENDING_REVIEW = 1;
	/** 暂存 */
	public static final int CONTRIBUTE_TEMPORARY_STORAGE = 2;
	/** 已发布 */
	public static final int CONTRIBUTE_RELEASE = 3;

	/**
	 * 内容状态(1:草稿; 2-初稿 3:流转中; 4:已审核; 5:已发布; 6:驳回; 7:下线 8-归档; 9:暂存; 10:智能审核中; 11:审核成功; 12:审核失败)
	 **/
	public static final int STATUS_DRAFT = 1;
	public static final int STATUS_FIRST_DRAFT = 2;
	public static final int STATUS_FLOWABLE = 3;
	public static final int STATUS_WAIT_PUBLISH = 4;
	public static final int STATUS_PUBLISH = 5;
	public static final int STATUS_BACK = 6;
	public static final int STATUS_NOSHOWING = 7;
	public static final int STATUS_PIGEONHOLE = 8;
	public static final int STATUS_TEMPORARY_STORAGE = 9;
	public static final int STATUS_SMART_AUDIT = 10;
	public static final int STATUS_SMART_AUDIT_SUCCESS = 11;
	public static final int STATUS_SMART_AUDIT_FAILURE = 12;
	
	/**
	 * 不允许重复的状态（暂不含归档）
	 * 
	 * @Title: getNotAllowRepeatStatus
	 * @return: List
	 */
	public static List<Integer> getNotAllowRepeatStatus() {
		return Arrays.asList(STATUS_DRAFT, STATUS_FIRST_DRAFT, STATUS_FLOWABLE, STATUS_WAIT_PUBLISH, STATUS_PUBLISH,
				STATUS_BACK, STATUS_NOSHOWING, STATUS_TEMPORARY_STORAGE);
	}

	/** 得到状态值 **/
	public static String status(Integer status) {
		switch (status) {
		case STATUS_DRAFT:
			return "草稿";
		case STATUS_FIRST_DRAFT:
			return "初稿";
		case STATUS_FLOWABLE:
			return "流转中";
		case STATUS_WAIT_PUBLISH:
			return "待发布";
		case STATUS_PUBLISH:
			return "已发布";
		case STATUS_BACK:
			return "驳回";
		case STATUS_NOSHOWING:
			return "下线";
		case STATUS_PIGEONHOLE:
			return "归档";
		case STATUS_TEMPORARY_STORAGE:
			return "暂存";
		default:
			break;
		}
		return " ";
	}

	/** 创建方式（1:直接创建 2:投稿 3:站群推送 4:站群采集 5:复制 6:链接型引用 7:镜像型引用 8 外部采集） **/
	public static final int CONTENT_CREATE_TYPE_ADD = 1;
	public static final int CONTENT_CREATE_TYPE_CONTRIBUTE = 2;
	public static final int CONTENT_CREATE_TYPE_SITE_PUSH = 3;
	public static final int CONTENT_CREATE_TYPE_SITE_COLLECT = 4;
	public static final int CONTENT_CREATE_TYPE_COPY = 5;
	public static final int CONTENT_CREATE_TYPE_URL = 6;
	public static final int CONTENT_CREATE_TYPE_MIRROR = 7;
	public static final int CONTENT_CREATE_TYPE_COLLECT = 8;

	/** 内容操作权限枚举 **/
	public enum ContentOperation {
		// 新增
		ADD,
		// 修改
		UPDATE,
		// 排序
		SORT,
		// 内容类型
		CONTENTTYPE,
		// 发布
		PUBLISH,
		// 下线
		OFFLINE,
		// 删除
		DELETE,
		// 预览
		PREVIEW,
		// 浏览
		VIEW,
		// 移动
		MOVE,
		// 置顶
		STICK,
		// 复制
		COPY,
		// 引用
		QUOTE,
		// 推送到站群
		SITE,
		// 推送到微信
		WECHAT,
		// 推送到微博
		SINA,
		// 归档
		PIGEONHOLE;
	}

	/**
	 * 发布平台-PC
	 */
	public static final String CONTENT_RELEASE_TERRACE_PC = "releasePc";
	public static final Integer CONTENT_RELEASE_TERRACE_PC_NUMBER = 1;
	/**
	 * 发布平台-WAP
	 */
	public static final String CONTENT_RELEASE_TERRACE_WAP = "releaseWap";
	public static final Integer CONTENT_RELEASE_TERRACE_WAP_NUMBER = 2;
	/**
	 * 发布平台-APP
	 */
	public static final String CONTENT_RELEASE_TERRACE_APP = "releaseApp";
	public static final Integer CONTENT_RELEASE_TERRACE_APP_NUMBER = 3;
	/**
	 * 发布平台-小程序
	 */
	public static final String CONTENT_RELEASE_TERRACE_MINIPROGRAM = "releaseMiniprogram";
	public static final Integer CONTENT_RELEASE_TERRACE_MINIPROGRAM_NUMBER = 4;

	/** 标题 */
	public static final String ITEM_TITLE_NAME = "title";
	/** 文章链接 */
	public static final String ITEM_OUT_LINK_NAME = "outLink";
	/** 时间 */
	public static final String ITEM_RELEASE_TIME_NAME = "releaseTime";
	/** 来源 */
	public static final String ITEM_CONTENT_SOURCE_NAME = "contentSourrceId";
	/** 内容 */
	public static final String ITEM_CONTXT_NAME = "contxt";
	/** 描述 */
	public static final String ITEM_DESCRIPTION_NAME = "desctiption";
	/** 图片 */
	public static final String ITEM_RESOURCE_NAME = "resource";
	/** 转发数 */
	public static final String ITEM_FORWARD_NAME = "forward";
	/** 评论数 */
	public static final String ITEM_REPEAT_NAME = "repeat";
	/** 点赞数 */
	public static final String ITEM_PRAISED_NAME = "praised";

	/** 搜索词位置 **/
	public enum SearchPosition {
		/**
		 * 标题
		 */
		title,
		/**
		 * 正文
		 */
		txt,
		/**
		 * 标题和正文
		 */
		titleAndTxt
	}

	/**
	 * 关键词位置
	 * 
	 * @author: tom
	 * @date: 2019年6月4日 上午11:50:34
	 */
	public enum SearchKeyCondition {
		/**
		 * 包含以下任意一个关键词
		 */
		any,
		/**
		 * 包含以下全部关键词
		 */
		all,
		/**
		 * 不包含以下关键词
		 */
		notInclude
	}

	/**
	 * 发布时间阶段
	 * 
	 * @author: tom
	 * @date: 2019年6月4日 上午11:51:36
	 */
	public enum ReleaseTimeStage {
		/**
		 * 一天内
		 */
		oneDay,
		/**
		 * 一周内
		 */
		oneWeek,
		/**
		 * 一月内
		 */
		oneMonth,
		/**
		 * 一年内
		 */
		oneYear,
		/**
		 * 指定时间范围段
		 */
		timeRage,
	}

	/**
	 * 栏目静态化操作
	 * 
	 * @author: tom
	 * @date: 2019年6月11日 下午6:19:20
	 */
	public enum ChannelStaticOperator {
		/**
		 * 生成栏目静态页
		 */
		channel,
		/**
		 * 生成子栏目静态页
		 */
		channelChild,
		/**
		 * 生成内容静态页
		 */
		channelContent;

		private static Map<String, ChannelStaticOperator> types = new HashMap<String, ChannelStaticOperator>(3);

		static {
			types.put(channel.name(), channel);
			types.put(channelChild.name(), channelChild);
			types.put(channelContent.name(), channelContent);
		}

		public static ChannelStaticOperator getValueOf(final String name) {
			ChannelStaticOperator rt = types.get(name);
			return rt;
		}
	}

	/**
	 * 发布类型-资源上传
	 */
	public static final Short DISTRIBUTE_TYPE_UPLOAD = 1;
	/**
	 * 发布类型-静态页面
	 */
	public static final Short DISTRIBUTE_TYPE_HTML = 2;
	/**
	 * 生成静态页面任务session Key 存放当前生成栏目页进度对象
	 */
	public static final String STATIC_CURR_CHANNEL_COUNT_KEY = "staticCurrChannelCount";
	/**
	 * 生成静态页面任务session Key 存放当前生成内容页进度对象
	 */
	public static final String STATIC_CURR_CONTENT_COUNT_KEY = "staticCurrContentCount";

	/**
	 * 生成静态页面任务session Key 存放当前生成各个栏目页进度对象
	 */
	public static final String STATIC_EACH_CHANNEL_COUNT_KEY = "staticEachChannelCount";

	/**
	 * 访问量
	 */
	public static final String CONTENT_NUM_TYPE_VIEWS = "views";

	public static final String CONTENT_NUM_TYPE_DAY_VIEWS = "dayViews";
	/**
	 * 评论量
	 */
	public static final String CONTENT_NUM_TYPE_COMMENTS = "comments";

	public static final String CONTENT_NUM_TYPE_DAY_COMMENTS = "dayComments";
	/**
	 * 下载量
	 */
	public static final String CONTENT_NUM_TYPE_DOWNLOADS = "downloads";

	public static final String CONTENT_NUM_TYPE_DAY_DOWNLOADS = "dayDownloads";
	/**
	 * 点赞数
	 */
	public static final String CONTENT_NUM_TYPE_UPS = "ups";

	public static final String CONTENT_NUM_TYPE_DAY_UPS = "dayUps";

	/**
	 * 点踩数
	 */
	public static final String CONTENT_NUM_TYPE_DOWNS = "downs";

	public static final String CONTENT_NUM_TYPE_DAY_DOWNS = "dayDowns";

	public static final String CONTENT_NUM_TYPE_END_TIME = "endTime";

	/** 导入方式，直接导入（不做任何处理） **/
	public static final Integer IMPORT_TYPE_1 = 1;
	/** 导入方式，清除格式 **/
	public static final Integer IMPORT_TYPE_2 = 2;
	/** 导入方式，仅导入文字 **/
	public static final Integer IMPORT_TYPE_3 = 3;
	
	/**
	 * 内容审核的字段或数据类型枚举
	 * @author CM
	 */
	public enum ContentCheckFieldAndDataType {
		/**
		 * 文本审核
		 */
		txt,
		/**
		 * 图片审核
		 */
		img,
		/**
		 * 视频审核
		 */
		video,
		/**
		 * 文本+图片审核
		 */
		txtAndImg,
		/**
		 * 图片+视频审核
		 */
		imgAndVideo,
		/**
		 * 文本+视频审核
		 */
		txtAndVideo,
		/**
		 * 文本+图片+视频审核
		 */
		txtAndImgAndVideo;
	}
	
}
