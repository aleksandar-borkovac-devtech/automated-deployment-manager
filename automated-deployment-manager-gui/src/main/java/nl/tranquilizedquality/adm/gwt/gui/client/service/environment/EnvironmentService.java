package nl.tranquilizedquality.adm.gwt.gui.client.service.environment;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDeployerParameter;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestination;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestinationHost;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestinationHostSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDestinationSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironmentSearchCommand;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service that provides environment related services.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
@RemoteServiceRelativePath("EnvironmentService.rpc")
public interface EnvironmentService extends RemoteService {

    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util {

        /** The async service. */
        private static EnvironmentServiceAsync instance;

        /**
         * Retrieves an instance of the {@link EnvironmentServiceAsync}.
         * 
         * @return Returns a {@link EnvironmentServiceAsync}.
         */
        public static EnvironmentServiceAsync getInstance() {
            if (instance == null) {
                instance = GWT.create(EnvironmentService.class);
            }
            return instance;
        }
    }

    /**
     * Retrieves a {@link ClientDestination} with the specified unique
     * identifier.
     * 
     * @param id
     *            The unique identifier of the destination.
     * @return Returns a {@link ClientDestination} or null if none could be
     *         found.
     */
    ClientDestination findDestinationById(Long id);

    /**
     * Saves the specified destination.
     * 
     * @param destination
     *            The destination that will be saved.
     * @return Returns the saved {@link ClientDestination}.
     * @throws EnvironmentServiceException
     *             Is thrown when something goes wrong.
     */
    ClientDestination saveDestination(ClientDestination destination)
            throws EnvironmentServiceException;

    /**
     * Searches for destinations based on the specified search criteria.
     * 
     * @param config
     *            The paging configuration.
     * @param sc
     *            The search criteria.
     * @return Returns a {@link PagingLoadResult} containing the search results.
     */
    PagingLoadResult<ClientDestination> findDestinations(PagingLoadConfig config,
            ClientDestinationSearchCommand sc);

    /**
     * Retrieves a {@link ClientEnvironment} with the specified unique
     * identifier.
     * 
     * @param id
     *            The unique identifier of the environment.
     * @return Returns a {@link ClientEnvironment} or null if none could be
     *         found.
     */
    ClientEnvironment findEnvironmentById(Long id);

    /**
     * Retrieves all available environments.
     * 
     * @return Returns a {@link List} containing {@link ClientEnvironment}
     *         objects.
     */
    List<ClientEnvironment> findEnvironments();

    /**
     * Retrieves all available environments as a paging result.
     * 
     * @param config
     *            The paging configuration.
     * @return Returns the available environments.
     */
    PagingLoadResult<ClientEnvironment> findAvailableEnvironments(PagingLoadConfig config);

    /**
     * Saves the specified environment.
     * 
     * @param environment
     *            The environment that will be saved.
     * @return Returns the saved {@link ClientEnvironment}.
     * @throws EnvironmentServiceException
     *             Is thrown when something goes wrong.
     */
    ClientEnvironment saveEnvironment(ClientEnvironment environment)
            throws EnvironmentServiceException;

    /**
     * Deletes the specified {@link ClientDeployerParameter}.
     * 
     * @param location
     *            The location that will be deleted.
     */
    void deleteDestinationLocation(ClientDeployerParameter location);

    /**
     * Retrieves a {@link DeployerParameter} with the specified identifier.
     * 
     * @param id
     *            The unique identifier.
     * @return Returns a {@link ClientDeployerParameter} or null if none is
     *         found.
     */
    ClientDeployerParameter findLocationById(Long id);

    /**
     * Retrieves all available destinations.
     * 
     * @return Returns a {@link List} of destinations or an empty one if none
     *         could be found.
     */
    List<ClientDestination> findAvailableDestinations();

    /**
     * Retrieves the {@link ClientDestinationHost} with the specified id.
     * 
     * @param id
     *            The unique identifier of the host.
     * @param callback
     *            Returns a {@link ClientDestinationHost} or null if none could
     *            be found.
     */
    ClientDestinationHost findDestinationHostById(Long id);

    /**
     * Stores the specified host.
     * 
     * @param host
     *            The host that will be saved.
     * @return Returns the stored {@link ClientDestinationHost}.
     */
    ClientDestinationHost saveDestinationHost(ClientDestinationHost host);

    /**
     * Searches for hosts on the specified search criteria.
     * 
     * @param loadConfig
     *            The paging configuration.
     * @param sc
     *            The search criteria.
     * @return Returns the {@link ClientDestinationHost} objects.
     */
    PagingLoadResult<ClientDestinationHost> findDestinationHosts(PagingLoadConfig loadConfig,
            ClientDestinationHostSearchCommand sc);

    /**
     * Searches for available hosts.
     * 
     * @return Returns a list of hosts or an empty one if there are no hosts
     *         available.
     */
    List<ClientDestinationHost> findDestinationHosts();

    /**
     * Searches for environments based on the search criteria passed in.
     * 
     * @param sc
     *            The search criteria used for searching for environments.
     * @return Returns a {@link List} containing {@link ClientEnvironment}
     *         objects or an empty one if none could be found.
     */
    PagingLoadResult<ClientEnvironment> findEnvironments(PagingLoadConfig loadConfig,
            ClientEnvironmentSearchCommand sc);

}
