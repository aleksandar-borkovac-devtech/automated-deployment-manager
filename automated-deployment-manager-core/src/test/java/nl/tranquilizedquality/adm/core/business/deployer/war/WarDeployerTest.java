/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 12 jul. 2011 File: WarDeployerTest.java
 * Package: nl.tranquilizedquality.adm.core.business.deployer.war
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
package nl.tranquilizedquality.adm.core.business.deployer.war;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.core.business.deployer.connector.ssh.SshCommand;
import nl.tranquilizedquality.adm.core.business.deployer.war.WarDeployer;
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
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * Test for {@link WarDeployer}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 jul. 2011
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class WarDeployerTest {

    /** Deployer that will be tested. */
    private WarDeployer warDeployer;

    /** Mocked connector. */
    private ProtocolConnector connector;

    private File file;

    private HibernateDeployerParameter location;

    private HibernateDestination destination;

    private HibernateMavenArtifact artifact;

    private HibernateEnvironment environment;

    private HibernateDestinationHost destinationHost;

    private HibernateDeployer tomcatDeployer;

    private List<DeployerParameter> locations;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        warDeployer = new WarDeployer();

        connector = createMock(ProtocolConnector.class);

        final HibernateRelease release = new HibernateRelease();
        release.setName("INT Release");
        release.setReleaseDate(new Date());

        final List<Destination> destinations = new ArrayList<Destination>();

        final HibernateMavenModule module = new HibernateMavenModule();
        module.setArtifactId("adm-gui");
        module.setName("adm-gui");
        module.setType(ArtifactType.WAR);
        module.setDestinations(destinations);
        module.setGroup("nl.Tranquilized Quality.adm");

        locations = new ArrayList<DeployerParameter>();

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

        tomcatDeployer = new HibernateDeployer();
        tomcatDeployer.setName("TOMCAT_DEPLOYER");

        destination = new HibernateDestination();
        destination.setDeployer(tomcatDeployer);
        destination.setEnvironment(environment);
        destination.setDeployerParameters(locations);
        destination.setDestinationHost(destinationHost);
        destinations.add(destination);

        location = new HibernateDeployerParameter();
        location.setValue("/srv/tomcat/instances/8080/backups/");
        location.setType(DeployerParameterType.BACKUP_LOCATION);
        locations.add(location);

        location = new HibernateDeployerParameter();
        location.setValue("/srv/tomcat/instances/8080/");
        location.setType(DeployerParameterType.APP_SERVER_LOCATION);
        locations.add(location);

        location = new HibernateDeployerParameter();
        location.setValue("/srv/tomcat/instances/8080/webapps/");
        location.setType(DeployerParameterType.WEB_APPS_LOCATION);
        locations.add(location);

        file = new File("src/test/resources/automated-deployment-manager-core-1.0.0-SNAPSHOT.war");

        artifact = new HibernateMavenArtifact();
        artifact.setParentModule(module);
        artifact.setRelease(release);
        artifact.setVersion("1.0.0");
        artifact.setFile(file.getAbsolutePath());
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.deployer.war.WarDeployer#deploy(nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact, nl.tranquilizedquality.adm.commons.business.domain.Destination, nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector)}
     * .
     */
    @Test
    public void testDeploySnapshot() {
        final SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        final String now = formatter.format(new Date());
        final String backupDir = "/srv/tomcat/instances/8080/backups/INT_Release_" + now + "/adm-gui";
        connector.performCommand(SshCommand.COPY, "/srv/tomcat/instances/8080/webapps/automated-deployment-manager-core.war "
                + backupDir);
        expectLastCall().once();
        connector.transferFileToHost(isA(File.class), isA(String.class));
        expectLastCall().once();
        expect(connector.getOutputBuffer()).andReturn("");

        replay(connector);

        warDeployer.deploy(artifact, destination, backupDir, connector);

        verify(connector);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.deployer.war.WarDeployer#deploy(nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact, nl.tranquilizedquality.adm.commons.business.domain.Destination, nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector)}
     * .
     */
    @Test
    public void testDeployFinal() {
        final SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        final String now = formatter.format(new Date());
        final String backupDir = "/srv/tomcat/instances/8080/backups/INT_Release_" + now + "/adm-gui";

        file = new File("src/test/resources/automated-deployment-manager-core-1.0.0.war");
        artifact.setFile(file.getAbsolutePath());

        connector.performCommand(SshCommand.COPY, "/srv/tomcat/instances/8080/webapps/automated-deployment-manager-core.war "
                + backupDir);
        expectLastCall().once();
        connector.transferFileToHost(isA(File.class), isA(String.class));
        expectLastCall().once();
        expect(connector.getOutputBuffer()).andReturn("");

        replay(connector);

        warDeployer.deploy(artifact, destination, backupDir, connector);

        verify(connector);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.deployer.war.WarDeployer#deploy(nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact, nl.tranquilizedquality.adm.commons.business.domain.Destination, nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector)}
     * .
     */
    @Test
    public void testDeployNoVersion() {
        final SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        final String now = formatter.format(new Date());
        final String backupDir = "/srv/tomcat/instances/8080/backups/INT_Release_" + now + "/adm-gui";

        file = new File("src/test/resources/automated-deployment-manager-core.war");
        artifact.setFile(file.getAbsolutePath());

        connector.performCommand(SshCommand.COPY, "/srv/tomcat/instances/8080/webapps/automated-deployment-manager-core.war "
                + backupDir);
        expectLastCall().once();
        connector.transferFileToHost(isA(File.class), isA(String.class));
        expectLastCall().once();
        expect(connector.getOutputBuffer()).andReturn("");

        replay(connector);

        warDeployer.deploy(artifact, destination, backupDir, connector);

        verify(connector);
    }

    /**
     * Test method for
     * {@link nl.tranquilizedquality.adm.core.business.deployer.war.WarDeployer#deploy(nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact, nl.tranquilizedquality.adm.commons.business.domain.Destination, nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector)}
     * .
     */
    @Test
    public void testDeployContextPath() {
        final SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        final String now = formatter.format(new Date());
        final String backupDir = "/srv/tomcat/instances/8080/backups/INT_Release_" + now + "/adm-gui";

        file = new File("src/test/resources/automated-deployment-manager-core.war");
        artifact.setFile(file.getAbsolutePath());
        final HibernateDeployerParameter contextPath = new HibernateDeployerParameter();
        contextPath.setType(DeployerParameterType.CONTEXT_PATH);
        contextPath.setValue("adm");
        locations.add(contextPath);

        connector.performCommand(SshCommand.COPY, "/srv/tomcat/instances/8080/webapps/adm.war " + backupDir);
        expectLastCall().once();
        connector.transferFileToHost(isA(File.class), isA(String.class));
        expectLastCall().once();
        expect(connector.getOutputBuffer()).andReturn("");

        replay(connector);

        warDeployer.deploy(artifact, destination, backupDir, connector);

        verify(connector);
    }

    @Test
    public void testDeployFailure() {
        destination.setDeployerParameters(new ArrayList<DeployerParameter>());

        replay(connector);

        try {
            warDeployer.deploy(artifact, destination, null, connector);
            fail("Deployment should fail!");
        } catch (final Exception e) {
            final String message = e.getMessage();
            assertEquals("Destination locations not configured properly!", message);
        }

        verify(connector);
    }

}
