/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 3 jul. 2011 File: ArtifactManagerTest.java
 * Package: nl.tranquilizedquality.adm.core.business.manager.impl
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
package nl.tranquilizedquality.adm.core.business.manager.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.commons.util.http.HttpUtil;
import nl.tranquilizedquality.adm.core.business.manager.impl.ArtifactManagerImpl;
import nl.tranquilizedquality.adm.core.persistence.dao.RepositoryDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * Test for {@link ArtifactManagerImpl}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jul. 2011
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class ArtifactManagerTest {

    /** Manager that will be tested. */
    private ArtifactManagerImpl artifactManager;

    /** Mocked DAO. */
    private RepositoryDao<Repository> repositoryDao;

    /** Mocked HTTP utility. */
    private HttpUtil httpUtil;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        artifactManager = new ArtifactManagerImpl();

        repositoryDao = createMock(RepositoryDao.class);
        httpUtil = createMock(HttpUtil.class);

        artifactManager.setRepositoryDao(repositoryDao);
        artifactManager.setWorkDirectory("target/");
        artifactManager.setHttpUtil(httpUtil);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.ArtifactManagerImpl#retrieveArtifact(nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact, java.lang.String)}
     * .
     */
    @Test
    public void testRetrieveArtifact() throws Exception {
        final HibernateRepository repository = new HibernateRepository();
        repository.setEnabled(true);
        repository.setId(1L);
        repository.setName("Development Nexus");
        repository.setRepositoryUrl("http://development/nexus/content/repositories/releases/");

        final HibernateMavenModule module = new HibernateMavenModule();
        module.setId(1L);
        module.setName("dam-gwt-gui");
        module.setType(ArtifactType.WAR);
        module.setGroup("nl.Tranquilized Quality.dam");
        module.setArtifactId("dam-gwt-gui");

        final HibernateRelease release = new HibernateRelease();
        release.setId(1L);
        release.setName("11.4");
        release.setReleaseDate(new Date());

        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifact.setParentModule(module);
        artifact.setVersion("1.0.0-M1");
        artifact.setRelease(release);

        final ArrayList<Repository> repositories = new ArrayList<Repository>();
        repositories.add(repository);

        expect(repositoryDao.findAll()).andReturn(repositories);
        expect(httpUtil.downloadFile(isA(String.class), isA(String.class), isA(String.class))).andReturn(
                new File("target/11.4/artifacts/dam-gwt-gui-1.0.0-M1.war"));

        replay(repositoryDao);
        replay(httpUtil);

        final File retrievedArtifact = artifactManager.retrieveArtifact(artifact, "11.4");

        verify(repositoryDao);
        verify(httpUtil);

        assertNotNull("No artifact!", retrievedArtifact);

        final String operatingSystem = System.getProperty("os.name");
        if (operatingSystem != null && operatingSystem.startsWith("Windows")) {
            assertEquals("target\\11.4\\artifacts\\dam-gwt-gui-1.0.0-M1.war", retrievedArtifact.getPath());
        } else {
            assertEquals("target/11.4/artifacts/dam-gwt-gui-1.0.0-M1.war", retrievedArtifact.getPath());
        }

    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.ArtifactManagerImpl#retrieveArtifact(nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact, java.lang.String)}
     * .
     */
    @Test
    public void testRetrieveArtifactNexusRepository() throws Exception {
        final HibernateRepository repository = new HibernateRepository();
        repository.setEnabled(true);
        repository.setId(1L);
        repository.setName("Development Nexus");
        repository.setRepositoryUrl("http://localhost.itservices.lan/nexus/service/local/artifact/maven/redirect");
        repository.setRepositoryId("public-snapshots");

        final HibernateMavenModule module = new HibernateMavenModule();
        module.setId(1L);
        module.setName("dam-gwt-gui");
        module.setType(ArtifactType.WAR);
        module.setGroup("nl.Tranquilized Quality.dam");
        module.setArtifactId("dam-gwt-gui");

        final HibernateRelease release = new HibernateRelease();
        release.setId(1L);
        release.setName("11.4");
        release.setReleaseDate(new Date());

        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifact.setParentModule(module);
        artifact.setVersion("1.0.0-M1");
        artifact.setRelease(release);

        final ArrayList<Repository> repositories = new ArrayList<Repository>();
        repositories.add(repository);

        expect(repositoryDao.findAll()).andReturn(repositories);
        expect(
                httpUtil.downloadFile(
                        "http://localhost.itservices.lan/nexus/service/local/artifact/maven/redirect?r=public-snapshots&g=nl.Tranquilized Quality.dam&a=dam-gwt-gui&v=1.0.0-M1&p=war",
                        "target/11.4/artifacts/", "dam-gwt-gui-1.0.0-M1.war")).andReturn(
                new File("target/11.4/artifacts/dam-gwt-gui-1.0.0-M1.war"));

        replay(repositoryDao);
        replay(httpUtil);

        final File retrievedArtifact = artifactManager.retrieveArtifact(artifact, "11.4");

        verify(repositoryDao);
        verify(httpUtil);

        assertNotNull("No artifact!", retrievedArtifact);

        final String operatingSystem = System.getProperty("os.name");
        if (operatingSystem != null && operatingSystem.startsWith("Windows")) {
            assertEquals("target\\11.4\\artifacts\\dam-gwt-gui-1.0.0-M1.war", retrievedArtifact.getPath());
        } else {
            assertEquals("target/11.4/artifacts/dam-gwt-gui-1.0.0-M1.war", retrievedArtifact.getPath());
        }

    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.ArtifactManagerImpl#retrieveArtifact(nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact, java.lang.String)}
     * .
     */
    @Test
    public void testRetrieveDistribution() throws Exception {
        final HibernateRepository repository = new HibernateRepository();
        repository.setEnabled(true);
        repository.setId(1L);
        repository.setName("Development Nexus");
        repository.setRepositoryUrl("http://development/nexus/content/repositories/releases/");

        final HibernateMavenModule module = new HibernateMavenModule();
        module.setId(1L);
        module.setName("Dizizid Administration Module");
        module.setType(ArtifactType.TAR_GZIP);
        module.setGroup("nl.Tranquilized Quality.dam");
        module.setArtifactId("dam-dist");
        module.setIdentifier("bin");

        final HibernateRelease release = new HibernateRelease();
        release.setId(1L);
        release.setName("11.4");
        release.setReleaseDate(new Date());

        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifact.setParentModule(module);
        artifact.setVersion("1.0.0-M1");
        artifact.setRelease(release);

        final ArrayList<Repository> repositories = new ArrayList<Repository>();
        repositories.add(repository);

        expect(repositoryDao.findAll()).andReturn(repositories);
        expect(
                httpUtil.downloadFile(
                        "http://development/nexus/content/repositories/releases/nl/Tranquilized Quality/dam/dam-dist/1.0.0-M1/dam-dist-1.0.0-M1-bin.tar.gz",
                        "target/11.4/artifacts/", "dam-dist-1.0.0-M1.tar.gz")).andReturn(
                new File("target/11.4/artifacts/dam-gwt-gui-1.0.0-M1.war"));

        replay(repositoryDao);
        replay(httpUtil);

        final File retrievedArtifact = artifactManager.retrieveArtifact(artifact, "11.4");

        verify(repositoryDao);
        verify(httpUtil);

        assertNotNull("No artifact!", retrievedArtifact);

        final String operatingSystem = System.getProperty("os.name");
        if (operatingSystem != null && operatingSystem.startsWith("Windows")) {
            assertEquals("target\\11.4\\artifacts\\dam-gwt-gui-1.0.0-M1.war", retrievedArtifact.getPath());
        } else {
            assertEquals("target/11.4/artifacts/dam-gwt-gui-1.0.0-M1.war", retrievedArtifact.getPath());
        }

    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.manager.impl.ArtifactManagerImpl#retrieveArtifact(nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact, java.lang.String)}
     * .
     */
    @Test
    public void testRetrieveDistributionNexusRepository() throws Exception {
        final HibernateRepository repository = new HibernateRepository();
        repository.setEnabled(true);
        repository.setId(1L);
        repository.setName("Development Nexus");
        repository.setRepositoryUrl("http://development/nexus/service/local/artifact/maven/redirect");
        repository.setRepositoryId("public-snapshots");

        final HibernateMavenModule module = new HibernateMavenModule();
        module.setId(1L);
        module.setName("Dizizid Administration Module");
        module.setType(ArtifactType.TAR_GZIP);
        module.setGroup("nl.Tranquilized Quality.dam");
        module.setArtifactId("dam-dist");
        module.setIdentifier("bin");

        final HibernateRelease release = new HibernateRelease();
        release.setId(1L);
        release.setName("11.4");
        release.setReleaseDate(new Date());

        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifact.setParentModule(module);
        artifact.setVersion("1.0.0-M1");
        artifact.setRelease(release);

        final ArrayList<Repository> repositories = new ArrayList<Repository>();
        repositories.add(repository);

        expect(repositoryDao.findAll()).andReturn(repositories);
        expect(
                httpUtil.downloadFile(
                        "http://development/nexus/service/local/artifact/maven/redirect?r=public-snapshots&g=nl.Tranquilized Quality.dam&a=dam-dist&v=1.0.0-M1&c=bin&p=tar.gz",
                        "target/11.4/artifacts/", "dam-dist-1.0.0-M1.tar.gz")).andReturn(
                new File("target/11.4/artifacts/dam-dist-1.0.0-M1.tar.gz"));

        replay(repositoryDao);
        replay(httpUtil);

        final File retrievedArtifact = artifactManager.retrieveArtifact(artifact, "11.4");

        verify(repositoryDao);
        verify(httpUtil);

        assertNotNull("No artifact!", retrievedArtifact);

        final String operatingSystem = System.getProperty("os.name");
        if (operatingSystem != null && operatingSystem.startsWith("Windows")) {
            assertEquals("target\\11.4\\artifacts\\dam-dist-1.0.0-M1.tar.gz", retrievedArtifact.getPath());
        } else {
            assertEquals("target/11.4/artifacts/dam-dist-1.0.0-M1.tar.gz", retrievedArtifact.getPath());
        }

    }

}
