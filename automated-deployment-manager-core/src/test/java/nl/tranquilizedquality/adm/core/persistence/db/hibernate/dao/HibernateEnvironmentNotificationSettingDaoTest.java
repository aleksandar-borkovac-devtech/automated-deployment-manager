/*
 * @(#)HibernateEnvironmentNotificationSettingDaoTest.java 23 okt. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployer;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployerParameter;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestination;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestinationHost;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateDeployerDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateDestinationDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateDestinationHostDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateEnvironmentDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateEnvironmentNotificationSettingDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserGroupDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test for {@link HibernateEnvironmentNotificationSettingDao}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 23 okt. 2012
 */
public class HibernateEnvironmentNotificationSettingDaoTest extends AbstractDaoTest {

    /** DAO that will be tested. */
    @Autowired
    private HibernateEnvironmentNotificationSettingDao environmentNotificationSettingDao;

    @Autowired
    private HibernateDestinationDao destinationDao;

    @Autowired
    private HibernateEnvironmentDao environmentDao;

    @Autowired
    private HibernateDestinationHostDao destinationHostDao;

    @Autowired
    private HibernateDeployerDao deployerDao;

    @Autowired
    private UserDao<User> userDao;

    @Autowired
    private UserGroupDao<UserGroup> userGroupDao;

    private HibernateUserGroup userGroup;

    @Before
    public void setUp() {
        final HibernateUser user = new HibernateUser();
        user.setActive(true);
        user.setActiveFrom(new Date());
        user.setCreated(new Date());
        user.setCreatedBy("s-petrus");
        user.setName("Salomo Petrus");
        user.setPassword("asfdasdfas");
        user.setUserName("s-petrus");
        user.setPasswordSent(true);
        user.setBlocked(false);

        userDao.save(user);
        userDao.flush();

        final List<User> users = new ArrayList<User>();
        users.add(user);

        userGroup = new HibernateUserGroup();
        userGroup.setCreated(new Date());
        userGroup.setCreatedBy("s-petrus");
        userGroup.setName("adminsitrators");
        userGroup.setUsers(users);

        userGroupDao.save(userGroup);
        userGroupDao.flush();

        /*
         * Add environment.
         */
        final HibernateEnvironment environment = new HibernateEnvironment();
        environment.setName("dev");
        environment.setDescription("Integration Test environment");
        environment.setCreated(new Date());
        environment.setCreatedBy("s-petrus");

        environmentDao.save(environment);
        environmentDao.flush();

        /*
         * Add destination host.
         */
        final HibernateDestinationHost destinationHost = new HibernateDestinationHost();
        destinationHost.setHostName("cybertron");
        destinationHost.setUsername("megatron");
        destinationHost.setPassword("energon");
        destinationHost.setPort(22);
        destinationHost.setProtocol(Protocol.SSH);
        destinationHost.setUserGroup(userGroup);

        destinationHostDao.save(destinationHost);
        destinationHostDao.flush();

        /*
         * Add tomcat deployer.
         */
        final HibernateDeployer tomcatDeployer = new HibernateDeployer();
        tomcatDeployer.setName("TOMCAT");

        deployerDao.save(tomcatDeployer);
        deployerDao.flush();

        /*
         * Add destination.
         */
        final HibernateDestination destination = new HibernateDestination();
        destination.setDeployer(tomcatDeployer);
        destination.setEnvironment(environment);
        destination.setCreated(new Date());
        destination.setCreatedBy("s-petrus");
        destination.setDestinationHost(destinationHost);
        destination.setName("Provisioning INT");
        destination.setUserGroup(userGroup);
        destination.setPrefix("tomcat");

        /*
         * Add location.
         */
        final HibernateDeployerParameter parameter = new HibernateDeployerParameter();
        parameter.setValue("/srv/tomcat/instances/optimus/webapps");
        parameter.setType(DeployerParameterType.WEB_APPS_LOCATION);
        parameter.setRank(1);
        parameter.setCreated(new Date());
        parameter.setCreatedBy("s-petrus");

        final List<DeployerParameter> locations = new ArrayList<DeployerParameter>();
        locations.add(parameter);

        destination.setDeployerParameters(locations);

        /*
         * Save destination.
         */
        destinationDao.save(destination);
        destinationDao.flush();

        final HibernateEnvironmentNotificationSetting settings = new HibernateEnvironmentNotificationSetting();
        settings.setEnvironment(environment);
        settings.setUser(user);
        settings.setCreated(new Date());
        settings.setCreatedBy("s-petrus");

        environmentNotificationSettingDao.save(settings);

    }

    @Test
    public void testFindAll() {
        final List<HibernateEnvironmentNotificationSetting> all = environmentNotificationSettingDao.findAll();
        assertFalse("No settings found!", all.isEmpty());
        assertEquals("Invalid number of settings!", 1, all.size());
    }

}
