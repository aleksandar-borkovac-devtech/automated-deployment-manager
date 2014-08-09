/*
 * @(#)RemoteShellCommandDistributionPackageDeployerTest.java 4 okt. 2012
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.deployer.shell;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.core.business.deployer.shell.RemoteShellCommandDistributionPackageDeployer;
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
 * Test for {@link RemoteShellCommandDistributionPackageDeployer}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 4 okt. 2012
 */
public class RemoteShellCommandDistributionPackageDeployerTest {

    /** Deployer that will be tested. */
    private RemoteShellCommandDistributionPackageDeployer deployer;

    /** Mocked connector. */
    private ProtocolConnector connector;

    private HibernateDestinationHost destinationHost;

    private HibernateDestination destination;

    private HibernateMavenArtifact distribution;

    private HibernateEnvironment environment;

    private HibernateDeployer shellScriptDeployer;

    private File tarFile;

    private HibernateMavenModule module;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        deployer = new RemoteShellCommandDistributionPackageDeployer();
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
     * {@link nl.tranquilizedquality.adm.core.business.deployer.shell.RemoteShellCommandDistributionPackageDeployer#deploy(nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact, nl.tranquilizedquality.adm.commons.business.domain.Destination, java.lang.String, nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector)}
     * .
     */
    @Test
    public void testDeploy() {

        connector.performCustomCommand("echo \"salomo\"");
        expectLastCall().once();
        connector.performCustomCommand("cp test.sh salomo.sh");
        expectLastCall().once();
        connector.performCustomCommand("rm salomo.sh");
        expectLastCall().once();
        connector.performCustomCommand("cp /Users/snkpetrus/Development/TQ/automated-deployment-manager/pom.xml /Users/snkpetrus/salomo.xml");
        expectLastCall().once();
        connector.disconnectFromHost();
        expectLastCall().once();
        expect(connector.getSessionLogs()).andReturn("");

        replay(connector);

        deployer.deploy(distribution, destination, null, connector);

        verify(connector);
    }

}
