package nl.tranquilizedquality.adm.core.persistence.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.DestinationHostSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link DestinationHost} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 okt. 2011
 * @param <T>
 */
public interface DestinationHostDao<T extends DestinationHost> extends BaseDao<T, Long> {

    /**
     * Searches for {@link DestinationHost} objects based on the specified
     * search criteria.
     * 
     * @param sc
     *            The search criteria.
     * @return Returns a {@link List} of {@link DestinationHost} objects or an
     *         empty one of none could be found.
     */
    List<DestinationHost> findBySearchCommand(DestinationHostSearchCommand sc);

    /**
     * Counts the number of {@link DestinationHost} objects based on the
     * specified search criteria.
     * 
     * @param sc
     *            The search criteria.
     * @return Returns an integer value of 0 or greater.
     */
    int findNumberOfDestinationHosts(DestinationHostSearchCommand sc);

}
