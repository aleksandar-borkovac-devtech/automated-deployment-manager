package nl.tranquilizedquality.adm.core.business.manager.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.tranquilizedquality.adm.commons.business.deployer.ArtifactDeployer;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.DeployStatus;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifactSnapshot;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.core.business.deployer.exception.DeployException;
import nl.tranquilizedquality.adm.core.business.manager.ArtifactManager;
import nl.tranquilizedquality.adm.core.business.manager.DeployProgress;
import nl.tranquilizedquality.adm.core.business.manager.MavenArtifactManager;
import nl.tranquilizedquality.adm.core.business.manager.NotificationManager;
import nl.tranquilizedquality.adm.core.business.manager.ReleaseHistoryManager;
import nl.tranquilizedquality.adm.core.business.manager.impl.DeploymentManagerImpl;
import nl.tranquilizedquality.adm.core.persistence.dao.EnvironmentDao;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseDao;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployer;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestination;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestinationHost;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifactSnapshot;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateReleaseExecution;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link DeploymentManagerImpl}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jul. 2011
 */
public class DeploymentManagerTest extends EasyMockSupport {

    /** Manager that will be tested. */
    private DeploymentManagerImpl deploymentManager;

    /** Mocked manager. */
    private ArtifactManager artifactManager;

    /** Mocked DAO. */
    private ReleaseDao<Release> releaseDao;

    /** Mocked DAO. */
    private EnvironmentDao<Environment> environmentDao;

    /** Mocked DAO. */
    private MavenArtifactManager mavenArtifactManager;

    /** Mocked deployer. */
    private ArtifactDeployer artifactDeployer;

    /** Mocked manager. */
    private ReleaseHistoryManager releaseHistoryManager;

    /** Mocked manager. */
    private DeployProgress deployProgress;

    /** Mocked manager. */
    private NotificationManager notificationManager;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        deploymentManager = new DeploymentManagerImpl();

        releaseDao = createMock(ReleaseDao.class);
        artifactManager = createMock(ArtifactManager.class);
        artifactDeployer = createMock(ArtifactDeployer.class);
        releaseHistoryManager = createMock(ReleaseHistoryManager.class);
        mavenArtifactManager = createMock(MavenArtifactManager.class);
        environmentDao = createMock(EnvironmentDao.class);
        deployProgress = createMock(DeployProgress.class);
        notificationManager = createMock(NotificationManager.class);

        final Map<String, ArtifactDeployer> deployers = new HashMap<String, ArtifactDeployer>();
        deployers.put("TEST_DEPLOYER", artifactDeployer);

