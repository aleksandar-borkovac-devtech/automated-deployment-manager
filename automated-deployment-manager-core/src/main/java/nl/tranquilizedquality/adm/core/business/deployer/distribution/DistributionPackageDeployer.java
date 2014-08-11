/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 7 jul. 2011 File: DistributionPackageDeployer.java
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

import nl.tranquilizedquality.adm.commons.business.deployer.Deployer;
import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.util.file.FileExtractorUtil;
import nl.tranquilizedquality.adm.core.business.deployer.connector.ssh.SshCommand;
import nl.tranquilizedquality.adm.core.business.deployer.exception.DeployException;
import nl.tranquilizedquality.adm.core.util.AdmValidate;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.springframework.beans.factory.annotation.Required;

/**
 * Deployer that can deploy a distribution package containing configuration
 * files.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 7 jul. 2011
 */
public class DistributionPackageDeployer implements Deployer<MavenArtifact> {

    /** logger for this class */
    private static final Log LOGGER = LogFactory.getLog(DistributionPackageDeployer.class);

    /** The work directory of ADM itself. */
    private String workDirectory;

    @Override
    public String deploy(final MavenArtifact artifact, final Destination destination, final String backupLocation,
            final ProtocolConnector connector) {
        final MavenModule parentModule = artifact.getParentModule();
        final ArtifactType type = parentModule.getType();
        final String fileName = artifact.getFile();
        final File artifactFile = new File(fileName);

        /*
         * Setup temporary destination path.
         */
        final Date now = new Date();
        final String destinationPath = workDirectory + File.separator + now.getTime() + File.separatorChar;

        /*
         * Extract TAR.GZ file in work directory.
         */
        try {
            final File destinationDirectory = new File(destinationPath);
            FileExtractorUtil.extractTarGz(artifactFile, destinationDirectory);
        } catch (final Exception e) {
            final String msg = "Failed to extract distribution package! -> " + artifactFile.getAbsolutePath() + " to "
                    + destinationPath;

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new DeployException(msg, e);
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Extracted " + destinationPath + artifactFile.getName());
        }

        /*
         * Get location values.
         */
        String appServerHome = null;

        final List<DeployerParameter> deployerParameters = destination.getDeployerParameters();
        for (final DeployerParameter location : deployerParameters) {
            final DeployerParameterType locationType = location.getType();
            switch (locationType) {
                case APP_SERVER_LOCATION:
                    appServerHome = location.getValue();
                    break;
            }
        }

        /*
         * Check if there is a TOMCAT_HOME specified.
         */
        AdmValidate.notEmpty(appServerHome, "No TOMCAT_HOME specified!");

        /*
         * Setup distribution directory name.
         */
        final String name = artifactFile.getName();
        final String extractedDirectoryName = StringUtils.substringBefore(name, type.getExtention());
        final String distributionDirectory = destinationPath + extractedDirectoryName + "/";

        /*
         * Copy configuration files. Existing configuration files will be
         * overwritten.
         */
        final StringBuilder tomcatConfigurationDirectory = new StringBuilder();
        tomcatConfigurationDirectory.append(distributionDirectory);

        /*
         * Only add prefix if not empty.
         */
        final Environment environment = destination.getEnvironment();
        final String environmentPrefix = destination.getPrefix();
        if (!StringUtils.isEmpty(environmentPrefix)) {
            tomcatConfigurationDirectory.append(environmentPrefix);
            tomcatConfigurationDirectory.append("-");
        }

        final String environmentName = environment.getName();
        tomcatConfigurationDirectory.append(StringUtils.lowerCase(environmentName));
        tomcatConfigurationDirectory.append("/");

        /*
         * Transfer conf directory from distribution to destination Tomcat
         * instance.
         */
        final String localConfDirPath = tomcatConfigurationDirectory.toString() + "conf/";
        final File confDir = new File(localConfDirPath);

        if (confDir.exists()) {
            connector.transferFileToHost(confDir, appServerHome);
        }

        /*
         * Transfer lib directory from distribution to destination Tomcat
         * instance.
         */
        final String localLibDirPath = tomcatConfigurationDirectory.toString() + "lib/";
        final File libDir = new File(localLibDirPath);

        if (libDir.exists()) {
            connector.transferFileToHost(libDir, appServerHome);
        }

        /*
         * Transfer shared directory from distribution to destination Tomcat
         * instance.
         */
        final String localSharedDirPath = tomcatConfigurationDirectory.toString() + "shared/";
        final File sharedDir = new File(localSharedDirPath);

        if (sharedDir.exists()) {
            connector.transferFileToHost(sharedDir, appServerHome);
        }

        /*
         * Transfer WAR from distribution to destination Tomcat instance.
         */
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Copied new configuration files from " + tomcatConfigurationDirectory.toString());
        }

        /*
         * Deploy packaged WAR file.
         */
        deployWarFileInArchive(artifactFile, artifact, destination, backupLocation, connector, destinationPath);

