package nl.tranquilizedquality.adm.gwt.gui.server.service.scope;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.gwt.ext.server.service.AbstractService;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScope;
import nl.tranquilizedquality.adm.gwt.gui.client.model.scope.ClientScopeSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.service.scope.ScopeService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.scope.ScopeServiceException;
import nl.tranquilizedquality.adm.security.business.exception.ScopeManagerException;
import nl.tranquilizedquality.adm.security.business.manager.ScopeManager;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateScope;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

/**
 * The service that manages the {@link Scope} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 */
public class ScopeServiceImpl extends AbstractService implements ScopeService {

    /** Manager that will be used to retrieve {@link Scope} objects. */
    private ScopeManager scopeManager;

    /** Session bean keeping state of the imported scope. */
    private ImportedScope importedScope;

    /**
     * The factory that transforms the client side models into domain objects
     * and visa versa.
     */
    private static final ScopeFactory SCOPE_FACTORY = new ScopeFactory();

    @Override
    public String getScopeImportFeedback() {
        return importedScope.getErrorMessage();
    }

    @Override
    public PagingLoadResult<ClientScope> findScopes(final PagingLoadConfig config, final ClientScopeSearchCommand sc,
            final List<ClientScope> filter) {
        final List<ClientScope> clientScopes = findClientScopes(config, sc);

        clientScopes.removeAll(filter);

        int count = scopeManager.findNumberOfScopes(sc);
        count -= filter.size();

        return new BasePagingLoadResult<ClientScope>(clientScopes, config.getOffset(), count);
    }

    @Override
    public PagingLoadResult<ClientScope> findScopes(final PagingLoadConfig config, final ClientScopeSearchCommand sc) {
        final List<ClientScope> clientScopes = findClientScopes(config, sc);

        final int count = scopeManager.findNumberOfScopes(sc);

        return new BasePagingLoadResult<ClientScope>(clientScopes, config.getOffset(), count);
    }

    /**
     * Finds scopes based on the specified search criteria and creates the
     * client side representation beans.
     * 
     * @param config
     *        The {@link PagingLoadConfig}.
     * @param sc
     *        The search criteria.
     * @return Returns a list of {@link ClientScope} objects.
     */
    private List<ClientScope> findClientScopes(final PagingLoadConfig config, final ClientScopeSearchCommand sc) {
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

        final List<Scope> scopes = scopeManager.findScopes(sc);

        final List<ClientScope> clientScopes = SCOPE_FACTORY.createClientBeans(scopes);
        return clientScopes;
    }

    @Override
    public ClientScope findScopeById(final Long id) {
        final Scope scope = scopeManager.findScopeById(id);

        final ClientScope clientScope = SCOPE_FACTORY.createClientBean(scope);

        return clientScope;
    }

    @Override
    public ClientScope saveScope(final ClientScope scope) throws ScopeServiceException {
        final HibernateScope hibernateScope = SCOPE_FACTORY.createPersistentBean(scope);

        final Errors errors = new BindException(hibernateScope, hibernateScope.getClass().getName());

        try {
            final Scope storedScope = scopeManager.storeScope(hibernateScope, errors);

            return SCOPE_FACTORY.createClientBean(storedScope);
        } catch (final ScopeManagerException e) {
            final List<String> errorList = createErrorList(errors);

            throw new ScopeServiceException("Failed to store scope!", e, errorList);
        }

    }

    @Override
    public List<ClientScope> findManagedScopesWithGrantableRoles(final ClientUser user) {
        /*
         * Find the scopes.
         */
        final List<Scope> scopes = scopeManager.findManagedScopesWithGrantableRolesByUser(user);

        /*
         * Convert to client beans.
         */
        return SCOPE_FACTORY.createClientBeans(scopes);
    }

    /**
     * @param scopeManager
     *        the scopeManager to set
     */
    @Required
    public void setScopeManager(final ScopeManager scopeManager) {
        this.scopeManager = scopeManager;
    }

    /**
     * @param importedScope
     *        the importedScope to set
     */
    public void setImportedScope(final ImportedScope importedScope) {
        this.importedScope = importedScope;
    }
}
