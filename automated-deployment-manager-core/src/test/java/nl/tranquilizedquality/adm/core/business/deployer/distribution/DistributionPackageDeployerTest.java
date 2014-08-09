/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 21 jul. 2011 File: DistributionPackageDeployerTest.java
 * Package: nl.tranquilizedquality.adm.core.business.deployer.distribution
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
package nl.tranquilizedquality.adm.core.business.deployer.distribution;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
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
import nl.tranquilizedquality.adm.core.business.deployer.connector.ssh.SshCommand;
import nl.tranquilizedquality.adm.core.business.deployer.distribution.DistributionPackageDeployer;
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
 * Test for {@link DistributionPackageDeployer}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 21 jul. 2011
 */
public class DistributionPackageDeployerTest {

    /** Deployer that will be tested. */
    private DistributionPackageDeployer distDeployer;

    /** Mocked connector. */
    private ProtocolConnector connector;

    private File file;

    private HibernateDeployerParameter location;

    private HibernateDestinationHost destinationHost;

    private HibernateDestination destination;

    private HibernateMavenArtifact artifact;

    private HibernateMavenArtifact distribution;

    private HibernateEnvironment environment;

    private HibernateDeployer deployer;

    private File tarFile;

    private HibernateMavenModule module;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        distDeployer = new DistributionPackageDeployer();
        distDeployer.setWorkDirectory("target/");

        connector = createMock(ProtocolConnector.class);

        final HibernateRelease release = new HibernateRelease();
        release.setName("INT Release");
        release.setReleaseDate(new Date());

        final List<Destination> destinations = new ArrayList<Destination>();

        module = new HibernateMavenModule();
        module.setName("adm-gui");
        module.setType(ArtifactType.WAR);
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

        deployer = new HibernateDeployer();
        deployer.setName("TOMCAT_DEPLOYER");

        destination = new HibernateDestination();
        destination.setDeployer(deployer);
        destination.setEnvironment(environment);
        destination.setDeployerParameters(locations);
        destination.setPrefix("tomcat");
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
        tarFile = new File("src/test/resources/automated-deployment-manager-dist-1.0.0-SNAPSHOT.tar.gz");

        artifact = new HibernateMavenArtifact();
        artifact.setParentModule(module);
        artifact.setRelease(release);
        artifact.setVersion("1.0.0");
        artifact.setFile(file.getAbsolutePath());

        final HibernateMavenModule distMain = new HibernateMavenModule();
        distMain.setName("adm-gui");
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
     * {@link nl.tranquilizedquality.adm.core.business.deployer.distribution.DistributionPackageDeployer#deploy(nl.tranquilizedquality.adm.commons.business.domain.DistributionMavenArtifact, nl.tranquilizedquality.adm.commons.business.domain.Destination, nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector)}
     * .
     */
    @Test
    public void testDeploy() {
        connector.transferFileToHost(isA(File.class), isA(String.class));
        expectLastCall().times(4);
        connector.performCommand(SshCommand.COPY,
                "/srv/tomcat/instances/8080/webapps/adm-gui.war /srv/tomcat/instances/8080/backup/INT_Release_2154687/adm-gui/");
        expectLastCall().once();
        connector.performCommand(SshCommand.RECURSIVE_REMOVE_FORCED, "/srv/tomcat/instances/8080/webapps/adm-gui");
        expectLastCall().once();
        expect(connector.getOutputBuffer()).andReturn("");

        replay(connector);

        distDeployer.deploy(distribution, destination, "/srv/tomcat/instances/8080/backup/INT_Release_2154687/adm-gui/", connector);

        verify(connector);
    }

}
