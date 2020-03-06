package com.jeecms.common.resource.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * 配置指定方法将会被AOP切面类ResourceAspect所拦截
 * 拦截后会根据自定义注解进行查询资源 & 设置资源等逻辑
 * @author tom
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ResourceMethod { }
