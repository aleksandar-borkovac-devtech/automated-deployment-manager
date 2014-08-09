package nl.tranquilizedquality.adm.commons.business.deployer;

import nl.tranquilizedquality.adm.commons.business.deployer.connector.ProtocolConnector;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;

/**
 * Representation of a deployer that can deploy a {@link MavenArtifact} to a {@link Destination}
 * using a {@link ProtocolConnector}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 7 jul. 2011
 * @param <T>
 *        The implementation type.
 */
public interface Deployer<T extends MavenArtifact> {

    /**
     * Deploys the specified {@link MavenArtifact} to the {@link Destination} using the specified
     * {@link ProtocolConnector}.
     * 
     * @param artifact
     *        The artifact that will be deployed.
     * @param destination
     *        The destination server.
     * @param backupLocation
     *        The location where configuration files and artifacts are
     *        backed-up.
     * @param connector
     *        The connector to be used to do the deployment itself.
     * @return Returns the log of the deployment.
     */
    String deploy(T artifact, Destination destination, String backupLocation, ProtocolConnector connector);

}
