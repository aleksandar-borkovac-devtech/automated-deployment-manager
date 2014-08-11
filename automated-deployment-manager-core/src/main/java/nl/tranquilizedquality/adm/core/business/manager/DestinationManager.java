package nl.tranquilizedquality.adm.core.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.DestinationHostSearchCommand;
import nl.tranquilizedquality.adm.commons.business.command.DestinationSearchCommand;
import nl.tranquilizedquality.adm.commons.business.command.EnvironmentSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.DestinationHost;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;

import org.springframework.validation.Errors;

/**
 * Manager that manages destinations.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public interface DestinationManager {

    /**
     * Searches for destinations based on the specified search criteria.
     * 
     * @param sc
     *            The search criteria that will be used.
     * @return Returns a {@link List} containing {@link Destination} objects or
     *         an empty one if none could be found.
     */
    List<Destination> findDestinations(DestinationSearchCommand sc);

    /**
     * Retrieves all available destinations.
     * 
     * @return Returns a {@link List} of {@link Destination} objects or an empty
     *         one if none are available.
     */
    List<Destination> findAvailableDestinations();

    /**
     * Counts the number of destinations based on the specified search criteria.
     * 
     * @param sc
     *            The search criteria that will be used.
     * @return Returns an integer value of 0 or greater.
     */
    int findNumberOfDestinations(DestinationSearchCommand sc);

    /**
     * Retrieves a list of all available environments.
     * 
     * @return Returns a {@link List} containing {@link Environment} objects or
     *         an empty one if none are available.
     */
    List<Environment> findAvailableEnvironments();

    /**
     * Stores a {@link Destination}.
     * 
     * @param destination
     *            The {@link Destination} that will be stored.
     * @param errors
     *            {@link Errors} object that will be populated when something
     *            goes wrong.
     * @return Returns the stored {@link Destination}.
     */
    Destination storeDestination(Destination destination, Errors errors);

    /**
     * Retrieves the destination with the specified id.
     * 
     * @param id
     *            The unique identifier of the {@link Destination}.
     * @return Returns a {@link Destination} or null if none could be found.
     */
    Destination findDestinationById(Long id);

    /**
     * Retrieves the environment with the specified id.
     * 
     * @param id
     *            The unique identifier of the {@link Environment}.
     * @return Returns a {@link Environment} or null if none could be found.
     */
    Environment findEnvironmentById(Long id);

    /**
     * Stores the specified environment.
     * 
     * @param environment
     *            The {@link Environment} that will be stored.
     * @param errors
     *            {@link Errors} object that will be populated when something
     *            goes wrong.
     * @return Returns the stored {@link Environment}.
     */
    Environment storeEnvironment(Environment environment, Errors errors);

    /**
     * Removes the specified location.
     * 
     * @param location
     *            The location that will be removed.
     */
    void removeDestinationLocation(DeployerParameter location);

    /**
     * Retrieves a {@link DeployerParameter} with the specified unique
     * identifier.
     * 
     * @param id
     *            The unique identifier to use.
     * @return Returns a {@link DeployerParameter} or null if none could be
     *         found.
     */
    DeployerParameter findDestinationLocationById(Long id);

    /**
     * Retrieves a {@link DestinationHost} with the specified unique identifier.
     * 
     * @param id
     *            The unique identifier to use.
     * @return Returns a {@link DestinationHost} or null if none could be found.
     */
    DestinationHost findDestinationHostById(Long id);

    /**
     * Stores a {@link DestinationHost}.
     * 
     * @param destinationHost
     *            The {@link DestinationHost} that will be stored.
     * @param errors
     *            {@link Errors} object that will be populated when something
     *            goes wrong.
     * @return Returns the stored {@link DestinationHost}.
     */
    DestinationHost storeDestinationHost(DestinationHost destinationHost, Errors errors);

    /**
     * Searches for destinations based on the specified search criteria.
     * 
     * @param sc
     *            The search criteria that will be used.
     * @return Returns a {@link List} containing {@link DestinationHost} objects
     *         or an empty one if none could be found.
     */
    List<DestinationHost> findDestinationHosts(DestinationHostSearchCommand sc);

    /**
     * Retrieves the number of hosts based on the specified search criteria.
     * 
     * @param sc
     *            The search criteria.
     * @return Returns an integer value of 0 or higher.
     */
    int findNumberOfDestinationHosts(DestinationHostSearchCommand sc);

    /**
     * Retrieves all available hosts.
     * 
     * @return Returns a {@link List} containing {@link DestinationHost} objects
     *         or an empty one if none could be found.
     */
    List<DestinationHost> findAvailableDestinationHosts();

    /**
     * Retrieves the environments that match the specified search criteria.
     * 
     * @param sc
     *            The search criteria that will be used.
     * @return Returns a {@link List} containing {@link Environment} objects or
     *         an empty one if none could be found.
     */
    List<Environment> findEnvironments(EnvironmentSearchCommand sc);

    /**
     * Retrieves all the environments the logged in user is allowed to deploy
     * to.
     * 
     * @return Returns a {@link List} containing environments or an empty one if
     *         there are none.
     */
    List<Environment> findDeployEnvironments();

}
