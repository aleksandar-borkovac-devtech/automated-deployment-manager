package nl.tranquilizedquality.adm.commons.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.RepositorySearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Repository;

import org.springframework.validation.Errors;

/**
 * Manager that manages {@link Repository} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 aug. 2011
 */
public interface RepositoryManager {

    /**
     * Stores the passed in {@link Repository}.
     * 
     * @param repository
     *            The repository that will be stored.
     * @param errors
     *            {@link Errors} object for reporting back errors.
     * @return Returns the saved {@link Repository}.
     */
    Repository storeRepository(Repository repository, Errors errors);

    /**
     * Counts the number of repositories based on the passed in search criteria.
     * 
     * @param sc
     *            The search criteria.
     * @return Returns an integer value of 0 or greater.
     */
    int findNumberOfRepositories(RepositorySearchCommand sc);

    /**
     * Searches for repositories based on the passed in search criteria.
     * 
     * @param sc
     *            The search criteria.
     * @return Returns a {@link List} containing {@link Repository} objects or
     *         an empty one if none could be found.
     */
    List<Repository> findRepositories(RepositorySearchCommand sc);

    /**
     * Finds the {@link Repository} with the specified ID.
     * 
     * @param id
     *            The unique identifier to use.
     * @return Returns a {@link Repository} or null if none could be found.
     */
    Repository findRepositoryById(Long id);

}
