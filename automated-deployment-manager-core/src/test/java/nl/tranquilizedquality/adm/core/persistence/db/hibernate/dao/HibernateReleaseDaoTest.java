/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 27 jul. 2011 File: HibernateReleaseDaoTest.java
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.ReleaseSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateMavenArtifactDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateMavenModuleDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateReleaseDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserDao;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserGroupDao;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test for {@link HibernateReleaseDao}
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 27 jul. 2011
 */
public class HibernateReleaseDaoTest extends AbstractDaoTest {

    /** DAO that will be tested. */
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

    private HibernateUserGroup userGroup;

    /**
     * Setup before transaction so changes are committed before the test starts.
     * This way the collections can be retrieved properly.
     */
    @Before
    public void setUp() {
        mavenArtifactDao.deleteAll();
        mavenModuleDao.deleteAll();
        releaseDao.deleteAll();

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
        module.setCreated(new Date());
        module.setCreatedBy("s-petrus");
        module.setGroup("nl.Tranquilized Quality.adm");
        module.setArtifactId("adm-dist");
        module.setTargetSystemShutdown(true);
        module.setTargetSystemStartup(true);
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
        artifact.setCreated(new Date());
        artifact.setCreatedBy("s-petrus");
        artifact.setTargetSystemShutdown(true);
        artifact.setTargetSystemStartup(true);
        artifact.setRank(1);
        artifact.setUserGroup(userGroup);

        mavenArtifactDao.save(artifact);
        mavenArtifactDao.flush();
    }

    @Test
    public void testFindAll() {
        final List<HibernateRelease> releases = releaseDao.findAll();

        assertNotNull("No available release!", releases);
        assertEquals("Invalid number of releases!", 1, releases.size());

        final HibernateRelease release = releases.get(0);
        releaseDao.refresh(release);
        final List<MavenArtifact> artifacts = release.getArtifacts();

        assertNotNull("No artifacts!", artifacts);
        assertEquals("No artifacts!", 1, artifacts.size());

        final MavenArtifact mavenArtifact = artifacts.iterator().next();
        final MavenModule parentModule = mavenArtifact.getParentModule();
        final String artifactId = parentModule.getArtifactId();

        assertEquals("Invalid artifact ID!", "adm-dist", artifactId);
    }

    @Test
    public void testFindBySearchCommand() {
        final List<UserGroup> userGroups = new ArrayList<UserGroup>();
        userGroups.add(userGroup);

        final ReleaseSearchCommand sc = new ReleaseSearchCommand();
        sc.setReleaseName("Sprint 1");
        sc.setStatus(ReleaseStatus.DRAFT);

        final Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, -1);
        sc.setReleaseDateStart(instance.getTime());
        sc.setArtifactId("adm-dist");
        sc.setUserGroups(userGroups);

        List<Release> releases = releaseDao.findBySearchCommand(sc);

        assertEquals("Invalid number of releases!", 1, releases.size());

        instance.add(Calendar.YEAR, 1);
        sc.setReleaseDateStart(instance.getTime());

        releases = releaseDao.findBySearchCommand(sc);

        assertEquals("Invalid number of releases!", 0, releases.size());
    }

    @Test
    public void testFindNumberOfReleases() {
        final List<UserGroup> userGroups = new ArrayList<UserGroup>();
        userGroups.add(userGroup);

        final ReleaseSearchCommand sc = new ReleaseSearchCommand();
        sc.setReleaseName("Sprint 1");
        sc.setStatus(ReleaseStatus.DRAFT);
        sc.setUserGroups(userGroups);

        final Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, -1);
        sc.setReleaseDateStart(instance.getTime());
        int count = releaseDao.findNumberOfReleases(sc);

        assertEquals("Invalid number of releases!", 1, count);

        instance.add(Calendar.YEAR, 1);
        sc.setReleaseDateStart(instance.getTime());

        count = releaseDao.findNumberOfReleases(sc);

        assertEquals("Invalid number of releases!", 0, count);
    }

    @Test
    public void testFindBySearchCommandArchived() {
        final List<UserGroup> userGroups = new ArrayList<UserGroup>();
        userGroups.add(userGroup);

        final ReleaseSearchCommand sc = new ReleaseSearchCommand();
        sc.setReleaseName("Sprint 1");
        sc.setStatus(ReleaseStatus.DRAFT);

        final Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, -1);
        sc.setReleaseDateStart(instance.getTime());
        sc.setArtifactId("adm-dist");
        sc.setUserGroups(userGroups);
        sc.setArchived(true);

        final List<Release> releases = releaseDao.findBySearchCommand(sc);

        assertEquals("Invalid number of releases!", 0, releases.size());
    }

}
