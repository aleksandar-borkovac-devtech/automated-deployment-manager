package nl.tranquilizedquality.adm.security.persistence.db.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.ScopeSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link Scope} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 * @param <T>
 *            The implementation type.
 */
public interface ScopeDao<T extends Scope> extends BaseDao<T, Long> {

    /**
     * Retrieves a {@link Scope} by its name.
     * 
     * @param name
     *            The name to search for.
     * @return Returns a {@link Scope} or null if none could be found.
     */
    Scope findByName(String name);

    /**
     * Searches for {@link Scope} objects based on the
     * {@link ScopeSearchCommand}.
     * 
     * @param sc
     *            The search criteria.
     * @return Returns a {@link List} containing {@link Scope} objects.
     */
    List<Scope> findScopesBySearchCommand(ScopeSearchCommand sc);

    /**
     * Returns the number of {@link Scope} objects that would have been returned
     * when searching on the specified search criteria.
     * 
     * @param sc
     *            The search criteria.
     * @return Returns the number of scopes.
     */
    int findNumberOfScopesBySearchCommand(ScopeSearchCommand sc);

}
