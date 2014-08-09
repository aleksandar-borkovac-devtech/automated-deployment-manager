package nl.tranquilizedquality.adm.security.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.RoleSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.security.business.exception.RoleManagerException;

import org.springframework.validation.Errors;

/**
 * Manager that manages {@link Role} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public interface RoleManager {

	/**
	 * Retrieves a {@link Role} by the specified id.
	 * 
	 * @param id
	 *            The unique identifier of the {@link Role} that will be
	 *            retrieved.
	 * @return Returns a {@link Role} or null if none could be found.
	 */
	Role findRoleById(Long id);

	/**
	 * Searches for {@link Role} objects with the specified search criteria.
	 * 
	 * @param sc
	 *            The search criteria.
	 * @return Returns a {@link List} containing {@link Role} objects or an
	 *         empty one if none could be found.
	 */
	List<Role> findRoles(RoleSearchCommand sc);

	/**
	 * Counts the number of {@link Role} objects that would be returned when
	 * searching on the specified search criteria.
	 * 
	 * @param sc
	 *            The search criteria.
	 * @return Returns a value of 0 or higher.
	 */
	int findNumberOfRoles(RoleSearchCommand sc);

	/**
	 * Saves the specified {@link Role} object.
	 * 
	 * @param role
	 *            The {@link Role} that will be saved.
	 * @param errors
	 *            The {@link Errors} object that will be used when something
	 *            goes wrong during storage.
	 * @return Returns the saved {@link Role} object.
	 * @throws RoleManagerException
	 *             Is thrown when something goes wrong during role storage.
	 */
	Role storeRole(Role role, Errors errors) throws RoleManagerException;

	/**
	 * Searches for {@link Role} objects that can be granted by the logged in
	 * user for the specified {@link Scope} in the search command.
	 * 
	 * @param sc
	 *            The search criteria.
	 * @return Returns a list of {@link Role} objects or an empty one if there
	 *         are no roles for the scope.
	 */
	List<Role> findGrantableRolesByLoggedInUserAndScope(RoleSearchCommand sc);

	/**
	 * Deletes a {@link Role} if there are no users that have that role.
	 * 
	 * @param role
	 *            The {@link Role} that will be removed. The {@link Errors}
	 *            object that will be used when something goes wrong during
	 *            deletion.
	 * @throws RoleManagerException
	 *             Is thrown when something goes wrong during deletion.
	 */
	void deleteRole(Role role, Errors errors) throws RoleManagerException;

}
