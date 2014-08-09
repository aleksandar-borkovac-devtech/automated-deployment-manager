/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 1 sep. 2011 File: HibernateEnvironmentDao.java
 * Package: nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao
 * 
 * Copyright (c) 2011 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.EnvironmentSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.EnvironmentDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate implementation of the environment Dao.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 sep. 2011
 */
public class HibernateEnvironmentDao extends AbstractHibernateBaseDao<HibernateEnvironment, Long> implements EnvironmentDao<HibernateEnvironment> {

	@Override
	protected Class<HibernateEnvironment> getDomainClass() {
		return HibernateEnvironment.class;
	}

	@Override
	public HibernateEnvironment newDomainObject() {
		return new HibernateEnvironment();
	}

	@Override
	public int findNumberOfEnvironmentsBySearchCommand(final EnvironmentSearchCommand sc) {
		final Session session = getCurrentSession();
		final Criteria criteria = getEnvironmentCriteria(sc, session);
		criteria.setProjection(Projections.rowCount());

		final Long count = (Long) criteria.uniqueResult();
		return count.intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Environment> findEnvironmentsBySearchCommand(final EnvironmentSearchCommand sc) {
		final Session session = getCurrentSession();
		final Criteria criteria = getEnvironmentCriteria(sc, session);

		configurePagingAndSorting(sc, criteria);

		return criteria.list();
	}

	/**
	 * Creates a hibernate criteria object based on the passed in search
	 * criteria.
	 * 
	 * @param sc
	 *            The search criteria.
	 * @param session
	 *            The session to use for doing the query.
	 * @return Returns a {@link Criteria}.
	 */
	private Criteria getEnvironmentCriteria(final EnvironmentSearchCommand sc, final Session session) {
		final Criteria criteria = getDefaultCriteria();

		final String environmentName = sc.getName();
		if (!StringUtils.isEmpty(environmentName)) {
			criteria.add(Restrictions.ilike("name", environmentName.trim(), MatchMode.START));
		}

		final String prefix = sc.getPrefix();
		if (prefix != null) {
			criteria.add(Restrictions.eq("status", prefix));
		}

		return criteria;
	}

}
