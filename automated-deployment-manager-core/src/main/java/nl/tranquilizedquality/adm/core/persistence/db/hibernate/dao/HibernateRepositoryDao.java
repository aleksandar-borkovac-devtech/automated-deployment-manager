/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 3 jun. 2011 File: HibernateRepositoryDao.java
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

import nl.tranquilizedquality.adm.commons.business.command.RepositorySearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.RepositoryDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRepository;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate implementation of a DAO that manages {@link HibernateRepository}
 * beans.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public class HibernateRepositoryDao extends AbstractHibernateBaseDao<HibernateRepository, Long> implements RepositoryDao<HibernateRepository> {

	@Override
	protected Class<HibernateRepository> getDomainClass() {
		return HibernateRepository.class;
	}

	@Override
	public HibernateRepository newDomainObject() {
		return new HibernateRepository();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Repository> findBySearchCommand(final RepositorySearchCommand sc) {
		final Session session = getCurrentSession();
		final Criteria criteria = getRepositoryCriteria(sc, session);

		configurePagingAndSorting(sc, criteria);

		return criteria.list();
	}

	@Override
	public int findNumberOfRepositories(final RepositorySearchCommand sc) {
		final Session session = getCurrentSession();
		final Criteria criteria = getRepositoryCriteria(sc, session);
		criteria.setProjection(Projections.rowCount());

		final Long count = (Long) criteria.uniqueResult();
		return count.intValue();
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
	private Criteria getRepositoryCriteria(final RepositorySearchCommand sc, final Session session) {
		final Criteria criteria = session.createCriteria(this.domainClass);

		final String name = sc.getName();
		if (!StringUtils.isEmpty(name)) {
			criteria.add(Restrictions.ilike("name", name.trim(), MatchMode.START));
		}

		final Boolean enabled = sc.getEnabled();
		if (enabled != null) {
			criteria.add(Restrictions.eq("enabled", enabled));
		}

		return criteria;
	}

}
