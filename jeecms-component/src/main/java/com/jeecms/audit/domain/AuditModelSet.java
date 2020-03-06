/*
* @Copyright:  江西金磊科技发展有限公司  All rights reserved. 
* Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
*/

package com.jeecms.audit.domain;

import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.content.domain.CmsModel;
import com.jeecms.content.domain.CmsModelItem;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 审核模型设置
 * 
 * @author ljw
 * @version 1.0
 * @date 2019-12-16
 */
@Entity
@Table(name = "jc_audit_model_set")
public class AuditModelSet extends AbstractDomain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	/** 模型ID */
	private Integer modelId;
	/**站点ID**/
	private Integer siteId;
	/**模型对象**/
	private CmsModel model;
	/**模型字段**/
	private List<AuditModelItem> items;

	public AuditModelSet() {
	}

	@Override
	@Id
	@Column(name = "id", nullable = false, length = 11)
	@TableGenerator(name = "jc_audit_model_set", pkColumnValue = "jc_audit_model_set", 
			initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "jc_audit_model_set")
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "model_id", nullable = false, length = 11)
	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_id", insertable = false, updatable = false)
	public CmsModel getModel() {
		return model;
	}

	public void setModel(CmsModel model) {
		this.model = model;
	}

	@OneToMany(mappedBy = "modelSet")
	@Where(clause = " deleted_flag=0 ")
	public List<AuditModelItem> getItems() {
		return items;
	}

	public void setItems(List<AuditModelItem> items) {
		this.items = items;
	}


	@Column(name = "site_id", nullable = false, length = 11)
	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	
	/** 模型名称 **/
	@Transient
	public String getModelName() {
		String modelName = "";
		if (getModel() != null) {
			modelName = getModel().getModelName();
		}
		return modelName;
	}
	
	/** 审核字段 **/
	@Transient
	public String getItemsName() {
		StringBuilder builder = new StringBuilder(500);
		if (getItems() != null && !getItems().isEmpty()) {
			for (AuditModelItem auditModelItem : getItems()) {
				CmsModelItem item = auditModelItem.getModelItem();
				if (Objects.nonNull(item)) {
					builder.append(item.getItemLabel());
					builder.append("、");
				}
			}
		}
		return builder.substring(0, builder.length() != 0 ? builder.length() - 1 : 0);
	}

	/** 审核字段IDS **/
	@Transient
	public List<Integer> getItemIds() {
		List<Integer> ids = new ArrayList<>(16);
		if (getItems() != null && !getItems().isEmpty()) {
			for (AuditModelItem auditModelItem : getItems()) {
				CmsModelItem item = auditModelItem.getModelItem();
				if (Objects.nonNull(item)) {
					ids.add(item.getId());
				}
			}
		}
		return ids;
	}
	
}