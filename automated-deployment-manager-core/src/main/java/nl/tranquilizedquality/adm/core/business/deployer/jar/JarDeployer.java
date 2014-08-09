/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 7 jul. 2011 File: JarDeployer.java
 * Package: nl.tranquilizedquality.adm.core.business.deployer.jar
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
package nl.tranquilizedquality.adm.core.business.deployer.jar;

import java.io.File;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.deployer.Deployer;
import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterType;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.core.util.AdmValidate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Deployer that can deploy JAR files.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 7 jul. 2011
 */
public class JarDeployer implements Deployer<MavenArtifact> {

    /** logger for this class */
    private static final Log LOGGER = LogFactory.getLog(JarDeployer.class);

    @Override
    public String deploy(final MavenArtifact artifact, final Destination destination, final String artifactBackupDirectory,
            final ProtocolConnector connector) {
        /*
         * Retrieve the configured locations.
         */
        final List<DeployerParameter> locations = destination.getDeployerParameters();

        String jarArtifactLocation = null;

        for (final DeployerParameter location : locations) {
            final DeployerParameterType type = location.getType();
            if (DeployerParameterType.JAR_LOCATION == type) {
                jarArtifactLocation = location.getValue();
            }
        }

        AdmValidate.notNull(jarArtifactLocation, "No artifact location specified!");

        /*
         * Copy JAR file.
         */
        final String fileName = artifact.getFile();
        final File artifactFile = new File(fileName);
        connector.transferFileToHost(artifactFile, jarArtifactLocation);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Deployed " + artifactFile.getName() + " to " + jarArtifactLocation);
        }

        return connector.getOutputBuffer();
    }

}