        return connector.getOutputBuffer();
    }

    /**
     * Deploys the WAR file within the archive if it exists.
     * 
     * @param artifactFile
     *            The actual archive.
     * @param artifact
     *            Artifact information.
     * @param destination
     *            The destination information.
     * @param artifactBackupDirectory
     *            The directory where to put the previous installed WAR file.
     * @param destinationPath
     *            The unique work directory for this distribution where files
     *            can be transfered from to the destination host.
     * @param connector
     *            Connector to execute remote commands.
     */
    private void deployWarFileInArchive(final File artifactFile, final MavenArtifact artifact, final Destination destination,
            final String artifactBackupDirectory, final ProtocolConnector connector, final String destinationPath) {

        /*
         * Retrieve the WAR name.
         */
        final String warName = extractWarName(artifactFile);

        /*
         * If there is no WAR found stop.
         */
        if (StringUtils.isEmpty(warName)) {
            if (LOGGER.isWarnEnabled()) {
                final String msg = "No WAR found in distribution package.";
                LOGGER.warn(msg);
            }

            return;
        }

        /*
         * Get location values.
         */
        String webAppsDirectory = null;
        String appServerHome = null;

        final List<DeployerParameter> deployerParameters = destination.getDeployerParameters();
        for (final DeployerParameter location : deployerParameters) {
            final DeployerParameterType type = location.getType();
            switch (type) {
                case WEB_APPS_LOCATION:
                    webAppsDirectory = location.getValue();
                    break;

                case APP_SERVER_LOCATION:
                    appServerHome = location.getValue();
                    break;
            }
        }

        /*
         * Check if the configuration is done properly.
         */
        if (StringUtils.isEmpty(artifactBackupDirectory) || StringUtils.isEmpty(webAppsDirectory)
                || StringUtils.isEmpty(appServerHome)) {
            final String msg = "Destination locations not configured!";

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
                LOGGER.error("Artifact Backup Directory: " + artifactBackupDirectory);
                LOGGER.error("Webapps Directory: " + webAppsDirectory);
            }

            throw new DeployException(msg);
        }

        /*
         * Backup WAR.
         */
        final String copyCommand = webAppsDirectory + warName + " " + artifactBackupDirectory;

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Deploying " + warName);
            LOGGER.info(copyCommand);
        }

        connector.performCommand(SshCommand.COPY, copyCommand);

        /*
         * Remove exploded WAR.
         */
        final String explodedDirectoryName = StringUtils.substringBefore(warName, ".");
        final String removeCommand = webAppsDirectory + explodedDirectoryName;
        connector.performCommand(SshCommand.RECURSIVE_REMOVE_FORCED, removeCommand);

        /*
         * Copy new WAR to webapps directory.
         */
        final MavenModule parentModule = artifact.getParentModule();
        final ArtifactType type = parentModule.getType();

        final String name = artifactFile.getName();
        final String extractedDirectoryName = StringUtils.substringBefore(name, type.getExtention());
        final String distributionDirectory = destinationPath + extractedDirectoryName + "/";
        final String warFilePath = distributionDirectory + warName;

        /*
         * Transfer WAR file to web apps.
         */
        final File warFile = new File(warFilePath);
        connector.transferFileToHost(warFile, webAppsDirectory);
    }

    /**
     * Extracts the WAR file name from the passed in archive file by going
     * through all entries looking for a WAR file.
     * 
     * @param artifactFile
     *            The archive.
     * @return Returns the WAR name if there is one otherwise it will return an
     *         empty string.
     */
    private String extractWarName(final File artifactFile) {
        final Date now = new Date();
        final String tarFile = workDirectory + now.getTime() + "_archive.tar";
        FileOutputStream out = null;
        GZIPInputStream gzIn = null;

        try {
            final FileInputStream fin = new FileInputStream(artifactFile);
            final BufferedInputStream in = new BufferedInputStream(fin);
            out = new FileOutputStream(tarFile);
            gzIn = new GZIPInputStream(in);

            final int buffersize = 1024;
            final byte[] buffer = new byte[buffersize];
            int n = 0;

            while (-1 != (n = gzIn.read(buffer))) {
                out.write(buffer, 0, n);
            }

            out.close();
            gzIn.close();

            /*
             * Go through the TAR file entries to see if there is a WAR file
             * packaged.
             */
            final FileInputStream is = new FileInputStream(tarFile);
            final TarInputStream tarInput = new TarInputStream(is);

            String warName = "";
            boolean loop = true;
            while (loop) {
                final TarEntry entry = tarInput.getNextEntry();

                /*
                 * Check if there is still an entry if not stop looping through
                 * the archive.
                 */
                if (entry == null) {
                    loop = false;
                } else {
                    final String name = entry.getName();

                    if (StringUtils.contains(name, ".war")) {
                        /*
                         * Check if the name contains the a directory name.
                         */
                        if (StringUtils.contains(name, "/")) {

                            /*
                             * Extract the WAR name.
                             */
                            warName = StringUtils.substringAfterLast(name, "/");
                        } else {

                            /*
                             * No directory name so just use the name.
                             */
                            warName = name;
                        }

                        /*
                         * Stop looping.
                         */
                        loop = false;
                    }
                }

            }

            return warName;
        } catch (final FileNotFoundException e) {
            final String msg = "Failed to extract file name since file couldn't be found!";

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new DeployException(msg, e);
        } catch (final IOException e) {
            final String msg = "Failed to extract file name since file couldn't be accessed!";

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg);
            }

            throw new DeployException(msg, e);
        } finally {
            /*
             * Delete file.
             */
            FileUtils.deleteQuietly(new File(tarFile));
        }

    }

    /**
     * @param workDirectory
     *            the workDirectory to set
     */
    @Required
    public void setWorkDirectory(final String workDirectory) {
        this.workDirectory = workDirectory;
    }

}
