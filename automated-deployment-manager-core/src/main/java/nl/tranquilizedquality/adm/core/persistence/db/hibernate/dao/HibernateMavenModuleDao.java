/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 3 jun. 2011 File: HibernateMavenModuleDao.java
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

import nl.tranquilizedquality.adm.commons.business.command.MavenModuleSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.MavenModuleDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate implementation of a DAO that manages {@link HibernateMavenModule}
 * beans.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public class HibernateMavenModuleDao extends AbstractHibernateBaseDao<HibernateMavenModule, Long> implements
        MavenModuleDao<HibernateMavenModule> {

    @Override
    protected Class<HibernateMavenModule> getDomainClass() {
        return HibernateMavenModule.class;
    }

    @Override
    public HibernateMavenModule newDomainObject() {
        return new HibernateMavenModule();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MavenModule> findBySearchCommand(final MavenModuleSearchCommand sc) {
        final Session session = getCurrentSession();
        final Criteria criteria = getMavenArtifactCriteria(sc, session);

        configurePagingAndSorting(sc, criteria);

        return criteria.list();
    }

    @Override
    public int findNumberOfMavenArtifacts(final MavenModuleSearchCommand sc) {
        final Session session = getCurrentSession();
        final Criteria criteria = getMavenArtifactCriteria(sc, session);
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
    private Criteria getMavenArtifactCriteria(final MavenModuleSearchCommand sc, final Session session) {
        final Criteria criteria = session.createCriteria(this.domainClass);

        final String name = sc.getName();
        if (!StringUtils.isEmpty(name)) {
            criteria.add(Restrictions.ilike("name", name.trim(), MatchMode.START));
        }

        final ArtifactType type = sc.getType();
        if (type != null) {
            criteria.add(Restrictions.eq("type", type));
        }

        final String group = sc.getGroup();
        if (!StringUtils.isEmpty(group)) {
            criteria.add(Restrictions.ilike("group", group.trim(), MatchMode.START));
        }

        final String artifactId = sc.getArtifactId();
        if (!StringUtils.isEmpty(artifactId)) {
            criteria.add(Restrictions.ilike("artifactId", artifactId.trim(), MatchMode.START));
        }

        final List<UserGroup> userGroups = sc.getUserGroups();
        if (userGroups != null && !userGroups.isEmpty()) {
            criteria.add(Restrictions.in("userGroup", userGroups));
        } else {
            criteria.add(Restrictions.isNull("userGroup"));
        }

        final Long excludeMavenModuleId = sc.getExcludeMavenModuleId();
        if (excludeMavenModuleId != null) {
            criteria.add(Restrictions.ne("id", excludeMavenModuleId));
        }

        return criteria;
    }
}
