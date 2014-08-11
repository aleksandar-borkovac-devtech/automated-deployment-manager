/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 3 jun. 2011 File: TomcatArtifactDeployer.java
 * Package: nl.tranquilizedquality.adm.core.business.deployer
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
package nl.tranquilizedquality.adm.core.business.deployer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import nl.tranquilizedquality.adm.commons.business.deployer.ArtifactDeployer;
import nl.tranquilizedquality.adm.commons.business.deployer.Deployer;
import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.core.business.deployer.connector.ssh.SshCommand;
import nl.tranquilizedquality.adm.core.business.deployer.exception.DeployException;
import nl.tranquilizedquality.adm.core.business.deployer.exception.UnsupportedArtifactTypeException;
import nl.tranquilizedquality.adm.core.business.deployer.exception.UnsupportedProtocolException;
import nl.tranquilizedquality.adm.core.business.manager.impl.AbstractProgressManager;
import nl.tranquilizedquality.adm.core.util.AdmValidate;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Deployer that can deploy an artifact to Tomcat.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public class TomcatArtifactDeployer extends AbstractProgressManager implements ArtifactDeployer {

    /** logger for this class */
    private static final Log LOGGER = LogFactory.getLog(TomcatArtifactDeployer.class);

    /**
     * If Tomcat is configures as a service the service name will be used to
     * start and stop it.
     */
    private String tomcatServiceName;

    /**
     * The start command that will be used when Tomcat is installed as a
     * service.
     */
    private String tomcatServiceStartCommand;

    /**
     * The stop command that will be used when Tomcat is installed as a service.
     */
    private String tomcatServiceStopCommand;

    /** The connectors this deployer supports. */
    private Map<Protocol, ProtocolConnector> connectors;

    /** The deployers used by this {@link ArtifactDeployer}. */
    @SuppressWarnings("rawtypes")
    private Map<ArtifactType, Deployer> deployers;

    /**
     * Default constructor.
     */
    @SuppressWarnings("rawtypes")
    public TomcatArtifactDeployer() {
        connectors = new EnumMap<Protocol, ProtocolConnector>(Protocol.class);
        deployers = new EnumMap<ArtifactType, Deployer>(ArtifactType.class);
        tomcatServiceStartCommand = "start";
        tomcatServiceStopCommand = "stop";
    }

    @Override
    public String deploy(final MavenArtifact artifact, final Destination destination) {

        registerActivity("Deploying to Tomcat");

        final DestinationHost destinationHost = destination.getDestinationHost();
        final String hostName = destinationHost.getHostName();
        final String username = destinationHost.getUsername();
        final String password = destinationHost.getPassword();
        final String privateKey = destinationHost.getPrivateKey();
        final String terminal = destinationHost.getTerminal();
        final Integer port = destinationHost.getPort();
        final Protocol protocol = destinationHost.getProtocol();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Selecting connector for protocol: " + protocol);
        }

        /*
         * Retrieve protocol.
         */
        final ProtocolConnector connector = connectors.get(protocol);

        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Connector selected.");
                LOGGER.info("Connecting to host: " + hostName + " on port " + port + " with user: " + username);
            }

            /*
             * Connects to the host machine.
             */
            if (StringUtils.isEmpty(privateKey)) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Using password authentication..");
                }

                connector.connectToHost(terminal, hostName, port, username, password, null);
            } else {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Using key authentication..");
                }

                /*
                 * Retrieve the bytes of the private key.
                 */
                final byte[] bytes = privateKey.getBytes();

                /*
                 * Connect to the host using key authentication.
                 */
                connector.connectToHost(terminal, hostName, port, username, password, bytes);
            }

            /*
             * Deploy the artifact.
             */
            switch (protocol) {
                case SSH:
                    deployThroughSSH(artifact, destination, connector);
                    break;

                default:
                    final String msg = "Protocol not supported by deployer! " + protocol;
                    throw new UnsupportedProtocolException(msg);
            }
        } catch (final Exception e) {
            final String msg = "Failed to deploy the artifact!";
            final String logs = connector.getOutputBuffer();

            final DeployException deployException = new DeployException(msg, e);
            deployException.setLogs(logs);

            throw deployException;
        } finally {
            /*
             * Disconnects from the host.
             */
            connector.disconnectFromHost();
        }

        /*
         * Return the logs.
         */
        return connector.getSessionLogs();
    }

    /**
     * Deploys the {@link MavenArtifact} to the specified {@link Destination}
     * using SSH.
     * 
     * @param artifact
     *            The artifact that will be deployed.
     * @param destination
     *            The destination where the artifact will be deployed to.
     * @param connector
     *            The {@link ProtocolConnector} that will be used to communicate
     *            with the destination host.
     */
    @SuppressWarnings({"rawtypes", "unchecked" })
    private void deployThroughSSH(final MavenArtifact artifact, final Destination destination, final ProtocolConnector connector) {
        String backupDirectory = null;
        String tomcatHome = null;
        String contextPath = null;

        /*
         * For some weird reason there is no transaction available although the
         * deploy method is marked with an aspect for transaction management.
         */
        final List<DeployerParameter> locations = destination.getDeployerParameters();
        for (final DeployerParameter location : locations) {
            final DeployerParameterType locationType = location.getType();
            switch (locationType) {
                case APP_SERVER_LOCATION:
                    tomcatHome = location.getValue();
                    break;

                case BACKUP_LOCATION:
                    backupDirectory = location.getValue();
                    break;

                case CONTEXT_PATH:
                    contextPath = location.getValue();
                    break;

                default:
                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("No matching location type found..");
                    }
            }
        }

        connector.performCommand(SshCommand.CHANGE_DIRECTORY, tomcatHome);

        final MavenModule parentModule = artifact.getParentModule();
        final ArtifactType type = parentModule.getType();

        /*
         * Check if backup needs to be made.
         */
        AdmValidate.notEmpty(backupDirectory, "No backup directory specified!");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Backing up previous installation...");
        }

        /*
         * Go to TOMCAT_HOME.
         */
        connector.performCommand(SshCommand.CHANGE_DIRECTORY, tomcatHome);

        /*
         * Create backup directory.
         */
        connector.performCommand(SshCommand.MAKE_DIRECTORY, backupDirectory);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Created directory " + backupDirectory);
        }

        /*
         * Create release specific backup directory. Release name shouldn't
         * contain spaces since Linux Doesn't support that. If it does the
         * spaces will be replaced with _.
         */
        final Release release = artifact.getRelease();
        final String name = release.getName();
        final String compatibleName = StringUtils.replace(name, " ", "_");
        final SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");
        final String now = formatter.format(new Date());
        final String releaseBackupDirectory = backupDirectory + compatibleName + "_" + now + "/";
        connector.performCommand(SshCommand.MAKE_DIRECTORY, releaseBackupDirectory);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Created release backup directory " + releaseBackupDirectory);
        }

        /*
         * Create artifact specific backup directory.
         */
        final String artifactBackupDirectory = releaseBackupDirectory + parentModule.getArtifactId();
        connector.performCommand(SshCommand.MAKE_DIRECTORY, artifactBackupDirectory);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Created artifact backup directory " + artifactBackupDirectory);
        }

        final Boolean targetSystemShutdown = artifact.getTargetSystemShutdown();
        final Boolean targetSystemStartup = artifact.getTargetSystemStartup();

        /*
         * Stop Tomcat.
         */
        if (targetSystemShutdown) {
            stopTomcat(connector, tomcatHome);
        }

        switch (type) {
            case JAR: {

                /*
                 * Deploy artifact.
                 */
                final Deployer deployer = deployers.get(type);
                deployer.deploy(artifact, destination, artifactBackupDirectory, connector);
                break;
            }

            case WAR: {
                /*
                 * Clean up Tomcat.
                 */
                cleanUpTomcat(artifact, destination, connector, tomcatHome);

                removeExplodedWar(artifact, connector, tomcatHome, contextPath);

                /*
                 * Deploy artifact.
                 */
                final Deployer deployer = deployers.get(type);
                deployer.deploy(artifact, destination, artifactBackupDirectory, connector);
                break;
            }

            case TAR_GZIP: {
                /*
                 * Backup Tomcat conf directory.
                 */
                String copyCommand = tomcatHome + "conf/ " + artifactBackupDirectory;
                connector.performCommand(SshCommand.RECURSIVE_COPY, copyCommand);

                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Backedup configuration directory...");
                }

                /*
                 * Backup Tomcat shared directory.
                 */
                copyCommand = tomcatHome + "shared/ " + artifactBackupDirectory;
                connector.performCommand(SshCommand.RECURSIVE_COPY, copyCommand);

                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Backedup shared directory...");
                }

                /*
                 * Clean up Tomcat.
                 */
                cleanUpTomcat(artifact, destination, connector, tomcatHome);

                /*
                 * Deploy artifact.
                 */
                final Deployer deployer = deployers.get(type);
                deployer.deploy(artifact, destination, artifactBackupDirectory, connector);
                break;
            }

            default:
                final String msg = "Type " + type + " is not supported by this deployer!";
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(msg);
                }

                throw new UnsupportedArtifactTypeException(msg);
        }

        /*
         * Start Tomcat.
         */
        if (targetSystemStartup) {
            startTomcat(connector, tomcatHome);
        }

        /*
         * Flush the output buffer since we are finished deploying the artifact.
         */
        connector.flushOutputBuffer();
    }

    /**
     * Cleans up the work directory of Tomcat.
     * 
     * @param artifact
     *            The artifact that will be deployed.
     * @param destination
     *            The destination server.
     * @param connector
     *            The {@link ProtocolConnector} that will be used to perform the
     *            clean up.
     * @param tomcatHome
     *            The root directory of where Tomcat is installed.
     */
    private void cleanUpTomcat(final MavenArtifact artifact, final Destination destination, final ProtocolConnector connector,
            final String tomcatHome) {

        registerActivity("Cleaning up Tomcat");

        /*
         * Remove work directory.
         */
        final String tomcatWorkDirectory = tomcatHome + "work/";
        connector.performCommand(SshCommand.RECURSIVE_REMOVE_FORCED, tomcatWorkDirectory);

    }

    /**
     * Removes the exploded WAR specified by the artifact.
     * 
     * @param artifact
     *            The artifact that will be deployed.
     * @param connector
     *            The connector used to perform commands.
     * @param tomcatHome
     *            The root directory of where Tomcat is installed.
     * @param contextPath
     *            The name of the context path which represents the name of the
     *            the exploded WAR directory.
     */
    private void removeExplodedWar(final MavenArtifact artifact, final ProtocolConnector connector, final String tomcatHome,
            final String contextPath) {

        registerActivity("Removing exploded WAR directory");

        String name = null;
        if (StringUtils.isBlank(contextPath)) {
            final MavenModule parentModule = artifact.getParentModule();
            name = parentModule.getArtifactId();
        } else {
            name = contextPath;
        }

        final String webAppsDirectory = tomcatHome + "webapps/";
        final String fileToBeRemoved = webAppsDirectory + name;
        connector.performCommand(SshCommand.RECURSIVE_REMOVE_FORCED, fileToBeRemoved);
    }

    /**
     * Starts up Tomcat by either using a service or the default Tomcat
     * startup.sh script.
     * 
     * @param connector
     *            The connector that will be used to start up Tomcat on the
     *            remote host.
     */
    private void startTomcat(final ProtocolConnector connector, final String tomcatHome) {

        registerActivity("Starting Tomcat");

        /*
         * Start Tomcat.
         */
        if (!StringUtils.isEmpty(tomcatServiceName) && !StringUtils.isEmpty(tomcatServiceStartCommand)) {
            final String startCommand = "service " + tomcatServiceName + " " + tomcatServiceStartCommand;
            connector.performCustomCommand(startCommand);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Starting Tomcat with service: " + tomcatServiceName);
            }
        } else {
            /*
             * Use Tomcat default startup.sh script.
             */
            connector.performCommand(SshCommand.CHANGE_DIRECTORY, tomcatHome + "bin/");

            final String startCommand = "./startup.sh";
            connector.performCustomCommand(startCommand);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Starting Tomcat with ./startup.sh");
            }
        }
    }

    /**
     * Stops the Tomcat server on the remote host by using either a service or
     * the default shutdown.sh script from Tomcat. If Tomcat couldn't be stopped
     * it will try to kill the process on the remote host and if that doesn't
     * work it will throw an exception.
     * 
     * @param connector
     *            The connector that will be used to stop Tomcat.
     */
    private void stopTomcat(final ProtocolConnector connector, final String tomcatHome) {

        registerActivity("Stopping Tomcat");

        /*
         * Stop Tomcat gracefully.
         */
        if (!StringUtils.isEmpty(tomcatServiceName) && !StringUtils.isEmpty(tomcatServiceStopCommand)) {
            final String stopCommand = "service " + tomcatServiceName + " " + tomcatServiceStopCommand;
            connector.performCustomCommand(stopCommand);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Stopped Tomcat with serivice: " + tomcatServiceName);
            }
        } else {
            /*
             * Use Tomcat default shutdown.sh script.
             */
            connector.performCommand(SshCommand.CHANGE_DIRECTORY, tomcatHome + "bin/");

            final String stopCommand = "./shutdown.sh";
            connector.performCustomCommand(stopCommand);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Stopped Tomcat with ./shutdown.sh");
            }
        }

        /*
         * Wait for 20 seconds to check the response buffer.
         */
        pause(20000);

        /*
         * Check if Tomcat is shutdown and then continue.
         */
        final String checkPidCommand = "ps -ef | grep '[B]ootstrap start' | grep " + tomcatHome + " | awk '{print \"PID: \"$2 }'\n";
        /*
         * Check process ID of the Tomcat instance.
         */
        connector.performCustomCommand(checkPidCommand);

        /*
         * Wait for 2 seconds to check if Tomcat is down.
         */
        pause(2000);

        /*
         * Check if Tomcat is still running by looking at the response output
         * line which is the second last.
         */
        boolean tomcatDown = isTomcatDown(connector);

        /*
         * If Tomcat is NOT down kill it if possible.
         */
        if (!tomcatDown) {
            /*
             * Check process ID.
             */
            connector.performCustomCommand(checkPidCommand);

            /*
             * Wait for 2 seconds for response on PID command.
             */
            pause(2000);

            /*
             * Kill Tomcat process.
             */
            final List<String> bufferLines = connector.getOutputBufferLines();
            final String checkPidOutputLine = bufferLines.get(bufferLines.size() - 2);
            final String pid = StringUtils.substringAfter(checkPidOutputLine, "PID: ");
            final String killCommand = "kill -9 " + pid;
            connector.performCustomCommand(killCommand);

            /*
             * Wait for 10 seconds to check if Tomcat was killed.
             */
            pause(10000);

            /*
             * Check if Tomcat was killed.
             */
            tomcatDown = isTomcatDown(connector);
        }

        /*
         * If Tomcat is still not DOWN fail the deployment since there is
         * something wrong with the Tomcat insance or you don't have any
         * permissions to kill the process or something like this.
         */
        if (!tomcatDown) {
            final String msg = "Tomcat couldn't be stopped! Please check your Tomcat instance and permissions.";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to shutdown Tomcat!");
                LOGGER.error(msg);
            }

            throw new DeployException(msg);
        }
    }

    /**
     * Waits the specified number of miliseconds.
     * 
     * @param waitTime
     *            The time to wait in miliseconds.
     */
    private void pause(final long waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (final InterruptedException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to sleep!", e);
            }
        }
    }

    /**
     * Determines if Tomcat is still running on the remote host.
     * 
     * @param connector
     *            The connector that will be used to check if Tomcat is still
     *            running or not.
     * @return Returns true if Tomcat is down otherwise it will return false.
     */
    private boolean isTomcatDown(final ProtocolConnector connector) {
        final List<String> bufferLines = connector.getOutputBufferLines();

        /*
         * To avoid an index out of bounds exception we should return false.
         */
        final int bufferLinesSize = bufferLines.size();
        if (bufferLines == null || bufferLines.isEmpty() || bufferLinesSize <= 2) {
            return false;
        }

        /*
         * We need to get the second last line since the last line will always
         * be the prompt itself.
         */
        final String checkPidOutputLine = bufferLines.get(bufferLinesSize - 2);
        final boolean contains = StringUtils.contains(checkPidOutputLine, "PID:");

        return !contains;
    }

    /**
     * @param tomcatServiceName
     *            the tomcatServiceName to set
     */
    public void setTomcatServiceName(final String tomcatServiceName) {
        this.tomcatServiceName = tomcatServiceName;
    }

    /**
     * @param tomcatServiceStartCommand
     *            the tomcatServiceStartCommand to set
     */
    public void setTomcatServiceStartCommand(final String tomcatServiceStartCommand) {
        this.tomcatServiceStartCommand = tomcatServiceStartCommand;
    }

    /**
     * @param tomcatServiceStopCommand
     *            the tomcatServiceStopCommand to set
     */
    public void setTomcatServiceStopCommand(final String tomcatServiceStopCommand) {
        this.tomcatServiceStopCommand = tomcatServiceStopCommand;
    }

    /**
     * @param connectors
     *            the connectors to set
     */
    @Required
    public void setConnectors(final Map<Protocol, ProtocolConnector> connectors) {
        this.connectors = connectors;
    }

    /**
     * @param deployers
     *            the deployers to set
     */
    @Required
    @SuppressWarnings({"rawtypes" })
    public void setDeployers(final Map<ArtifactType, Deployer> deployers) {
        this.deployers = deployers;
    }

}
