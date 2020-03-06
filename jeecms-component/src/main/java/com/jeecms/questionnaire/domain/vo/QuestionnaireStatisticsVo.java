/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/28 19:25
 */
public class QuestionnaireStatisticsVo {


	/**
	 * number : 3
	 * subjects : [{"isMust":true,"options":[{"rate":100,"name":"选项2"}],"index":5,"id":224,"title":"5.级联题","type":5},{"isMust":true,"options":[{"rate":0,"name":"选项1"},{"rate":100,"name":"选项2"}],"index":1,"id":220,"title":"1.单选题","type":1},{"isMust":true,"options":[{"rate":66.67,"name":"选项1"},{"rate":33.34,"name":"选项2"}],"index":2,"id":221,"title":"2.多选题","type":2},{"isMust":true,"options":[],"index":3,"id":222,"title":"3.问答题","type":3},{"isMust":true,"options":[{"rate":33.34,"name":"选项1"},{"rate":66.67,"name":"选项2"}],"index":4,"id":223,"title":"4.下拉题","type":4}]
	 * title : 这121212
	 */

	private Integer number;
	private String title;
	private List<SubjectsBean> subjects;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<SubjectsBean> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<SubjectsBean> subjects) {
		this.subjects = subjects;
	}

	public static class SubjectsBean {
		/**
		 * isMust : true
		 * options : [{"rate":100,"name":"选项2"}]
		 * index : 5
		 * id : 224
		 * title : 5.级联题
		 * type : 5
		 */

		private boolean isMust;
		private Integer index;
		private Integer id;
		private String title;
		private Short type;
		private List<OptionsBean> options;

		public boolean isIsMust() {
			return isMust;
		}

		public void setIsMust(boolean isMust) {
			this.isMust = isMust;
		}

		public Integer getIndex() {
			return index;
		}

		public void setIndex(Integer index) {
			this.index = index;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Short getType() {
			return type;
		}

		public void setType(Short type) {
			this.type = type;
		}

		public List<OptionsBean> getOptions() {
			return options;
		}

		public void setOptions(List<OptionsBean> options) {
			this.options = options;
		}

		public static class OptionsBean {
			/**
			 * rate : 100
			 * name : 选项2
			 */

			private BigDecimal rate;
			private String name;

			public BigDecimal getRate() {
				return rate;
			}

			public void setRate(BigDecimal rate) {
				this.rate = rate;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}
		}
	}
}
