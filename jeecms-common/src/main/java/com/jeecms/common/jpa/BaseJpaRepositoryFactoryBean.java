package com.jeecms.common.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * JPA Repository 工厂类
 * @author: tom
 * @date:   2018年1月26日 下午2:59:37     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved. Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 * */
public class BaseJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends
		JpaRepositoryFactoryBean<T, S, ID> {
	
	public BaseJpaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new BaseJpaRepositoryFactory(entityManager);
	}
	
}
