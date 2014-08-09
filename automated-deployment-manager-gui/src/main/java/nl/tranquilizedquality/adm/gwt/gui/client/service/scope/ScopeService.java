package nl.tranquilizedquality.adm.gwt.gui.client.service.scope;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScope;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScopeSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * {@link RemoteService} implementation that manages {@link Scope} related
 * activities.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
@RemoteServiceRelativePath("ScopeService.rpc")
public interface ScopeService extends RemoteService {

    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util {

        private static ScopeServiceAsync instance;

        public static ScopeServiceAsync getInstance() {
            if (instance == null) {
                instance = GWT.create(ScopeService.class);
            }
            return instance;
        }
    }

    /**
     * Searches for {@link ClientScope} objects based on the passed in search
     * criteria.
     * 
     * @param config
     *        The {@link PagingLoadConfig} containing the paging
     *        information.
     * @param sc
     *        The search criteria.
     * @return Returns a {@link PagingLoadResult} that will be used by a proxy
     *         to populate a paging grid.
     */
    PagingLoadResult<ClientScope> findScopes(PagingLoadConfig config, ClientScopeSearchCommand sc);

    /**
     * Searches for {@link ClientScope} objects based on the passed in search
     * criteria.
     * 
     * @param config
     *        The {@link PagingLoadConfig} containing the paging
     *        information.
     * @param sc
     *        The search criteria.
     * @param filter
     *        The filter list
     * @return Returns a {@link PagingLoadResult} that will be used by a proxy
     *         to populate a paging grid.
     */
    PagingLoadResult<ClientScope> findScopes(PagingLoadConfig config, ClientScopeSearchCommand sc, List<ClientScope> filter);

    /**
     * Retrieves a {@link ClientScope} with the specified unique identifier.
     * 
     * @param id
     *        The unique identifier of the {@link ClientScope} that will be
     *        retrieved.
     * @return Returns a {@link ClientScope} object.
     */
    ClientScope findScopeById(Long id);

    /**
     * Saves the specified {@link ClientScope} object.
     * 
     * @param scope
     *        The {@link ClientScope} object that will be saved.
     * @return Returns the saved {@link ClientScope} object.
     * @throws ScopeServiceException
     *         Is thrown when something goes wrong during scope storage.
     */
    ClientScope saveScope(ClientScope scope) throws ScopeServiceException;

    /**
     * Retrieves all managed scopes with grantable roles for the specified {@link ClientUser}.
     * 
     * @param user
     *        The {@link ClientUser} where the {@link ClientScope} objects
     *        will be retrieved for.
     * @return Returns a {@link List} containing {@link ClientScope} objects or
     *         an empty one if none could be found.
     */
    List<ClientScope> findManagedScopesWithGrantableRoles(ClientUser user);

    /**
     * Retrieves the feedback message from the scope import.
     * 
     * @return Returns the success or error message.
     */
    String getScopeImportFeedback();

}
