/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 3 jun. 2011 File: HibernateReleaseExecutionDao.java
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

import nl.tranquilizedquality.adm.commons.business.command.ReleaseExecutionSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseExecutionDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateReleaseExecution;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate DAO that manages {@link HibernateReleaseExecution} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public class HibernateReleaseExecutionDao extends AbstractHibernateBaseDao<HibernateReleaseExecution, Long> implements ReleaseExecutionDao<HibernateReleaseExecution> {

	@Override
	protected Class<HibernateReleaseExecution> getDomainClass() {
		return HibernateReleaseExecution.class;
	}

	@Override
	public HibernateReleaseExecution newDomainObject() {
		return new HibernateReleaseExecution();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ReleaseExecution> findBySearchCommand(final ReleaseExecutionSearchCommand sc) {
		final Criteria criteria = createCriteria(sc);

		configurePagingAndSorting(sc, criteria);

		return criteria.list();
	}

	@Override
	public int findNumberOfReleaseExecutionsBySearchCommand(final ReleaseExecutionSearchCommand sc) {
		final Criteria criteria = createCriteria(sc);
		criteria.setProjection(Projections.rowCount());

		final Long count = (Long) criteria.uniqueResult();
		return count.intValue();
	}

	/**
	 * Creates the hibernate criteria based on the specified {@link Release}.
	 * 
	 * @param release
	 *            The release that will be used to create the hibernate
	 *            criteria.
	 * @return Returns the hibernate criteria.
	 */
	private Criteria createCriteria(final ReleaseExecutionSearchCommand sc) {
		final Criteria criteria = getDefaultCriteria();

		final Release release = sc.getRelease();
		if (release != null) {
			criteria.add(Restrictions.eq("release", release));
		}

		return criteria;
	}

}
