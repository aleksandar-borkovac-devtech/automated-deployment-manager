package nl.tranquilizedquality.adm.security.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.ScopeSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.security.business.exception.ScopeManagerException;

import org.springframework.validation.Errors;

/**
 * Manager that manages {@link Scope} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public interface ScopeManager {

    /**
     * Searches for {@link Scope} objects based on the specified search
     * criteria.
     * 
     * @param sc
     *            The search criteria.
     * @return Returns a {@link List} containing {@link Scope} objects or an
     *         empty one if none could be found.
     */
    List<Scope> findScopes(ScopeSearchCommand sc);

    /**
     * Retrieves a {@link Scope} by the specified id.
     * 
     * @param id
     *            The unique identifier of the {@link Scope} that will be
     *            retrieved.
     * @return Returns a {@link Scope} or null if none could be found.
     */
    Scope findScopeById(Long id);

    /**
     * Counts the total number of scopes that meet the search criteria.
     * 
     * @param sc
     *            The search criteria.
     * @return Returns the number of scopes.
     */
    int findNumberOfScopes(ScopeSearchCommand sc);

    /**
     * Saves the specified scope and reports errors back through the passed in
     * {@link Errors} object.
     * 
     * @param scope
     *            The {@link Scope} object that will be saved.
     * @param errors
     *            The {@link Errors} object that will be saved.
     * @return Returns the saved {@link Scope} object.
     * @throws ScopeManagerException
     *             Is thrown when something goes wrong during scope storage.
     */
    Scope storeScope(Scope scope, Errors errors) throws ScopeManagerException;

    /**
     * Retrieves the managed scopes from the specified {@link User}.
     * 
     * @param user
     *            The {@link User} where the {@link Scope} objects will be
     *            retrieved from which he/she manages.
     * @return Returns a list containing {@link Scope} objects or an empty one
     *         if he is not a scope manager at all.
     */
    List<Scope> findManagedScopesByUser(User user);

    /**
     * Retrieves the managed scopes from the specified {@link User} with only
     * the roles that the user is allowed to grant.
     * 
     * @param user
     *            The {@link User} where the {@link Scope} objects will be
     *            retrieved from which he/she manages.
     * @return Returns a list containing {@link Scope} objects or an empty one
     *         if he/she is not a scope manager at all.
     */
    List<Scope> findManagedScopesWithGrantableRolesByUser(User user);

    /**
     * Stores the specified scope with the associated privileges and roles
     * 
     * @param scope
     * @param errors
     * @return
     * @throws ScopeManagerException
     */
    Scope importScope(Scope scope, Errors errors) throws ScopeManagerException;
}
