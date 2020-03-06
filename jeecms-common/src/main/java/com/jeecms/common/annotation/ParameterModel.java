package com.jeecms.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数实体映射注解 配置该注解的参数会使用 {@link CustomerArgumentResolver}类完成参数装载
 * controller自动装配参数样例 @ParameterModel TeacherEntity teacher @ParameterModel
 * StudentEntity student
 */
@Target(value = ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterModel {

}
