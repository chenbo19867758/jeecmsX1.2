
package com.jeecms.common.interceptor;

import com.jeecms.common.base.domain.AbstractDelFlagDomain;
import com.jeecms.common.base.domain.AbstractDomain;
import com.jeecms.common.base.domain.AbstractTreeDomain;
import com.jeecms.common.jpa.JpaQueryBuilder;
import com.jeecms.common.util.CurrUserContextUtils;
import com.jeecms.common.web.ApplicationContextProvider;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据访问层拦截
 * 
 * @Description:
 * @author:
 * @date: 2018年5月30日 上午8:41:30
 * @Copyright: 江西金磊科技发展有限公司 All rights reserved. Notice
 *             仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
public class TreeInterceptor extends EmptyInterceptor {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(TreeInterceptor.class);

	private EntityManager entityManager;

	/**
	 * 获取 EntityManager
	 * 
	 * @Title: getEntityManager
	 * @return: EntityManager
	 */
	protected EntityManager getEntityManager() {
		if (entityManager == null) {
			entityManager = (EntityManager) ApplicationContextProvider.getBean("entityManager");
		}
		return entityManager;
	}

	/**
	 * 如果为树形结构,则按"左右值编码"方式来调整lft和rgt节点的值
	 * https://www.biaodianfu.com/the-nested-set-model.html
	 */

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		setCreateInfoBeforeSave(entity, state, propertyNames);
		if (entity instanceof AbstractTreeDomain) {
			getEntityManager();
			AbstractTreeDomain<?, ?> tree = (AbstractTreeDomain<?, ?>) entity;
			Number parentId = tree.getParentId();
			String beanName = tree.getClass().getName();
			Integer myPosition = 0;
			if (parentId != null) {
				// 如果父节点不为null，则获取节点的右边位置
				String hql = "select bean." + tree.getRgtName() + " from " + beanName + " bean where bean.id="
						+ parentId;
				entityManager.setFlushMode(FlushModeType.COMMIT);
				myPosition = (Integer) JpaQueryBuilder.getSingleResult(hql, entityManager);
				String hql1 = "update " + beanName + " bean set bean." + tree.getRgtName() + " = bean."
						+ tree.getRgtName() + " + 2 WHERE bean." + tree.getRgtName() + " >= :myPosition";
				String hql2 = "update " + beanName + " bean set bean." + tree.getLftName() + " = bean."
						+ tree.getLftName() + " + 2 WHERE bean." + tree.getLftName() + " >= :myPosition";
				if (!StringUtils.isBlank(tree.getTreeCondition())) {
					hql1 += " and (" + tree.getTreeCondition() + ")";
					hql2 += " and (" + tree.getTreeCondition() + ")";
				}
				JpaQueryBuilder jb1 = new JpaQueryBuilder(hql1).setParameter("myPosition", myPosition);
				jb1.executeUpdate(entityManager);
				JpaQueryBuilder jb2 = new JpaQueryBuilder(hql2).setParameter("myPosition", myPosition);
				jb2.executeUpdate(entityManager);
			} else {
				// 否则查找最大的右边位置
				String hql = "select max(bean." + tree.getRgtName() + ") from " + beanName + " bean";
				if (!StringUtils.isBlank(tree.getTreeCondition())) {
					hql += " where " + tree.getTreeCondition();
				}
				entityManager.setFlushMode(FlushModeType.COMMIT);
				Number myPositionNumber = (Number) JpaQueryBuilder.getSingleResult(hql, entityManager);
				// 如不存在，则为1
				if (myPositionNumber == null) {
					myPosition = 1;
				} else {
					myPosition = myPositionNumber.intValue() + 1;
				}
			}
			// 更新数据
			for (int i = 0; i < propertyNames.length; i++) {
				if (propertyNames[i].equals(tree.getLftName())) {
					state[i] = myPosition;
				}
				if (propertyNames[i].equals(tree.getRgtName())) {
					state[i] = myPosition + 1;
				}
			}
		}
		return true;
	}

	/**
	 * 刷新数据(修改,iterator,list等时候)
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		setUpdateInfoBeforeUpdate(entity, currentState, propertyNames);
		getEntityManager();
		if (entity instanceof AbstractTreeDomain) {
			AbstractTreeDomain<?, ?> tree = (AbstractTreeDomain<?, ?>) entity;
			if (previousState != null) {
				for (int i = 0; i < propertyNames.length; i++) {
					if (propertyNames[i].equals(tree.getParentIdName())) {
						return updateParent(tree, (Integer) previousState[i], (Integer) currentState[i]);
					}
				}
			}
		}
		entityManager.setFlushMode(FlushModeType.AUTO);
		return false;
	}

	private boolean updateParent(AbstractTreeDomain<?, ?> tree, Integer preParent, Integer currParent) {
		// 都为空、或都不为空且相等时，不作处理
		boolean notUpdateParent = (preParent == null && currParent == null)
				|| (preParent != null && currParent != null && preParent.equals(currParent));
		if (notUpdateParent) {
			return false;
		}
		String beanName = tree.getClass().getName();
		if (log.isDebugEnabled()) {
			log.debug("update Tree {}, id={}, " + "pre-parent id={}, curr-parent id={}", new Object[] { beanName,
					tree.getId(), preParent == null ? null : preParent, currParent == null ? null : currParent });
		}
		// 先空出位置。当前父节点存在时，才需要空出位置。
		Integer currParentRgt;
		if (currParent != null) {
			// 获得节点跨度
			String hql = "select bean." + tree.getLftName() + ",bean." + tree.getRgtName() + " from " + beanName
					+ " bean where bean.id=:id";
			JpaQueryBuilder jb = new JpaQueryBuilder(hql);
			jb.setParameter("id", tree.getId());
			entityManager.setFlushMode(FlushModeType.COMMIT);
			Object[] position = (Object[]) jb.getSingleResult(entityManager);

			int nodeLft = ((Number) position[0]).intValue();
			int nodeRgt = ((Number) position[1]).intValue();
			int span = nodeRgt - nodeLft + 1;
			log.debug("current node span={}", span);

			// 获得当前父节点右位置
			jb.setParameter("id", currParent);
			Object[] currPosition = (Object[]) jb.getSingleResult(entityManager);
			int currParentLft = ((Number) currPosition[0]).intValue();
			currParentRgt = ((Number) currPosition[1]).intValue();
			log.debug("current parent lft={} rgt={}", currParentLft, currParentRgt);

			// 空出位置
			String hql1 = "update " + beanName + " bean set bean." + tree.getRgtName() + " = bean." + tree.getRgtName()
					+ " + " + span + " WHERE bean." + tree.getRgtName() + " >= :parentRgt";
			String hql2 = "update " + beanName + " bean set bean." + tree.getLftName() + " = bean." + tree.getLftName()
					+ " + " + span + " WHERE bean." + tree.getLftName() + " >= :parentRgt";
			if (!StringUtils.isBlank(tree.getTreeCondition())) {
				hql1 += " and (" + tree.getTreeCondition() + ")";
				hql2 += " and (" + tree.getTreeCondition() + ")";
			}
			JpaQueryBuilder updateJB = new JpaQueryBuilder(hql1);
			updateJB.setParameter("parentRgt", currParentRgt);
			updateJB.executeUpdate(entityManager);
			JpaQueryBuilder updateJB2 = new JpaQueryBuilder(hql2);
			updateJB2.setParameter("parentRgt", currParentRgt);
			updateJB2.executeUpdate(entityManager);
			log.debug("vacated span hql: {}, {}, parentRgt={}", new Object[] { hql1, hql2, currParentRgt });
		} else {
			// 否则查找最大的右边位置
			String hql = "select max(bean." + tree.getRgtName() + ") from " + beanName + " bean";
			if (!StringUtils.isBlank(tree.getTreeCondition())) {
				hql += " where " + tree.getTreeCondition();
			}
			entityManager.setFlushMode(FlushModeType.COMMIT);
			currParentRgt = (Integer) JpaQueryBuilder.getSingleResult(hql, entityManager);
			currParentRgt++;
			log.debug("max node left={}", currParentRgt);
		}

		// 再调整自己
		String hql = "select bean." + tree.getLftName() + ",bean." + tree.getRgtName() + " from " + beanName
				+ " bean where bean.id=" + tree.getId();
		entityManager.setFlushMode(FlushModeType.COMMIT);
		Object[] position = (Object[]) JpaQueryBuilder.getSingleResult(hql, entityManager);

		int nodeLft = ((Number) position[0]).intValue();
		int nodeRgt = ((Number) position[1]).intValue();
		int span = nodeRgt - nodeLft + 1;
		if (log.isDebugEnabled()) {
			log.debug("before adjust self left={} right={} span={}", new Object[] { nodeLft, nodeRgt, span });
		}
		int offset = currParentRgt - nodeLft;
		hql = "update " + beanName + " bean set bean." + tree.getLftName() + "=bean." + tree.getLftName()
				+ "+:offset, bean." + tree.getRgtName() + "=bean." + tree.getRgtName() + "+:offset WHERE bean."
				+ tree.getLftName() + " between :nodeLft and :nodeRgt";
		if (!StringUtils.isBlank(tree.getTreeCondition())) {
			hql += " and (" + tree.getTreeCondition() + ")";
		}
		JpaQueryBuilder jb = new JpaQueryBuilder(hql);
		jb.setParameter("offset", offset);
		jb.setParameter("nodeLft", nodeLft);
		jb.setParameter("nodeRgt", nodeRgt);
		jb.executeUpdate(entityManager);
		if (log.isDebugEnabled()) {
			log.debug("adjust self hql: {}, offset={}, nodeLft={}, nodeRgt={}",
					new Object[] { hql, offset, nodeLft, nodeRgt });
		}

		// 最后删除（清空位置）
		String hql1 = "update " + beanName + " bean set bean." + tree.getRgtName() + " = bean." + tree.getRgtName()
				+ " - " + span + " WHERE bean." + tree.getRgtName() + " > :nodeRgt";
		String hql2 = "update " + beanName + " bean set bean." + tree.getLftName() + " = bean." + tree.getLftName()
				+ " - " + span + " WHERE bean." + tree.getLftName() + " > :nodeRgt";
		if (!StringUtils.isBlank(tree.getTreeCondition())) {
			hql1 += " and (" + tree.getTreeCondition() + ")";
			hql2 += " and (" + tree.getTreeCondition() + ")";
		}
		JpaQueryBuilder updateJB = new JpaQueryBuilder(hql1);
		updateJB.setParameter("nodeRgt", nodeRgt);
		updateJB.executeUpdate(entityManager);
		JpaQueryBuilder updateJB2 = new JpaQueryBuilder(hql2);
		updateJB2.setParameter("nodeRgt", nodeRgt);
		updateJB2.executeUpdate(entityManager);
		if (log.isDebugEnabled()) {
			log.debug("clear span hql1:{}, hql2:{}, nodeRgt:{}", new Object[] { hql1, hql2, nodeRgt });
		}
		entityManager.setFlushMode(FlushModeType.AUTO);
		return true;
	}

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (entity instanceof AbstractTreeDomain) {
			AbstractTreeDomain<?, ?> tree = (AbstractTreeDomain<?, ?>) entity;
			String beanName = tree.getClass().getName();
			getEntityManager();
			String hql = "select bean." + tree.getLftName() + " from " + beanName + " bean where bean.id="
					+ tree.getId();
			entityManager.setFlushMode(FlushModeType.COMMIT);
			Integer myPosition = (Integer) JpaQueryBuilder.getSingleResult(hql, entityManager);
			String hql1 = "update " + beanName + " bean set bean." + tree.getRgtName() + " = bean." + tree.getRgtName()
					+ " - 2 WHERE bean." + tree.getRgtName() + " > :myPosition";
			String hql2 = "update " + beanName + " bean set bean." + tree.getLftName() + " = bean." + tree.getLftName()
					+ " - 2 WHERE bean." + tree.getLftName() + " > :myPosition";
			if (!StringUtils.isBlank(tree.getTreeCondition())) {
				hql1 += " and (" + tree.getTreeCondition() + ")";
				hql2 += " and (" + tree.getTreeCondition() + ")";
			}
			JpaQueryBuilder updateJB = new JpaQueryBuilder(hql1);
			updateJB.setParameter("myPosition", myPosition);
			updateJB.executeUpdate(entityManager);
			JpaQueryBuilder updateJB2 = new JpaQueryBuilder(hql2);
			updateJB2.setParameter("myPosition", myPosition);
			updateJB2.executeUpdate(entityManager);
			entityManager.setFlushMode(FlushModeType.AUTO);
		}
	}

	@SuppressWarnings("rawtypes")
	private void setCreateInfoBeforeSave(Object entity, Object[] state, String[] propertyNames) {
		getEntityManager();
		if (entity instanceof AbstractDelFlagDomain) {
			AbstractDelFlagDomain delDomain = (AbstractDelFlagDomain) entity;
			for (int i = 0; i < propertyNames.length; i++) {
				if (propertyNames[i].equals(delDomain.getHasDeletedName())) {
					/** 业务上可主动设置true false */
					if (state[i] == null) {
						state[i] = false;
					}
				}
			}
			if (entity instanceof AbstractDomain) {
				AbstractDomain domain = (AbstractDomain) entity;
				for (int i = 0; i < propertyNames.length; i++) {
					if (propertyNames[i].equals(domain.getCreateTimeName())) {
						state[i] = new Date();
					}
					if (propertyNames[i].equals(domain.getCreateUserName())) {
						if (state[i] == null) {
							state[i] = CurrUserContextUtils.getCurrentUsername();
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void setUpdateInfoBeforeUpdate(Object entity, Object[] state, String[] propertyNames) {
		if (entity instanceof AbstractDomain) {
			AbstractDomain domain = (AbstractDomain) entity;
			for (int i = 0; i < propertyNames.length; i++) {
				if (propertyNames[i].equals(domain.getUpdateTimeName())) {
					state[i] = new Date();
				}
				if (propertyNames[i].equals(domain.getUpdateUserName())) {
					if (state[i] == null) {
						state[i] = CurrUserContextUtils.getCurrentUsername();
					}
				}
			}
		}
	}

}
