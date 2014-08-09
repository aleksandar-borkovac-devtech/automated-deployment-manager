package nl.tranquilizedquality.adm.commons.business.domain;

import java.util.List;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Representation of a destination where an artifact can be deployed to.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 */
public interface Destination extends UpdatableDomainObject<Long> {

    /**
     * Retrieves the name of the destination.
     * 
     * @return Returns the name of the destination.
     */
    String getName();

    /**
     * Retrieves the unique identifier for deployer that should deploy an
     * artifact to this destination.
     * 
     * @return Returns a {@link String} representation of the deployer ID.
     */
    Deployer getDeployer();

    /**
     * The environment this destination represents.
     * 
     * @return Returns a {@link Environment} object.
     */
    Environment getEnvironment();

    /**
     * Retrieves the parameters used for a the configured deployer.
     * 
     * @return Returns a {@link List} containing configured parameters or an empty
     *         one if there are none.
     */
    List<DeployerParameter> getDeployerParameters();

    /**
     * Sets the specified parameters.
     * 
     * @param locations
     *        The parameters that will be set.
     */
    void setDeployerParameters(List<DeployerParameter> locations);

    /**
     * Retrieves the server where to deploy to.
     * 
     * @return Returns a {@link DestinationHost}.
     */
    DestinationHost getDestinationHost();

    /**
     * Retrieves the user group that this maven module belongs to.
     * 
     * @return Returns the {@link UserGroup}.
     */
    UserGroup getUserGroup();

    /**
     * Retrieves the prefix used in retrieving configuration files from a
     * distribution package.
     * 
     * @return Returns a {@link String} representing the prefix or null if there
     *         is no prefix.
     */
    String getPrefix();

}
