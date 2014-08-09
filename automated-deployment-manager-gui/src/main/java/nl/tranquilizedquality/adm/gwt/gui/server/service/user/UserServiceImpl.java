package nl.tranquilizedquality.adm.gwt.gui.server.service.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.EnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.commons.gwt.ext.client.service.AbstractServiceException;
import nl.tranquilizedquality.adm.commons.gwt.ext.server.service.AbstractService;
import nl.tranquilizedquality.adm.core.business.manager.UserSettingsManager;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientUserRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.settings.ClientEnvironmentNotificationSetting;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.user.UserServiceException;
import nl.tranquilizedquality.adm.gwt.gui.server.service.role.RoleFactory;
import nl.tranquilizedquality.adm.security.business.exception.UserManagerException;
import nl.tranquilizedquality.adm.security.business.manager.SecurityContextManager;
import nl.tranquilizedquality.adm.security.business.manager.UserManager;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateRole;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUser;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserRole;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

/**
 * Remote service implementation that manages {@link User} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 3 feb. 2011
 */
public class UserServiceImpl extends AbstractService implements UserService {

    /**
     * The factory that transforms the client side models into domain objects
     * and visa versa.
     */
    private static final UserFactory USER_FACTORY = new UserFactory();

    /**
     * The factory that transforms the client side models into domain objects
     * and visa versa.
     */
    private static final RoleFactory ROLE_FACTORY = new RoleFactory();

    /**
     * The factory that transforms the client side models into domain objects
     * and visa versa.
     */
    private static final UserRoleFactory USER_ROLE_FACTORY = new UserRoleFactory();

    /**
     * The factory that transforms the client side models into domain objects
     * and visa versa.
     */
    private static final EnvironmentUserSettingsFactory ENVIRONMENT_USER_SETTINGS_FACTORY = new EnvironmentUserSettingsFactory();

    /** Manager that manages {@link User} objects. */
    private UserManager userManager;

    /** Manager used to retrieve the logged in user. */
    private SecurityContextManager securityContextManager;

    /** Session scoped bean to store user specific data. */
    private UserDataBean userDataBean;

    private UserSettingsManager userSettingsManager;

    @Override
    public PagingLoadResult<ClientUserRole> findUserRolesByUser(final ClientUser user) {
        final HibernateUser hibernateUser = USER_FACTORY.createPersistentBean(user);

        final List<UserRole> userRoles = userManager.findUserRoles(hibernateUser);

        final List<ClientUserRole> clientBeans = USER_ROLE_FACTORY.createClientBeans(userRoles);

        return new BasePagingLoadResult<ClientUserRole>(clientBeans);
    }

    @Override
    public void setExportCriteria(final ClientUserSearchCommand sc) {
        userDataBean.addData("sc", sc);
    }

    @Override
    public void assignRoles(final List<ClientUser> clientUsers, final List<ClientRole> clientRoles) {
        /*
         * Retrieve the logged in user.
         */
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        final String username = principal.getUsername();

        /*
         * Retrieve the actual user.
         */
        final User loggedInUser = userManager.findUserByUsername(username);

        /*
         * Create hibernate beans.
         */
        final List<HibernateUser> users = USER_FACTORY.createPersistentBeans(clientUsers);
        final List<HibernateRole> roles = ROLE_FACTORY.createPersistentBeans(clientRoles);

        /*
         * Assign roles.
         */
        userManager.assignRoles(roles, users, loggedInUser);
    }

