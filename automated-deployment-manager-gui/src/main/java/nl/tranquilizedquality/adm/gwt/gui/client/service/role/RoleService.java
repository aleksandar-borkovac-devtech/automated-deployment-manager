package nl.tranquilizedquality.adm.gwt.gui.client.service.role;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.gwt.gui.client.model.navigation.RoleTreeItem;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRoleSearchCommand;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * {@link RemoteService} implementation that manages {@link Role} related
 * activities.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
@RemoteServiceRelativePath("RoleService.rpc")
public interface RoleService extends RemoteService {

    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util {

        private static RoleServiceAsync instance;

        public static RoleServiceAsync getInstance() {
            if (instance == null) {
                instance = GWT.create(RoleService.class);
            }
            return instance;
        }
    }

    /**
     * Retrieves a list of children of the given {@link DomainObject} model list
     * object. If the identifier is null it will return the model list itself.
     * 
     * @param treeItem
     *            The {@link RoleTreeItem} that identifies the object who's
     *            children will be retrieved.
     * @return Returns a list of children objects.
     */
    List<ModelData> getRoleChildren(RoleTreeItem treeItem, List<? extends DomainObject<Long>> model);

    /**
     * Searches for {@link ClientRole} objects based on the specified search
     * criteria.
     * 
     * @param config
     *            The {@link PagingLoadConfig} containing paging information.
     * @param sc
     *            The search criteria.
     * @return Returns the {@link PagingLoadResult}.
     */
    PagingLoadResult<ClientRole> findRoles(PagingLoadConfig config, ClientRoleSearchCommand sc);

    /**
     * Searches for {@link ClientRole} objects based on the specified search
     * criteria.
     * 
     * @param sc
     *            The search criteria.
     * @return Returns the {@link List} containing {@link ClientRole} objects or
     *         an empty one if none are available.
     */
    List<ClientRole> findRoles(ClientRoleSearchCommand sc);

    /**
     * Retrieves a {@link ClientRole} with the specified id.
     * 
     * @param id
     *            The unique identifier of the {@link ClientRole}.
     * @return Returns a {@link ClientRole}.
     */
    ClientRole findRoleById(Long id);

    /**
     * Saves the specified role.
     * 
     * @param role
     *            The {@link ClientRole} that will be stored.
     * @return Returns the saved {@link ClientRole}.
     * @throws RoleServiceException
     *             Is thrown when something goes wrong during saving of role.
     */
    ClientRole saveRole(ClientRole role) throws RoleServiceException;

    /**
     * Retrieves the grantable roles for a user for a specific scope specified
     * in the search criteria.
     * 
     * @param config
     *            The {@link PagingLoadConfig} containing paging information.
     * @param sc
     *            The search criteria.
     * @return Returns the {@link PagingLoadResult}.
     */
    PagingLoadResult<ClientRole> findGrantableRoles(PagingLoadConfig config, ClientRoleSearchCommand sc);

    /**
     * Deletes the specified {@link ClientRole} if there are no users that have
     * the specified role.
     * 
     * @param role
     *            The {@link ClientRole} that will be deleted.
     * @throws RoleServiceException
     *             Is thrown when something goes wrong during deletion.
     */
    void deleteRole(ClientRole role) throws RoleServiceException;

}
