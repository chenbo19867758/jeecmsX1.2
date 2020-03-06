package com.jeecms.common.resource.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源定义字段实体 该实体用于获取实体内初始化资源时的字段
 * 
 * @author tom
 */
public class ResourcePushEntity {
	/**
	 * 实体内的主键字段
	 */
	private Field idField;
	/**
	 * 资源设置项目实体列表 用于处理一个实体内对应多个资源注解配置
	 */
	private List<ResourcePushItemEntity> items = new ArrayList<ResourcePushItemEntity>();

	public Field getIdField() {
		return idField;
	}

	public List<ResourcePushItemEntity> getItems() {
		return items;
	}

	public void setIdField(Field idField) {
		this.idField = idField;
	}

	public void setItems(List<ResourcePushItemEntity> items) {
		this.items = items;
	}
}
