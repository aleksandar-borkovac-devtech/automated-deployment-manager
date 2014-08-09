package nl.tranquilizedquality.adm.gwt.gui.client.service.user;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientUserRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.settings.ClientEnvironmentNotificationSetting;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous service used by the client side to manage {@link ClientMember} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
public interface UserServiceAsync {

    /**
     * Searches for users based on the specified search criteria.
     * 
     * @param config
     *        The page loading configuration.
     * @param sc
     *        The search criteria.
     * @param callback
     *        Callback function.
     * @param callback
     *        Returns a {@link PagingLoadResult} containing the members that
     *        were found or an empty one if none could be found.
     */
    void findUsers(PagingLoadConfig config, ClientUserSearchCommand sc, AsyncCallback<PagingLoadResult<ClientUser>> callback);

    /**
     * Retrieves a user with the specified unique identifier.
     * 
     * @param id
     *        The unique identifier of the member.
     * @param callback
     *        Returns a {@link ClientUser} or null if none could be found.
     */
    void findUserById(Long id, AsyncCallback<ClientUser> callback);

    /**
     * Retrieves a user that is currently logged in.
     * 
     * @param callback
     *        Returns a {@link ClientUser}.
     */
    void findLoggedInUser(AsyncCallback<ClientUser> callback);

    /**
     * Searches for users based on the search criteria filtered by the passed in
     * filter.
     * 
     * @param config
     *        The {@link PagingLoadConfig} with the paging information.
     * @param sc
     *        The search criteria.
     * @param filter
     *        The {@link List} containing {@link ClientUser} objects that
     *        will be left out the search result if they are part of it.
     * @param callback
     *        The {@link AsyncCallback} will be used to return the {@link PagingLoadResult} that can
     *        be used by a grid.
     */
    void findUsers(PagingLoadConfig config, ClientUserSearchCommand sc, List<ClientUser> filter,
            AsyncCallback<PagingLoadResult<ClientUser>> callback);

    /**
     * Saves the user.
     * 
     * @param user
     *        The {@link ClientUser} that will be saved.
     * @param callback
     *        The {@link AsyncCallback} that will be used to return the
     *        saved {@link ClientUser}.
     */
    void saveUser(ClientUser user, AsyncCallback<ClientUser> callback);

    /**
     * Stores the passed in {@link UserRole}.
     * 
     * @param userRole
     *        The {@link UserRole} that will be saved.
     * @param callback
     *        The {@link AsyncCallback} that will be used to return the {@link ClientUserRole} that
     *        was saved.
     */
    void saveUserRole(ClientUserRole userRole, AsyncCallback<ClientUserRole> callback);

    /**
     * Revokes a role from a user.
     * 
     * @param userRole
     *        The {@link UserRole} that will be revoked.
     * @param callback
     *        The {@link AsyncCallback} that will used to handle any
     *        exceptions that may occur.
     */
    void revokeUserRole(ClientUserRole userRole, AsyncCallback<Void> callback);

    /**
     * Assigns the specified roles to the specified users.
     * 
     * @param clientUsers
     *        The users that where the roles will be assigned to.
     * @param clientRoles
     *        The roles that will be assigned to the users.
     * @param callback
     *        The {@link AsyncCallback} that will be used to report back
     *        errors.
     */
    void assignRoles(List<ClientUser> clientUsers, List<ClientRole> clientRoles, AsyncCallback<Void> callback);

    /**
     * Sets the specified search criteria to be used for the Excel export.
     * 
     * @param sc
     *        The search criteria that will be set to the user session.
     * @param callback
     *        The {@link AsyncCallback} that will be used to report back
     *        errors.
     */
    void setExportCriteria(ClientUserSearchCommand sc, AsyncCallback<Void> callback);

    /**
     * Retrieves all roles from the specified user.
     * 
     * @param user
     *        The {@link ClientUser} where the roles will be retrieved for.
     * @param callback
     *        The {@link PagingLoadResult} containing the search results.
     */
    void findUserRolesByUser(ClientUser user, AsyncCallback<PagingLoadResult<ClientUserRole>> callback);

    /**
     * Resets the password of the specified user.
     * 
     * @param user
     *        The user where the password will be resetted for.
     * @param callback
     *        Callback method used to retrieve the updated account.
     */
    void resetPassword(ClientUser user, AsyncCallback<ClientUser> callback);

    /**
     * Updates the user account details of the logged in user.
     * 
     * @param user
     *        The logged in user where the details will be updated for.
     * @param callback
     *        Returns the updated account.
     * @throws UserServiceException
     *         Is thrown when something goes wrong during storage.
     */
    void updateUserAccount(ClientUser user, AsyncCallback<ClientUser> callback);

    /**
     * Retrieves the settings of the logged in user.
     * 
     * @param callback
     *        Returns a {@link List} containing {@link ClientEnvironmentNotificationSetting}.
     */
    void findSettingsForLoggedInUser(AsyncCallback<List<ClientEnvironmentNotificationSetting>> callback);

    /**
     * Saves the settings for the logged in user.
     * 
     * @param settings
     *        The settings that will be updated.
     * @param callback
     *        Callback function used to handle the response.
     */
    void saveSettings(List<ClientEnvironmentNotificationSetting> settings, AsyncCallback<Void> callback);

}
