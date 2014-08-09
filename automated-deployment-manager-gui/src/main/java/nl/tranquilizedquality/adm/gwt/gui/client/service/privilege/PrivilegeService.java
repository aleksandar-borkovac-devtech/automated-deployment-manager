package nl.tranquilizedquality.adm.gwt.gui.client.service.privilege;

import nl.tranquilizedquality.adm.commons.business.domain.Privilege;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilege;
import nl.tranquilizedquality.adm.gwt.gui.client.model.privilege.ClientPrivilegeSearchCommand;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service that manages {@link Privilege} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
@RemoteServiceRelativePath("PrivilegeService.rpc")
public interface PrivilegeService extends RemoteService {

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {

		private static PrivilegeServiceAsync instance;

		public static PrivilegeServiceAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(PrivilegeService.class);
			}
			return instance;
		}
	}

	/**
	 * Searches for {@link ClientPrivilege} objects.
	 * 
	 * @param config
	 *            The {@link PagingLoadConfig} that contains the paging
	 *            configuration.
	 * @param sc
	 *            The search criteria.
	 * @return Returns a {@link PagingLoadResult} containing the search result.
	 */
	PagingLoadResult<ClientPrivilege> findPrivileges(PagingLoadConfig config, ClientPrivilegeSearchCommand sc);

	/**
	 * Retrieves a {@link ClientPrivilege} with the specified unique identifier.
	 * 
	 * @param id
	 *            The unique identifier of the {@link ClientPrivilege}.
	 * @return Returns a {@link ClientPrivilege}.
	 */
	ClientPrivilege findPrivilegeById(Long id);

	/**
	 * Saves the passed in {@link ClientPrivilege}.
	 * 
	 * @param privilege
	 *            The {@link ClientPrivilege} that will be saved.
	 * @return Returns the saved {@link ClientPrivilege}.
	 * @throws PrivilegeServiceException
	 *             Is thrown when something goes wrong during privilege saving.
	 */
	ClientPrivilege savePrivilege(ClientPrivilege privilege) throws PrivilegeServiceException;

}
