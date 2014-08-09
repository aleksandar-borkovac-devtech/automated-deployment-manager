package nl.tranquilizedquality.adm.core.persistence.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.EnvironmentSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link Environment} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 1 sep. 2011
 * @param <T>
 *            The implementation type this DAO will manage.
 */
public interface EnvironmentDao<T extends Environment> extends BaseDao<T, Long> {

	/**
	 * Searches for environments based on the passed in search criteria.
	 * 
	 * @param sc
	 *            The search criteria that will be used to search for
	 *            environments.
	 * @return Returns a {@link List} containing {@link Environment}
	 */
	List<Environment> findEnvironmentsBySearchCommand(EnvironmentSearchCommand sc);

	/**
	 * Counts the number of environments that would be returned based on the
	 * passed in search criteria.
	 * 
	 * @param sc
	 *            The search criteria that will be used to search for
	 *            environments.
	 * @return Returns an integer value of 0 or greater.
	 */
	int findNumberOfEnvironmentsBySearchCommand(EnvironmentSearchCommand sc);

}
