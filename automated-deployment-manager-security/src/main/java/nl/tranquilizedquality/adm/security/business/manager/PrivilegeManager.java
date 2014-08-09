package nl.tranquilizedquality.adm.security.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.PrivilegeSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.security.business.exception.PrivilegeManagerException;

import org.springframework.validation.Errors;

/**
 * Manager that manages {@link Privilege} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public interface PrivilegeManager {

	/**
	 * Searches for {@link Privilege} objects based on the specified search
	 * criteria.
	 * 
	 * @param sc
	 *            The search criteria to search on.
	 * @return Returns a list containing {@link Privilege} objects or an empty
	 *         one if none could be found.
	 */
	List<Privilege> findPrivileges(PrivilegeSearchCommand sc);

	/**
	 * Returns the number of privileges that would be returned when searching on
	 * the specified search criteria.
	 * 
	 * @param sc
	 *            The search criteria to search on.
	 * @return Returns a integer value of 0 or higher.
	 */
	int findNumberOfPrivileges(PrivilegeSearchCommand sc);

	/**
	 * Retrieves a {@link Privilege} with the specified id.
	 * 
	 * @param id
	 *            The unique identifier of the {@link Privilege}.
	 * @return Returns a {@link Privilege}.
	 */
	Privilege findPrivilegeById(Long id);

	/**
	 * Validates and stores the specified {@link Privilege}
	 * 
	 * @param privilege
	 *            The {@link Privilege} that will be stored.
	 * @param errors
	 *            The {@link Errors} object that will be populated with error
	 *            information when something goes wrong.
	 * @return Returns the stored {@link Privilege}.
	 * @throws PrivilegeManagerException
	 *             Is thrown when something goes wrong during storage.
	 */
	Privilege storePrivilege(Privilege privilege, Errors errors) throws PrivilegeManagerException;

}
