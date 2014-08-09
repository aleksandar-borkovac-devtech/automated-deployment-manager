package nl.tranquilizedquality.adm.security.persistence.db.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link UserRole} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 * @param <T>
 */
public interface UserRoleDao<T extends UserRole> extends BaseDao<T, Long> {

	/**
	 * Retrieves all active and inactive roles from a {@link User}.
	 * 
	 * @param user
	 *            The {@link User} where the roles will be retrieve for.
	 * @return Returns a {@link List} containing {@link UserRole} objects or an
	 *         empty one if none can be found.
	 */
	List<UserRole> findByUser(User user);
}
