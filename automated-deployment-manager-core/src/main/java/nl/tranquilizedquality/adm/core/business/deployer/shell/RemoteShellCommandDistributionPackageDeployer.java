package nl.tranquilizedquality.adm.core.business.deployer.shell;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.deployer.Deployer;
import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.ArtifactType;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.util.file.FileExtractorUtil;
import nl.tranquilizedquality.adm.core.business.deployer.exception.DeployException;
import nl.tranquilizedquality.adm.core.util.AdmValidate;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Shell script deployer that executes shell commands on the remote machine
 * based on a *.rsh script which contains all the shell script commands that
 * need to be executed on the remote service. The distribution package should
 * have a script called like deploy_${environment.name}.rsh in it.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 20 sep. 2012
 */
public class RemoteShellCommandDistributionPackageDeployer implements Deployer<MavenArtifact> {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(ShellScriptDistributionPackageDeployer.class);

    /** The work directory of ADM itself. */
    private String workDirectory;

    @Override
    public String deploy(final MavenArtifact artifact, final Destination destination, final String backupLocation,
            final ProtocolConnector connector) {
        final MavenModule parentModule = artifact.getParentModule();
        final ArtifactType type = parentModule.getType();
        final String fileName = artifact.getFile();
        final File artifactFile = new File(fileName);

        AdmValidate.isTrue(type == ArtifactType.TAR_GZIP, "Unsupported artifact type! " + type);

        /*
         * Setup temporary destination path.
         */
        final Date now = new Date();
        final String destinationPath = workDirectory + File.separator + now.getTime() + File.separatorChar;

        /*
         * Extract TAR.GZ file in work directory.
         */
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Extracting TAR.GZ to " + destinationPath + artifactFile.getName());
            }

            final File destinationDirectory = new File(destinationPath);
            FileExtractorUtil.extractTarGz(artifactFile, destinationDirectory);
        } catch (final Exception e) {
            final String msg = "Failed to extract distribution package! -> " + artifactFile.getAbsolutePath() + " to "
                    + destinationPath;

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg, e);
            }

            throw new DeployException(msg, e);
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Extracted " + destinationPath + artifactFile.getName());
        }

        /*
         * Retrieve the environment name.
         */
        final Environment environment = destination.getEnvironment();

        /*
         * Navigate to deploy script directory.
         */
        final String environmentName = environment.getName();
        final String deployScriptDirectory =
                destinationPath + parentModule.getArtifactId() + "-" + artifact.getVersion() + File.separatorChar;
        final String deployScript = deployScriptDirectory + "/deploy_" + environmentName.toLowerCase() + ".rsh";

        try {
            final File configurationFile = new File(deployScript);
            final List<String> readLines = IOUtils.readLines(new FileInputStream(configurationFile));

            for (final String command : readLines) {
                connector.performCustomCommand(command);
            }
        } catch (final Exception e) {
            final String msg = "Failed to execute remote shell script! -> " + deployScript;

            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg, e);
            }

            throw new DeployException(msg, e);
        } finally {
            connector.disconnectFromHost();
        }

        final String logs = connector.getSessionLogs();

        if (StringUtils.contains(logs, "ERROR") || StringUtils.contains(logs, "error") || StringUtils.contains(logs, "Exception")) {
            final DeployException deployException = new DeployException("Errors occured during execution of the deployment!");
            deployException.setLogs(logs);
            throw deployException;
        }

        return logs;
    }

    @Required
    public void setWorkDirectory(final String workDirectory) {
        this.workDirectory = workDirectory;
    }

}
