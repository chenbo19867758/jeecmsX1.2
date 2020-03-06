package com.jeecms.common.resource.entity;

import java.lang.reflect.Field;

import com.jeecms.common.resource.annotations.ResourceField;

/**
 * 资源设置项目实体 一个实例对应一个@ResourceField注解配置
 * 
 * @author tom
 */
public class ResourcePushItemEntity {
	/**
	 * 实体内配置@ResourceField的字段
	 */
	private Field resourceField;
	/**
	 * 实体内配置@ResourceField的targetId的值对应的字段
	 */
	private Field targetIdField;
	/**
	 * 配置资源注解实例
	 */
	private ResourceField resourceAnnotation;

	public Field getResourceField() {
		return resourceField;
	}

	public Field getTargetIdField() {
		return targetIdField;
	}

	public ResourceField getResourceAnnotation() {
		return resourceAnnotation;
	}

	public void setResourceField(Field resourceField) {
		this.resourceField = resourceField;
	}

	public void setTargetIdField(Field targetIdField) {
		this.targetIdField = targetIdField;
	}

	public void setResourceAnnotation(ResourceField resourceAnnotation) {
		this.resourceAnnotation = resourceAnnotation;
	}

}
