package nl.tranquilizedquality.adm.gwt.gui.client.service.user;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientUserRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.settings.ClientEnvironmentNotificationSetting;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Remote service that will be used to manipulate and retrieve
 * {@link ClientMember} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
@RemoteServiceRelativePath("UserService.rpc")
public interface UserService extends RemoteService {

    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util {

        /** The async service. */
        private static UserServiceAsync instance;

        /**
         * Retrieves an instance of the {@link UserServiceAsync}.
         * 
         * @return Returns a {@link UserServiceAsync}.
         */
        public static UserServiceAsync getInstance() {
            if (instance == null) {
                instance = GWT.create(UserService.class);
            }
            return instance;
        }
    }

    /**
     * Searches for users based on the specified search criteria.
     * 
     * @param config
     *            The page loading configuration.
     * @param sc
     *            The search criteria.
     * @return Returns a {@link PagingLoadResult} containing the members that
     *         were found or an empty one if none could be found.
     */
    PagingLoadResult<ClientUser> findUsers(PagingLoadConfig config, ClientUserSearchCommand sc);

    /**
     * Retrieves a user with the specified unique identifier.
     * 
     * @param id
     *            The unique identifier of the member.
     * @return Returns a {@link ClientUser} or null if none could be found.
     */
    ClientUser findUserById(Long id);

    /**
     * Retrieves a user that is currently logged in.
     * 
     * @return Returns a {@link ClientUser}.
     */
    ClientUser findLoggedInUser();

    /**
     * Saves the passed in user and returns the saved {@link ClientUser}.
     * 
     * @param user
     *            The {@link ClientUser} that will be saved.
     * @return Returns the saved {@link ClientUser}.
     * @throws UserServiceException
     *             Is thrown when validation errors occur.
     */
    ClientUser saveUser(ClientUser user) throws AbstractServiceException;

    /**
     * Searches for {@link ClientUser} objects based on the specified search
     * criteria and filters the result with the specified filter.
     * 
     * @param config
     *            The {@link PagingLoadConfig} that is used to retrieve paging
     *            information.
     * @param sc
     *            The search criteria.
     * @param filter
     *            A {@link List} containing {@link ClientUser} objects that
     *            needs to be removed from the result.
     * @return Returns a {@link PagingLoadResult} containing {@link ClientUser}
     *         objects.
     */
    PagingLoadResult<ClientUser> findUsers(PagingLoadConfig config, ClientUserSearchCommand sc, List<ClientUser> filter);

    /**
     * Saves the passed in user and returns the saved {@link ClientUserRole}.
     * 
     * @param userRole
     *            The {@link ClientUserRole} that will be saved.
     * @return Returns the saved {@link ClientUserRole}.
     * @throws UserServiceException
     *             Is thrown when something goes wrong during storage.
     */
    ClientUserRole saveUserRole(ClientUserRole userRole) throws AbstractServiceException;

    /**
     * Removes the passed in {@link UserRole}.
     * 
     * @param userRole
     *            The {@link UserRole} that will be removed.
     * @throws UserServiceException
     *             Is thrown when something goes wrong during deletion.
     */
    void revokeUserRole(ClientUserRole userRole) throws AbstractServiceException;

    /**
     * Assigns the specified roles to the specified users.
     * 
     * @param clientUsers
     *            The users that where the roles will be assigned to.
     * @param clientRoles
     *            The roles that will be assigned to the users.
     */
    void assignRoles(List<ClientUser> clientUsers, List<ClientRole> clientRoles);

    /**
     * Sets the specified search criteria to be used for the Excel export.
     * 
     * @param sc
     *            The search criteria that will be set to the user session.
     */
    void setExportCriteria(ClientUserSearchCommand sc);

    /**
     * Retrieves all roles from the specified user.
     * 
     * @param user
     *            The {@link ClientUser} where the roles will be retrieved for.
     * @return Returns the {@link PagingLoadResult} containing the search
     *         results.
     */
    PagingLoadResult<ClientUserRole> findUserRolesByUser(ClientUser user);

    /**
     * Resets the password for the specified user.
     * 
     * @param user
     *            The user where the password will be reset for.
     * @return Returns a {@link ClientUser}.
     */
    ClientUser resetPassword(ClientUser user);

    /**
     * Updates the user account details of the logged in user.
     * 
     * @param user
     *            The logged in user where the details will be updated for.
     * @return Returns the updated account.
     * @throws UserServiceException
     *             Is thrown when something goes wrong during storage.
     */
    ClientUser updateUserAccount(ClientUser user) throws AbstractServiceException;

    /**
     * Retrieves the settings of the logged in user.
     * 
     * @return Returns a {@link List} containing
     *         {@link ClientEnvironmentNotificationSetting}.
     */
    List<ClientEnvironmentNotificationSetting> findSettingsForLoggedInUser();

    /**
     * Saves the settings for the logged in user.
     * 
     * @param settings
     *            The settings that will be updated.
     */
    void saveSettings(List<ClientEnvironmentNotificationSetting> settings);

}
