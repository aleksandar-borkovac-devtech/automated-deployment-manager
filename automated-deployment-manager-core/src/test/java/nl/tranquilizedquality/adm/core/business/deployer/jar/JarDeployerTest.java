package nl.tranquilizedquality.adm.core.business.deployer.jar;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.core.business.deployer.jar.JarDeployer;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployer;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployerParameter;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestination;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestinationHost;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link JarDeployer}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 7 jul. 2011
 */
public class JarDeployerTest {

    /** Deployer that will be tested. */
    private JarDeployer jarDeployer;

    /** Mocked connector. */
    private ProtocolConnector connector;

    private File file;

    private HibernateDeployerParameter location;

    private HibernateDestination destination;

    private HibernateMavenArtifact artifact;

    private HibernateEnvironment environment;

    private HibernateDestinationHost destinationHost;

    private HibernateDeployer deployer;

    @Before
    public void setUp() throws Exception {
        jarDeployer = new JarDeployer();

        connector = createMock(ProtocolConnector.class);

        final HibernateRelease release = new HibernateRelease();
        release.setName("INT Release");
        release.setReleaseDate(new Date());

        final List<Destination> destinations = new ArrayList<Destination>();

        final HibernateMavenModule module = new HibernateMavenModule();
        module.setName("adm-core");
        module.setType(ArtifactType.JAR);
        module.setDestinations(destinations);
        module.setGroup("nl.Tranquilized Quality.adm");

        final List<DeployerParameter> locations = new ArrayList<DeployerParameter>();

        environment = new HibernateEnvironment();
        environment.setId(1L);
        environment.setName("INT");
        environment.setDescription("Integration Test environment");

        destinationHost = new HibernateDestinationHost();
        destinationHost.setHostName("development");
        destinationHost.setProtocol(Protocol.SSH);
        destinationHost.setUsername("s-petrus");
        destinationHost.setPassword("password");
        destinationHost.setPort(22);

        deployer = new HibernateDeployer();
        deployer.setName("TOMCAT_DEPLOYER");

        destination = new HibernateDestination();
        destination.setDeployer(deployer);
        destination.setEnvironment(environment);
        destination.setDeployerParameters(locations);
        destination.setDestinationHost(destinationHost);
        destinations.add(destination);

        location = new HibernateDeployerParameter();
        location.setValue("/srv/tomcat/instances/8080/shared/lib");
        location.setType(DeployerParameterType.JAR_LOCATION);
        locations.add(location);

        file = new File("src/test/resources/automated-deployment-manager-core-1.0.0-SNAPSHOT.jar");

        artifact = new HibernateMavenArtifact();
        artifact.setParentModule(module);
        artifact.setRelease(release);
        artifact.setVersion("1.0.0");
        artifact.setFile(file.getAbsolutePath());
    }

    @Test
    public void testDeploy() {
        connector.transferFileToHost(isA(File.class), isA(String.class));
        expectLastCall().once();
        expect(connector.getOutputBuffer()).andReturn("");

        replay(connector);

        jarDeployer.deploy(artifact, destination, null, connector);

        verify(connector);
    }

    @Test
    public void testDeployFailure() {
        destination.setDeployerParameters(new ArrayList<DeployerParameter>());

        replay(connector);

        try {
            jarDeployer.deploy(artifact, destination, null, connector);
            fail("Deployment should fail!");
        } catch (final Exception e) {
            final String message = e.getMessage();
            assertEquals("No artifact location specified!", message);
        }

        verify(connector);
    }
}
