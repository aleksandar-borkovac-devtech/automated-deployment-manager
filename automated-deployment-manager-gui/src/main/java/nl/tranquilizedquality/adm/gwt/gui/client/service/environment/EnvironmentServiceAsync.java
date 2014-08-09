/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 11 sep. 2011 File: EnvironmentServiceAsync.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.service.environment
 * 
 * Copyright (c) 2011 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
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
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous counterpart of the {@link EnvironmentyService}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 11 sep. 2011
 */
public interface EnvironmentServiceAsync {

	/**
	 * Retrieves a {@link ClientDestination} with the specified unique
	 * identifier.
	 * 
	 * @param id
	 *            The unique identifier of the destination.
	 * @param callback
	 *            Returns a {@link ClientDestination} or null if none could be
	 *            found.
	 */
	void findDestinationById(Long id, AsyncCallback<ClientDestination> callback);

	/**
	 * Saves the specified destination.
	 * 
	 * @param destination
	 *            The destination that will be saved.
	 * @param callback
	 *            Returns the saved {@link ClientDestination}.
	 */
	void saveDestination(ClientDestination destination, AsyncCallback<ClientDestination> callback);

	/**
	 * Searches for destinations based on the specified search criteria.
	 * 
	 * @param config
	 *            The paging configuration.
	 * @param sc
	 *            The search criteria.
	 * @param callback
	 *            Returns a {@link PagingLoadResult} containing the search
	 *            results.
	 */
	void findDestinations(PagingLoadConfig config, ClientDestinationSearchCommand sc,
			AsyncCallback<PagingLoadResult<ClientDestination>> callback);

	/**
	 * Retrieves a {@link ClientEnvironment} with the specified unique
	 * identifier.
	 * 
	 * @param id
	 *            The unique identifier of the environment.
	 * @param callback
	 *            Returns a {@link ClientEnvironment} or null if none could be
	 *            found.
	 */
	void findEnvironmentById(Long id, AsyncCallback<ClientEnvironment> callback);

	/**
	 * Retrieves all available environments.
	 * 
	 * @param callback
	 *            Returns a {@link List} containing {@link ClientEnvironment}
	 *            objects.
	 */
	void findEnvironments(AsyncCallback<List<ClientEnvironment>> callback);

	/**
	 * Retrieves all available environments as a paging result.
	 * 
	 * @param config
	 *            The paging configuration.
	 * @param callback
	 *            Returns the environments.
	 */
	void findAvailableEnvironments(PagingLoadConfig config,
			AsyncCallback<PagingLoadResult<ClientEnvironment>> callback);

	/**
	 * Saves the specified environment.
	 * 
	 * @param environment
	 *            The environment that will be saved.
	 * @param callback
	 *            Returns the saved {@link ClientEnvironment}.
	 */
	void saveEnvironment(ClientEnvironment environment, AsyncCallback<ClientEnvironment> callback);

	/**
	 * Deletes the specified {@link ClientDeployerParameter}.
	 * 
	 * @param location
	 *            The location that will be deleted.
	 * @param callback
	 *            Callback to return result.
	 */
	void deleteDestinationLocation(ClientDeployerParameter location, AsyncCallback<Void> callback);

	/**
	 * Retrieves a {@link DeployerParameter} with the specified identifier.
	 * 
	 * @param id
	 *            The unique identifier.
	 * @param callback
	 *            Returns a {@link ClientDeployerParameter} or null if none is
	 *            found.
	 */
	void findLocationById(Long id, AsyncCallback<ClientDeployerParameter> callback);

	/**
	 * Retrieves all available destinations.
	 * 
	 * @param callback
	 *            Returns a {@link List} of destinations or an empty one if none
	 *            could be found.
	 */
	void findAvailableDestinations(AsyncCallback<List<ClientDestination>> callback);

	/**
	 * Retrieves the {@link ClientDestinationHost} with the specified id.
	 * 
	 * @param id
	 *            The unique identifier of the host.
	 * @param callback
	 *            Returns a {@link ClientDestinationHost} or null if none could
	 *            be found.
	 */
	void findDestinationHostById(Long id, AsyncCallback<ClientDestinationHost> callback);

	/**
	 * Stores the specified host.
	 * 
	 * @param host
	 *            The host that will be saved.
	 * @param callback
	 *            Returns the stored {@link ClientDestinationHost}.
	 */
	void saveDestinationHost(ClientDestinationHost host,
			AsyncCallback<ClientDestinationHost> callback);

	/**
	 * Searches for hosts on the specified search criteria.
	 * 
	 * @param loadConfig
	 *            The paging configuration.
	 * @param sc
	 *            The search criteria.
	 * @param callback
	 *            Returns the {@link ClientDestinationHost} objects.
	 */
	void findDestinationHosts(PagingLoadConfig loadConfig, ClientDestinationHostSearchCommand sc,
			AsyncCallback<PagingLoadResult<ClientDestinationHost>> callback);

	/**
	 * Retrieves all available hosts.
	 * 
	 * @param hosts
	 *            The available hosts.
	 */
	void findDestinationHosts(AsyncCallback<List<ClientDestinationHost>> hosts);

	/**
	 * Searches for environments based on the search criteria passed in.
	 * 
	 * @param sc
	 *            The search criteria used for searching for environments.
	 * @return Returns a {@link List} containing {@link ClientEnvironment}
	 *         objects or an empty one if none could be found.
	 */
	void findEnvironments(PagingLoadConfig loadConfig, ClientEnvironmentSearchCommand sc,
			AsyncCallback<PagingLoadResult<ClientEnvironment>> callback);

}
