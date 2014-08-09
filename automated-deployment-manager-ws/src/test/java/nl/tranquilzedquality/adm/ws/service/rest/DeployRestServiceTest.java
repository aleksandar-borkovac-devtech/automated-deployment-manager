package nl.tranquilzedquality.adm.ws.service.rest;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.MavenArtifactSearchCommand;
import nl.tranquilizedquality.adm.commons.business.command.ReleaseSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.core.business.manager.DeploymentManager;
import nl.tranquilizedquality.adm.core.business.manager.DestinationManager;
import nl.tranquilizedquality.adm.core.business.manager.MavenArtifactManager;
import nl.tranquilizedquality.adm.core.business.manager.ReleaseManager;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;
import nl.tranquilzedquality.adm.ws.service.rest.DeployRestService;
import nl.tranquilzedquality.adm.ws.service.rest.DeployRestServiceImpl;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link DeployRestService}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 4 okt. 2012
 */
public class DeployRestServiceTest extends EasyMockSupport {

    /** Service that will be tested. */
    private DeployRestServiceImpl deployRestService;

    /** Mocked manager. */
    private DeploymentManager deploymentManager;

    /** Mocked manager. */
    private DestinationManager destinationManager;

    /** Mocked manager. */
    private MavenArtifactManager mavenArtifactManager;

    /** Mocked manager. */
    private ReleaseManager releaseManager;

    @Before
    public void setUp() throws Exception {
        deployRestService = new DeployRestServiceImpl();

        deploymentManager = createMock(DeploymentManager.class);
        destinationManager = createMock(DestinationManager.class);
        mavenArtifactManager = createMock(MavenArtifactManager.class);
        releaseManager = createMock(ReleaseManager.class);

        deployRestService.setDeploymentManager(deploymentManager);
        deployRestService.setDestinationManager(destinationManager);
        deployRestService.setMavenArtifactManager(mavenArtifactManager);
        deployRestService.setReleaseManager(releaseManager);
    }

    @Test
    public void testDeployArtifact() {
        final List<MavenArtifact> artifacts = new ArrayList<MavenArtifact>();
        final HibernateMavenArtifact artifact = new HibernateMavenArtifact();
        artifacts.add(artifact);

        final List<Environment> environments = new ArrayList<Environment>();
        final HibernateEnvironment environment = new HibernateEnvironment();
        environment.setName("INT");
        environments.add(environment);

        expect(destinationManager.findDeployEnvironments()).andReturn(environments);
        expect(mavenArtifactManager.findArtifacts(isA(MavenArtifactSearchCommand.class))).andReturn(artifacts);
        deploymentManager.deployArtifact(artifact, environment);
        expectLastCall().once();

        replayAll();

        deployRestService.deployArtifact("S12.17", "nl.Tranquilized Quality.adm", "adm-dist", "1.0.0-RC2", "INT");

        verifyAll();

    }

    @Test
    public void testDeployRelease() {

        final List<Release> releases = new ArrayList<Release>();
        final HibernateRelease release = new HibernateRelease();
        release.setName("S12.13");
        releases.add(release);

        final List<Environment> environments = new ArrayList<Environment>();
        final HibernateEnvironment environment = new HibernateEnvironment();
        environment.setName("INT");
        environments.add(environment);

        expect(releaseManager.findReleases(isA(ReleaseSearchCommand.class))).andReturn(releases);
        expect(destinationManager.findDeployEnvironments()).andReturn(environments);
        deploymentManager.deployRelease(release, environment);
        expectLastCall().once();

        replayAll();

        deployRestService.deployRelease("S12.13", "INT");

        verifyAll();
    }

}
