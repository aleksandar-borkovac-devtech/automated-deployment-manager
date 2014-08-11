/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 3 jun. 2011 File: HibernateMavenArtifactDao.java
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

import nl.tranquilizedquality.adm.commons.business.command.MavenArtifactSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.MavenArtifactDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate implementation of a DAO that manages {@link HibernateMavenArtifact}
 * beans.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public class HibernateMavenArtifactDao extends AbstractHibernateBaseDao<HibernateMavenArtifact, Long> implements
        MavenArtifactDao<HibernateMavenArtifact> {

    @Override
    protected Class<HibernateMavenArtifact> getDomainClass() {
        return HibernateMavenArtifact.class;
    }

    @Override
    public HibernateMavenArtifact newDomainObject() {
        return new HibernateMavenArtifact();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MavenArtifact> findBySearchCommand(final MavenArtifactSearchCommand sc) {
        final Session session = getCurrentSession();
        final Criteria criteria = getMavenArtifactCriteria(sc, session);

        configurePagingAndSorting(sc, criteria);

        return criteria.list();
    }

    @Override
    public int findNumberOfMavenArtifacts(final MavenArtifactSearchCommand sc) {
        final Session session = getCurrentSession();
        final Criteria criteria = getMavenArtifactCriteria(sc, session);
        criteria.setProjection(Projections.rowCount());

        final Long count = (Long) criteria.uniqueResult();
        return count.intValue();
    }

    @Override
    public int findMaximumRankInRelease(final Release release) {
        final Criteria criteria = getDefaultCriteria();
        criteria.add(Restrictions.eq("release", release));
        criteria.setProjection(Projections.max("rank"));

        return (Integer) criteria.uniqueResult();
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
    private Criteria getMavenArtifactCriteria(final MavenArtifactSearchCommand sc, final Session session) {
        final Criteria criteria = session.createCriteria(this.domainClass);
        Criteria parentModuleCriteria = null;

        final String name = sc.getName();
        if (!StringUtils.isEmpty(name)) {
            parentModuleCriteria = criteria.createCriteria("parentModule");
            parentModuleCriteria.add(Restrictions.ilike("name", name.trim(), MatchMode.START));
        }

        final ArtifactType type = sc.getType();
        if (type != null) {
            if (parentModuleCriteria == null) {
                parentModuleCriteria = criteria.createCriteria("parentModule");
            }
            parentModuleCriteria.add(Restrictions.eq("type", type));
        }

        final String group = sc.getGroup();
        if (!StringUtils.isEmpty(group)) {
            if (parentModuleCriteria == null) {
                parentModuleCriteria = criteria.createCriteria("parentModule");
            }
            parentModuleCriteria.add(Restrictions.ilike("group", group.trim(), MatchMode.START));
        }

        final String artifactId = sc.getArtifactId();
        if (!StringUtils.isEmpty(artifactId)) {
            if (parentModuleCriteria == null) {
                parentModuleCriteria = criteria.createCriteria("parentModule");
            }
            parentModuleCriteria.add(Restrictions.ilike("artifactId", artifactId.trim(), MatchMode.START));
        }

        Criteria releaseCriteria = null;
        final String releaseName = sc.getReleaseName();
        if (!StringUtils.isEmpty(releaseName)) {
            if (releaseCriteria == null) {
                releaseCriteria = criteria.createCriteria("release");
            }
            releaseCriteria.add(Restrictions.ilike("name", releaseName.trim(), MatchMode.START));
        }

        final ReleaseStatus status = sc.getStatus();
        if (status != null) {
            if (releaseCriteria == null) {
                releaseCriteria = criteria.createCriteria("release");
            }
            releaseCriteria.add(Restrictions.eq("status", status));
        }

        final Date start = sc.getReleaseDateStart();
        if (start != null) {
            if (releaseCriteria == null) {
                releaseCriteria = criteria.createCriteria("release");
            }
            releaseCriteria.add(Restrictions.ge("releaseDate", start));
        }

        final Date end = sc.getReleaseDateEnd();
        if (end != null) {
            if (releaseCriteria == null) {
                releaseCriteria = criteria.createCriteria("release");
            }
            releaseCriteria.add(Restrictions.le("releaseDate", end));
        }

        final List<UserGroup> userGroups = sc.getUserGroups();
        if (userGroups != null && !userGroups.isEmpty()) {
            criteria.add(Restrictions.in("userGroup", userGroups));
        } else {
            criteria.add(Restrictions.isNull("userGroup"));
        }

        final String version = sc.getVersion();
        if (StringUtils.isNotBlank(version)) {
            criteria.add(Restrictions.eq("version", version));
        }

        return criteria;
    }

}
