package nl.tranquilizedquality.adm.core.persistence.dao;

import nl.tranquilizedquality.adm.commons.business.domain.Deployer;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link Deployer} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 12 dec. 2012
 * @param <T>
 *        The implementation type.
 */
public interface DeployerDao<T extends Deployer> extends BaseDao<T, Long> {

}
