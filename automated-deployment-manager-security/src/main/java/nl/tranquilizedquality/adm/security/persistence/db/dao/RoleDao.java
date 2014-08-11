package nl.tranquilizedquality.adm.security.persistence.db.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.RoleSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link Role} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 * @param <T>
 *            The entity object.
 */
public interface RoleDao<T extends Role> extends BaseDao<T, Long> {

    /**
     * Searches for users who match the search criteria
     * 
     * @param searchCommand
     *            criteria
     * @return List of roles matching the criteria
     */
    List<Role> findRoles(RoleSearchCommand searchCommand);

    /**
     * Finds the number of roles based on the specified search criteria.
     * 
     * @param searchCommand
     *            The search criteria to search on.
     * @return Returns a value of 0 or higher.
     */
    int findNumberOfRoles(RoleSearchCommand searchCommand);

}