    @Override
    public ClientUser updateUserAccount(final ClientUser user) throws AbstractServiceException {
        /*
         * Create a hibernate user that can be used in the manager.
         */
        final HibernateUser hibernateUser = USER_FACTORY.createPersistentBean(user);

        /*
         * Create the binding errors object so validation errors can be reported
         * back.
         */
        final Errors errors = new BindException(hibernateUser, hibernateUser.getClass().getName());

        try {
            /*
             * Check if we are doing an insert or an update.
             */
            boolean assignDefaultRoles = false;
            if (!hibernateUser.isPersistent()) {
                assignDefaultRoles = true;
            }

            /*
             * Save the user.
             */
            final User storedUser = userManager.updateUserAccount(hibernateUser, errors);

            /*
             * Assign default roles to the user only if it's a new user.
             */
            if (assignDefaultRoles) {
                userManager.assignDefaultRoles(storedUser, errors);

                /*
                 * Retrieve the stored user with the default roles attached to
                 * it.
                 */
                final User foundUser = userManager.findUserById(storedUser.getId());

                /*
                 * Create client beans.
                 */
                final ClientUser clientUser = USER_FACTORY.createClientBean(foundUser);

                /*
                 * Return the client bean.
                 */
                return clientUser;
            } else {
                /*
                 * Create client beans.
                 */
                final ClientUser clientUser = USER_FACTORY.createClientBean(storedUser);

                /*
                 * Return the client bean.
                 */
                return clientUser;
            }
        } catch (final UserManagerException e) {
            /*
             * Create a list with errors from the binding errors object.
             */
            final List<String> errorList = createErrorList(errors);

            /*
             * Throw the exception with the error list so the GUI can show what
             * went wrong.
             */
            throw new UserServiceException("Failed to store user!", e, errorList);
        }
    }

    @Override
    public ClientUser saveUser(final ClientUser user) throws AbstractServiceException {
        /*
         * Create a hibernate user that can be used in the manager.
         */
        final HibernateUser hibernateUser = USER_FACTORY.createPersistentBean(user);

        /*
         * Create the binding errors object so validation errors can be reported
         * back.
         */
        final Errors errors = new BindException(hibernateUser, hibernateUser.getClass().getName());

        try {
            /*
             * Check if we are doing an insert or an update.
             */
            boolean assignDefaultRoles = false;
            if (!hibernateUser.isPersistent()) {
                assignDefaultRoles = true;
            }

            /*
             * Save the user.
             */
            final User storedUser = userManager.storeUser(hibernateUser, errors);

            /*
             * Assign default roles to the user only if it's a new user.
             */
            if (assignDefaultRoles) {
                userManager.assignDefaultRoles(storedUser, errors);

                /*
                 * Retrieve the stored user with the default roles attached to
                 * it.
                 */
                final User foundUser = userManager.findUserById(storedUser.getId());

                /*
                 * Create client beans.
                 */
                final ClientUser clientUser = USER_FACTORY.createClientBean(foundUser);

                /*
                 * Return the client bean.
                 */
                return clientUser;
            } else {
                /*
                 * Create client beans.
                 */
                final ClientUser clientUser = USER_FACTORY.createClientBean(storedUser);

                /*
                 * Return the client bean.
                 */
                return clientUser;
            }
        } catch (final UserManagerException e) {
            /*
             * Create a list with errors from the binding errors object.
             */
            final List<String> errorList = createErrorList(errors);

            /*
             * Throw the exception with the error list so the GUI can show what
             * went wrong.
             */
            throw new UserServiceException("Failed to store user!", e, errorList);
        }
    }

    @Override
    public ClientUserRole saveUserRole(final ClientUserRole userRole) throws AbstractServiceException {
        final HibernateUserRole hibernateUserRole = USER_ROLE_FACTORY.createPersistentBean(userRole);

        final Errors errors = new BindException(hibernateUserRole, hibernateUserRole.getClass().getName());

        try {
            final UserRole storedUserRole = userManager.storeUserRole(hibernateUserRole, errors);

            final ClientUserRole clientUserRole = USER_ROLE_FACTORY.createClientBean(storedUserRole);

            return clientUserRole;

        } catch (final UserManagerException e) {
            final List<String> errorList = createErrorList(errors);

            throw new UserServiceException("Failed to store user role!", e, errorList);
        }
    }

    @Override
    public void revokeUserRole(final ClientUserRole userRole) throws AbstractServiceException {
        final HibernateUserRole hibernateUserRole = USER_ROLE_FACTORY.createPersistentBean(userRole);

        final Errors errors = new BindException(hibernateUserRole, hibernateUserRole.getClass().getName());

        try {
            userManager.revokeUserRole(hibernateUserRole, errors);
        } catch (final UserManagerException e) {
            final List<String> errorList = createErrorList(errors);

            throw new UserServiceException("Failed to remove user role!", e, errorList);
        }
    }

