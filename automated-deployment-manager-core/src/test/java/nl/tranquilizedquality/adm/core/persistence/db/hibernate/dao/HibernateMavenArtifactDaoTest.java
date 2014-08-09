/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 28 jul. 2011 File: HibernateMavenArtifactDaoTest.java
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

import nl.tranquilizedquality.adm.commons.business.command.MavenArtifactSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployer;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployerParameter;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestination;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestinationHost;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateDeployerDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateDestinationDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateDestinationHostDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.dao.HibernateEnvironmentDao;
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
 * Test for {@link HibernateMavenArtifactDao}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 jul. 2011
 */
public class HibernateMavenArtifactDaoTest extends AbstractDaoTest {

    /** DAO that will be tested. */
    @Autowired
    private HibernateMavenArtifactDao mavenArtifactDao;

    @Autowired
    private HibernateMavenModuleDao mavenModuleDao;

    @Autowired
    private HibernateReleaseDao releaseDao;

    @Autowired
    private HibernateDestinationDao destinationDao;

    @Autowired
    private HibernateEnvironmentDao environmentDao;

    @Autowired
    private HibernateDestinationHostDao destinationHostDao;

    @Autowired
    private UserDao<User> userDao;

    @Autowired
    private UserGroupDao<UserGroup> userGroupDao;

    @Autowired
    private HibernateDeployerDao deployerDao;

    private HibernateRelease release;

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

        release = new HibernateRelease();
        release.setName("Sprint 1");
        release.setUserGroup(userGroup);
        release.setReleaseDate(new Date());
        release.setCreated(new Date());
        release.setCreatedBy("s-petrus");

        releaseDao.save(release);
        releaseDao.flush();

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

        final List<DeployerParameter> parameters = new ArrayList<DeployerParameter>();
        parameters.add(parameter);

        destination.setDeployerParameters(parameters);

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

        /*
         * Configure destination for artifact.
         */
        final List<Destination> destinations = new ArrayList<Destination>();
        destinations.add(destination);

        module.setDestinations(destinations);

        mavenModuleDao.save(module);
        mavenModuleDao.flush();

        /*
         * Create artifact.
         */
        HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifact.setVersion("1.0.0-SNAPSHOT");
        artifact.setParentModule(module);
        artifact.setRelease(release);
        artifact.setTargetSystemShutdown(true);
        artifact.setTargetSystemStartup(true);
        artifact.setRank(1);
        artifact.setCreated(new Date());
        artifact.setCreatedBy("s-petrus");
        artifact.setUserGroup(userGroup);

        mavenArtifactDao.save(artifact);
        mavenArtifactDao.flush();

        /*
         * Create artifact.
         */
        artifact = new HibernateMavenArtifact();
        artifact.setVersion("1.2.0-SNAPSHOT");
        artifact.setParentModule(module);
        artifact.setRelease(release);
        artifact.setTargetSystemShutdown(true);
        artifact.setTargetSystemStartup(true);
        artifact.setRank(3);
        artifact.setCreated(new Date());
        artifact.setCreatedBy("s-petrus");
        artifact.setUserGroup(userGroup);

        mavenArtifactDao.save(artifact);
        mavenArtifactDao.flush();

        /*
         * Save destination.
         */
        destinationDao.save(destination);
        destinationDao.flush();

        mavenArtifactDao.save(artifact);
        mavenArtifactDao.flush();
    }

    @Test
    public void testFindAll() {
        final List<HibernateMavenArtifact> artifacts = mavenArtifactDao.findAll();

        assertNotNull("No artifacts!", artifacts);
        assertEquals("Invalid number of artifacts!", 2, artifacts.size());

        final HibernateMavenArtifact artifact = artifacts.get(0);
        final MavenModule parentModule = artifact.getParentModule();

        final List<Destination> destinations = parentModule.getDestinations();

        assertNotNull("No destinations!", destinations);
        assertEquals("Invalid number of destinations!", 1, destinations.size());
    }

    @Test
    public void testFindBySearchCommand() {
        final List<UserGroup> userGroups = new ArrayList<UserGroup>();
        userGroups.add(userGroup);

        final MavenArtifactSearchCommand sc = new MavenArtifactSearchCommand();
        sc.setArtifactId("adm-dist");
        sc.setName("adm-dist");
        sc.setType(ArtifactType.TAR_GZIP);
        sc.setUserGroups(userGroups);

        List<MavenArtifact> artifacts = mavenArtifactDao.findBySearchCommand(sc);

        assertEquals("Invalid number of artifacts!", 2, artifacts.size());

        sc.setArtifactId("adms-dist");

        artifacts = mavenArtifactDao.findBySearchCommand(sc);

        assertEquals("Invalid number of artifacts!", 0, artifacts.size());
    }

    @Test
    public void testFindNumberOfMavenArtifacts() {
        final List<UserGroup> userGroups = new ArrayList<UserGroup>();
        userGroups.add(userGroup);

        final MavenArtifactSearchCommand sc = new MavenArtifactSearchCommand();
        sc.setArtifactId("adm-dist");
        sc.setName("adm-dist");
        sc.setType(ArtifactType.TAR_GZIP);
        sc.setUserGroups(userGroups);

        int count = mavenArtifactDao.findNumberOfMavenArtifacts(sc);

        assertEquals("Invalid number of artifacts!", 2, count);

        sc.setArtifactId("adms-dist");

        count = mavenArtifactDao.findNumberOfMavenArtifacts(sc);

        assertEquals("Invalid number of artifacts!", 0, count);
    }

    @Test
    public void testFindMaximumRankInRelease() {
        final int rank = mavenArtifactDao.findMaximumRankInRelease(release);

        assertEquals("Invalid maximum rank!", 3, rank);
    }

}
