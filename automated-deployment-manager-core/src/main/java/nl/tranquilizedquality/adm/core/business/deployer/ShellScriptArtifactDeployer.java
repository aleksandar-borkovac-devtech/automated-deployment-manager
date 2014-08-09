/**
 *
 */
package nl.tranquilizedquality.adm.core.business.deployer;

import java.util.Map;

import nl.tranquilizedquality.adm.commons.business.deployer.ArtifactDeployer;
import nl.tranquilizedquality.adm.commons.business.deployer.Deployer;
import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.Protocol;
import nl.tranquilizedquality.adm.core.business.deployer.exception.DeployException;
import nl.tranquilizedquality.adm.core.business.deployer.exception.UnsupportedProtocolException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Deployer that can deploy artifacts using a shell script.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 20 feb. 2013
 */
public class ShellScriptArtifactDeployer implements ArtifactDeployer {

    /** logger for this class */
    private static final Log LOGGER = LogFactory.getLog(ShellScriptArtifactDeployer.class);

    /** The deployer that can deploy an artifact using shell scripts. */
    private Deployer<MavenArtifact> shellScriptDeployer;

    /** The connectors this deployer supports. */
    private Map<Protocol, ProtocolConnector> connectors;

    @Override
    public String deploy(final MavenArtifact artifact, final Destination destination) {
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

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Connector selected.");
            LOGGER.info("Connecting to host: " + hostName + " on port " + port + " with user: " + username);
        }

        /*
         * Check if there was a connector found.
         */
        if (connector == null) {
            final String msg = "No connector found for protocol: " + protocol;

            final DeployException deployException = new DeployException(msg);

            throw deployException;
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

        String logs = null;

        try {
            /*
             * Deploy the artifact.
             */
            switch (protocol) {
                case SSH:
                case SSHPS:
                    logs = shellScriptDeployer.deploy(artifact, destination, null, connector);
                    break;

                default:
                    final String msg = "Protocol not supported by deployer! " + protocol;
                    throw new UnsupportedProtocolException(msg);
            }
        } catch (final Exception e) {
            /*
             * Disconnects from the host.
             */
            connector.disconnectFromHost();

            final String msg = "Failed to deploy the artifact!";

            if (e instanceof DeployException) {
                logs = ((DeployException) e).getLogs();
            } else {
                logs = e.getMessage();
            }

            final DeployException deployException = new DeployException(msg, e);
            deployException.setLogs(logs);

            throw deployException;
        } finally {
            /*
             * Disconnects from the host.
             */
            connector.disconnectFromHost();
        }

        return logs;
    }

    @Required
    public void setShellScriptDeployer(final Deployer<MavenArtifact> shellScriptDeployer) {
        this.shellScriptDeployer = shellScriptDeployer;
    }

    @Required
    public void setConnectors(final Map<Protocol, ProtocolConnector> connectors) {
        this.connectors = connectors;
    }

}
