/**
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
package com.jeecms.common.constants;

/**
 * 邮箱、手机、推送模版内容
 * @author: tom
 * @date:   2018年12月24日 下午6:10:18     
 */
public enum MessageTempleEnum {
	
		LOG_WARN_TEMP("日志预警通知" , "%s下数据库%s中日志表已超预警阀值%sM，请及时关注使用情况！"),
		LOG_DANGER_TEMP("日志告警通知" , "%s下数据库%s中日志表已超警戒阀值%sM，请及时清理！");
		
		private String title;
		private String content;
		
		private MessageTempleEnum(String title, String content) {
			this.title = title;
			this.content = content;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		
}
