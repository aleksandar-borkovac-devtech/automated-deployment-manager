/*
 * @(#)RemotePowerShellDistributionPackageDeployerTest.java 22 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.deployer.shell;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.easymock.EasyMock.contains;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.core.business.deployer.exception.DeployException;
import nl.tranquilizedquality.adm.core.business.deployer.shell.RemotePowerShellDistributionPackageDeployer;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployer;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestination;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestinationHost;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link RemotePowerShellDistributionPackageDeployer}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 feb. 2013
 */
public class RemotePowerShellDistributionPackageDeployerTest extends EasyMockSupport {

    /** Deployer that will be tested. */
    private RemotePowerShellDistributionPackageDeployer deployer;

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
        deployer = new RemotePowerShellDistributionPackageDeployer();
        deployer.setRemoteWorkDirectory("target/");

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
        shellScriptDeployer.setName("REMOTE_POWER_SHELL");

        destination = new HibernateDestination();
        destination.setDeployer(shellScriptDeployer);
        destination.setEnvironment(environment);
        destination.setDeployerParameters(locations);
        destination.setDestinationHost(destinationHost);
        destination.setPrefix("tomcat");
        destinations.add(destination);

        tarFile = new File("src/test/resources/automated-deployment-manager-dist-1.0.0-SNAPSHOT.tar.gz");

        final HibernateMavenModule distMain = new HibernateMavenModule();
        distMain.setName("bos-gui");
        distMain.setType(ArtifactType.ZIP);
        distMain.setDestinations(destinations);
        distMain.setGroup("nl.Tranquilized Quality.bos");
        distMain.setArtifactId("bos-gui");

        distribution = new HibernateMavenArtifact();
        distribution.setParentModule(distMain);
        distribution.setRelease(release);
        distribution.setVersion("1.0.0-SNAPSHOT");
        distribution.setFile(tarFile.getAbsolutePath());
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.deployer.shell.RemotePowerShellDistributionPackageDeployer#deploy(nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact, nl.tranquilizedquality.adm.commons.business.domain.Destination, java.lang.String, nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector)}
     * .
     */
    @Test
    public void testDeploy() {

        connector.performCustomCommand(contains("mkdir target/"));
        expectLastCall().once();
        connector.transferFileToHost(isA(File.class), contains("target/"));
        expectLastCall().once();
        connector.performCustomCommand(contains("unzip -o target/"));
        expectLastCall().once();
        connector.performCustomCommand(contains("cd target/"));
        expectLastCall().once();
        connector.performCustomCommand(contains(".\\deploy_demo.ps1"));
        expectLastCall().once();
        connector.disconnectFromHost();
        expectLastCall().once();
        expect(connector.getOutputBuffer()).andReturn("logs");

        replayAll();

        deployer.deploy(distribution, destination, null, connector);

        verifyAll();
    }

    @Test
    public void testDeployFailure() {

        connector.performCustomCommand(contains("mkdir target/"));
        expectLastCall().once();
        connector.transferFileToHost(isA(File.class), contains("target/"));
        expectLastCall().andThrow(new DeployException("Failed to tranfer file")).once();

        replayAll();

        try {
            deployer.deploy(distribution, destination, null, connector);
            fail("File tranfer should have failed!");
        } catch (final Exception e) {
            assertTrue("Invalid exception thrown!", e instanceof DeployException);
        }

        verifyAll();
    }

}
