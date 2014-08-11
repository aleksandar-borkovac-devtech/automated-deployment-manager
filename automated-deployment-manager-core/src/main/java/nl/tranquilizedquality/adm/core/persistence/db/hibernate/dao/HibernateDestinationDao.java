/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 3 jun. 2011 File: HibernateDestinationDao.java
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

import nl.tranquilizedquality.adm.commons.business.command.DestinationSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.DestinationDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestination;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate implementation of a destination DAO.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public class HibernateDestinationDao extends AbstractHibernateBaseDao<HibernateDestination, Long> implements
        DestinationDao<HibernateDestination> {

    @Override
    protected Class<HibernateDestination> getDomainClass() {
        return HibernateDestination.class;
    }

    @Override
    public HibernateDestination newDomainObject() {
        return new HibernateDestination();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Destination> findBySearchCommand(final DestinationSearchCommand sc) {
        final Session session = getCurrentSession();
        final Criteria criteria = getDestinationCriteria(sc, session);

        configurePagingAndSorting(sc, criteria);

        return criteria.list();
    }

    @Override
    public int findNumberOfDestinations(final DestinationSearchCommand sc) {
        final Session session = getCurrentSession();
        final Criteria criteria = getDestinationCriteria(sc, session);
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
    private Criteria getDestinationCriteria(final DestinationSearchCommand sc, final Session session) {
        final Criteria criteria = session.createCriteria(this.domainClass);

        final String environment = sc.getEnvironment();
        if (!StringUtils.isEmpty(environment)) {
            final Criteria environmentCriteria = criteria.createCriteria("environment");
            environmentCriteria.add(Restrictions.ilike("name", environment.trim(), MatchMode.START));
        }

        Criteria destinationHostCriteria = null;
        final String hostName = sc.getHostName();
        if (!StringUtils.isEmpty(hostName)) {
            destinationHostCriteria = criteria.createCriteria("destinationHost");
            destinationHostCriteria.add(Restrictions.eq("hostName", hostName));
        }

        final Protocol protocol = sc.getProtocol();
        if (protocol != null) {
            if (destinationHostCriteria == null) {
                destinationHostCriteria = criteria.createCriteria("destinationHost");
            }
            destinationHostCriteria.add(Restrictions.eq("protocol", protocol));
        }

        final List<UserGroup> userGroups = sc.getUserGroups();
        if (userGroups != null && !userGroups.isEmpty()) {
            criteria.add(Restrictions.in("userGroup", userGroups));
        } else {
            criteria.add(Restrictions.isNull("userGroup"));
        }

        return criteria;
    }

}
