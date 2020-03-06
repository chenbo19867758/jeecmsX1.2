package com.jeecms.resource.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jeecms.common.resource.annotations.ResourceField;
import com.jeecms.common.resource.entity.ResourcePushEntity;
import com.jeecms.common.resource.entity.ResourcePushItemEntity;
import com.jeecms.common.util.ReflectUtils;
import com.jeecms.resource.service.ResourcePushService;
import com.jeecms.resource.service.ResourcesSpaceDataService;

import javax.persistence.Id;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 统一资源设置业务逻辑实现类
 * @author tom
 */
@Service("ResourcePushSupport")
public class ResourcePushServiceImpl implements ResourcePushService{
    /**
     * logback
     */
    Logger logger = LoggerFactory.getLogger(ResourcePushServiceImpl.class);
    /**
     * 统一资源业务逻辑实现类
     */
    @Autowired
    private ResourcesSpaceDataService resourceService;

    /**
     *
     * @param object 需要设置资源的实例
     * @throws Exception
     */
    @Override
    public void push(Object object) throws Exception{
        /**
         * 获取资源设置所需要的字段对象
         */
        ResourcePushEntity declareField = getNeedFields(object);
        for (ResourcePushItemEntity item : declareField.getItems()) {
            /**
             * 获取目标编号
             */
            Object targetId = getTargetIdValue(declareField.getIdField(),item,object);

            logger.info("自动设置：{}，资源类型：{}，相关信息.",targetId);
            /**
             * 设置资源字段的值
             */
            setFieldValue(object,targetId,item);
        }
    }

    /**
     *
     * @param objectList 需要设置资源的实例列表
     * @throws Exception
     */
    @Override
    public void push(List<Object> objectList) throws Exception{
        for (Object object : objectList) {
            push(object);
        }
    }

    /**
     * 获取所需定义的字段实体
     * @param object 需要获取的实例
     * @return
     */
    @SuppressWarnings("rawtypes")
	ResourcePushEntity getNeedFields(Object object)
    {
        ResourcePushEntity resourceDeclare = new ResourcePushEntity();
        /**
         * 获取类内定义的字段列表
         */
        Class clazz = object.getClass();
        List<Field> fields = ReflectUtils.getAllFields(clazz);
        for (Field field : fields) {
            /**
             * 获取@ResourceField注解字段
             */
            if (field.isAnnotationPresent(ResourceField.class)) {
                instancePushItem(field,clazz,resourceDeclare);
            }
            /**
             * 获取@Id注解字段
             */
            if (field.isAnnotationPresent(Id.class)) {
                resourceDeclare.setIdField(field);
            }
        }
        return resourceDeclare;
    }

    /**
     * 实例化设置资源项目
     * @param field 配置@ResourceField注解的字段
     * @param resourceDeclare 资源设置实体
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	void instancePushItem(Field field,Class clazz,ResourcePushEntity resourceDeclare)
    {
        logger.info("字段：{}，配置了@ResourceField注解.",field.getName());
        ResourcePushItemEntity pushItem = new ResourcePushItemEntity();
        pushItem.setResourceField(field);
        pushItem.setResourceAnnotation(field.getAnnotation(ResourceField.class));
        /**
         * 如果配置@ResourceField内targetIdField时
         * 获取对应的field的实例
         */
        if(!StringUtils.isEmpty(pushItem.getResourceField())) {
            String targetIdFieldName = getTargetIdFieldName(pushItem.getResourceField());
            if (!StringUtils.isEmpty(targetIdFieldName)) {
                pushItem.setTargetIdField(getField(clazz, targetIdFieldName));
            }
        }
        /**
         * 设置资源选项到集合内
         */
        resourceDeclare.getItems().add(pushItem);
    }
    /**
     * 获取target_id field的名称
     * @param field
     * @return
     */
    String getTargetIdFieldName(Field field)
    {
        ResourceField resourceField = field.getAnnotation(ResourceField.class);
        return !StringUtils.isEmpty(resourceField) ? resourceField.targetIdField() : null;
    }

    /**
     * 获取定义的字段
     * @param clazz 获取字段的类型
     * @param fieldName 字段的名称
     * @return
     */
    @SuppressWarnings("rawtypes")
	Field getField(Class clazz,String fieldName)
    {
        Field field = null;
        try {
            field = ReflectUtils.getField(clazz,fieldName);
        }catch (Exception e) {
            logger.error("字段：【{}】未在{}，内获取到，无法根据@ResourceField配置targetIdField设置资源.");
        }
        return field;
    }

    /**
     * 查询字段的值
     * @param field 字段实例
     * @param object 对象实例
     * @return
     * @throws Exception
     */
    Object getFieldValue(Field field,Object object) throws Exception
    {
        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), object.getClass());  
      //获得get方法  
        Method get = pd.getReadMethod();
        return get.invoke(object);
    	/*
        field.setAccessible(true);
        return field.get(object);
        */
    }

    /**
     * 获取目标编号
     * @param pushItem 资源选项实例
     * @param object 对象实例
     * @return
     * @throws Exception
     */
    Object getTargetIdValue(Field idField,ResourcePushItemEntity pushItem, Object object) throws Exception
    {
        /**
         * 获取设置的目标字段
         */
        Field targetField = pushItem.getTargetIdField();
        /**
         * 不存在目标字段时，使用主键字段
         */
        if(StringUtils.isEmpty(targetField)) {
            logger.warn("未配置@ResourceField内targetIdField的值，尝试使用@Id注解主键的值。");
            targetField = idField;
        }
        if(StringUtils.isEmpty(targetField))
        {
            throw new NullPointerException("无法查询资源，检查：" + object.getClass().getName() + "，是否配置了@Id以及@ResourceField注解targetIdField。");
        }
        /**
         * 获取目标编号
         */
        Object targetId = getFieldValue(targetField,object);

        logger.info("目标字段内容获取成功"+targetId);
        if(StringUtils.isEmpty(targetId))
        {
            throw new NullPointerException("未查询到：" + object.getClass().getName() + "配置资源信息，检查配置信息。");
        }
        return targetId;
    }

    /**
     * 设置字段的值
     * 根据字段的类型设置不同的值
     * 字段如果为String，则直接获取资源路径进行赋值
     * 字段如果为List<String>，则遍历获取所有的资源列表后进行赋值
     * @param object 对象实例
     * @param targetId 目标编号
     * @param pushItem 资源项实例
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	void setFieldValue(Object object,Object targetId,ResourcePushItemEntity pushItem) throws Exception
    {
        /**
         * 查询指定资源标识的资源列表
         */
    	List<Integer>ids=new ArrayList<>();
    	if(targetId instanceof Iterable){
    		ids.addAll((Collection<? extends Integer>)targetId);
    	}else{
    		ids.add((Integer) targetId);
    	}
        List<String> resourceResults =resourceService.selectUrlsByIds(ids);
        if(StringUtils.isEmpty(resourceResults))
        {
            return;
        }
        /**
         * 获取资源字段以及字段类型
         */
        Field resourceField = pushItem.getResourceField();
        resourceField.setAccessible(true);
        /**
         * 配置为需要多个返回值时，将查询列表设置给资源字段
         */
        if(pushItem.getResourceAnnotation().multiple()) {
            resourceField.set(object,resourceResults);
        }
        /**
         * 配置需要单个返回值时，将索引为0的数据设置到资源字段
         */
        else{
            resourceField.set(object,resourceResults.get(0));
        }
    }
}
