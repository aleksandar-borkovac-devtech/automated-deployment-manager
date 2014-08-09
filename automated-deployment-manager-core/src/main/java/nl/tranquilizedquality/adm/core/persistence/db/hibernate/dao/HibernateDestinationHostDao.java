/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 30 okt. 2011 File: HibernateDestinationHostDao.java
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

import nl.tranquilizedquality.adm.commons.business.command.DestinationHostSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.DestinationHostDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestinationHost;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate implementation of a DestinationDao.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 okt. 2011
 */
public class HibernateDestinationHostDao extends AbstractHibernateBaseDao<HibernateDestinationHost, Long> implements DestinationHostDao<HibernateDestinationHost> {

	@Override
	protected Class<HibernateDestinationHost> getDomainClass() {
		return HibernateDestinationHost.class;
	}

	@Override
	public HibernateDestinationHost newDomainObject() {
		return new HibernateDestinationHost();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DestinationHost> findBySearchCommand(final DestinationHostSearchCommand sc) {
		final Session session = getCurrentSession();
		final Criteria criteria = getDestinationHostCriteria(sc, session);

		configurePagingAndSorting(sc, criteria);

		return criteria.list();
	}

	@Override
	public int findNumberOfDestinationHosts(final DestinationHostSearchCommand sc) {
		final Session session = getCurrentSession();
		final Criteria criteria = getDestinationHostCriteria(sc, session);
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
	private Criteria getDestinationHostCriteria(final DestinationHostSearchCommand sc, final Session session) {
		final Criteria criteria = getDefaultCriteria();

		final String hostName = sc.getHostName();
		if (!StringUtils.isEmpty(hostName)) {
			criteria.add(Restrictions.eq("hostName", hostName));
		}

		final Protocol protocol = sc.getProtocol();
		if (protocol != null) {
			criteria.add(Restrictions.eq("protocol", protocol));
		}

		final List<UserGroup> userGroups = sc.getUserGroups();
		if (userGroups != null && !userGroups.isEmpty()) {
			criteria.add(Restrictions.in("userGroup", userGroups));
		}
		else {
			criteria.add(Restrictions.isNull("userGroup"));
		}

		return criteria;
	}

}
