/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 23, 2012 File: fHibernateUserGroupDao.java
 * Package: nl.Tranquilized Quality.adm.core.persistence.db.hibernate.dao
 * 
 * Copyright (c) 2012 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.security.persistence.db.hibernate.dao;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.UserGroupSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserGroupDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * DAO used to manage {@link HibernateUserGroup} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 23, 2012
 */
public class HibernateUserGroupDao extends AbstractHibernateBaseDao<HibernateUserGroup, Long> implements UserGroupDao<HibernateUserGroup> {

	@Override
	protected Class<HibernateUserGroup> getDomainClass() {
		return HibernateUserGroup.class;
	}

	@Override
	public HibernateUserGroup newDomainObject() {
		return new HibernateUserGroup();
	}

	@SuppressWarnings("unchecked")
	public List<UserGroup> findFilteredUserGroups(final UserGroupSearchCommand sc) {
		final Criteria criteria = getDefaultCriteria();

		final List<UserGroup> userGroups = sc.getUserGroups();
		if (userGroups != null && !userGroups.isEmpty()) {
			final List<Long> userGroupIds = new ArrayList<Long>();
			for (final UserGroup userGroup : userGroups) {
				final Long userGroupId = userGroup.getId();
				userGroupIds.add(userGroupId);
			}
			criteria.add(Restrictions.in("id", userGroupIds));
		}
		else {
			criteria.add(Restrictions.isNull("id"));
		}

		return criteria.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserGroup> findUserGroupsBySearchCommand(final UserGroupSearchCommand sc) {
		final Criteria criteria = getUserGroupCriteria(sc);

		configurePagingAndSorting(sc, criteria);

		return criteria.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserGroup> findUserGroupsByUser(final User user) {
		final Criteria criteria = getDefaultCriteria();

		final Criteria usersCriteria = criteria.createCriteria("users");
		usersCriteria.add(Restrictions.eq("id", user.getId()));

		return criteria.list();
	}

	@Override
	public int findNumberUserGroups(final UserGroupSearchCommand sc) {
		final Criteria criteria = getUserGroupCriteria(sc);
		criteria.setProjection(Projections.rowCount());

		final Long count = (Long) criteria.uniqueResult();
		return count.intValue();
	}

	/**
	 * Retrieves the hibernate criteria based on the passed in search criteria.
	 * 
	 * @return Returns a {@link Criteria}.
	 */
	private Criteria getUserGroupCriteria(final UserGroupSearchCommand sc) {
		final Criteria criteria = getDefaultCriteria();

		final String name = sc.getName();
		if (StringUtils.isNotBlank(name)) {
			criteria.add(Restrictions.ilike("name", name, MatchMode.START));
		}

		return criteria;
	}

}
