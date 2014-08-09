/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 30 aug. 2011 File: RepositoryServiceImpl.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.server.service.repository
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
package nl.tranquilizedquality.adm.gwt.gui.server.service.repository;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.commons.business.manager.RepositoryManager;
import nl.tranquilizedquality.adm.commons.gwt.ext.server.service.AbstractService;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRepository;
import nl.tranquilizedquality.adm.gwt.gui.client.model.repository.ClientRepository;
import nl.tranquilizedquality.adm.gwt.gui.client.model.repository.ClientRepositorySearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.repository.RepositoryService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.repository.RepositoryServiceException;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

/**
 * Service that provides repository services.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 aug. 2011
 */
public class RepositoryServiceImpl extends AbstractService implements RepositoryService {

	/**
	 * Factory that transforms client side beans into persistent beans and vice
	 * versa.
	 */
	private static final RepositoryFactory REPOSITORY_FACTORY = new RepositoryFactory();

	/** Manager that manages repositories. */
	private RepositoryManager repositoryManager;

	@Override
	public ClientRepository findRepositoryById(final Long id) {
		final Repository repository = repositoryManager.findRepositoryById(id);

		final ClientRepository clientBean = REPOSITORY_FACTORY.createClientBean(repository);

		return clientBean;
	}

	@Override
	public ClientRepository saveRepository(final ClientRepository repository)
			throws RepositoryServiceException {
		final HibernateRepository hibernateRepository = REPOSITORY_FACTORY.createPersistentBean(repository);

		final Errors errors = new BindException(hibernateRepository, hibernateRepository.getClass().getName());

		try {
			final Repository storedOffice = repositoryManager.storeRepository(hibernateRepository, errors);

			final ClientRepository storedClientOffice = REPOSITORY_FACTORY.createClientBean(storedOffice);

			return storedClientOffice;
		}
		catch (final Exception e) {
			final List<String> errorList = createErrorList(errors);

			throw new RepositoryServiceException("Failed to save repository!", e, errorList);
		}
	}

	@Override
	public PagingLoadResult<ClientRepository> findRepositories(final PagingLoadConfig config,
			final ClientRepositorySearchCommand sc) {
		/*
		 * Setup search command.
		 */
		final SortDir sortDir = config.getSortDir();
		if (sortDir.equals(SortDir.ASC)) {
			sc.setAsc(true);
		}
		else {
			sc.setAsc(false);
		}

		final String sortField = config.getSortField();
		if (sortField != null) {
			sc.setOrderBy(sortField);
		}

		final int limit = config.getLimit();
		sc.setMaxResults(limit);

		final int offset = config.getOffset();
		sc.setStart(offset);

		/*
		 * Search for the offices.
		 */
		final List<Repository> offices = repositoryManager.findRepositories(sc);

		/*
		 * Create client beans.
		 */
		final List<ClientRepository> clientBeans = REPOSITORY_FACTORY.createClientBeans(offices);

		/*
		 * Retrieve the total count.
		 */
		final int count = repositoryManager.findNumberOfRepositories(sc);

		/*
		 * Return the results for a grid.
		 */
		return new BasePagingLoadResult<ClientRepository>(clientBeans, config.getOffset(), count);
	}

	/**
	 * @param repositoryManager
	 *            the repositoryManager to set
	 */
	@Required
	public void setRepositoryManager(final RepositoryManager repositoryManager) {
		this.repositoryManager = repositoryManager;
	}

}
