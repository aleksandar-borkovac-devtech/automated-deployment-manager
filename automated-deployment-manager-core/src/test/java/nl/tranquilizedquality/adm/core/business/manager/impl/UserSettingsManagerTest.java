/*
 * @(#)UserSettingsManagerTest.java 23 okt. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.manager.impl;

import static junit.framework.Assert.assertNotNull;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.EnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.core.business.manager.UserSettingsManager;
import nl.tranquilizedquality.adm.core.business.manager.impl.UserSettingsManagerImpl;
import nl.tranquilizedquality.adm.core.persistence.dao.EnvironmentDao;
import nl.tranquilizedquality.adm.core.persistence.dao.EnvironmentNotificationSettingDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link UserSettingsManager}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 23 okt. 2012
 */
public class UserSettingsManagerTest extends EasyMockSupport {

    /** Manager that will be tested. */
    private UserSettingsManagerImpl userSettingsManager;

    /** Mocked DAO. */
    private EnvironmentDao<Environment> environmentDao;

    /** Mocked DAO. */
    private EnvironmentNotificationSettingDao<EnvironmentNotificationSetting> environmentNotificationSettingDao;

    /**
     * @throws java.lang.Exception
     */
    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        userSettingsManager = new UserSettingsManagerImpl();

        environmentDao = createMock(EnvironmentDao.class);
        environmentNotificationSettingDao = createMock(EnvironmentNotificationSettingDao.class);

        userSettingsManager.setEnvironmentDao(environmentDao);
        userSettingsManager.setEnvironmentNotificationSettingDao(environmentNotificationSettingDao);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.UserSettingsManagerImpl#storeSettings(java.util.List)}
     * .
     */
    @Test
    public void testStoreSettings() {

        final List<EnvironmentNotificationSetting> settings = new ArrayList<EnvironmentNotificationSetting>();
        final HibernateEnvironmentNotificationSetting setting = new HibernateEnvironmentNotificationSetting();
        setting.setEmailNotification(true);
        setting.setUser(new HibernateUser());
        setting.setEnvironment(new HibernateEnvironment());
        settings.add(setting);

        expect(environmentNotificationSettingDao.save(setting)).andReturn(setting);

        replayAll();

        userSettingsManager.storeSettings(settings);

        verifyAll();
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.UserSettingsManagerImpl#storeSettings(java.util.List)}
     * .
     */
    @Test
    public void testStoreSettingsExcisting() {

        final List<EnvironmentNotificationSetting> settings = new ArrayList<EnvironmentNotificationSetting>();
        final HibernateEnvironmentNotificationSetting setting = new HibernateEnvironmentNotificationSetting();
        setting.setId(1L);
        setting.setEmailNotification(true);
        setting.setUser(new HibernateUser());
        setting.setEnvironment(new HibernateEnvironment());
        settings.add(setting);

        expect(environmentNotificationSettingDao.findById(1L)).andReturn(setting);
        expect(environmentNotificationSettingDao.save(setting)).andReturn(setting);

        replayAll();

        userSettingsManager.storeSettings(settings);

        verifyAll();
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.UserSettingsManagerImpl#findSettingsForUser(nl.tranquilizedquality.adm.commons.business.domain.User)}
     * .
     */
    @Test
    public void testFindSettingsForUser() {

        final HibernateUser user = new HibernateUser();

        final List<Environment> environments = new ArrayList<Environment>();
        final HibernateEnvironment environment = new HibernateEnvironment();
        environment.setName("INT");
        environments.add(environment);

        expect(environmentNotificationSettingDao.findSettingsForUser(user)).andReturn(
                new ArrayList<EnvironmentNotificationSetting>());
        expect(environmentDao.findAll()).andReturn(environments);
        expect(environmentNotificationSettingDao.save(isA(EnvironmentNotificationSetting.class))).andReturn(
                new HibernateEnvironmentNotificationSetting());

        replayAll();

        userSettingsManager.findSettingsForUser(user);

        verifyAll();
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.UserSettingsManagerImpl#createDefaultNotificationSettingsForUser(nl.tranquilizedquality.adm.commons.business.domain.User)}
     * .
     */
    @Test
    public void testCreateDefaultNotificationSettingsForUser() {

        final HibernateUser user = new HibernateUser();
        final List<Environment> environments = new ArrayList<Environment>();
        final HibernateEnvironment environment = new HibernateEnvironment();
        environment.setName("INT");
        environments.add(environment);

        expect(environmentDao.findAll()).andReturn(environments);
        expect(environmentNotificationSettingDao.save(isA(EnvironmentNotificationSetting.class))).andReturn(
                new HibernateEnvironmentNotificationSetting());

        replayAll();

        final List<EnvironmentNotificationSetting> settings = userSettingsManager.createDefaultNotificationSettingsForUser(user);

        verifyAll();

        assertNotNull("No settings!", settings);
    }

}
