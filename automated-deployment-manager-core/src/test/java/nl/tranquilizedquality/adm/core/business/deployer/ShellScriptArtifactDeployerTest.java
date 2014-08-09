/**
 * 
 */
package nl.tranquilizedquality.adm.core.business.deployer;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.deployer.Deployer;
import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.core.business.deployer.ShellScriptArtifactDeployer;
import nl.tranquilizedquality.adm.core.business.deployer.exception.DeployException;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployer;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestination;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestinationHost;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link ShellScriptArtifactDeployer}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 8 feb. 2012
 */
public class ShellScriptArtifactDeployerTest {

    /** Deployer that will be tested. */
    private ShellScriptArtifactDeployer deployer;

    /** Mocked connector. */
    private ProtocolConnector connector;

    /** Mocked deployer. */
    private Deployer<MavenArtifact> shellScriptArtifactDeployer;

    private HibernateDestinationHost destinationHost;

    private HibernateDestination destination;

    private HibernateMavenArtifact artifact;

    private HibernateMavenArtifact distribution;

    private HibernateEnvironment environment;

    private HibernateDeployer shellScriptDeployer;

    private File tarFile;

    private HibernateMavenModule module;

    /**
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        deployer = new ShellScriptArtifactDeployer();

        connector = createMock(ProtocolConnector.class);
        shellScriptArtifactDeployer = createMock(Deployer.class);

        final EnumMap<Protocol, ProtocolConnector> connectors = new EnumMap<Protocol, ProtocolConnector>(Protocol.class);
        connectors.put(Protocol.SSH, connector);
        deployer.setConnectors(connectors);
        deployer.setConnectors(connectors);
        deployer.setShellScriptDeployer(shellScriptArtifactDeployer);

        final HibernateRelease release = new HibernateRelease();
        release.setName("INT Release");
        release.setReleaseDate(new Date());

        final List<Destination> destinations = new ArrayList<Destination>();

        module = new HibernateMavenModule();
        module.setName("adm-db");
        module.setType(ArtifactType.TAR_GZIP);
        module.setDestinations(destinations);
        module.setGroup("nl.Tranquilized Quality.adm");

        final List<DeployerParameter> locations = new ArrayList<DeployerParameter>();

        environment = new HibernateEnvironment();
        environment.setId(1L);
        environment.setName("DEMO");
        environment.setDescription("DEMO environment");

        destinationHost = new HibernateDestinationHost();
        destinationHost.setHostName("development");
        destinationHost.setProtocol(Protocol.SSH);
        destinationHost.setUsername("s-petrus");
        destinationHost.setPassword("password");
        destinationHost.setPort(22);

        shellScriptDeployer = new HibernateDeployer();
        shellScriptDeployer.setName("SHELL_SCRIPT_DEPLOYER");

        destination = new HibernateDestination();
        destination.setDeployer(shellScriptDeployer);
        destination.setEnvironment(environment);
        destination.setDeployerParameters(locations);
        destination.setDestinationHost(destinationHost);
        destination.setPrefix("tomcat");
        destinations.add(destination);

        tarFile = new File("src/test/resources/automated-deployment-manager-dist-1.0.0-SNAPSHOT.tar.gz");

        artifact = new HibernateMavenArtifact();
        artifact.setParentModule(module);
        artifact.setRelease(release);
        artifact.setVersion("1.0.0");
        artifact.setFile(tarFile.getAbsolutePath());

        final HibernateMavenModule distMain = new HibernateMavenModule();
        distMain.setName("adm-db");
        distMain.setType(ArtifactType.TAR_GZIP);
        distMain.setDestinations(destinations);
        distMain.setGroup("nl.Tranquilized Quality.adm");

        distribution = new HibernateMavenArtifact();
        distribution.setParentModule(distMain);
        distribution.setRelease(release);
        distribution.setVersion("1.0.0");
        distribution.setFile(tarFile.getAbsolutePath());
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.deployer.ShellScriptArtifactDeployer#deploy(nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact, nl.tranquilizedquality.adm.commons.business.domain.Destination)}
     * .
     */
    @Test
    public void testDeploy() {
        connector.connectToHost(destinationHost.getTerminal(), destinationHost.getHostName(), destinationHost.getPort(),
                destinationHost.getUsername(), destinationHost.getPassword(), null);
        expectLastCall().once();
        expect(shellScriptArtifactDeployer.deploy(distribution, destination, null, connector)).andReturn("");
        connector.disconnectFromHost();
        expectLastCall().once();

        replay(connector);
        replay(shellScriptArtifactDeployer);

        deployer.deploy(artifact, destination);

        verify(connector);
        verify(shellScriptArtifactDeployer);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.deployer.ShellScriptArtifactDeployer#deploy(nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact, nl.tranquilizedquality.adm.commons.business.domain.Destination)}
     * .
     */
    @Test
    public void testDeployInvalidProtocol() {

        destinationHost.setProtocol(Protocol.SFTP);

        replay(connector);
        replay(shellScriptArtifactDeployer);

        try {
            deployer.deploy(artifact, destination);
        } catch (final Exception e) {
            assertTrue("Invalid exception thrown!", e instanceof DeployException);
        }

        verify(connector);
        verify(shellScriptArtifactDeployer);
    }

}
