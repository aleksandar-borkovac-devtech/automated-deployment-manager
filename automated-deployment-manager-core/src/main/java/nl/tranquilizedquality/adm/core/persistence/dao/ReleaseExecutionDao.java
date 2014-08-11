package nl.tranquilizedquality.adm.core.persistence.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.ReleaseExecutionSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link ReleaseExecution} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 * @param <T>
 *            The implementation type.
 */
public interface ReleaseExecutionDao<T extends ReleaseExecution> extends BaseDao<T, Long> {

    /**
     * Searches for release history for a specific release.
     * 
     * @param sc
     *            The search criteria used to retrieve the release executions.
     * @return Returns a list of {@link ReleaseExecution} objects or an empty
     *         one if none could be found.
     */
    List<ReleaseExecution> findBySearchCommand(ReleaseExecutionSearchCommand sc);

    /**
     * Counts the number of release executions of the specified release.
     * 
     * @param sc
     *            The search criteria used to retrieve the release executions.
     * @return Returns an integer value of 0 or greater.
     */
    int findNumberOfReleaseExecutionsBySearchCommand(ReleaseExecutionSearchCommand sc);

}
