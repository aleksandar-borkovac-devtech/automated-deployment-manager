package nl.tranquilizedquality.adm.core.persistence.dao;

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link DeployerParameter} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 * @param <T>
 *            Implementation type.
 */
public interface DeployerParameterDao<T extends DeployerParameter> extends BaseDao<T, Long> {

}
