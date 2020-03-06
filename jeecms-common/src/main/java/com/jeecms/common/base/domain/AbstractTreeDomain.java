package com.jeecms.common.base.domain;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 含tree的lft rgt 基类 继承 AbstractSortDomain
 * 
 * @author: tom
 * @date: 2018年12月24日 下午3:11:58
 * @param <T>
 *            domain
 * @param <ID>
 *            id
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved.Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@SuppressWarnings("rawtypes")
@MappedSuperclass
public abstract class AbstractTreeDomain<T extends AbstractTreeDomain, ID extends Serializable>
		extends AbstractSortDomain<ID> {

	private static final long serialVersionUID = -6529930769002715205L;
	/**
	 * 默认树左边属性名称
	 */
	public static final String DEF_LEFT_NAME = "lft";
	/**
	 * 默认树右边属性名称
	 */
	public static final String DEF_RIGHT_NAME = "rgt";
	/**
	 * 默认父节点属性名称
	 */
	public static final String DEF_PARENT_NAME = "parent";
	/**
	 * 默认父节点Id属性名称
	 */
	public static final String DEF_PARENT_ID_NAME = "parentId";
	/**
	 * 实体类别名
	 */
	public static final String ENTITY_ALIAS = "bean";

	/** 树左边属性 */
	protected Integer lft;
	/** 树左边属性 */
	protected Integer rgt;
	/** 父级ID */
	protected Integer parentId;
	/** 父级对象 */
	protected T parent;
	/** 子集 */
	protected List<T> childs = new ArrayList<T>(0);
	/** 子集 */
	protected List<T> children = new ArrayList<T>(0);

	/**
	 * 获得节点列表。从父节点到自身。
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transient
	@JSONField(serialize = false)
	public List<T> getNodeList() {
		LinkedList<T> list = new LinkedList<T>();
		T node = (T) this;
		while (node != null) {
			list.addFirst(node);
			if (node.getParent() != null) {
				node = (T) node.getParent();
			} else {
				break;
			}
		}
		return list;
	}

	/**
	 * 获得节点列表ID。从父节点到自身。
	 *
	 * @return
	 */
	@Transient
	public Integer[] getNodeIds() {
		List<T> ctgs = getNodeList();
		Integer[] ids = new Integer[ctgs.size()];
		int i = 0;
		for (T c : ctgs) {
			ids[i++] = (Integer) c.getId();
		}
		return ids;
	}

	/**
	 * 获得深度
	 *
	 * @return 第一层为0，第二层为1，以此类推。
	 */
	@SuppressWarnings("unchecked")
	@Transient
	public int getDeep() {
		int deep = 0;
		T parent = getParent();
		while (parent != null) {
			deep++;
			parent = (T) parent.getParent();
		}
		return deep;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	public T getParent() {
		return parent;
	}

	public void setParent(T parent) {
		this.parent = parent;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	@Where(clause = "deleted_flag=0")
	public List<T> getChilds() {
		return childs;
	}

	public void setChilds(List<T> childs) {
		this.childs = childs;
	}

	@Transient
	public List<T> getChildren() {
		return getChilds();
	}

	public void setChildren(List<T> children) {
		this.setChilds(children);
	}

	@Column(name = "parent_id", precision = 20, scale = 0)
	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Column(name = "lft", updatable = false)
	public Integer getLft() {
		return lft;
	}

	public void setLft(Integer lft) {
		this.lft = lft;
	}

	@Column(name = "rgt", updatable = false)
	public Integer getRgt() {
		return rgt;
	}

	public void setRgt(Integer rgt) {
		this.rgt = rgt;
	}

	/**
	 * 获得树左边属性名称
	 * 
	 * @return
	 */
	@Transient
	public String getLftName() {
		return DEF_LEFT_NAME;
	}

	/**
	 * 获得树右边属性名称
	 * 
	 * @return
	 */
	@Transient
	public String getRgtName() {
		return DEF_RIGHT_NAME;
	}

	/**
	 * 获得父节点属性名称
	 * 
	 * @return
	 */
	@Transient
	public String getParentName() {
		return DEF_PARENT_NAME;
	}

	@Transient
	public String getParentIdName() {
		return DEF_PARENT_ID_NAME;
	}

	/**
	 * 获得附加条件 通过附加条件可以维护多棵树相互独立的树，附加条件使用hql语句，实体别名为bean。例如：bean.website.id=5
	 * 
	 * @return 为null则不添加任何附加条件
	 */
	@Transient
	public String getTreeCondition() {
		return "bean.hasDeleted=false";
	}

}
