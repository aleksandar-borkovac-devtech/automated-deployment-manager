package nl.tranquilizedquality.adm.commons.business.domain;

import nl.tranquilizedquality.adm.commons.domain.UpdatableDomainObject;

/**
 * Representation of parameter used for a deployer.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 7 jul. 2011
 */
public interface DeployerParameter extends UpdatableDomainObject<Long> {

    /**
     * Retrieves the value of the parameter.
     * 
     * @return Returns a {@link String} representation of the value of the parameter.
     */
    String getValue();

    /**
     * Retrieves the type of path this is.
     * 
     * @return Returns a {@link DeployerParameterType}.
     */
    DeployerParameterType getType();

    /**
     * Retrieves the rank of the parameter to determine at what position a parameter will be passed.
     * 
     * @return Return an {@link Integer} value of 0 or greater. If 0 is used the order doesn't
     *         matter.
     */
    Integer getRank();

}