        deploymentManager.setArtifactManager(artifactManager);
        deploymentManager.setReleaseDao(releaseDao);
        deploymentManager.setDeployers(deployers);
        deploymentManager.setReleaseHistoryManager(releaseHistoryManager);
        deploymentManager.setMavenArtifactManager(mavenArtifactManager);
        deploymentManager.setEnvironmentDao(environmentDao);
        deploymentManager.setDeployProgress(deployProgress);
        deploymentManager.setNotificationManager(notificationManager);
    }

    @Test
    public void testDeployRelease() {
        final List<MavenArtifact> artifacts = new ArrayList<MavenArtifact>();
        final List<Destination> destinations = new ArrayList<Destination>();
        final List<MavenModule> dependencies = new ArrayList<MavenModule>();

        final HibernateMavenModule module = new HibernateMavenModule();
        module.setId(1L);
        module.setName("dam-gwt-gui");
        module.setType(ArtifactType.WAR);
        module.setGroup("nl.Tranquilized Quality.dam");
        module.setArtifactId("dam-gwt-gui");
        module.setDestinations(destinations);
        module.setDeploymentDependencies(dependencies);

        final HibernateRelease release = new HibernateRelease();
        release.setId(1L);
        release.setName("11.4");
        release.setReleaseDate(new Date());
        release.setArtifacts(artifacts);
        release.setStatus(ReleaseStatus.READY);

        final HibernateEnvironment environment = new HibernateEnvironment();
        environment.setId(1L);
        environment.setName("dev");
        environment.setDescription("Integration Test environment");

        final HibernateDestinationHost destinationHost = new HibernateDestinationHost();
        destinationHost.setHostName("development");
        destinationHost.setProtocol(Protocol.SSH);
        destinationHost.setUsername("s-petrus");
        destinationHost.setPassword("password");
        destinationHost.setPort(22);

        final HibernateDeployer testDeployer = new HibernateDeployer();
        testDeployer.setName("TEST_DEPLOYER");

        final HibernateDestination destination = new HibernateDestination();
        destination.setDeployer(testDeployer);
        destination.setEnvironment(environment);
        destination.setDestinationHost(destinationHost);
        destinations.add(destination);

        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifact.setId(1L);
        artifact.setVersion("1.0.0-M1");
        artifact.setRelease(release);
        artifact.setParentModule(module);
        artifacts.add(artifact);

        final HibernateMavenArtifactSnapshot snapshot = new HibernateMavenArtifactSnapshot();
        snapshot.setId(1L);
        snapshot.setVersion("1.0.0-M1");
        snapshot.setArtifactId("dam-gwt-gui");
        snapshot.setGroup("nl.Tranquilized Quality.dam");

        final HibernateReleaseExecution execution = new HibernateReleaseExecution();
        final List<MavenArtifactSnapshot> snapshotArtifacts = new ArrayList<MavenArtifactSnapshot>();
        snapshotArtifacts.add(snapshot);
        execution.setArtifacts(snapshotArtifacts);

        deployProgress.addProgress(40);
        expectLastCall().times(2);
        deployProgress.registerActivity(isA(String.class));
        expectLastCall().anyTimes();
        deployProgress.complete();
        expectLastCall().times(1);
        expect(environmentDao.findById(environment.getId())).andReturn(environment);
        expect(releaseHistoryManager.createHistory(release, environment, artifacts)).andReturn(execution);
        releaseHistoryManager.registerActivity(execution, "Deploying release...");
        expectLastCall().once();
        expect(releaseDao.findById(1L)).andReturn(release).once();
        releaseHistoryManager.registerActivity(execution, "Release found...");
        expectLastCall().once();
        releaseHistoryManager.registerActivity(execution, "Deploying artifact dam-gwt-gui version 1.0.0-M1");
        expectLastCall().once();
        expect(mavenArtifactManager.findArtifactById(artifact.getId())).andReturn(artifact);
        releaseHistoryManager.registerActivity(execution, "Retrieved artifact pom.xml");
        expectLastCall().once();
        expect(artifactManager.retrieveArtifact(artifact, release.getName())).andReturn(new File("pom.xml"));
        expect(artifactDeployer.deploy(artifact, destination)).andReturn("logs");
        releaseHistoryManager.registerActivity(execution, "Artifact dam-gwt-gui deployed to dev");
        expectLastCall().once();
        expect(releaseDao.save(release)).andReturn(release);
        releaseDao.flush();
        expectLastCall().once();
        releaseHistoryManager.registerLogs(execution, snapshot, "logs");
        expectLastCall().once();
        releaseHistoryManager.registerActivity(execution, "Finished deploying", DeployStatus.SUCCESS);
        expectLastCall().once();
        notificationManager.sendDeploymentNotification(release, artifacts, environment);
        expectLastCall().once();

        replayAll();

        deploymentManager.deployArtifacts(release.getArtifacts(), release, environment);

        verifyAll();

        assertEquals("Invalid release count!", new Integer(1), release.getReleaseCount());
        assertEquals("Invalid release failures!", new Integer(0), release.getReleaseFailureCount());
        assertEquals("Invalid release status!", ReleaseStatus.SUCCESS, release.getStatus());
    }

    @Test
    public void testDeployDistribution() {
        final List<MavenArtifact> artifacts = new ArrayList<MavenArtifact>();
        final List<Destination> destinations = new ArrayList<Destination>();

        final HibernateMavenModule module = new HibernateMavenModule();
        module.setId(1L);
        module.setName("dam-dist");
        module.setType(ArtifactType.TAR_GZIP);
        module.setGroup("nl.Tranquilized Quality.dam");
        module.setArtifactId("dam-dist");
        module.setDestinations(destinations);
        module.setIdentifier("bin");

        final HibernateRelease release = new HibernateRelease();
        release.setId(1L);
        release.setName("11.4");
        release.setReleaseDate(new Date());
        release.setArtifacts(artifacts);
        release.setStatus(ReleaseStatus.READY);

        final HibernateEnvironment environment = new HibernateEnvironment();
        environment.setId(1L);
        environment.setName("dev");
        environment.setDescription("Integration Test environment");

        final HibernateDestinationHost destinationHost = new HibernateDestinationHost();
        destinationHost.setHostName("development");
        destinationHost.setProtocol(Protocol.SSH);
        destinationHost.setUsername("s-petrus");
        destinationHost.setPassword("password");
        destinationHost.setPort(22);

        final HibernateDeployer testDeployer = new HibernateDeployer();
        testDeployer.setName("TEST_DEPLOYER");

        final HibernateDestination destination = new HibernateDestination();
        destination.setDeployer(testDeployer);
        destination.setEnvironment(environment);
        destination.setDestinationHost(destinationHost);
        destinations.add(destination);

        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifact.setId(1L);
        artifact.setVersion("1.0.0-M1");
        artifact.setRelease(release);
        artifact.setParentModule(module);
        artifacts.add(artifact);

        final HibernateMavenArtifactSnapshot snapshot = new HibernateMavenArtifactSnapshot();
        snapshot.setId(1L);
        snapshot.setVersion("1.0.0-M1");
        snapshot.setArtifactId("dam-dist");
        snapshot.setGroup("nl.Tranquilized Quality.dam");
        snapshot.setIdentifier("bin");

        final HibernateReleaseExecution execution = new HibernateReleaseExecution();
        final List<MavenArtifactSnapshot> snapshotArtifacts = new ArrayList<MavenArtifactSnapshot>();
        snapshotArtifacts.add(snapshot);
        execution.setArtifacts(snapshotArtifacts);

        deployProgress.addProgress(40);
        expectLastCall().times(2);
        deployProgress.registerActivity(isA(String.class));
        expectLastCall().anyTimes();
        deployProgress.complete();
        expectLastCall().times(1);
        expect(environmentDao.findById(environment.getId())).andReturn(environment);
        expect(releaseHistoryManager.createHistory(release, environment, artifacts)).andReturn(execution);
        releaseHistoryManager.registerActivity(execution, "Deploying release...");
        expectLastCall().once();
        expect(releaseDao.findById(1L)).andReturn(release).once();
        releaseHistoryManager.registerActivity(execution, "Release found...");
        expectLastCall().once();
        releaseHistoryManager.registerActivity(execution, "Deploying artifact dam-dist version 1.0.0-M1");
        expectLastCall().once();
        expect(mavenArtifactManager.findArtifactById(artifact.getId())).andReturn(artifact);
        releaseHistoryManager.registerActivity(execution, "Retrieved artifact pom.xml");
        expectLastCall().once();
        expect(artifactManager.retrieveArtifact(artifact, release.getName())).andReturn(new File("pom.xml"));
        expect(artifactDeployer.deploy(artifact, destination)).andReturn("logs");
        releaseHistoryManager.registerActivity(execution, "Artifact dam-dist deployed to dev");
        expectLastCall().once();
        expect(releaseDao.save(release)).andReturn(release);
        releaseDao.flush();
        expectLastCall().once();
        releaseHistoryManager.registerLogs(execution, snapshot, "logs");
        expectLastCall().once();
        releaseHistoryManager.registerActivity(execution, "Finished deploying", DeployStatus.SUCCESS);
        expectLastCall().once();
        notificationManager.sendDeploymentNotification(release, artifacts, environment);
        expectLastCall().once();

        replayAll();

        deploymentManager.deployArtifacts(release.getArtifacts(), release, environment);

        verifyAll();

        assertEquals("Invalid release count!", new Integer(1), release.getReleaseCount());
        assertEquals("Invalid release failures!", new Integer(0), release.getReleaseFailureCount());
        assertEquals("Invalid release status!", ReleaseStatus.SUCCESS, release.getStatus());
    }

    @Test
    public void testDeployReleaseOtherEnvironment() {
        final List<MavenArtifact> artifacts = new ArrayList<MavenArtifact>();
        final List<Destination> destinations = new ArrayList<Destination>();

        final HibernateMavenModule module = new HibernateMavenModule();
        module.setId(1L);
        module.setName("dam-gwt-gui");
        module.setType(ArtifactType.WAR);
        module.setGroup("nl.Tranquilized Quality.dam");
        module.setArtifactId("dam-gwt-gui");
        module.setDestinations(destinations);

        final HibernateRelease release = new HibernateRelease();
        release.setId(1L);
        release.setName("11.4");
        release.setReleaseDate(new Date());
        release.setArtifacts(artifacts);
        release.setStatus(ReleaseStatus.READY);

        final HibernateEnvironment environment = new HibernateEnvironment();
        environment.setId(1L);
        environment.setName("dev");
        environment.setDescription("Development environment");

        final HibernateEnvironment integrationEnvironment = new HibernateEnvironment();
        integrationEnvironment.setId(2L);
        integrationEnvironment.setName("int");
        integrationEnvironment.setDescription("Integration Test environment");

        final HibernateDestinationHost destinationHost = new HibernateDestinationHost();
        destinationHost.setHostName("development");
        destinationHost.setProtocol(Protocol.SSH);
        destinationHost.setUsername("s-petrus");
        destinationHost.setPassword("password");
        destinationHost.setPort(22);

        final HibernateDeployer testDeployer = new HibernateDeployer();
        testDeployer.setName("TEST_DEPLOYER");

        final HibernateDestination destination = new HibernateDestination();
        destination.setDeployer(testDeployer);
        destination.setEnvironment(environment);
        destination.setDestinationHost(destinationHost);
        destinations.add(destination);

        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifact.setId(1L);
        artifact.setVersion("1.0.0-M1");
        artifact.setRelease(release);
        artifact.setParentModule(module);
        artifacts.add(artifact);

        final HibernateMavenArtifactSnapshot snapshot = new HibernateMavenArtifactSnapshot();
        snapshot.setId(1L);
        snapshot.setVersion("1.0.0-M1");
        snapshot.setArtifactId("dam-gwt-gui");
        snapshot.setGroup("nl.Tranquilized Quality.dam");

        final HibernateReleaseExecution execution = new HibernateReleaseExecution();
        final List<MavenArtifactSnapshot> snapshotArtifacts = new ArrayList<MavenArtifactSnapshot>();
        snapshotArtifacts.add(snapshot);
        execution.setArtifacts(snapshotArtifacts);

        deployProgress.addProgress(40);
        expectLastCall().times(2);
        deployProgress.registerActivity(isA(String.class));
        expectLastCall().anyTimes();
        deployProgress.complete();
        expectLastCall().times(1);
        expect(environmentDao.findById(integrationEnvironment.getId())).andReturn(environment);
        expect(releaseHistoryManager.createHistory(release, environment, artifacts)).andReturn(execution);
        releaseHistoryManager.registerActivity(execution, "Deploying release...");
        expectLastCall().once();
        expect(releaseDao.findById(1L)).andReturn(release).once();
        releaseHistoryManager.registerActivity(execution, "Release found...");
        expectLastCall().once();
        releaseHistoryManager.registerActivity(execution, "Deploying artifact dam-gwt-gui version 1.0.0-M1");
        expectLastCall().once();
        expect(mavenArtifactManager.findArtifactById(artifact.getId())).andReturn(artifact);
        releaseHistoryManager.registerActivity(execution, "Failed to deploy artifact dam-gwt-gui", "Couldn't deploy artifact to int");
        expectLastCall().once();
        releaseHistoryManager.registerActivity(execution, "Artifact dam-gwt-gui will NOT be deployed to dev");
        expectLastCall().once();
        expect(releaseDao.save(release)).andReturn(release);
        releaseDao.flush();
        expectLastCall().once();
        releaseHistoryManager.registerActivity(execution, "Finished deploying", DeployStatus.FAILED);
        expectLastCall().once();
        notificationManager.sendDeploymentNotification(release, artifacts, integrationEnvironment);
        expectLastCall().once();

        replayAll();

        deploymentManager.deployArtifacts(release.getArtifacts(), release, integrationEnvironment);

        verifyAll();

        assertEquals("Invalid release count!", new Integer(1), release.getReleaseCount());
        assertEquals("Invalid release failures!", new Integer(1), release.getReleaseFailureCount());
        assertEquals("Invalid release status!", ReleaseStatus.FAILED, release.getStatus());
    }

    @Test
    public void testDeployReleaseNoDeployer() {
        final List<MavenArtifact> artifacts = new ArrayList<MavenArtifact>();
        final List<Destination> destinations = new ArrayList<Destination>();

        final HibernateMavenModule module = new HibernateMavenModule();
        module.setId(1L);
        module.setName("dam-gwt-gui");
        module.setType(ArtifactType.WAR);
        module.setGroup("nl.Tranquilized Quality.dam");
        module.setArtifactId("dam-gwt-gui");
        module.setDestinations(destinations);

        final HibernateRelease release = new HibernateRelease();
        release.setId(1L);
        release.setName("11.4");
        release.setReleaseDate(new Date());
        release.setArtifacts(artifacts);
        release.setStatus(ReleaseStatus.READY);

        final HibernateEnvironment environment = new HibernateEnvironment();
        environment.setId(1L);
        environment.setName("dev");
        environment.setDescription("Integration Test environment");

        final HibernateDestinationHost destinationHost = new HibernateDestinationHost();
        destinationHost.setHostName("development");
        destinationHost.setProtocol(Protocol.SSH);
        destinationHost.setUsername("s-petrus");
        destinationHost.setPassword("password");
        destinationHost.setPort(22);

        final HibernateDeployer deployer = new HibernateDeployer();
        deployer.setName("DEPLOYER");

        final HibernateDestination destination = new HibernateDestination();
        destination.setDeployer(deployer);
        destination.setEnvironment(environment);
        destination.setDestinationHost(destinationHost);
        destinations.add(destination);

        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifact.setId(1L);
        artifact.setVersion("1.0.0-M1");
        artifact.setRelease(release);
        artifact.setParentModule(module);
        artifacts.add(artifact);

        final HibernateMavenArtifactSnapshot snapshot = new HibernateMavenArtifactSnapshot();
        snapshot.setId(1L);
        snapshot.setVersion("1.0.0-M1");
        snapshot.setArtifactId("dam-gwt-gui");
        snapshot.setGroup("nl.Tranquilized Quality.dam");

        final HibernateReleaseExecution execution = new HibernateReleaseExecution();
        final List<MavenArtifactSnapshot> snapshotArtifacts = new ArrayList<MavenArtifactSnapshot>();
        snapshotArtifacts.add(snapshot);
        execution.setArtifacts(snapshotArtifacts);

        deployProgress.addProgress(40);
        expectLastCall().times(1);
        deployProgress.registerActivity(isA(String.class));
        expectLastCall().anyTimes();
        expect(environmentDao.findById(environment.getId())).andReturn(environment);
        expect(releaseHistoryManager.createHistory(release, environment, artifacts)).andReturn(execution);
        releaseHistoryManager.registerActivity(execution, "Deploying release...");
        expectLastCall().once();
        expect(releaseDao.findById(1L)).andReturn(release).once();
        releaseHistoryManager.registerActivity(execution, "Release found...");
        expectLastCall().once();
        releaseHistoryManager.registerActivity(execution, "Deploying artifact dam-gwt-gui version 1.0.0-M1");
        expectLastCall().once();
        expect(mavenArtifactManager.findArtifactById(artifact.getId())).andReturn(artifact);
        releaseHistoryManager.registerActivity(execution, "Deploy artifact to destination...", "No deployer found! -> DEPLOYER");
        expectLastCall().once();

        replayAll();

        try {
            deploymentManager.deployArtifacts(release.getArtifacts(), release, environment);
            fail("No deployer should be found!");
        } catch (final Exception e) {
            final String message = e.getMessage();

            assertEquals("Invalid message!", "No deployer found! -> DEPLOYER", message);
        }

        verifyAll();
    }

    @Test
    public void testDeployReleaseDoNotContinue() {

        final List<MavenArtifact> artifacts = new ArrayList<MavenArtifact>();
        final List<Destination> destinations = new ArrayList<Destination>();
        final List<MavenModule> dependencies = new ArrayList<MavenModule>();

        final HibernateMavenModule module = new HibernateMavenModule();
        module.setId(1L);
        module.setName("dam-gwt-gui");
        module.setType(ArtifactType.WAR);
        module.setGroup("nl.Tranquilized Quality.dam");
        module.setArtifactId("dam-gwt-gui");
        module.setDestinations(destinations);
        dependencies.add(module);

        final HibernateMavenModule dependencyModule = new HibernateMavenModule();
        dependencyModule.setId(1L);
        dependencyModule.setName("adm-ws");
        dependencyModule.setType(ArtifactType.WAR);
        dependencyModule.setGroup("nl.Tranquilized Quality.adm");
        dependencyModule.setArtifactId("adm-ws");
        dependencyModule.setDestinations(new ArrayList<Destination>());
        dependencyModule.setDeploymentDependencies(dependencies);

        final HibernateRelease release = new HibernateRelease();
        release.setId(1L);
        release.setName("11.4");
        release.setReleaseDate(new Date());
        release.setArtifacts(artifacts);
        release.setStatus(ReleaseStatus.READY);

        final HibernateEnvironment environment = new HibernateEnvironment();
        environment.setId(1L);
        environment.setName("dev");
        environment.setDescription("Integration Test environment");

        final HibernateDestinationHost destinationHost = new HibernateDestinationHost();
        destinationHost.setHostName("development");
        destinationHost.setProtocol(Protocol.SSH);
        destinationHost.setUsername("s-petrus");
        destinationHost.setPassword("password");
        destinationHost.setPort(22);

        final HibernateDeployer testDeployer = new HibernateDeployer();
        testDeployer.setName("TEST_DEPLOYER");

        final HibernateDestination destination = new HibernateDestination();
        destination.setDeployer(testDeployer);
        destination.setEnvironment(environment);
        destination.setDestinationHost(destinationHost);
        destinations.add(destination);

        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifact.setId(1L);
        artifact.setVersion("1.0.0-M1");
        artifact.setRelease(release);
        artifact.setParentModule(module);
        artifacts.add(artifact);

        final HibernateMavenArtifact dependencyArtifact = new HibernateMavenArtifact();
        dependencyArtifact.setId(2L);
        dependencyArtifact.setVersion("1.0.0-M1");
        dependencyArtifact.setRelease(release);
        dependencyArtifact.setParentModule(dependencyModule);
        artifacts.add(dependencyArtifact);

        final HibernateMavenArtifactSnapshot snapshot = new HibernateMavenArtifactSnapshot();
        snapshot.setId(1L);
        snapshot.setVersion("1.0.0-M1");
        snapshot.setArtifactId("dam-gwt-gui");
        snapshot.setGroup("nl.Tranquilized Quality.dam");

        final HibernateReleaseExecution execution = new HibernateReleaseExecution();
        final List<MavenArtifactSnapshot> snapshotArtifacts = new ArrayList<MavenArtifactSnapshot>();
        snapshotArtifacts.add(snapshot);
        execution.setArtifacts(snapshotArtifacts);

        deployProgress.addProgress(20);
        expectLastCall().times(4);
        deployProgress.registerActivity(isA(String.class));
        expectLastCall().anyTimes();
        deployProgress.complete();
        expectLastCall().times(1);
        expect(environmentDao.findById(environment.getId())).andReturn(environment);
        expect(releaseHistoryManager.createHistory(release, environment, artifacts)).andReturn(execution);
        releaseHistoryManager.registerActivity(execution, "Deploying release...");
        expectLastCall().once();
        expect(releaseDao.findById(1L)).andReturn(release).once();
        releaseHistoryManager.registerActivity(execution, "Release found...");
        expectLastCall().once();
        releaseHistoryManager.registerActivity(execution, "Deploying artifact dam-gwt-gui version 1.0.0-M1");
        expectLastCall().once();
        expect(mavenArtifactManager.findArtifactById(artifact.getId())).andReturn(artifact);
        releaseHistoryManager.registerActivity(execution, "Retrieved artifact pom.xml");
        expectLastCall().once();
        expect(artifactManager.retrieveArtifact(artifact, release.getName())).andReturn(new File("pom.xml"));
        expect(artifactDeployer.deploy(artifact, destination)).andThrow(new DeployException("Failed to deploy artifact!"));
        releaseHistoryManager.registerActivity(execution, "Failed to deploy artifact!!", "Failed to deploy artifact!");
        expectLastCall().once();
        releaseHistoryManager.registerLogs(execution, snapshot, null);
        expectLastCall().once();
        releaseHistoryManager.registerActivity(execution, "Failed to deploy artifact " + module.getName(),
                "Couldn't deploy artifact to dev");
        expectLastCall().once();
        expect(mavenArtifactManager.findArtifactById(dependencyArtifact.getId())).andReturn(dependencyArtifact);

        releaseHistoryManager.registerActivity(execution, "Deploying artifact adm-ws version 1.0.0-M1");
        expectLastCall().once();
        releaseHistoryManager.registerActivity(execution, "Deploy artifact to destination...",
                "Cannot deploy artifact because the dependent module dam-gwt-gui failed to deploy before.");
        expectLastCall().once();
        releaseHistoryManager.registerActivity(execution, "Failed to deploy artifact " + dependencyModule.getName(),
                "Couldn't deploy artifact to dev");
        expectLastCall().once();
        expect(releaseDao.save(release)).andReturn(release);
        releaseDao.flush();
        expectLastCall().once();
        releaseHistoryManager.registerActivity(execution, "Finished deploying", DeployStatus.FAILED);
        expectLastCall().once();
        notificationManager.sendDeploymentNotification(release, artifacts, environment);
        expectLastCall().once();

        replayAll();

        deploymentManager.deployArtifacts(release.getArtifacts(), release, environment);

        verifyAll();

        assertEquals("Invalid release count!", new Integer(1), release.getReleaseCount());
        assertEquals("Invalid release failures!", new Integer(1), release.getReleaseFailureCount());
        assertEquals("Invalid release status!", ReleaseStatus.FAILED, release.getStatus());
    }

}
