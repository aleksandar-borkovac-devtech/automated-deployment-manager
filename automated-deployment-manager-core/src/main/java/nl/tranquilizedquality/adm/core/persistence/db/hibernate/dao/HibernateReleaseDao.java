/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 3 jun. 2011 File: HibernateReleaseDao.java
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

import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.ReleaseSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

/**
 * Hibernate implementation of a DAO that manages {@link HibernateRelease} beans.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public class HibernateReleaseDao extends AbstractHibernateBaseDao<HibernateRelease, Long> implements ReleaseDao<HibernateRelease> {

    @Override
    protected Class<HibernateRelease> getDomainClass() {
        return HibernateRelease.class;
    }

    @Override
    public HibernateRelease newDomainObject() {
        return new HibernateRelease();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Release> findBySearchCommand(final ReleaseSearchCommand sc) {
        final Session session = getCurrentSession();
        final Criteria criteria = getReleaseCriteria(sc, session);

        configurePagingAndSorting(sc, criteria);

        return criteria.list();
    }

    @Override
    public int findNumberOfReleases(final ReleaseSearchCommand sc) {
        final Session session = getCurrentSession();
        final Criteria criteria = getReleaseCriteria(sc, session);
        criteria.setProjection(Projections.rowCount());

        final Long count = (Long) criteria.uniqueResult();
        return count.intValue();
    }

    /**
     * Creates a hibernate criteria object based on the passed in search
     * criteria.
     * 
     * @param sc
     *        The search criteria.
     * @param session
     *        The session to use for doing the query.
     * @return Returns a {@link Criteria}.
     */
    private Criteria getReleaseCriteria(final ReleaseSearchCommand sc, final Session session) {
        final Criteria criteria = session.createCriteria(this.domainClass);

        final boolean archived = sc.isArchived();
        criteria.add(Restrictions.eq("archived", archived));

        final String releaseName = sc.getReleaseName();
        if (!StringUtils.isEmpty(releaseName)) {
            criteria.add(Restrictions.ilike("name", releaseName.trim(), MatchMode.START));
        }

        final ReleaseStatus status = sc.getStatus();
        if (status != null) {
            criteria.add(Restrictions.eq("status", status));
        }

        final Date start = sc.getReleaseDateStart();
        if (start != null) {
            criteria.add(Restrictions.ge("releaseDate", start));
        }

        final Date end = sc.getReleaseDateEnd();
        if (end != null) {
            criteria.add(Restrictions.le("releaseDate", end));
        }

        final List<UserGroup> userGroups = sc.getUserGroups();
        if (userGroups != null && !userGroups.isEmpty()) {
            criteria.add(Restrictions.in("userGroup", userGroups));
        } else {
            criteria.add(Restrictions.isNull("userGroup"));
        }

        /*
         * Artifact criteria.
         */
        Criteria parentModuleCriteria = null;
        Criteria artifactsCriteria = null;
        final String name = sc.getName();
        if (!StringUtils.isEmpty(name)) {
            artifactsCriteria = criteria.createCriteria("artifacts", JoinType.LEFT_OUTER_JOIN);
            parentModuleCriteria = artifactsCriteria.createCriteria("parentModule");
            parentModuleCriteria.add(Restrictions.ilike("name", name.trim(), MatchMode.START));
        }

        final ArtifactType type = sc.getType();
        if (type != null) {
            if (artifactsCriteria == null) {
                artifactsCriteria = criteria.createCriteria("artifacts", JoinType.LEFT_OUTER_JOIN);
            }

            if (parentModuleCriteria == null) {
                parentModuleCriteria = artifactsCriteria.createCriteria("parentModule");
            }

            parentModuleCriteria.add(Restrictions.eq("type", type));
        }

        final String group = sc.getGroup();
        if (!StringUtils.isEmpty(group)) {
            if (artifactsCriteria == null) {
                artifactsCriteria = criteria.createCriteria("artifacts", JoinType.LEFT_OUTER_JOIN);
            }

            if (parentModuleCriteria == null) {
                parentModuleCriteria = artifactsCriteria.createCriteria("parentModule");
            }

            parentModuleCriteria.add(Restrictions.ilike("group", group.trim(), MatchMode.START));
        }

        final String artifactId = sc.getArtifactId();
        if (!StringUtils.isEmpty(artifactId)) {
            if (artifactsCriteria == null) {
                artifactsCriteria = criteria.createCriteria("artifacts", JoinType.LEFT_OUTER_JOIN);
            }

            if (parentModuleCriteria == null) {
                parentModuleCriteria = artifactsCriteria.createCriteria("parentModule");
            }

            parentModuleCriteria.add(Restrictions.ilike("artifactId", artifactId.trim(), MatchMode.START));
        }

        return criteria;
    }
}
