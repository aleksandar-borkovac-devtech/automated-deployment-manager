package nl.tranquilizedquality.adm.gwt.gui.client.service.privilege;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilege;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilegeSearchCommand;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async service for managing {@link Privilege} objects
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public interface PrivilegeServiceAsync {

    /**
     * Searches for {@link Privilege} objects based on the specified search
     * criteria.
     * 
     * @param config
     *            The {@link PagingLoadConfig} containing the paging
     *            information.
     * @param sc
     *            The search criteria.
     * @param callback
     *            The {@link AsyncCallback} that will be used to return the
     *            search results.
     */
    void findPrivileges(PagingLoadConfig config, ClientPrivilegeSearchCommand sc,
            AsyncCallback<PagingLoadResult<ClientPrivilege>> callback);

    /**
     * Retrieves a {@link ClientPrivilege} by its unique identifier.
     * 
     * @param id
     *            The unique identifier of the {@link Privilege}.
     * @param callback
     *            The {@link AsyncCallback} used to return the
     *            {@link ClientPrivilege}.
     */
    void findPrivilegeById(Long id, AsyncCallback<ClientPrivilege> callback);

    /**
     * Saves the specified {@link ClientPrivilege}.
     * 
     * @param privilege
     *            The {@link ClientPrivilege} that will be saved.
     * @param callback
     *            The {@link AsyncCallback} that will be used to return the
     *            saved {@link ClientPrivilege}.
     */
    void savePrivilege(ClientPrivilege privilege, AsyncCallback<ClientPrivilege> callback);
}
