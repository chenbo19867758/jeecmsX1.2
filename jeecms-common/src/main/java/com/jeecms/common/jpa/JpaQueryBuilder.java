package com.jeecms.common.jpa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;

import com.jeecms.common.page.Paginable;

/**
 * Java Persistence Query Language 查询 由于
 * {@link org.springframework.data.jpa.repository.query.QueryUtils#applySorting}
 * 查询语句中的表名需要加别名，比如 select * from Shop s，而不能select * from Shop
 *
 * @author tom
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class JpaQueryBuilder {
	private StringBuilder sqlBuilder;
	private String countQueryString;
	private String countProjection;
	private List<Parameter> parameters = new ArrayList<>();
	/**
	 * native查询时，某些类型无法匹配，比如nvarchar的值是-9，hibernate的方言无法匹配这一类型
	 */
	private Map<String, Type> scalars = new LinkedHashMap<>();

	private static final String COUNT_STRING = "^\\s*(select\\s+(?:distinct\\s+)?([\\s\\S]*?)\\s+)?"
			+ "from\\s+[\\s\\S]*?(\\sgroup\\s+by\\s+[\\s\\S]*?)?(\\sorder\\s[\\s\\S]*?)?$";
	private static final Pattern COUNT_PATTERN = Pattern.compile(COUNT_STRING, Pattern.CASE_INSENSITIVE);

	public JpaQueryBuilder() {
		sqlBuilder = new StringBuilder();
	}

	public JpaQueryBuilder(String queryString) {
		sqlBuilder = new StringBuilder(queryString);
	}

	public JpaQueryBuilder append(String queryString) {
		sqlBuilder.append(queryString);
		return this;
	}

	public JpaQueryBuilder setParameter(String name, Object value) {
		parameters.add(new Parameter(name, value, null));
		return this;
	}

	public JpaQueryBuilder setParameter(String name, Date value, TemporalType temporalType) {
		parameters.add(new Parameter(name, value, temporalType));
		return this;
	}

	public JpaQueryBuilder setParameter(String name, Calendar value, TemporalType temporalType) {
		parameters.add(new Parameter(name, value, temporalType));
		return this;
	}

	public JpaQueryBuilder addScalar(String columnAlias, Type type) {
		scalars.put(columnAlias, type);
		return this;
	}

	public JpaQueryBuilder addScalar(String columnAlias) {
		scalars.put(columnAlias, null);
		return this;
	}

	public Query createNativeQuery(EntityManager em) {
		return createNativeQuery(em, (Sort) null);
	}

	public <T> Query createNativeQuery(EntityManager em, Class<T> resultClass) {
		return createNativeQuery(em, resultClass, null);
	}

	public Query createNativeQuery(EntityManager em, Sort sort) {
		return createNativeQuery(em, null, sort);
	}

	/**
	 * 构造原生查询对象
	 * 
	 * @Title: createNativeQuery
	 * @param em
	 *            EntityManager
	 * @param resultClass
	 *            Class
	 * @param sort
	 *            Sort
	 * @return: Query
	 */
	public <T> Query createNativeQuery(EntityManager em, Class<T> resultClass, Sort sort) {
		String sql = sqlBuilder.toString();
		if (sort != null) {
			// String alias = QueryUtils.detectAlias(sql);
			String alias = QuerydslUtils.detectAlias(sql);
			sql = QueryUtils.applySorting(sql, sort, alias);
		}
		Query query;
		if (resultClass != null) {
			query = em.createNativeQuery(sql, resultClass);
		} else {
			query = em.createNativeQuery(sql);
		}
		applyParameters(query);
		return query;
	}

	/**
	 * 构造jpa查询对象
	 * 
	 * @Title: createQuery
	 * @param em
	 *            EntityManager
	 * @param sort
	 *            Sort
	 * @return: Query
	 */
	public Query createQuery(EntityManager em, Sort sort) {
		String queryString = sqlBuilder.toString();
		if (sort != null) {
			String alias = QuerydslUtils.detectAlias(queryString);
			queryString = QueryUtils.applySorting(queryString, sort, alias);
		}
		Query query = em.createQuery(queryString);
		applyParameters(query);
		return query;
	}

	/**
	 * 构造TypedQuery查询对象
	 * 
	 * @Title: createQuery
	 * @param em
	 *            EntityManager
	 * @param resultClass
	 *            Class
	 * @param sort
	 *            Sort
	 * @return: TypedQuery
	 */
	public <T> TypedQuery<T> createQuery(EntityManager em, Class<T> resultClass, Sort sort) {
		String queryString = sqlBuilder.toString();
		if (sort != null) {
			String alias = QuerydslUtils.detectAlias(queryString);
			queryString = QueryUtils.applySorting(queryString, sort, alias);
		}
		TypedQuery<T> query = em.createQuery(queryString, resultClass);
		applyParameters(query);
		return query;
	}

	public Query createQuery(EntityManager em) {
		return createQuery(em, (Sort) null);
	}

	public <T> TypedQuery<T> createQuery(EntityManager em, Class<T> resultClass) {
		return createQuery(em, resultClass, null);
	}

	/**
	 * 构造jpa Query对象
	 * 
	 * @Title: createCountQuery
	 * @param em
	 *            EntityManager
	 * @return: Query
	 */
	public Query createCountQuery(EntityManager em) {
		// String
		// countQueryStr=QueryUtils.createCountQueryFor(sqlBuilder.toString(),
		// getCountProjection());
		String countQueryStr = QuerydslUtils.createCountQueryFor(sqlBuilder.toString(), getCountProjection());
		String cqs = StringUtils.isNotBlank(getCountQueryString()) ? getCountQueryString() : countQueryStr;
		Query query = em.createQuery(cqs);
		applyParameters(query);
		return query;
	}

	/**
	 * 构造原生查询对象
	 * 
	 * @Title: createNativeCountQuery
	 * @param em
	 *            EntityManager
	 * @return: Query
	 */
	public Query createNativeCountQuery(EntityManager em) {
		String cqs = StringUtils.isNotBlank(getCountQueryString()) ? getCountQueryString() : 
			createCountQueryString();
		Query query = em.createNativeQuery(cqs);
		applyParameters(query);
		return query;
	}

	private void applyParameters(Query query) {
		for (Parameter parameter : parameters) {
			String name = parameter.getName();
			Object value = parameter.getValue();
			TemporalType temporalType = parameter.getTemporalType();
			if (temporalType == null) {
				query.setParameter(name, value);
			} else {
				if (value instanceof Date) {
					query.setParameter(name, (Date) value, temporalType);
				} else if (value instanceof Calendar) {
					query.setParameter(name, (Calendar) value, temporalType);
				} else {
					throw new IllegalStateException(""
							+ "value must be java.utile.Date or java.util.Calendar");
				}
			}
		}
	}

	public String getQueryString() {
		return sqlBuilder.toString();
	}

	/**
	 * 获取count 查询语句
	 * @Title: createCountQueryString  
	 * @return: String
	 */
	public String createCountQueryString() {
		Matcher m = COUNT_PATTERN.matcher(sqlBuilder);
		if (!m.matches()) {
			throw new IllegalStateException("query string invalidated: " + sqlBuilder);
		}
		StringBuilder countJpql = new StringBuilder();
		int countStart = m.start(2);
		int countEnd = m.end(2);
		int groupStart = m.start(3);
		int orderStart = m.start(4);
		int end = groupStart == -1 ? orderStart : groupStart;

		if (countStart != -1) {
			countJpql.append(sqlBuilder.substring(0, countStart));
			countJpql.append("count(");
			countJpql.append(getCountProjection() != null ? getCountProjection() : "*");
			countJpql.append(") ");
			if (end != -1) {
				countJpql.append(sqlBuilder.substring(countEnd, end));
			} else {
				countJpql.append(sqlBuilder.substring(countEnd));
			}
		} else {
			countJpql.append("select count(");
			countJpql.append(getCountProjection() != null ? getCountProjection() : "*");
			countJpql.append(") ");
			if (end != -1) {
				countJpql.append(sqlBuilder.substring(0, end));
			} else {
				countJpql.append(sqlBuilder);
			}
		}
		// 清除fetch关键字
		String queryCount = StringUtils.replace(countJpql.toString(), " fetch ", " ");
		return queryCount;
	}

	public List list(EntityManager em) {
		return createQuery(em).getResultList();
	}

	public <T> List<T> list(EntityManager em, Class<T> resultClass) {
		return createQuery(em, resultClass).getResultList();
	}

	public List list(EntityManager em, Sort sort) {
		return createQuery(em, sort).getResultList();
	}

	public <T> List<T> list(EntityManager em, Class<T> resultClass, Sort sort) {
		return createQuery(em, resultClass, sort).getResultList();
	}

	/**
	 * 获取list数据 
	 * @Title: list  
	 * @param em EntityManager
	 * @param paginable Paginable
	 * @return: List
	 */
	public List list(EntityManager em, Paginable paginable) {
		Sort sort = null;
		if (paginable != null) {
			sort = paginable.getSort();
		}
		Query query = createQuery(em, sort);
		if (paginable != null) {
			Integer firstResult = paginable.getOffset();
			if (firstResult != null && firstResult > 0) {
				query.setFirstResult(firstResult);
			}
			Integer maxResults = paginable.getMax();
			if (maxResults != null && maxResults > 0) {
				query.setMaxResults(maxResults);
			}
		}
		return query.getResultList();
	}


	/**
	 * 获取list数据 
	 * @Title: list  
	 * @param em EntityManager
	 * @param resultClass Class 结果数据class
	 * @param paginable Paginable
	 * @return: List
	 */
	public <T> List<T> list(EntityManager em, Class<T> resultClass, Paginable paginable) {
		Sort sort = null;
		if (paginable != null) {
			sort = paginable.getSort();
		}
		TypedQuery<T> query = createQuery(em, resultClass, sort);
		if (paginable != null) {
			Integer firstResult = paginable.getOffset();
			if (firstResult != null && firstResult > 0) {
				query.setFirstResult(firstResult);
			}
			Integer maxResults = paginable.getMax();
			if (maxResults != null && maxResults > 0) {
				query.setMaxResults(maxResults);
			}
		}
		return query.getResultList();
	}

	/**
	 * 获取分页数据
	 * @Title: page  
	 * @param em EntityManager
	 * @param resultClass Class 结果数据class
	 * @param pageable Pageable
	 * @return: Page 
	 */
	public <T> Page<T> page(EntityManager em, Class<T> resultClass, Pageable pageable) {
		Query countQuery = this.createCountQuery(em);
		long total = ((Number) countQuery.getSingleResult()).longValue();

		List<T> content;
		if (total > pageable.getOffset()) {
			TypedQuery<T> query = this.createQuery(em, resultClass, pageable.getSort());
			query.setFirstResult((int) pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
			content = query.getResultList();
		} else {
			content = Collections.emptyList();
		}
		return new PageImpl<T>(content, pageable, total);
	}

	/**
	 * 获取分页数据
	 * @Title: page  
	 * @param em EntityManager
	 * @param pageable Pageable
	 * @return: Page 
	 */
	public Page page(EntityManager em, Pageable pageable) {
		Query countQuery = this.createCountQuery(em);
		long total = ((Number) countQuery.getSingleResult()).longValue();

		List content;
		if (total > pageable.getOffset()) {
			Query query = this.createQuery(em, pageable.getSort());
			query.setFirstResult((int) pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
			content = query.getResultList();
		} else {
			content = Collections.EMPTY_LIST;
		}
		return new PageImpl(content, pageable, total);
	}

	public List nativeList(EntityManager em) {
		return createNativeQuery(em).getResultList();
	}

	public List nativeList(EntityManager em, Sort sort) {
		return createNativeQuery(em, sort).getResultList();
	}

	/**
	 * 获取list数据  原生sql方式
	 * @Title: nativeList   
	 * @param em EntityManager
	 * @param paginable Paginable
	 * @return: List
	 */
	public List nativeList(EntityManager em, Paginable paginable) {
		Query query = createNativeQuery(em, paginable.getSort());
		applyScalar(query);
		Integer firstResult = paginable.getOffset();
		if (firstResult != null && firstResult > 0) {
			query.setFirstResult(firstResult);
		}
		Integer maxResults = paginable.getMax();
		if (maxResults != null && maxResults > 0) {
			query.setMaxResults(maxResults);
		}
		return query.getResultList();
	}

	/**
	 * 获取list数据  原生sql方式
	 * @Title: nativeList   
	 * @param em EntityManager
	 * @param resultClass Class 结果数据class
	 * @param paginable Paginable
	 * @return: List
	 */
	public <T> List<T> nativeList(EntityManager em, Class<T> resultClass, Paginable paginable) {
		Query query = createNativeQuery(em, resultClass, paginable.getSort());
		applyScalar(query);
		Integer firstResult = paginable.getOffset();
		if (firstResult != null && firstResult > 0) {
			query.setFirstResult(firstResult);
		}
		Integer maxResults = paginable.getMax();
		if (maxResults != null && maxResults > 0) {
			query.setMaxResults(maxResults);
		}
		return query.getResultList();
	}

	/**
	 * 原生查询分页
	 * 
	 * @Title: nativePage
	 * @param em
	 *            EntityManager
	 * @param resultClass
	 *            Class
	 * @param pageable
	 *            Pageable
	 * @return: Page
	 */
	public <T> Page<T> nativePage(EntityManager em, Class<T> resultClass, Pageable pageable) {
		Query countQuery = this.createNativeCountQuery(em);
		long total = ((Number) countQuery.getSingleResult()).longValue();

		List<T> content;
		if (total > pageable.getOffset()) {
			Query query = this.createNativeQuery(em, resultClass, pageable.getSort());
			applyScalar(query);
			query.setFirstResult((int) pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
			content = (List<T>) query.getResultList();
		} else {
			content = Collections.emptyList();
		}
		return new PageImpl<T>(content, pageable, total);
	}

	/**
	 * 原生查询分页
	 * 
	 * @Title: nativePage
	 * @param em
	 *            EntityManager
	 * @param pageable
	 *            Pageable
	 * @return: Page
	 */
	public Page nativePage(EntityManager em, Pageable pageable) {
		Query countQuery = this.createNativeCountQuery(em);
		long total = ((Number) countQuery.getSingleResult()).longValue();

		List content;
		if (total > pageable.getOffset()) {
			Query query = this.createNativeQuery(em, pageable.getSort());
			applyScalar(query);
			query.setFirstResult((int) pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
			content = query.getResultList();
		} else {
			content = Collections.EMPTY_LIST;
		}
		return new PageImpl(content, pageable, total);
	}

	public void executeUpdate(EntityManager entityManager) {
		Query query = createQuery(entityManager);
		query.executeUpdate();
	}

	/**
	 * 执行hql
	 * 
	 * @Title: executeUpdate
	 * @param hql
	 *            hql
	 * @param entityManager
	 *            EntityManager
	 * @return: void
	 */
	public static void executeUpdate(String hql, EntityManager entityManager) {
		JpaQueryBuilder build = new JpaQueryBuilder(hql);
		Query query = build.createQuery(entityManager);
		query.executeUpdate();
	}

	public Object getSingleResult(EntityManager entityManager) {
		Query query = createQuery(entityManager);
		return query.getSingleResult();
	}

	/**
	 * 查询单个对象
	 * 
	 * @Title: getSingleResult
	 * @param hql
	 *            hql
	 * @param entityManager
	 *            EntityManager
	 * @return: Object
	 */
	public static Object getSingleResult(String hql, EntityManager entityManager) {
		JpaQueryBuilder jb = new JpaQueryBuilder(hql);
		return getSingleResult(jb, entityManager);
	}

	/**
	 * 查询单个对象 querydsl方式
	 * 
	 * @Title: getSingleResult
	 * @param jb
	 *            JpaQueryBuilder
	 * @param entityManager
	 *            EntityManager
	 * @return: Object
	 */
	public static Object getSingleResult(JpaQueryBuilder jb, EntityManager entityManager) {
		Query query = jb.createQuery(entityManager);
		Object object = query.getSingleResult();
		return object;
	}

	private void applyScalar(Query query) {
		if (scalars.isEmpty()) {
			return;
		}
		// SQLQuery sqlQuery = query.unwrap(SQLQuery.class);
		NativeQuery sqlQuery = query.unwrap(NativeQuery.class);
		for (Map.Entry<String, Type> entry : scalars.entrySet()) {
			if (entry.getValue() != null) {
				sqlQuery.addScalar(entry.getKey(), entry.getValue());
			} else {
				sqlQuery.addScalar(entry.getKey());
			}
		}
	}

	public String getCountQueryString() {
		return countQueryString;
	}

	public void setCountQueryString(String countQueryString) {
		this.countQueryString = countQueryString;
	}

	public String getCountProjection() {
		return countProjection;
	}

	public void setCountProjection(String countProjection) {
		this.countProjection = countProjection;
	}

	private static class Parameter {
		private String name;
		private Object value;
		private TemporalType temporalType;

		public Parameter(String name, Object val, TemporalType temporalType) {
			this.name = name;
			this.value = val;
			this.temporalType = temporalType;
		}

		public String getName() {
			return name;
		}

		public Object getValue() {
			return value;
		}

		public TemporalType getTemporalType() {
			return temporalType;
		}
	}
}
