/**
 * <pre>
 * Project: automated-deployment-manager-security Created on: 24 nov. 2011 File: fHibernateUserDao.java
 * Package: nl.Tranquilized Quality.adm.security.persistence.db.hibernate.dao
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
package nl.tranquilizedquality.adm.security.persistence.db.hibernate.dao;

import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.security.business.command.UserSearchCommand;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate DAO that manages {@link HibernateUser} objects.
 * 
 * @author Salomo Petrus
 * @since 24 nov. 2011
 * 
 */
public class HibernateUserDao extends AbstractHibernateBaseDao<HibernateUser, Long> implements UserDao<HibernateUser> {

    @Override
    protected Class<HibernateUser> getDomainClass() {
        return HibernateUser.class;
    }

    @Override
    public HibernateUser newDomainObject() {
        return new HibernateUser();
    }

    @Override
    public User findByEmail(final String email) {
        final Session currentSession = getCurrentSession();
        final Criteria criteria = currentSession.createCriteria(domainClass);
        criteria.setMaxResults(1);

        criteria.add(Restrictions.eq("email", email));

        return (User) criteria.uniqueResult();
    }

    @Override
    public User findActiveUserByUserName(final String userName) {
        final Criteria criteria = getDefaultCriteria();
        criteria.add(Restrictions.eq("userName", userName));
        criteria.add(Restrictions.eq("active", Boolean.TRUE));

        return (User) criteria.uniqueResult();
    }

    @Override
    public void updateLastLoginDate(final User user) {
        final Long id = user.getId();
        final HibernateUser foundUser = findById(id);

        if (foundUser != null) {
            foundUser.setLastLoginDate(new Date());
            save(foundUser);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findUsersBySearchCommand(final UserSearchCommand sc) {
        final Criteria criteria = createUserSearchCriteria(sc);

        configurePagingAndSorting(sc, criteria);

        return criteria.list();
    }

    @Override
    public int findNumberOfUsers(final UserSearchCommand sc) {
        final Criteria criteria = createUserSearchCriteria(sc);
        criteria.setProjection(Projections.rowCount());

        final Long count = (Long) criteria.uniqueResult();
        return count.intValue();
    }

    /**
     * Creates search criteria based on the passed in search command.
     * 
     * @param sc
     *            The search criteria that will be used to create a criteria.
     * @return Returns a hibernate {@link Criteria}.
     */
    private Criteria createUserSearchCriteria(final UserSearchCommand sc) {
        final Criteria criteria = getDefaultCriteria();

        final String userName = sc.getUserName();
        if (StringUtils.isNotBlank(userName)) {
            criteria.add(Restrictions.ilike("userName", userName, MatchMode.START));
        }

        final String name = sc.getName();
        if (StringUtils.isNotBlank(name)) {
            criteria.add(Restrictions.ilike("name", name, MatchMode.START));
        }

        return criteria;
    }

}
