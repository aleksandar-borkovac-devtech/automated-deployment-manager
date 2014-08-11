/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 7 jul. 2011 File: WarDeployer.java
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

import java.io.File;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.deployer.Deployer;
import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.core.business.deployer.connector.ssh.SshCommand;
import nl.tranquilizedquality.adm.core.business.deployer.exception.DeployException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Deployer that can deploy a WAR file. This WAR deployer strips off the version
 * from the WAR name so the context will be just the artifactId.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 7 jul. 2011
 */
public class WarDeployer implements Deployer<MavenArtifact> {

    /** logger for this class */
    private static final Log LOGGER = LogFactory.getLog(WarDeployer.class);

    @Override
    public String deploy(final MavenArtifact artifact, final Destination destination, final String artifactBackupDirectory,
            final ProtocolConnector connector) {

        /*
         * Get location values.
         */
        String webAppsDirectory = null;
        String appServerHome = null;
        String contextPath = null;

        final List<DeployerParameter> locations = destination.getDeployerParameters();
        for (final DeployerParameter location : locations) {
            final DeployerParameterType type = location.getType();
            switch (type) {
                case WEB_APPS_LOCATION:
                    webAppsDirectory = location.getValue();
                    break;

                case APP_SERVER_LOCATION:
                    appServerHome = location.getValue();
                    break;

                case CONTEXT_PATH:
                    contextPath = location.getValue();
                    break;

                default:
                    break;
            }
        }

        /*
         * Check if the configuration is done properly.
         */
        if (StringUtils.isEmpty(artifactBackupDirectory) || StringUtils.isEmpty(webAppsDirectory)
                || StringUtils.isEmpty(appServerHome)) {
            final String msg = "Destination locations not configured properly!";

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
                LOGGER.error("Artifact Backup Directory: " + artifactBackupDirectory);
                LOGGER.error("Webapps Directory: " + webAppsDirectory);
            }

            throw new DeployException(msg);
        }

        /*
         * Setup artifact file.
         */
        final String fileName = artifact.getFile();
        final File artifactFile = new File(fileName);

        /*
         * Rename war file to have no trailing version or use the specified
         * context path.
         */
        final String version = artifact.getVersion();
        File renamedFile = null;
        String artifactFileName = null;
        String strippedFileName = fileName;
        if (StringUtils.isNotBlank(contextPath)) {
            renamedFile = new File(contextPath + ".war");
            artifactFile.renameTo(renamedFile);
        } else {
            if (StringUtils.contains(fileName, version) && StringUtils.contains(fileName, "SNAPSHOT")) {

                strippedFileName = fileName.replaceFirst("-" + version + "-SNAPSHOT", "");

                renamedFile = new File(strippedFileName);
                artifactFile.renameTo(renamedFile);
            } else if (StringUtils.contains(fileName, version) && !StringUtils.contains(fileName, "SNAPSHOT")) {

                strippedFileName = fileName.replaceFirst("-" + version, "");

                renamedFile = new File(strippedFileName);
                artifactFile.renameTo(renamedFile);
            } else {
                renamedFile = artifactFile;
            }
        }

        artifactFileName = renamedFile.getName();

        /*
         * Backup WAR.
         */
        final String copyCommand = webAppsDirectory + artifactFileName + " " + artifactBackupDirectory;

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Deploying " + artifactFile.getName());
            LOGGER.info(copyCommand);
        }

        connector.performCommand(SshCommand.COPY, copyCommand);

        connector.transferFileToHost(renamedFile, webAppsDirectory);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Deployed " + artifactFile.getName());
        }

        return connector.getOutputBuffer();
    }

}
