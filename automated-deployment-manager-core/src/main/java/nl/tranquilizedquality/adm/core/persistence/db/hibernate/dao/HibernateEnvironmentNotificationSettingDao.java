/*
 * @(#)HibernateEnvironmentNotificationSettingDao.java 23 okt. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.EnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.hibernate.dao.AbstractHibernateBaseDao;
import nl.tranquilizedquality.adm.core.persistence.dao.EnvironmentNotificationSettingDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironmentNotificationSetting;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate DAO that manages {@link HibernateEnvironment} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 23 okt. 2012
 */
public class HibernateEnvironmentNotificationSettingDao extends
        AbstractHibernateBaseDao<HibernateEnvironmentNotificationSetting, Long>
        implements EnvironmentNotificationSettingDao<HibernateEnvironmentNotificationSetting> {

    @Override
    protected Class<HibernateEnvironmentNotificationSetting> getDomainClass() {
        return HibernateEnvironmentNotificationSetting.class;
    }

    @Override
    public HibernateEnvironmentNotificationSetting newDomainObject() {
        return new HibernateEnvironmentNotificationSetting();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<EnvironmentNotificationSetting> findSettingsForUser(final User user) {
        final Criteria criteria = getDefaultCriteria();
        criteria.add(Restrictions.eq("user", user));
        criteria.addOrder(Order.asc("environment"));

        return criteria.list();
    }

}
