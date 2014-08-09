package nl.tranquilizedquality.adm.core.persistence.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.ReleaseSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link Release} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 * @param <T>
 *            Implementation type.
 */
public interface ReleaseDao<T extends Release> extends BaseDao<T, Long> {

	/**
	 * Searches for releases based on the specified search criteria.
	 * 
	 * @param sc
	 *            The search criteria.
	 * @return Returns a {@link List} containing {@link Release} objects or an
	 *         empty one if none could be found.
	 */
	List<Release> findBySearchCommand(ReleaseSearchCommand sc);

	/**
	 * Counts the number of releases based on the specified search criteria.
	 * 
	 * @param sc
	 *            The search criteria.
	 * @return Returns a value of 0 or greater.
	 */
	int findNumberOfReleases(ReleaseSearchCommand sc);

}
