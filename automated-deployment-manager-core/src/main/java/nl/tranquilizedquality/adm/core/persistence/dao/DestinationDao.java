package nl.tranquilizedquality.adm.core.persistence.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.DestinationSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link Destination} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 * @param <T>
 *            The implementation type.
 */
public interface DestinationDao<T extends Destination> extends BaseDao<T, Long> {

	/**
	 * Searches for destinations based on the passed in search criteria.
	 * 
	 * @param sc
	 *            The search criteria.
	 * @return Returns a {@link List} containing {@link Destination} objects or
	 *         an empty one if none could be found.
	 */
	List<Destination> findBySearchCommand(DestinationSearchCommand sc);

	/**
	 * Searches for destinations based on the passed in search criteria.
	 * 
	 * @param sc
	 *            The search criteria.
	 * @return Returns an {@link Integer} value of the number of destinations.
	 */
	int findNumberOfDestinations(DestinationSearchCommand sc);

}
