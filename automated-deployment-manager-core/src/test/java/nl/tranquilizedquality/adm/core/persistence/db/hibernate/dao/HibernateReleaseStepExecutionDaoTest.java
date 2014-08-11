/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 28 jul. 2011 File: HibernateReleaseStepExecutionDaoTest.java
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.DeployStatus;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.core.persistence.dao.EnvironmentDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateReleaseExecution;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateReleaseStepExecution;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateMavenArtifactDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateMavenModuleDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateReleaseDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateReleaseExecutionDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateReleaseStepExecutionDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserGroupDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test for {@link HibernateReleaseStepExecutionDao}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 jul. 2011
 */
public class HibernateReleaseStepExecutionDaoTest extends AbstractDaoTest {

    /** DAO that will be tested. */
    @Autowired
    private HibernateReleaseStepExecutionDao releaseStepExecutionDao;

    @Autowired
    private HibernateReleaseExecutionDao releaseHistoryDao;

    @Autowired
    private HibernateReleaseDao releaseDao;

    @Autowired
    private HibernateMavenModuleDao mavenModuleDao;

    @Autowired
    private HibernateMavenArtifactDao mavenArtifactDao;

    @Autowired
    private UserDao<User> userDao;

    @Autowired
    private UserGroupDao<UserGroup> userGroupDao;

    @Autowired
    private EnvironmentDao<Environment> environmentDao;

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

        final HibernateUserGroup userGroup = new HibernateUserGroup();
        userGroup.setCreated(new Date());
        userGroup.setCreatedBy("s-petrus");
        userGroup.setName("adminsitrators");
        userGroup.setUsers(users);

        userGroupDao.save(userGroup);
        userGroupDao.flush();

        /*
         * Create release.
         */
        final HibernateRelease release = new HibernateRelease();
        release.setName("Sprint 1");
        release.setReleaseDate(new Date());
        release.setCreated(new Date());
        release.setCreatedBy("s-petrus");
        release.setUserGroup(userGroup);

        releaseDao.save(release);
        releaseDao.flush();

        /*
         * Create module.
         */
        final HibernateMavenModule module = new HibernateMavenModule();
        module.setName("adm-dist");
        module.setType(ArtifactType.TAR_GZIP);
        module.setGroup("nl.Tranquilized Quality.adm");
        module.setArtifactId("adm-dist");
        module.setTargetSystemShutdown(true);
        module.setTargetSystemStartup(true);
        module.setCreated(new Date());
        module.setCreatedBy("s-petrus");
        module.setUserGroup(userGroup);

        mavenModuleDao.save(module);
        mavenModuleDao.flush();

        /*
         * Create artifact.
         */
        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifact.setVersion("1.0.0-SNAPSHOT");
        artifact.setParentModule(module);
        artifact.setRelease(release);
        artifact.setTargetSystemShutdown(true);
        artifact.setTargetSystemStartup(true);
        artifact.setCreated(new Date());
        artifact.setCreatedBy("s-petrus");
        artifact.setRank(1);
        artifact.setUserGroup(userGroup);

        mavenArtifactDao.save(artifact);
        mavenArtifactDao.flush();

        /*
         * Create environment.
         */
        final HibernateEnvironment environment = new HibernateEnvironment();
        environment.setName("INT");
        environment.setDescription("Integration Test environment");
        environment.setCreated(new Date());
        environment.setCreatedBy("s-petrus");

        environmentDao.save(environment);
        environmentDao.flush();

        /*
         * Create release history.
         */
        final HibernateReleaseExecution history = new HibernateReleaseExecution();
        history.setRelease(release);
        history.setEnvironment(environment);
        history.setReleaseDate(new Date());
        history.setReleaseStatus(DeployStatus.SUCCESS);
        history.setCreated(new Date());
        history.setCreatedBy("s-petrus");

        releaseHistoryDao.save(history);
        releaseHistoryDao.flush();

        /*
         * Create step execution.
         */
        final HibernateReleaseStepExecution step = new HibernateReleaseStepExecution();
        step.setName("Backup previous installation");
        step.setReleaseExecution(history);
        step.setStatus(DeployStatus.SUCCESS);
        step.setExecutionDate(new Date());
        step.setCreated(new Date());
        step.setCreatedBy("s-petrus");

        releaseStepExecutionDao.save(step);
        releaseStepExecutionDao.flush();
    }

    @Test
    public void testFindAll() {
        final List<HibernateReleaseStepExecution> steps = releaseStepExecutionDao.findAll();

        assertNotNull("No steps!", steps);
        assertEquals("Invalid number of steps!", 1, steps.size());
    }

}
