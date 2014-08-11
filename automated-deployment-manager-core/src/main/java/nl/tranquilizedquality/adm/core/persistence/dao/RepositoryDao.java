package nl.tranquilizedquality.adm.core.persistence.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.RepositorySearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link Repository} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 jun. 2011
 * @param <T>
 *            The implementation type.
 */
public interface RepositoryDao<T extends Repository> extends BaseDao<T, Long> {

    /**
     * Searches for repositories based on the passed in search criteria.
     * 
     * @param sc
     *            The search criteria.
     * @return Returns a {@link List} of repositories or an empty one if none
     *         could be found.
     */
    List<Repository> findBySearchCommand(RepositorySearchCommand sc);

    /**
     * Counts the total number of records that would be returned based on the
     * passed in search criteria.
     * 
     * @param sc
     *            The search criteria.
     * @return Returns an integer value of 0 or greater.
     */
    int findNumberOfRepositories(RepositorySearchCommand sc);

}
