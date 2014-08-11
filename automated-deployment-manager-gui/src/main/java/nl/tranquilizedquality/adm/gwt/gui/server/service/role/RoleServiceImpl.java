package nl.tranquilizedquality.adm.gwt.gui.server.service.role;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.commons.business.domain.Role;
import nl.tranquilizedquality.adm.commons.business.domain.Scope;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.gwt.ext.server.service.AbstractService;
import nl.tranquilizedquality.adm.gwt.gui.client.model.navigation.RoleTreeItem;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRole;
import nl.tranquilizedquality.adm.gwt.gui.client.model.role.ClientRoleSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.role.RoleService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.role.RoleServiceException;
import nl.tranquilizedquality.adm.security.business.exception.RoleManagerException;
import nl.tranquilizedquality.adm.security.business.manager.RoleManager;
import nl.tranquilizedquality.adm.security.business.manager.ScopeManager;
import nl.tranquilizedquality.adm.security.persistence.db.hibernate.bean.HibernateRole;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

/**
 * The service that manages the {@link Role} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class RoleServiceImpl extends AbstractService implements RoleService {

    /** Manager that will be used to retrieve {@link Role} objects. */
    private RoleManager roleManager;

    /** Manager that will be used to retrieve {@link Scope} objects. */
    private ScopeManager scopeManager;

    /**
     * The factory that transforms the client side models into domain objects
     * and visa versa.
     */
    private static final RoleFactory ROLE_FACTORY = new RoleFactory();

    @Override
    public ClientRole saveRole(final ClientRole role) throws RoleServiceException {
        final HibernateRole hibernateRole = ROLE_FACTORY.createPersistentBean(role);

        final Errors errors = new BindException(hibernateRole, hibernateRole.getClass().getName());

        try {
            final Role storedRole = roleManager.storeRole(hibernateRole, errors);

            return ROLE_FACTORY.createClientBean(storedRole);
        } catch (final RoleManagerException e) {
            final List<String> errorList = createErrorList(errors);
            throw new RoleServiceException("Failed to store role!", e, errorList);
        }
    }

    @Override
    public void deleteRole(final ClientRole role) throws RoleServiceException {
        final HibernateRole hibernateRole = ROLE_FACTORY.createPersistentBean(role);

        final Errors errors = new BindException(hibernateRole, hibernateRole.getClass().getName());

        try {
            roleManager.deleteRole(hibernateRole, errors);
        } catch (final RoleManagerException e) {
            final List<String> errorList = createErrorList(errors);

            throw new RoleServiceException("Failed to delete role!", e, errorList);
        }
    }

    @Override
    public PagingLoadResult<ClientRole> findRoles(final PagingLoadConfig config, final ClientRoleSearchCommand sc) {

        /*
         * Search for the roles.
         */
        final Scope scope = sc.getScope();
        if (scope != null) {
            final Scope foundScope = scopeManager.findScopeById(scope.getId());
            sc.setScope(foundScope);
        }
        final List<Role> foundRoles = roleManager.findRoles(sc);

        /*
         * Create the client beans.
         */
        final List<ClientRole> clientBeans = ROLE_FACTORY.createClientBeans(foundRoles);

        int count = roleManager.findNumberOfRoles(sc);

        /*
         * Filter the list if there is a filter defined.
         */
        final List<Role> excludedRoles = sc.getExcludedRoles();
        if (excludedRoles != null) {
            clientBeans.removeAll(excludedRoles);
            count -= excludedRoles.size();
        }

        return new BasePagingLoadResult<ClientRole>(clientBeans, config.getOffset(), count);
    }

    @Override
    public List<ClientRole> findRoles(final ClientRoleSearchCommand sc) {
        /*
         * Search for the roles.
         */
        final Scope scope = sc.getScope();
        if (scope != null) {
            final Scope foundScope = scopeManager.findScopeById(scope.getId());
            sc.setScope(foundScope);
        }
        final List<Role> foundRoles = roleManager.findRoles(sc);

        /*
         * Create the client beans.
         */
        final List<ClientRole> clientBeans = ROLE_FACTORY.createClientBeans(foundRoles);

        return clientBeans;
    }

    @Override
    public PagingLoadResult<ClientRole> findGrantableRoles(final PagingLoadConfig config, final ClientRoleSearchCommand sc) {

        /*
         * Search for the roles.
         */
        final Scope scope = sc.getScope();
        if (scope != null) {
            final Scope foundScope = scopeManager.findScopeById(scope.getId());
            sc.setScope(foundScope);
        }
        final List<Role> foundRoles = roleManager.findGrantableRolesByLoggedInUserAndScope(sc);

        /*
         * Create the client beans.
         */
        final List<ClientRole> clientBeans = ROLE_FACTORY.createClientBeans(foundRoles);

        int count = foundRoles.size();

        /*
         * Filter the list if there is a filter defined.
         */
        final List<Role> excludedRoles = sc.getExcludedRoles();
        if (excludedRoles != null) {
            clientBeans.removeAll(excludedRoles);
            count -= excludedRoles.size();
        }

        return new BasePagingLoadResult<ClientRole>(clientBeans, config.getOffset(), count);
    }

    @Override
    public List<ModelData> getRoleChildren(final RoleTreeItem treeItem, final List<? extends DomainObject<Long>> model) {

        if (treeItem == null) {
            // If no id, then we are at the root, so return the roles
            List<ModelData> modelDataList = null;

            if (model != null) {
                // Only proceed if we got a model
                // There are two possibilities now.
                // We either got a Scope based model or a Role based model.
                modelDataList = new ArrayList<ModelData>();
                for (final DomainObject<Long> modelItem : model) {
                    if (modelItem instanceof Scope) {
                        modelDataList.add(new RoleTreeItem((Scope) modelItem));
                    }
                    else if (modelItem instanceof ClientRole) {
                        modelDataList.add(new RoleTreeItem((ClientRole) modelItem));
                    }
                }
            }

            return modelDataList;
        }
        else if (treeItem.getClientObject() instanceof Scope) {
            // A scope parent, so return its roles
            final List<ModelData> modelDataList = new ArrayList<ModelData>();
            for (final Role role : ((Scope) treeItem.getClientObject()).getRoles()) {
                modelDataList.add(new RoleTreeItem(role));
            }

            return modelDataList;
        }
        else {
            // A role parent, so retrieve it and return its privileges
            final Role role = roleManager.findRoleById(treeItem.getKey());
            final ClientRole clientRole = ROLE_FACTORY.createClientBean(role);

            final List<ModelData> modelDataList = new ArrayList<ModelData>();
            for (final Privilege privilege : clientRole.getPrivileges()) {
                modelDataList.add(new RoleTreeItem(privilege));
            }

            return modelDataList;
        }
    }

    @Override
    public ClientRole findRoleById(final Long id) {
        final Role foundRole = roleManager.findRoleById(id);

        final ClientRole clientRole = ROLE_FACTORY.createClientBean(foundRole);

        return clientRole;
    }

    /**
     * @param roleManager
     *            the roleManager to set
     */
    @Required
    public void setRoleManager(final RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    /**
     * @param scopeManager
     *            the scopeManager to set
     */
    @Required
    public void setScopeManager(final ScopeManager scopeManager) {
        this.scopeManager = scopeManager;
    }

}
