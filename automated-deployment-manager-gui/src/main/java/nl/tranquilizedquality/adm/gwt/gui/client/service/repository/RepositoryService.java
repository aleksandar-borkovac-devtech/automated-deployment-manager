package nl.tranquilizedquality.adm.gwt.gui.client.service.repository;

import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.gwt.gui.client.model.repository.ClientRepository;
import nl.tranquilizedquality.adm.gwt.gui.client.model.repository.ClientRepositorySearchCommand;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service that provides repository related services.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 aug. 2011
 */
@RemoteServiceRelativePath("RepositoryService.rpc")
public interface RepositoryService extends RemoteService {

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {

		/** The async service. */
		private static RepositoryServiceAsync instance;

		/**
		 * Retrieves an instance of the {@link RepositoryServiceAsync}.
		 * 
		 * @return Returns a {@link RepositoryServiceAsync}.
		 */
		public static RepositoryServiceAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(RepositoryService.class);
			}
			return instance;
		}
	}

	/**
	 * Searches for a {@link Repository} with the specified unique identifier.
	 * 
	 * @param id
	 *            The unique identifier to use.
	 * @return Returns a {@link ClientRepository} or null if none could be
	 *         found.
	 */
	ClientRepository findRepositoryById(Long id);

	/**
	 * Saves the specified repository.
	 * 
	 * @param repository
	 *            The repository that will be saved.
	 * @return Returns the saved {@link ClientRepository}.
	 * @throws RepositoryServiceException
	 *             Is thrown when something went wrong during storage.
	 */
	ClientRepository saveRepository(ClientRepository repository) throws RepositoryServiceException;

	/**
	 * Finds repositories based on the passed in search criteria.
	 * 
	 * @param config
	 *            The paging configuration.
	 * @param sc
	 *            The search criteria.
	 * @return Returns a {@link PagingLoadResult}.
	 */
	PagingLoadResult<ClientRepository> findRepositories(PagingLoadConfig config,
			ClientRepositorySearchCommand sc);

}
