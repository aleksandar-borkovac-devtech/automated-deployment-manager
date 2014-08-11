package nl.tranquilizedquality.adm.gwt.gui.client.service.scope;

import java.util.List;

import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScope;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScopeSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async scope service.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
public interface ScopeServiceAsync {

    /**
     * Retrieves scopes that match the search criteria.
     * 
     * @param config
     *            The {@link PagingLoadConfig} that determines which page to
     *            show etc.
     * @param sc
     *            The search criteria.
     * @param callback
     *            The {@link AsyncCallback} that will be used to return the
     *            {@link PagingLoadResult} containing {@link ClientScope}
     *            objects that matched the search criteria.
     */
    void findScopes(PagingLoadConfig config, ClientScopeSearchCommand sc, AsyncCallback<PagingLoadResult<ClientScope>> callback);

    /**
     * Retrieves scopes based on the search criteria and a filter. The filter
     * will be used to exclude certain {@link ClientScope} objects from the
     * search result.
     * 
     * @param config
     * @param sc
     * @param filter
     * @param callback
     */
    void findScopes(PagingLoadConfig config, ClientScopeSearchCommand sc, List<ClientScope> filter,
            AsyncCallback<PagingLoadResult<ClientScope>> callback);

    /**
     * Retrieves a {@link ClientScope} object with the specified unique
     * identifier.
     * 
     * @param id
     *            The unique identifier of the {@link ClientScope} that needs to
     *            be retrieved.
     * @param callback
     *            the {@link AsyncCallback} that will be used to return the
     *            {@link ClientScope} object.
     */
    void findScopeById(Long id, AsyncCallback<ClientScope> callback);

    /**
     * Saves the specified {@link ClientScope}.
     * 
     * @param scope
     *            The {@link ClientScope} that will be saved.
     * @param callback
     *            The {@link AsyncCallback} that will be used to return the
     *            saved {@link ClientScope}.
     */
    void saveScope(ClientScope scope, AsyncCallback<ClientScope> callback);

    /**
     * Retrieves all managed scopes with grantable roles for the specified
     * {@link ClientUser}.
     * 
     * @param user
     *            The {@link ClientUser} where the {@link ClientScope} objects
     *            will be retrieved for.
     * @param callback
     *            The {@link AsyncCallback} that will be used to return the
     *            {@link List} containing {@link ClientScope} objects or an
     *            empty one if none could be found.
     */
    void findManagedScopesWithGrantableRoles(ClientUser user, AsyncCallback<List<ClientScope>> callback);

    /**
     * Retrieves the feedback message from the scope import.
     * 
     * @param callback
     *            Returns the success or error message.
     */
    void getScopeImportFeedback(AsyncCallback<String> callback);
}
