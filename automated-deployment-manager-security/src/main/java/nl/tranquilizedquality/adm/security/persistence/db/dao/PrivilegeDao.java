package nl.tranquilizedquality.adm.security.persistence.db.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.PrivilegeSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO used to retrieve {@link Privilege} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public interface PrivilegeDao<T extends Privilege> extends BaseDao<T, Long> {

    /**
     * Searches for {@link Privilege} objects that belong the the specified
     * {@link User} and the scope name.
     * 
     * @param user
     *            The {@link User} where the privileges need to be retrieved
     *            for.
     * @param scopeName
     *            The scope name of the privileges.
     * @return Returns a list of {@link Privilege} objects or an empty one if
     *         none could be found.
     */
    List<Privilege> findByUserAndScope(User user, Scope scope);

    /**
     * Searches for privileges who match the search criteria
     * 
     * @param searchCommand
     *            criteria
     * @return List of privileges matching the criteria
     */
    List<Privilege> findPrivileges(PrivilegeSearchCommand searchCommand);

    /**
     * Finds the number of privileges based on the specified search criteria.
     * 
     * @param searchCommand
     *            The search criteria to search on.
     * @return Returns a value of 0 or higher.
     */
    int findNumberOfPrivileges(PrivilegeSearchCommand searchCommand);

}
