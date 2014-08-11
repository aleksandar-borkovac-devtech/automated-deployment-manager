package nl.tranquilizedquality.adm.security.persistence.db.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;
import nl.tranquilizedquality.adm.security.business.command.UserSearchCommand;

/**
 * DAO used to retrieve a {@link User}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 28 aug. 2011
 */
public interface UserDao<T extends User> extends BaseDao<T, Long> {

    /**
     * Searches for the active user with the specified username.
     * 
     * @param userName
     *            The user name of the {@link User} that needs to be retrieved.
     * @return Returns the {@link User}.
     */
    User findActiveUserByUserName(String userName);

    /**
     * Updates the last login date of the {@link User}.
     * 
     * @param user
     *            The user where the last login date will be updated.
     */
    void updateLastLoginDate(User user);

    /**
     * Searches for users based on the specified search criteria.
     * 
     * @param sc
     *            The search criteria to search on.
     * @return Returns a {@link List} containing users or an empty one if none
     *         could be found.
     */
    List<User> findUsersBySearchCommand(UserSearchCommand sc);

    /**
     * Counts the number of users based on the specified search criteria.
     * 
     * @param sc
     *            The search criteria that will be used to count the users.
     * @return Returns an integer value of 0 or greater.
     */
    int findNumberOfUsers(UserSearchCommand sc);

    /**
     * Searches for a user based on the specified email address.
     * 
     * @param email
     *            The email address of the user.
     * @return Returns the user with the specified email address.
     */
    User findByEmail(String email);

}
