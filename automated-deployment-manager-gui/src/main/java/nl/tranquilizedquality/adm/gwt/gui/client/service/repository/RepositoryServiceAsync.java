/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 30 aug. 2011 File: RepositoryServiceAsync.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.service.repository
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
package nl.tranquilizedquality.adm.gwt.gui.client.service.repository;

import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.gwt.gui.client.model.repository.ClientRepository;
import nl.tranquilizedquality.adm.gwt.gui.client.model.repository.ClientRepositorySearchCommand;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous counterpart of the {@link RepositoryService}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 aug. 2011
 */
public interface RepositoryServiceAsync {

	/**
	 * Searches for a {@link Repository} with the specified unique identifier.
	 * 
	 * @param id
	 *            The unique identifier to use.
	 * @param callback
	 *            Returns a {@link ClientRepository} or null if none could be
	 *            found.
	 */
	void findRepositoryById(Long id, AsyncCallback<ClientRepository> callback);

	/**
	 * Saves the specified repository.
	 * 
	 * @param repository
	 *            The repository that will be saved.
	 * @param callback
	 *            Returns the saved {@link ClientRepository}.
	 */
	void saveRepository(ClientRepository repository, AsyncCallback<ClientRepository> callback);

	/**
	 * Finds repositories based on the passed in search criteria.
	 * 
	 * @param config
	 *            The paging configuration.
	 * @param sc
	 *            The search criteria.
	 * @param callback
	 *            Returns a {@link PagingLoadResult}.
	 */
	void findRepositories(PagingLoadConfig config, ClientRepositorySearchCommand sc,
			AsyncCallback<PagingLoadResult<ClientRepository>> callback);

}
