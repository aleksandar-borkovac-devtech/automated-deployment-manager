/**
 * 
 */
package nl.tranquilizedquality.adm.core.business.deployer.shell;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

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
import nl.tranquilizedquality.adm.core.business.deployer.shell.ShellScriptDistributionPackageDeployer;
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
 * Test for {@link ShellScriptDistributionPackageDeployer}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 8 feb. 2012
 */
public class ShellScriptDistributionPackageDeployerTest {

    /** Deployer that will be tested. */
    private ShellScriptDistributionPackageDeployer deployer;

    /** Mocked connector. */
    private ProtocolConnector connector;

    private HibernateDestinationHost destinationHost;

    private HibernateDestination destination;

    private HibernateMavenArtifact distribution;

    private HibernateEnvironment environment;

    private HibernateDeployer shellScriptDeployer;

    private File tarFile;

    private HibernateMavenModule module;

    private HibernateDeployerParameter firstParameter;

    private HibernateDeployerParameter secondParameter;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        deployer = new ShellScriptDistributionPackageDeployer();
        deployer.setWorkDirectory("target/");

        connector = createMock(ProtocolConnector.class);

        final HibernateRelease release = new HibernateRelease();
        release.setName("INT Release");
        release.setReleaseDate(new Date());

        final List<Destination> destinations = new ArrayList<Destination>();

        module = new HibernateMavenModule();
        module.setName("adm-db");
        module.setType(ArtifactType.TAR_GZIP);
        module.setDestinations(destinations);
        module.setGroup("nl.Tranquilized Quality.adm");
        module.setArtifactId("automated-deployment-manager-dist");

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

        firstParameter = new HibernateDeployerParameter();
        firstParameter.setRank(1);
        firstParameter.setType(DeployerParameterType.SCRIPT_PARAMETER);
        firstParameter.setValue("tester");

        secondParameter = new HibernateDeployerParameter();
        secondParameter.setRank(1);
        secondParameter.setType(DeployerParameterType.SCRIPT_PARAMETER);
        secondParameter.setValue("tester");

        final List<DeployerParameter> parameters = new ArrayList<DeployerParameter>();
        parameters.add(firstParameter);
        parameters.add(secondParameter);
        destination.setDeployerParameters(parameters);

        tarFile = new File("src/test/resources/automated-deployment-manager-dist-1.0.0-SNAPSHOT.tar.gz");

        final HibernateMavenModule distMain = new HibernateMavenModule();
        distMain.setName("adm-db");
        distMain.setType(ArtifactType.TAR_GZIP);
        distMain.setDestinations(destinations);
        distMain.setGroup("nl.Tranquilized Quality.adm");
        distMain.setArtifactId("automated-deployment-manager-dist");

        distribution = new HibernateMavenArtifact();
        distribution.setParentModule(distMain);
        distribution.setRelease(release);
        distribution.setVersion("1.0.0-SNAPSHOT");
        distribution.setFile(tarFile.getAbsolutePath());
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.deployer.shell.ShellScriptDistributionPackageDeployer#deploy(nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact, nl.tranquilizedquality.adm.commons.business.domain.Destination, java.lang.String, nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector)}
     * .
     */
    @Test
    public void testDeploy() {

        replay(connector);

        deployer.deploy(distribution, destination, null, connector);

        verify(connector);
    }

}
