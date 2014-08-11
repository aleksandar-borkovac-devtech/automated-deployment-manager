package nl.tranquilizedquality.adm.security.business.manager;

import java.io.File;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.security.business.command.UserSearchCommand;
import nl.tranquilizedquality.adm.security.business.exception.UserManagerException;

import org.springframework.validation.Errors;

/**
 * Manager that manages {@link User} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public interface UserManager {

    /**
     * Searches for {@link User} objects based on the specified search criteria.
     * 
     * @param searchCommand
     *            The search criteria.
     * @return Returns a {@link List} containing {@link User} objects or an
     *         empty one if none could be found.
     */
    List<User> findUsers(UserSearchCommand searchCommand);

    /**
     * Retrieves a user by it's unique identifier.
     * 
     * @param id
     *            The unique identifier of the user to be retrieved.
     * @return Returns a {@link User} or null if none could be found.
     */
    User findUserById(Long id);

    /**
     * Returns the number of users based on the specified search criteria.
     * 
     * @param searchCommand
     *            The search criteria to search on.
     * @return Returns a number greater than 0 or 0 if none could be found.
     */
    int findNumberOfUsers(UserSearchCommand searchCommand);

    /**
     * Retrieves a user with the specified user name.
     * 
     * @param username
     *            The user name to search for.
     * @return Returns a {@link User} or null if none could be found.
     */
    User findUserByUsername(String username);

    /**
     * Validates and stores the passed in {@link User}.
     * 
     * @param user
     *            The {@link User} that will be stored. May not be null.
     * @param errors
     *            The {@link Errors} object that will be populated with error
     *            information when something goes wrong.
     * @return Returns the stored {@link User}.
     * @throws UserManagerException
     *             Is thrown when something goes wrong during storage.
     */
    User storeUser(User user, Errors errors) throws UserManagerException;

    /**
     * Removes a {@link UserRole} from a {@link User}.
     * 
     * @param userRole
     *            The {@link UserRole} that will be removed.
     * @param errors
     *            The {@link Errors} object that will be populated with error
     *            information when something goes wrong.
     * @throws UserManagerException
     *             Is thrown when something goes wrong during deletion.
     */
    void revokeUserRole(UserRole userRole, Errors errors) throws UserManagerException;

    /**
     * Validators and stores the passed in {@link UserRole}.
     * 
     * @param userRole
     *            The {@link UserRole} that will be saved.
     * @param errors
     *            The {@link Errors} object that will be populated with error
     *            information when something goes wrong.
     * @return Returns the stored {@link UserRole}.
     * @throws UserManagerException
     *             Is thrown when something goes wrong during storage.
     */
    UserRole storeUserRole(UserRole userRole, Errors errors) throws UserManagerException;

    /**
     * Assigns the default roles to the specified user.
     * 
     * @param user
     *            The {@link User} where the default roles will be assigned to.
     * @param errors
     *            The {@link Errors} object that will be populated with error
     *            information when something goes wrong.
     * @throws UserManagerException
     *             Is thrown when something goes wrong during storage.
     */
    void assignDefaultRoles(User user, Errors errors) throws UserManagerException;

    /**
     * Assigns the roles specified to the users.
     * 
     * @param roles
     *            A {@link List} containing {@link Role} objects that will be
     *            assigned.
     * @param users
     *            A {@link List} containing {@link User} objects who will be
     *            assigned roles.
     * @param loggedInUser
     *            The {@link User} that is granting the roles.
     */
    void assignRoles(List<? extends Role> roles, List<? extends User> users, User loggedInUser);

    /**
     * Finds all {@link UserRole} objects from the specified {@link User}.
     * 
     * @param user
     *            The {@link User} where the roles will be searched for.
     * @return Returns a {@link List} containing {@link UserRole} objects or an
     *         empty one if the user has no roles at all.
     */
    List<UserRole> findUserRoles(User user);

    /**
     * Stores the picture of the user with the specified id.
     * 
     * @param id
     *            The unique identifier of a user.
     * @param image
     *            The image that will be stored.
     */
    void storeUserPicture(Long id, byte[] image);

    /**
     * Searches for the picture of the member with the specified id. Will
     * validate if the user actually exists.
     * 
     * @param id
     *            The identifier of the user.
     * @param officeId
     *            The unique identifier of the office that is trying to retrieve
     *            the picture of the user.
     * @return Returns a {@link File}.
     */
    File findUserPicture(Long id, Long officeId);

    /**
     * Searches for the picture of the member with the specified id. Will
     * validate if the user actually exists.
     * 
     * @param id
     *            The identifier of the user.
     * @return Returns a {@link File}.
     */
    File findUserPicture(Long userId);

    /**
     * Resets the password for the user and sends an email to the user.
     * 
     * @param user
     *            The user where the password will be reset for.
     * @return Returns the user with the new password.
     */
    User resetPasswordForUser(User user);

    /**
     * Updates the specified user account. This is used for someone that wants
     * to update their own user account not for an administrator that is
     * updating user accounts.
     * 
     * @param user
     *            The user that will be updated.
     * @param errors
     *            The {@link Errors} object that will be populated with error
     *            information when something goes wrong.
     * @return Returns the updated user account.
     */
    User updateUserAccount(User user, Errors errors);

}
