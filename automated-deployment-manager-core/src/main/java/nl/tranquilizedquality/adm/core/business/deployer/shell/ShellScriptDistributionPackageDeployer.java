/**
 * 
 */
package nl.tranquilizedquality.adm.core.business.deployer.shell;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

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
import nl.tranquilizedquality.adm.core.business.deployer.exception.DeployException;
import nl.tranquilizedquality.adm.core.util.AdmValidate;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Deployer that can execute a shell script within a distribution package. The
 * distribution package should have a script called like
 * deploy_${environment.name}.sh in it.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 8 feb. 2012
 */
public class ShellScriptDistributionPackageDeployer implements Deployer<MavenArtifact> {

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
        final String deployScriptDirectory =
                destinationPath + parentModule.getArtifactId() + "-" + artifact.getVersion() + File.separatorChar;

        /*
         * Construct parameters that need to be passed to the script.
         */
        final StringBuilder parameters = new StringBuilder();
        final List<DeployerParameter> deployerParameters = destination.getDeployerParameters();
        for (final DeployerParameter parameter : deployerParameters) {
            final DeployerParameterType parameterType = parameter.getType();

            if (DeployerParameterType.SCRIPT_PARAMETER.equals(parameterType)) {
                parameters.append(" ");
                parameters.append(parameter.getValue());
            }
        }

        /*
         * Execute script.
         */
        final String environmentName = environment.getName();
        final String shellParameters = parameters.toString();

        final StringBuilder command = new StringBuilder();
        command.append("cd ");
        command.append(deployScriptDirectory);
        command.append(";");
        command.append("./deploy_");
        command.append(environmentName.toLowerCase());
        command.append(".sh");
        if (StringUtils.isNotBlank(shellParameters)) {
            command.append(shellParameters);
        }
        command.append(";");

        final String shellCommand = command.toString();
        final String logs = executeCommand(shellCommand);

        if (StringUtils.contains(logs, "Exception")) {
            final DeployException deployException = new DeployException("Errors occured during execution of the deployment!");
            deployException.setLogs(logs);
            throw deployException;
        }

        return logs;
    }

    /**
     * Executes a shell command.
     * 
     * @param command
     *            The command that will be executed.
     * @return Returns the log.
     */
    private String executeCommand(final String command) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("About to execute command: " + command);
            }

            // create a process for the shell
            final ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            pb.redirectErrorStream(true);

            final Process shell = pb.start();
            final InputStream shellIn = shell.getInputStream();

            final int shellExitStatus = shell.waitFor();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(shellExitStatus);
            }

            final String logs = IOUtils.toString(shellIn);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(logs);
            }

            // close the stream
            shellIn.close();

            return logs;
        } catch (final Exception e) {
            final String msg = "Failed to execute script!";
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(msg, e);
            }

            throw new DeployException(msg, e);
        }
    }

    @Required
    public void setWorkDirectory(final String workDirectory) {
        this.workDirectory = workDirectory;
    }

}
