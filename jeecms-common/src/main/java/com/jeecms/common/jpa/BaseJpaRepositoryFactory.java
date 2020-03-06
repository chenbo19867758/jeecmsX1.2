package com.jeecms.common.jpa;

import static org.springframework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

import com.jeecms.common.base.dao.BaseRepository;

/**
 * JPA Repository工厂
 * @author: tom
 * @date:   2018年1月26日 下午2:49:35     
 * @Copyright:  江西金磊科技发展有限公司  All rights reserved.Notice 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 * */
public class BaseJpaRepositoryFactory extends JpaRepositoryFactory {

	public BaseJpaRepositoryFactory(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return BaseRepository.class;
	}

	@SuppressWarnings("unused")
	private boolean isQueryDslExecutor(Class<?> repositoryInterface) {
		return QUERY_DSL_PRESENT
				&& BaseQueryDslPredicateExecutor.class
						.isAssignableFrom(repositoryInterface);
	}
}
