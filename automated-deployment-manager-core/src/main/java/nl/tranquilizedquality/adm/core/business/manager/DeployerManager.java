package nl.tranquilizedquality.adm.core.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Deployer;

/**
 * Manager that manages all available deployers within ADM.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 dec. 2012
 */
public interface DeployerManager {

    /**
     * Retrieves all available deployers.
     * 
     * @return Returns a {@link List} containing deployers.
     */
    List<Deployer> findAvailableDeployers();

}
