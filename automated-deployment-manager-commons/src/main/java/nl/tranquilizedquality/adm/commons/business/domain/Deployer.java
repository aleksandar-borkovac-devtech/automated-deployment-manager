package nl.tranquilizedquality.adm.commons.business.domain;

import java.util.List;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Representation of a deployer that can deploy an artifact.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 dec. 2012
 */
public interface Deployer extends UpdatableDomainObject<Long> {

    /**
     * Retrieves the name of the deployer.
     * 
     * @return Returns the name of the deployer.
     */
    String getName();

    /**
     * Retrieves the parameters that are part of this deployer.
     * 
     * @return Returns a {@link List} of parameters or an empty one if no parameters are needed.
     */
    List<DeployerParameterTemplate> getParameters();

    /**
     * Sets the parameters for this deployer.
     * 
     * @param parameters
     *        The parameters that should be set.
     */
    void setParameters(List<DeployerParameterTemplate> parameters);

}
