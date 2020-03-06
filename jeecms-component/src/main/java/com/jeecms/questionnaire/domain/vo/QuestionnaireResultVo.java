/*
 * @Copyright: 江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain.vo;

import java.util.List;

/**
 * @author xiaohui
 * @version 1.0
 * @date 2019/11/8 17:40
 */
public class QuestionnaireResultVo {

	/**
	 * number : 1
	 * subjects : [{"options":[{"rate":100,"name":"选项一"}],"index":null,"id":450,"title":"单选题","type":1}]
	 * title : 这是复制的问卷
	 */

	private int number;
	private String title;
	private List<SubjectsBean> subjects;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
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
		 * options : [{"rate":100,"name":"选项一"}]
		 * index : null
		 * id : 450
		 * title : 单选题
		 * type : 1
		 */

		private Object index;
		private int id;
		private String title;
		private int type;
		private List<OptionsBean> options;

		public Object getIndex() {
			return index;
		}

		public void setIndex(Object index) {
			this.index = index;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
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
			 * name : 选项一
			 */

			private int rate;
			private String name;

			public int getRate() {
				return rate;
			}

			public void setRate(int rate) {
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
