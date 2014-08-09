/*
 * @(#)RemotePowerShellDistributionPackageDeployer.java 20 feb. 2013
 * 
 * Copyright (c) 2009 Tranquilized Quality All rights reserved.
 * Tranquilized Quality PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package nl.tranquilizedquality.adm.core.business.deployer.shell;

import java.io.File;
import java.util.Date;

import nl.tranquilizedquality.adm.commons.business.deployer.Deployer;
import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.core.business.deployer.exception.DeployException;
import nl.tranquilizedquality.adm.core.util.AdmValidate;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Deployer that can deploy an artifact using packaged power shell scripts on a windows machine.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 20 feb. 2013
 */
public class RemotePowerShellDistributionPackageDeployer implements Deployer<MavenArtifact> {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(RemotePowerShellDistributionPackageDeployer.class);

    /** The remote work directory on the target machine. */
    private String remoteWorkDirectory;

    @Override
    public String deploy(final MavenArtifact artifact, final Destination destination, final String backupLocation,
            final ProtocolConnector connector) {
        final DestinationHost destinationHost = destination.getDestinationHost();
        final String hostName = destinationHost.getHostName();
        final MavenModule parentModule = artifact.getParentModule();
        final ArtifactType type = parentModule.getType();
        final String fileName = artifact.getFile();
        final File artifactFile = new File(fileName);

        AdmValidate.isTrue(type == ArtifactType.ZIP, "Unsupported artifact type! " + type);

        /*
         * Setup temporary destination path.
         */
        final Date now = new Date();
        final String destinationPath = remoteWorkDirectory + "/" + now.getTime() + "/";
        final String createRemoteWorkDirectory = "mkdir " + destinationPath;
        connector.performCustomCommand(createRemoteWorkDirectory);

        /*
         * Copy ZIP file to destination host.
         */
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Transferring ZIP to " + hostName);
            }

            connector.transferFileToHost(artifactFile, destinationPath);
        } catch (final Exception e) {
            final String msg =
                    "Failed to transfer distribution package! -> " + artifactFile.getAbsolutePath() + " to " + destinationPath
                        + " on host " + hostName;

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg, e);
            }

            throw new DeployException(msg, e);
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Transferred distribution " + destinationPath + artifactFile.getName());
        }

        /*
         * Extract ZIP file on remote machine.
         */

        final String unzipCommand = "unzip -o " + destinationPath + artifactFile.getName() + " -d " + destinationPath;
        connector.performCustomCommand(unzipCommand);

        /*
         * Retrieve the environment name.
         */
        final Environment environment = destination.getEnvironment();
        final String environmentName = environment.getName();

        /*
         * Navigate to deploy script directory.
         */final String deployScriptDirectory = destinationPath + parentModule.getArtifactId() + "-" + artifact.getVersion();
        final String changeToPowerShellDirectory = "cd " + deployScriptDirectory;
        connector.performCustomCommand(changeToPowerShellDirectory);

        /*
         * Execute power shell script.
         */
        final String deployScript = ".\\deploy_" + environmentName.toLowerCase() + ".ps1";

        try {
            connector.performCustomCommand(deployScript);
        } catch (final Exception e) {
            final String msg = "Failed to execute remote shell script! -> " + deployScript;

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg, e);
            }

            throw new DeployException(msg, e);
        } finally {
            connector.disconnectFromHost();
        }

        final String logs = connector.getOutputBuffer();

        if (StringUtils.contains(logs, "ERROR") || StringUtils.contains(logs, "error") || StringUtils.contains(logs, "Exception")) {
            final DeployException deployException = new DeployException("Errors occured during execution of the deployment!");
            deployException.setLogs(logs);
            throw deployException;
        }

        return logs;
    }

    @Required
    public void setRemoteWorkDirectory(final String remoteWorkDirectory) {
        this.remoteWorkDirectory = remoteWorkDirectory;
    }

}
