package com.jeecms.resource.service;

import java.util.List;

import com.jeecms.common.exception.GlobalException;

/**
 * 统一资源设置业务逻辑定义接口
 * @author tom
 */
public interface ResourcePushService
{
    /**
     * 设置单个实例的资源信息
     * @param object 需要设置资源的实例
     * @throws Exception 程序异常     
     */
    void push(Object object) throws Exception;

    /**
     * 设置多个实例的资源信息
     * @param objectList 需要设置资源的实例列表
     * @throws Exception 程序异常  
     */
    void push(List<Object> objectList) throws Exception;
}
