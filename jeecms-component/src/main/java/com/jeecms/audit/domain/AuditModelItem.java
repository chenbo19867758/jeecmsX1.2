/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.audit.domain;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.content.domain.CmsModelItem;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

/**
 * 审核模型字段
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-12-16
 */
@Entity
@Table(name = "jc_audit_model_item")
public class AuditModelItem extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 审核模型主键ID */
	private Integer auditModelId;
	/** 模型字段ID */
	private Integer modelItemId;
	/** 模型字段field */
	private String modelItemField;

	/**模型设置**/
	private AuditModelSet modelSet;
	/**模型字段**/
	private CmsModelItem modelItem;
	
	public AuditModelItem() {
	}

	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_audit_model_item", pkColumnValue = "jc_audit_model_item", 
			initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_audit_model_item")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "audit_model_id", nullable = false, length = 11)
	public Integer getAuditModelId() {
		return auditModelId;
	}

	public void setAuditModelId(Integer auditModelId) {
		this.auditModelId = auditModelId;
	}

	@Column(name = "model_item_field", nullable = false, length = 50)
	public String getModelItemField() {
		return modelItemField;
	}

	public void setModelItemField(String modelItemField) {
		this.modelItemField = modelItemField;
	}

	@Column(name = "model_item_id", nullable = false, length = 11)
	public Integer getModelItemId() {
		return modelItemId;
	}

	public void setModelItemId(Integer modelItemId) {
		this.modelItemId = modelItemId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "audit_model_id", referencedColumnName = "id", insertable = false, updatable = false)
	public AuditModelSet getModelSet() {
		return modelSet;
	}

	public void setModelSet(AuditModelSet modelSet) {
		this.modelSet = modelSet;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_item_id", referencedColumnName = "id", insertable = false, updatable = false)
	@NotFound(action= NotFoundAction.IGNORE)
	public CmsModelItem getModelItem() {
		//判断是否存在模型字段,这里在修改模型字段的时候会出现ID变化的问题，
		// 所以需要根据Field进一步获取
		if (modelItem == null) {
			Set<CmsModelItem> items = modelSet.getModel().getItems();
			if (!items.isEmpty()) {
				Optional<CmsModelItem> first = items.stream().filter(x -> x.getField()
						.equals(this.getModelItemField())).findFirst();
				if (first.isPresent()) {
					return  first.get();
				}
			}
		}
		return modelItem;
	}

	public void setModelItem(CmsModelItem modelItem) {
		this.modelItem = modelItem;
	}

}