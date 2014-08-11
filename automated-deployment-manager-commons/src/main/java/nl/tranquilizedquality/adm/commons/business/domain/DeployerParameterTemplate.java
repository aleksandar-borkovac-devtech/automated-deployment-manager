package nl.tranquilizedquality.adm.commons.business.domain;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Representation of a template configuration for a deployer. This basically
 * determines what parameter types are allowed/needed for a deployer.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 jan. 2013
 */
public interface DeployerParameterTemplate extends UpdatableDomainObject<Long> {

    /**
     * Retrieves the type of parameter.
     * 
     * @return Returns a {@link DeployerParameterType}.
     */
    DeployerParameterType getType();

    /**
     * Determines if the parameter is mandatory for the deployer to work.
     * 
     * @return Returns true if it's mandatory otherwise it will return false.
     */
    boolean isMandatory();

}
