/*
 * @(#)UserSettingsManagerImpl.java 23 okt. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.manager.impl;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.EnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.core.business.manager.UserSettingsManager;
import nl.tranquilizedquality.adm.core.persistence.dao.EnvironmentDao;
import nl.tranquilizedquality.adm.core.persistence.dao.EnvironmentNotificationSettingDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironmentNotificationSetting;

import org.springframework.beans.factory.annotation.Required;

/**
 * Manager that manages the user settings.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 23 okt. 2012
 */
public class UserSettingsManagerImpl implements UserSettingsManager {

    /** DAO used to retrieve environments. */
    private EnvironmentDao<Environment> environmentDao;

    /** DAO used to manage the environment settings. */
    private EnvironmentNotificationSettingDao<EnvironmentNotificationSetting> environmentNotificationSettingDao;

    @Override
    public void storeSettings(final List<EnvironmentNotificationSetting> settings) {

        for (final EnvironmentNotificationSetting environmentNotificationSetting : settings) {
            if (environmentNotificationSetting.isPersistent()) {
                final Long id = environmentNotificationSetting.getId();
                final EnvironmentNotificationSetting setting = environmentNotificationSettingDao.findById(id);
                setting.copy(environmentNotificationSetting);

                environmentNotificationSettingDao.save(setting);
            } else {
                environmentNotificationSettingDao.save(environmentNotificationSetting);
            }
        }

    }

    @Override
    public List<EnvironmentNotificationSetting> findSettingsForUser(final User user) {
        List<EnvironmentNotificationSetting> settings = environmentNotificationSettingDao.findSettingsForUser(user);

        if (settings.isEmpty()) {
            settings = createDefaultNotificationSettingsForUser(user);
        }

        return settings;
    }

    @Override
    public List<EnvironmentNotificationSetting> createDefaultNotificationSettingsForUser(final User user) {

        final List<EnvironmentNotificationSetting> settings = new ArrayList<EnvironmentNotificationSetting>();
        final List<Environment> environments = environmentDao.findAll();
        for (final Environment environment : environments) {
            final List<User> users = environment.getUsers();
            final boolean userIsDeployer = users.contains(user);

            final HibernateEnvironmentNotificationSetting setting = new HibernateEnvironmentNotificationSetting();
            setting.setEmailNotification(userIsDeployer);
            setting.setSmsNotification(false);
            setting.setEnvironment(environment);
            setting.setUser(user);
            environmentNotificationSettingDao.save(setting);
            settings.add(setting);
        }

        return settings;
    }

    @Required
    public void setEnvironmentNotificationSettingDao(
            final EnvironmentNotificationSettingDao<EnvironmentNotificationSetting> environmentNotificationSettingDao) {
        this.environmentNotificationSettingDao = environmentNotificationSettingDao;
    }

    @Required
    public void setEnvironmentDao(final EnvironmentDao<Environment> environmentDao) {
        this.environmentDao = environmentDao;
    }

}
