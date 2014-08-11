package nl.tranquilizedquality.adm.core.business.deployer;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.deployer.Deployer;
import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.core.business.deployer.TomcatArtifactDeployer;
import nl.tranquilizedquality.adm.core.business.deployer.connector.ssh.SshCommand;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployer;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDeployerParameter;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestination;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateDestinationHost;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * Test for {@link TomcatArtifactDeployer}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 7 jul. 2011
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TomcatArtifactDeployerTest extends EasyMockSupport {

    /** Deployer that will be tested. */
    private TomcatArtifactDeployer deployer;

    private HibernateMavenModule module;

    private HibernateDestination destination;

    private HibernateDeployerParameter location;

    private File file;

    private File tarFile;

    private HibernateMavenArtifact warArtifact;

    private HibernateMavenArtifact distribution;

    private HibernateEnvironment environment;

    private ProtocolConnector connector;

    private Deployer<MavenArtifact> jarDeployer;

    private Deployer<MavenArtifact> warDeployer;

    private Deployer<MavenArtifact> distDeployer;

    private File jarFile;

    private HibernateMavenArtifact jarArtifact;

    private HibernateMavenModule jarModule;

    private HibernateDestinationHost destinationHost;

    private HibernateDeployer tomcatDeployer;

    @Before
    @SuppressWarnings({"rawtypes", "unchecked" })
    public void setUp() throws Exception {
        deployer = new TomcatArtifactDeployer();

        connector = createMock(ProtocolConnector.class);
        warDeployer = createMock(Deployer.class);
        distDeployer = createMock(Deployer.class);
        jarDeployer = createMock(Deployer.class);

        final EnumMap<Protocol, ProtocolConnector> connectors = new EnumMap<Protocol, ProtocolConnector>(Protocol.class);
        connectors.put(Protocol.SSH, connector);
        deployer.setConnectors(connectors);

        final EnumMap<ArtifactType, Deployer> deployers = new EnumMap<ArtifactType, Deployer>(ArtifactType.class);
        deployers.put(ArtifactType.TAR_GZIP, distDeployer);
        deployers.put(ArtifactType.WAR, warDeployer);
        deployers.put(ArtifactType.JAR, jarDeployer);
        deployer.setDeployers(deployers);

        /*
         * Initialize warArtifact
         */
        final HibernateRelease release = new HibernateRelease();
        release.setName("INT Release");
        release.setReleaseDate(new Date());

        final List<Destination> destinations = new ArrayList<Destination>();

        module = new HibernateMavenModule();
        module.setName("Automated Deployment Manager");
        module.setType(ArtifactType.WAR);
        module.setDestinations(destinations);
        module.setGroup("nl.Tranquilized Quality.adm");
        module.setTargetSystemShutdown(true);
        module.setTargetSystemStartup(true);
        module.setArtifactId("adm-gui");

        jarModule = new HibernateMavenModule();
        jarModule.setName("Automated Deployment Manager Core Module");
        jarModule.setType(ArtifactType.JAR);
        jarModule.setDestinations(destinations);
        jarModule.setGroup("nl.Tranquilized Quality.adm");
        jarModule.setArtifactId("adm-core");

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
        tarFile = new File("src/test/resources/dist.tar.gz");
        jarFile = new File("src/test/resources/automated-deployment-manager-core-1.0.0-SNAPSHOT.jar");

        jarArtifact = new HibernateMavenArtifact();
        jarArtifact.setParentModule(jarModule);
        jarArtifact.setRelease(release);
        jarArtifact.setVersion("1.0.0");
        jarArtifact.setFile(jarFile.getAbsolutePath());
        jarArtifact.setTargetSystemShutdown(true);
        jarArtifact.setTargetSystemStartup(true);

        warArtifact = new HibernateMavenArtifact();
        warArtifact.setParentModule(module);
        warArtifact.setRelease(release);
        warArtifact.setVersion("1.0.0");
        warArtifact.setFile(file.getAbsolutePath());
        warArtifact.setTargetSystemShutdown(true);
        warArtifact.setTargetSystemStartup(true);

        final HibernateMavenModule distMain = new HibernateMavenModule();
        distMain.setName("Automated Deployment Manager Distribution Module");
        distMain.setType(ArtifactType.TAR_GZIP);
        distMain.setDestinations(destinations);
        distMain.setGroup("nl.Tranquilized Quality.adm");
        distMain.setTargetSystemShutdown(true);
        distMain.setTargetSystemStartup(true);
        distMain.setArtifactId("adm-gui-dist");

        distribution = new HibernateMavenArtifact();
        distribution.setParentModule(distMain);
        distribution.setRelease(release);
        distribution.setVersion("1.0.0");
        distribution.setFile(tarFile.getAbsolutePath());
        distribution.setTargetSystemShutdown(true);
        distribution.setTargetSystemStartup(true);
    }

    @Test
    public void testDeployJar() {
        final SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");
        final String now = formatter.format(new Date());
        final String backupDir = "/srv/tomcat/instances/8080/backups/INT_Release_" + now + "/adm-core";

        connector.connectToHost(destinationHost.getTerminal(), destinationHost.getHostName(), destinationHost.getPort(),
                destinationHost.getUsername(), destinationHost.getPassword(), null);
        expectLastCall().once();
        connector.performCommand(SshCommand.CHANGE_DIRECTORY, "/srv/tomcat/instances/8080/");
        expectLastCall().times(2);
        connector.performCommand(SshCommand.MAKE_DIRECTORY, "/srv/tomcat/instances/8080/backups/");
        expectLastCall().once();
        connector.performCommand(SshCommand.MAKE_DIRECTORY, "/srv/tomcat/instances/8080/backups/INT_Release_" + now + "/");
        expectLastCall().once();
        connector.performCommand(SshCommand.MAKE_DIRECTORY, backupDir);
        expectLastCall().once();
        expect(jarDeployer.deploy(jarArtifact, destination, backupDir, connector)).andReturn("");
        connector.performCommand(SshCommand.CHANGE_DIRECTORY, "/srv/tomcat/instances/8080/bin/");
        expectLastCall().times(2);
        connector.performCustomCommand("./shutdown.sh");
        expectLastCall().once();
        connector.performCustomCommand(isA(String.class));
        expectLastCall().atLeastOnce();
        final List<String> lines = new ArrayList<String>();
        lines.add("[sape@server]");
        lines.add("[sape@server]");
        lines.add("[sape@server]");
        expect(connector.getOutputBufferLines()).andReturn(lines);
        connector.flushOutputBuffer();
        expectLastCall().once();
        connector.disconnectFromHost();
        expectLastCall().once();
        expect(connector.getSessionLogs()).andReturn("Logs");

        replayAll();

        deployer.deploy(jarArtifact, destination);

        verifyAll();
    }

    @Test
    public void testDeployWar() {
        final SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");
        final String now = formatter.format(new Date());
        final String backupDir = "/srv/tomcat/instances/8080/backups/INT_Release_" + now + "/adm-gui";

        connector.connectToHost(destinationHost.getTerminal(), destinationHost.getHostName(), destinationHost.getPort(),
                destinationHost.getUsername(), destinationHost.getPassword(), null);
        expectLastCall().once();
        connector.performCommand(SshCommand.CHANGE_DIRECTORY, "/srv/tomcat/instances/8080/");
        expectLastCall().times(2);
        connector.performCommand(SshCommand.MAKE_DIRECTORY, "/srv/tomcat/instances/8080/backups/");
        expectLastCall().once();
        connector.performCommand(SshCommand.MAKE_DIRECTORY, "/srv/tomcat/instances/8080/backups/INT_Release_" + now + "/");
        expectLastCall().once();
        connector.performCommand(SshCommand.MAKE_DIRECTORY, backupDir);
        expectLastCall().once();
        expect(warDeployer.deploy(warArtifact, destination, backupDir, connector)).andReturn("");
        connector.performCommand(SshCommand.CHANGE_DIRECTORY, "/srv/tomcat/instances/8080/bin/");
        expectLastCall().times(2);
        connector.performCustomCommand("./shutdown.sh");
        expectLastCall().once();
        connector.performCustomCommand(isA(String.class));
        expectLastCall().atLeastOnce();
        final List<String> lines = new ArrayList<String>();
        lines.add("[sape@server]");
        lines.add("[sape@server]");
        lines.add("[sape@server]");
        expect(connector.getOutputBufferLines()).andReturn(lines);
        connector.performCommand(SshCommand.RECURSIVE_REMOVE_FORCED, "/srv/tomcat/instances/8080/work/");
        expectLastCall().once();
        connector.performCommand(SshCommand.RECURSIVE_REMOVE_FORCED, "/srv/tomcat/instances/8080/webapps/adm-gui");
        expectLastCall().once();
        connector.flushOutputBuffer();
        expectLastCall().once();
        connector.disconnectFromHost();
        expectLastCall().once();
        expect(connector.getSessionLogs()).andReturn("Logs");

        replayAll();

        deployer.deploy(warArtifact, destination);

        verifyAll();
    }

    @Test
    public void testDeployWarWithContextPath() {

        final List<DeployerParameter> deployerParameters = destination.getDeployerParameters();
        location = new HibernateDeployerParameter();
        location.setValue("adm");
        location.setType(DeployerParameterType.CONTEXT_PATH);
        deployerParameters.add(location);

        final SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");
        final String now = formatter.format(new Date());
        final String backupDir = "/srv/tomcat/instances/8080/backups/INT_Release_" + now + "/adm-gui";

        connector.connectToHost(destinationHost.getTerminal(), destinationHost.getHostName(), destinationHost.getPort(),
                destinationHost.getUsername(), destinationHost.getPassword(), null);
        expectLastCall().once();
        connector.performCommand(SshCommand.CHANGE_DIRECTORY, "/srv/tomcat/instances/8080/");
        expectLastCall().times(2);
        connector.performCommand(SshCommand.MAKE_DIRECTORY, "/srv/tomcat/instances/8080/backups/");
        expectLastCall().once();
        connector.performCommand(SshCommand.MAKE_DIRECTORY, "/srv/tomcat/instances/8080/backups/INT_Release_" + now + "/");
        expectLastCall().once();
        connector.performCommand(SshCommand.MAKE_DIRECTORY, backupDir);
        expectLastCall().once();
        expect(warDeployer.deploy(warArtifact, destination, backupDir, connector)).andReturn("");
        connector.performCommand(SshCommand.CHANGE_DIRECTORY, "/srv/tomcat/instances/8080/bin/");
        expectLastCall().times(2);
        connector.performCustomCommand("./shutdown.sh");
        expectLastCall().once();
        connector.performCustomCommand(isA(String.class));
        expectLastCall().atLeastOnce();
        final List<String> lines = new ArrayList<String>();
        lines.add("[sape@server]");
        lines.add("[sape@server]");
        lines.add("[sape@server]");
        expect(connector.getOutputBufferLines()).andReturn(lines);
        connector.performCommand(SshCommand.RECURSIVE_REMOVE_FORCED, "/srv/tomcat/instances/8080/work/");
        expectLastCall().once();
        connector.performCommand(SshCommand.RECURSIVE_REMOVE_FORCED, "/srv/tomcat/instances/8080/webapps/adm");
        expectLastCall().once();
        connector.flushOutputBuffer();
        expectLastCall().once();
        connector.disconnectFromHost();
        expectLastCall().once();
        expect(connector.getSessionLogs()).andReturn("Logs");

        replayAll();

        deployer.deploy(warArtifact, destination);

        verifyAll();
    }

    @Test
    public void testDeployDistribution() {
        final SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");
        final String now = formatter.format(new Date());
        final String backupDir = "/srv/tomcat/instances/8080/backups/INT_Release_" + now + "/adm-gui-dist";

        connector.connectToHost(destinationHost.getTerminal(), destinationHost.getHostName(), destinationHost.getPort(),
                destinationHost.getUsername(), destinationHost.getPassword(), null);
        expectLastCall().once();
        connector.performCommand(SshCommand.CHANGE_DIRECTORY, "/srv/tomcat/instances/8080/");
        expectLastCall().times(2);
        connector.performCommand(SshCommand.MAKE_DIRECTORY, "/srv/tomcat/instances/8080/backups/");
        expectLastCall().once();
        connector.performCommand(SshCommand.MAKE_DIRECTORY, "/srv/tomcat/instances/8080/backups/INT_Release_" + now + "/");
        expectLastCall().once();
        connector.performCommand(SshCommand.MAKE_DIRECTORY, backupDir);
        expectLastCall().once();
        connector.performCommand(SshCommand.RECURSIVE_COPY, "/srv/tomcat/instances/8080/conf/ " + backupDir);
        expectLastCall().once();
        connector.performCommand(SshCommand.RECURSIVE_COPY, "/srv/tomcat/instances/8080/shared/ " + backupDir);
        expectLastCall().once();
        expect(distDeployer.deploy(distribution, destination, backupDir, connector)).andReturn("");
        connector.performCommand(SshCommand.CHANGE_DIRECTORY, "/srv/tomcat/instances/8080/bin/");
        expectLastCall().times(2);
        connector.performCustomCommand("./shutdown.sh");
        expectLastCall().once();
        connector.performCustomCommand(isA(String.class));
        expectLastCall().atLeastOnce();
        final List<String> lines = new ArrayList<String>();
        lines.add("[sape@server]");
        lines.add("[sape@server]");
        lines.add("[sape@server]");
        expect(connector.getOutputBufferLines()).andReturn(lines);
        connector.performCommand(SshCommand.RECURSIVE_REMOVE_FORCED, "/srv/tomcat/instances/8080/work/");
        expectLastCall().once();
        connector.flushOutputBuffer();
        expectLastCall().once();
        connector.disconnectFromHost();
        expectLastCall().once();
        expect(connector.getSessionLogs()).andReturn("Logs");

        replayAll();

        deployer.deploy(distribution, destination);

        verifyAll();
    }

}
