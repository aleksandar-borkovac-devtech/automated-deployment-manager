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
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async role service.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
public interface RoleServiceAsync {

    /**
     * Retrieves the children of a {@link Role}.
     * 
     * @param treeItem
     *        The {@link RoleTreeItem} where the children will be retrieved
     *        from.
     * @param model
     *        The list with model objects.
     * @param callback
     *        The {@link AsyncCallback} that will be used to retrieve the {@link List} containing
     *        {@link ModelData} objects.
     */
    void getRoleChildren(RoleTreeItem treeItem, List<? extends DomainObject<Long>> model, AsyncCallback<List<ModelData>> callback);

    /**
     * Searches for {@link Role} objects based on the specified search criteria.
     * 
     * @param loadConfig
     *        The {@link PagingLoadConfig} that holds the paging
     *        information.
     * @param sc
     *        The search criteria.
     * @param callback
     *        The {@link AsyncCallback} that will be used to return the
     *        search results.
     */
    void findRoles(PagingLoadConfig loadConfig, ClientRoleSearchCommand sc, AsyncCallback<PagingLoadResult<ClientRole>> callback);

    /**
     * Searches for {@link Role} objects based on the specified search criteria.
     * 
     * @param sc
     *        The search criteria.
     * @param callback
     *        The {@link AsyncCallback} that will be used to return the
     *        search results.
     */
    void findRoles(ClientRoleSearchCommand sc, AsyncCallback<List<ClientRole>> callback);

    /**
     * Retrieves a {@link ClientRole} with the specified unique identifier.
     * 
     * @param id
     *        The unique identifier of a role.
     * @param callback
     *        The {@link AsyncCallback} that will be used to return the {@link ClientRole}.
     */
    void findRoleById(Long id, AsyncCallback<ClientRole> callback);

    /**
     * Saves the specified {@link ClientRole}.
     * 
     * @param role
     *        The {@link ClientRole} that will be saved.
     * @param callback
     *        The {@link AsyncCallback} that will be used to return the
     *        saved {@link ClientRole}.
     */
    void saveRole(ClientRole role, AsyncCallback<ClientRole> callback);

    /**
     * Deletes the specified {@link ClientRole} if there are no users that have
     * the specified role.
     * 
     * @param role
     *        The {@link ClientRole} that will be deleted.
     * @param callback
     *        The {@link AsyncCallback} used to return errors if applicable.
     */
    void deleteRole(ClientRole role, AsyncCallback<Void> callback);

    /**
     * Retrieves the grantable roles for a user for a specific scope specified
     * in the search criteria.
     * 
     * @param config
     *        The {@link PagingLoadConfig} containing paging information.
     * @param sc
     *        The search criteria.
     * @param callback
     *        The {@link AsyncCallback} used to return the search result.
     */
    void findGrantableRoles(PagingLoadConfig config, ClientRoleSearchCommand sc, AsyncCallback<PagingLoadResult<ClientRole>> callback);

}
