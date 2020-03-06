/*
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

package com.jeecms.questionnaire.domain;

import com.jeecms.common.base.domain.AbstractDomain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author xiaohui
 * @version 1.0
 * @date 2019-10-18
 */
@Entity
@Table(name = "jc_sys_vote_subject")
public class SysQuestionnaireSubject extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String UNIT_KB = "kb";
	public static final String UNIT_MB = "mb";

	public static final Integer PROHIBIT = 1;
	public static final Integer ALLOW = 2;

	private Integer id;
	/**
	 * 问卷id
	 */
	private Integer questionnaireId;
	/**
	 * 题目标题
	 */
	private String title;
	/**
	 * 题目类型（1单选2多选3填空4下拉5级联6附件）
	 */
	private Short type;
	/**
	 * 是否必答
	 */
	private Boolean isAnswer = false;
	/**
	 * 提示
	 */
	private String prompt;
	/**
	 * 每行列数
	 */
	private Integer column;
	/**
	 * 是否开启限制条件
	 */
	private Boolean isLimit;
	/**
	 * 限制条件
	 */
	private Integer limitCondition;
	/**
	 * 级联题选项json
	 */
	private String cascadeOption;
	/**
	 * 字数限制
	 */
	private Integer wordLimit;
	/**
	 * 是否开启字数限制
	 */
	private Boolean inputNumLimit;
	/**
	 * 输入框高度
	 */
	private Integer inputHigh;
	/**
	 * 输入框宽度
	 */
	private Integer inputWidth;
	/**
	 * 是否开启文件大小限制
	 */
	private Boolean isFileSize;
	/**
	 * 文件大小限制
	 */
	private Integer fileSizeLimit;
	/**
	 * 文件大小限制单位
	 */
	private String fileSizeLimitUnit;
	/**
	 * 是否开启文件数量限制
	 */
	private Boolean isFileCount;
	/**
	 * 文件数量限制
	 */
	private Integer fileCountLimit;
	/**
	 * 是否开启文件类型限制
	 */
	private Boolean isFileType;
	/**
	 * 设置允许0 设置禁止 1
	 */
	private Integer isAllowFile;
	/**
	 * 限制类型，多个用逗号隔开
	 */
	private String fileTypeLimit;
	/**
	 * true 单选 false 多选 默认单选
	 */
	private Integer isRadio = 1;
	/**
	 * 选项
	 */
	private List<SysQuestionnaireSubjectOption> options = new ArrayList<SysQuestionnaireSubjectOption>();

	/**
	 * 投票问卷
	 */
	private SysQuestionnaire questionnaire;

	/**
	 * 是否可拖动
	 */
	private Boolean dragable;
	/**
	 * 编辑类型
	 */
	private String editor;
	/**
	 * 题目左侧排序
	 */
	private Integer groupIndex;
	/**
	 * 前端使用字段
	 */
	private String icon;
	/**
	 * index
	 */
	private Integer index;
	/**
	 * 是否自定义字段
	 */
	private Boolean custom;
	/**
	 * 题目类型
	 */
	private String placeholder;
	/**
	 * 预览类型
	 */
	private String preview;
	/**
	 * value
	 */
	private String value;

	public SysQuestionnaireSubject() {
	}

	public SysQuestionnaireSubject(Integer questionnaireId, String title, Short type, Boolean isAnswer, String prompt, SysQuestionnaire questionnaire, Boolean dragable, String editor, Integer groupIndex, String icon, Integer index, Boolean custom, String placeholder, String preview, String value) {
		this.questionnaireId = questionnaireId;
		this.title = title;
		this.type = type;
		this.isAnswer = isAnswer;
		this.prompt = prompt;
		this.questionnaire = questionnaire;
		this.dragable = dragable;
		this.editor = editor;
		this.groupIndex = groupIndex;
		this.icon = icon;
		this.index = index;
		this.custom = custom;
		this.placeholder = placeholder;
		this.preview = preview;
		this.value = value;
	}

	public SysQuestionnaireSubject(Boolean isFileSize, Integer fileSizeLimit, String fileSizeLimitUnit, Boolean isFileCount, Integer fileCountLimit, Boolean isFileType, Integer isAllowFile, String fileTypeLimit) {
		this.isFileSize = isFileSize;
		this.fileSizeLimit = fileSizeLimit;
		this.fileSizeLimitUnit = fileSizeLimitUnit;
		this.isFileCount = isFileCount;
		this.fileCountLimit = fileCountLimit;
		this.isFileType = isFileType;
		this.isAllowFile = isAllowFile;
		this.fileTypeLimit = fileTypeLimit;
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_sys_vote_subject", pkColumnValue = "jc_sys_vote_subject", initialValue = 0, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_sys_vote_subject")
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "questionnaire_id", nullable = true, length = 11)
	public Integer getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Integer questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	@Column(name = "title", nullable = false, length = 150)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "type", nullable = false, length = 1)
	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	@Column(name = "is_answer", nullable = false, length = 1)
	public Boolean getIsAnswer() {
		return isAnswer;
	}

	public void setIsAnswer(Boolean isAnswer) {
		this.isAnswer = isAnswer;
	}

	@Column(name = "prompt", nullable = true, length = 255)
	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	@Column(name = "columns_per_row", nullable = true, length = 2)
	public Integer getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	@Column(name = "is_limit", nullable = true, length = 1)
	public Boolean getIsLimit() {
		return isLimit;
	}

	public void setIsLimit(Boolean isLimit) {
		this.isLimit = isLimit;
	}

	@Column(name = "limit_condition", nullable = true, length = 3)
	public Integer getLimitCondition() {
		return limitCondition;
	}

	public void setLimitCondition(Integer limitCondition) {
		this.limitCondition = limitCondition;
	}

	@Column(name = "cascade_option", nullable = true, length = 715827882)
	public String getCascadeOption() {
		return cascadeOption;
	}

	public void setCascadeOption(String cascadeOption) {
		this.cascadeOption = cascadeOption;
	}

	@Column(name = "word_limit", nullable = true, length = 6)
	public Integer getWordLimit() {
		return wordLimit;
	}

	public void setWordLimit(Integer wordLimit) {
		this.wordLimit = wordLimit;
	}

	@Column(name = "input_num_limit", nullable = true, length = 1)
	public Boolean getInputNumLimit() {
		return inputNumLimit;
	}

	public void setInputNumLimit(Boolean inputNumLimit) {
		this.inputNumLimit = inputNumLimit;
	}

	@Column(name = "input_high", nullable = true, length = 6)
	public Integer getInputHigh() {
		return inputHigh;
	}

	public void setInputHigh(Integer inputHigh) {
		this.inputHigh = inputHigh;
	}

	@Column(name = "input_width", nullable = true, length = 11)
	public Integer getInputWidth() {
		return inputWidth;
	}

	public void setInputWidth(Integer inputWidth) {
		this.inputWidth = inputWidth;
	}

	@Column(name = "is_file_size", nullable = true, length = 1)
	public Boolean getFileSize() {
		return isFileSize;
	}

	public void setFileSize(Boolean fileSize) {
		isFileSize = fileSize;
	}

	@Column(name = "file_size_limit", nullable = true, length = 6)
	public Integer getFileSizeLimit() {
		return fileSizeLimit;
	}

	public void setFileSizeLimit(Integer fileSizeLimit) {
		this.fileSizeLimit = fileSizeLimit;
	}

	@Column(name = "file_size_limit_unit", nullable = true, length = 6)
	public String getFileSizeLimitUnit() {
		return fileSizeLimitUnit;
	}

	public void setFileSizeLimitUnit(String fileSizeLimitUnit) {
		this.fileSizeLimitUnit = fileSizeLimitUnit;
	}

	@Column(name = "is_file_count", nullable = true, length = 1)
	public Boolean getFileCount() {
		return isFileCount;
	}

	public void setFileCount(Boolean fileCount) {
		isFileCount = fileCount;
	}

	@Column(name = "file_count_limit", nullable = true, length = 2)
	public Integer getFileCountLimit() {
		return fileCountLimit;
	}

	public void setFileCountLimit(Integer fileCountLimit) {
		this.fileCountLimit = fileCountLimit;
	}

	@Column(name = "is_file_type", nullable = true, length = 1)
	public Boolean getFileType() {
		return isFileType;
	}

	public void setFileType(Boolean fileType) {
		isFileType = fileType;
	}

	@Column(name = "is_allow_file", nullable = true, length = 1)
	public Integer getAllowFile() {
		return isAllowFile;
	}

	public void setAllowFile(Integer isAllowFile) {
		this.isAllowFile = isAllowFile;
	}

	@Column(name = "file_type_limit", nullable = true, length = 255)
	public String getFileTypeLimit() {
		return fileTypeLimit;
	}

	public void setFileTypeLimit(String fileTypeLimit) {
		this.fileTypeLimit = fileTypeLimit;
	}

	@Column(name = "is_radio", nullable = true, length = 1)
	public Integer getRadio() {
		return isRadio;
	}

	public void setRadio(Integer radio) {
		isRadio = radio;
	}

	@OneToMany(mappedBy = "questionnaireSubject", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.MERGE)
	public List<SysQuestionnaireSubjectOption> getOptions() {
		return options;
	}

	public void setOptions(List<SysQuestionnaireSubjectOption> options) {
		this.options = options;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "questionnaire_id", insertable = false, updatable = false)
	public SysQuestionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(SysQuestionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	@Column(name = "dragable", nullable = true, length = 1)
	public Boolean getDragable() {
		return dragable;
	}

	public void setDragable(Boolean dragable) {
		this.dragable = dragable;
	}

	@Column(name = "editor", nullable = true, length = 50)
	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	@Column(name = "group_index", nullable = true, length = 6)
	public Integer getGroupIndex() {
		return groupIndex;
	}

	public void setGroupIndex(Integer groupIndex) {
		this.groupIndex = groupIndex;
	}

	@Column(name = "icon", nullable = true, length = 50)
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "index_id", nullable = true, length = 6)
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	@Column(name = "is_custom", nullable = true, length = 1)
	public Boolean getCustom() {
		return custom;
	}

	public void setCustom(Boolean custom) {
		this.custom = custom;
	}

	@Column(name = "placeholder", nullable = true, length = 50)
	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	@Column(name = "preview", nullable = true, length = 50)
	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	@Column(name = "value", nullable = true, length = 500)
	public String getValue() {
		return value;
    }

	public void setValue(String value) {
		this.value = value;
	}

}