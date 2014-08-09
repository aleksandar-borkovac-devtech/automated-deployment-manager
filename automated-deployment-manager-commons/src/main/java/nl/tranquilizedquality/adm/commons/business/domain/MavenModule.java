package nl.tranquilizedquality.adm.commons.business.domain;

import java.util.List;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Representation of a Maven module that can be deployed and installed.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public interface MavenModule extends UpdatableDomainObject<Long> {

    /**
     * Retrieves the name of the module.
     * 
     * @return Returns a {@link String} representation of the module name.
     */
    String getName();

    /**
     * Retrieve the type of artifact this module is.
     * 
     * @return Returns the {@link ArtifactType}.
     */
    ArtifactType getType();

    /**
     * Retrieves the group this artifact belongs to.
     * 
     * @return Returns a {@link String} representation of the group.
     */
    String getGroup();

    /**
     * Retrieves the artifact id.
     * 
     * @return Returns a {@link String} representation of the artifact id.
     */
    String getArtifactId();

    /**
     * Retrieves the destination where to deploy the artifact.
     * 
     * @return Returns a {@link Destination}.
     */
    List<Destination> getDestinations();

    /**
     * Sets the specified destinations.
     * 
     * @param destinations
     *        The destinations to set.
     */
    void setDestinations(List<Destination> destinations);

    /**
     * Determines if the target system should be shutdown before deployment of
     * this module.
     * 
     * @return Returns true if the target system should be shutdown before
     *         deployment otherwise it will return false.
     */
    Boolean getTargetSystemShutdown();

    /**
     * Determines if the target system should be started up after deployment of
     * this module.
     * 
     * @return Returns true if the target system should be started up otherwise
     *         it will return false.
     */
    Boolean getTargetSystemStartup();

    /**
     * Retrieves the identifier for distribution pacakges since a single Maven
     * distribution module can have multiple TARGZ distribution files. The
     * distribution files are uniquely identified by a suffix in the name of the
     * artifact.
     * 
     * @return Returns a {@link String} or null if no identifier is set.
     */
    String getIdentifier();

    /**
     * Retrieves the user group that this maven module belongs to.
     * 
     * @return Returns the {@link UserGroup}.
     */
    UserGroup getUserGroup();

    /**
     * Retrieves the modules where this module depends on to go ahead with a deployment.
     * 
     * @return Returns a {@link List} containing the dependencies.
     */
    List<MavenModule> getDeploymentDependencies();

    /**
     * Sets the dependencies where this module depends on to go ahead with a deployment.
     * 
     * @param dependencies
     *        The dependencies that will be set.
     */
    void setDeploymentDependencies(List<MavenModule> dependencies);

}
