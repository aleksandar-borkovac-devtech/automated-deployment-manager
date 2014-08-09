package nl.tranquilizedquality.adm.commons.business.deployer;

import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;

/**
 * A Maven artifact deployer that deploys a {@link MavenArtifact} to a specific {@link Destination}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 22 okt. 2012
 */
public interface ArtifactDeployer {

    /**
     * Deploys an artifact to the specified destination.
     * 
     * @param artifact
     *        The artifact that will be deployed.
     * @param destination
     *        The destination where the artifact will be deployed to.
     * @return Returns a {@link String} containing the full log of connector
     *         commands made.
     */
    String deploy(MavenArtifact artifact, Destination destination);

}