    @Override
    public PagingLoadResult<ClientUser> findUsers(final PagingLoadConfig config, final ClientUserSearchCommand sc,
            final List<ClientUser> filter) {
        List<ClientUser> clientUsers = new ArrayList<ClientUser>();
        final boolean initSearch = sc.isInitSearch();
        if (!initSearch) {
            clientUsers = findClientUsers(config, sc);
        }

        clientUsers.removeAll(filter);

        int count = 0;
        if (!initSearch) {
            count = userManager.findNumberOfUsers(sc);
            count -= filter.size();
        }

        return new BasePagingLoadResult<ClientUser>(clientUsers, config.getOffset(), count);
    }

    @Override
    public PagingLoadResult<ClientUser> findUsers(final PagingLoadConfig config, final ClientUserSearchCommand sc) {
        final List<ClientUser> clientUsers = findClientUsers(config, sc);

        final int count = userManager.findNumberOfUsers(sc);

        return new BasePagingLoadResult<ClientUser>(clientUsers, config.getOffset(), count);
    }

    /**
     * @param config
     * @param sc
     * @return
     */
    private List<ClientUser> findClientUsers(final PagingLoadConfig config, final ClientUserSearchCommand sc) {
        sc.setMaxResults(config.getLimit());
        sc.setOrderBy(config.getSortField());

        final SortDir sortDir = config.getSortDir();

        /*
         * Setup sorting direction.
         */
        switch (sortDir) {
            case ASC:
            case NONE:
                sc.setAsc(true);
                break;
            case DESC:
                sc.setAsc(false);
                break;

            default:
                sc.setAsc(true);
                break;
        }

        final List<User> users = userManager.findUsers(sc);

        final List<ClientUser> clientUsers = USER_FACTORY.createClientBeans(users);
        return clientUsers;
    }

    @Override
    public ClientUser findUserById(final Long id) {
        final User user = userManager.findUserById(id);

        final ClientUser clientUser = USER_FACTORY.createClientBean(user);
        clientUser.setPassword(null);

        return clientUser;
    }

    @Override
    public ClientUser findLoggedInUser() {
        final User loggedInUser = securityContextManager.findLoggedInUser();
        loggedInUser.setUserRoles(new HashSet<UserRole>());

        final ClientUser clientUser = USER_FACTORY.createClientBean(loggedInUser);
        clientUser.setPassword(null);

        return clientUser;
    }

    @Override
    public ClientUser resetPassword(final ClientUser user) {

        final HibernateUser persistentBean = USER_FACTORY.createPersistentBean(user);

        final User resttedUser = userManager.resetPasswordForUser(persistentBean);

        final ClientUser clientUser = USER_FACTORY.createClientBean(resttedUser);
        clientUser.setPassword(null);

        return clientUser;
    }

    @Override
    public List<ClientEnvironmentNotificationSetting> findSettingsForLoggedInUser() {
        final User loggedInUser = securityContextManager.findLoggedInUser();
        final List<EnvironmentNotificationSetting> settings = userSettingsManager.findSettingsForUser(loggedInUser);

        for (final EnvironmentNotificationSetting environmentNotificationSetting : settings) {
            final User user = environmentNotificationSetting.getUser();
            user.setUserRoles(new HashSet<UserRole>());
            final Environment environment = environmentNotificationSetting.getEnvironment();
            environment.setUsers(new ArrayList<User>());
        }

        final List<ClientEnvironmentNotificationSetting> clientBeans = ENVIRONMENT_USER_SETTINGS_FACTORY.createClientBeans(settings);
        return clientBeans;
    }

    @Override
    public void saveSettings(final List<ClientEnvironmentNotificationSetting> settings) {

        final List<HibernateEnvironmentNotificationSetting> persistentBeans =
                ENVIRONMENT_USER_SETTINGS_FACTORY.createPersistentBeans(settings);
        final List<EnvironmentNotificationSetting> notificationSettings = new ArrayList<EnvironmentNotificationSetting>(persistentBeans);

        userSettingsManager.storeSettings(notificationSettings);
    }

    @Required
    public void setUserManager(final UserManager userManager) {
        this.userManager = userManager;
    }

    @Required
    public void setSecurityContextManager(final SecurityContextManager securityContextManager) {
        this.securityContextManager = securityContextManager;
    }

    /**
     * @param userDataBean
     *        the userDataBean to set
     */
    public void setUserDataBean(final UserDataBean userDataBean) {
        this.userDataBean = userDataBean;
    }

    @Required
    public void setUserSettingsManager(final UserSettingsManager userSettingsManager) {
        this.userSettingsManager = userSettingsManager;
    }

}
