package nl.tranquilizedquality.adm.gwt.gui.server.service.security;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.gwt.ext.server.service.AbstractService;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroupSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.security.UserGroupService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.security.UserGroupServiceException;
import nl.tranquilizedquality.adm.security.business.manager.UserGroupManager;
import nl.tranquilizedquality.adm.security.business.manager.UserManager;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateUserGroup;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

/**
 * Service that provides services to manage user groups.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 aug. 2012
 */
public class UserGroupServiceImpl extends AbstractService implements UserGroupService {

    /** Factory used to transform user groups. */
    private static final UserGroupFactory USER_GROUP_FACTORY = new UserGroupFactory();

    /** Factory used to transform users. */
    private static final UserFactory USER_FACTORY = new UserFactory();

    /** Manager that is used to manage user groups. */
    private UserGroupManager userGroupManager;

    /** Manager used to search for available users. */
    private UserManager userManager;

    @Override
    public ClientUserGroup findUserGroupById(final Long id) {
        final UserGroup userGroup = userGroupManager.findUserGroupById(id);

        final ClientUserGroup clientBean = USER_GROUP_FACTORY.createClientBean(userGroup);

        return clientBean;
    }

    @Override
    public ClientUserGroup saveUserGroup(final ClientUserGroup userGroup) throws UserGroupServiceException {
        final HibernateUserGroup hibernateUserGroup = USER_GROUP_FACTORY.createPersistentBean(userGroup);

        final Errors errors = new BindException(hibernateUserGroup, hibernateUserGroup.getClass().getName());

        try {
            final UserGroup storedUserGroup = userGroupManager.storeUserGroup(hibernateUserGroup, errors);

            final ClientUserGroup storedClientUserGroup = USER_GROUP_FACTORY.createClientBean(storedUserGroup);

            return storedClientUserGroup;
        } catch (final Exception e) {
            final List<String> errorList = createErrorList(errors);

            throw new UserGroupServiceException("Failed to save user group!", e, errorList);
        }
    }

    @Override
    public PagingLoadResult<ClientUserGroup> findUserGroups(final PagingLoadConfig config, final ClientUserGroupSearchCommand sc) {
        /*
         * Setup search command.
         */
        final SortDir sortDir = config.getSortDir();
        if (sortDir.equals(SortDir.ASC)) {
            sc.setAsc(true);
        }
        else {
            sc.setAsc(false);
        }

        final String sortField = config.getSortField();
        if (sortField != null) {
            sc.setOrderBy(sortField);
        }

        final int limit = config.getLimit();
        sc.setMaxResults(limit);

        final int offset = config.getOffset();
        sc.setStart(offset);

        /*
         * Search for the offices.
         */
        final List<UserGroup> userGroups = userGroupManager.findUserGroupsBySearchCommand(sc);

        /*
         * Create client beans.
         */
        final List<ClientUserGroup> clientBeans = USER_GROUP_FACTORY.createClientBeans(userGroups);

        /*
         * Retrieve the total count.
         */
        final int count = userGroupManager.findNumberOfUserGroups(sc);

        /*
         * Return the results for a grid.
         */
        return new BasePagingLoadResult<ClientUserGroup>(clientBeans, config.getOffset(), count);
    }

    @Override
    public List<ClientUserGroup> findUserGroups() {
        /*
         * Search for the offices.
         */
        final List<UserGroup> userGroups = userGroupManager.findLoggedInUserUserGroups();

        /*
         * Create client beans.
         */
        final List<ClientUserGroup> clientBeans = USER_GROUP_FACTORY.createClientBeans(userGroups);

        return clientBeans;
    }

    @Override
    public PagingLoadResult<ClientUser> findUsers(final PagingLoadConfig loadConfig, final ClientUserSearchCommand sc) {
        /*
         * Setup search command.
         */
        final SortDir sortDir = loadConfig.getSortDir();
        if (sortDir.equals(SortDir.ASC)) {
            sc.setAsc(true);
        }
        else {
            sc.setAsc(false);
        }

        final String sortField = loadConfig.getSortField();
        if (sortField != null) {
            sc.setOrderBy(sortField);
        }

        final int limit = loadConfig.getLimit();
        sc.setMaxResults(limit);

        final int offset = loadConfig.getOffset();
        sc.setStart(offset);

        /*
         * Search for the offices.
         */
        final List<User> users = userManager.findUsers(sc);

        /*
         * Create client beans.
         */
        final List<ClientUser> clientBeans = USER_FACTORY.createClientBeans(users);

        /*
         * Retrieve the total count.
         */
        final int count = userManager.findNumberOfUsers(sc);

        /*
         * Return the results for a grid.
         */
        return new BasePagingLoadResult<ClientUser>(clientBeans, loadConfig.getOffset(), count);
    }

    @Required
    public void setUserGroupManager(final UserGroupManager userGroupManager) {
        this.userGroupManager = userGroupManager;
    }

    @Required
    public void setUserManager(final UserManager userManager) {
        this.userManager = userManager;
    }

}
