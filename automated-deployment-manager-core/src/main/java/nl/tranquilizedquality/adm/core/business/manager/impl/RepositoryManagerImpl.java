/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 30 aug. 2011 File: RepositoryManagerImpl.java
 * Package: nl.tranquilizedquality.adm.core.business.manager.impl
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
package nl.tranquilizedquality.adm.core.business.manager.impl;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.RepositorySearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.commons.business.manager.RepositoryManager;
import nl.tranquilizedquality.adm.core.business.manager.exception.InvalidRepositoryException;
import nl.tranquilizedquality.adm.core.persistence.dao.RepositoryDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * Manager that manages repositories.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 30 aug. 2011
 */
public class RepositoryManagerImpl implements RepositoryManager {

	/** Logger for this class. */
	private static final Log LOGGER = LogFactory.getLog(ReleaseHistoryManagerImpl.class);

	/** DAO that manages repositories. */
	private RepositoryDao<Repository> repositoryDao;

	/** Validator that validates a {@link Repository}. */
	private Validator repositoryValidator;

	@Override
	public Repository storeRepository(final Repository repository, final Errors errors) {

		if (repository == null) {
			final String msg = "No repository specified!";
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(msg);
			}

			throw new InvalidRepositoryException(msg);
		}

		/*
		 * Validate the repository.
		 */
		repositoryValidator.validate(repository, errors);

		/*
		 * Check for errors.
		 */
		if (errors.hasErrors()) {
			final String msg = "Invalid repository.. " + repository.getName();

			if (LOGGER.isDebugEnabled()) {
				final List<ObjectError> allErrors = errors.getAllErrors();
				for (final ObjectError objectError : allErrors) {
					LOGGER.debug(objectError.getDefaultMessage());
				}
			}

			throw new InvalidRepositoryException(msg);
		}

		/*
		 * Check if we are doing an insert or an update.
		 */
		if (repository.isPersistent()) {
			/*
			 * Create supported domain object
			 */
			final Repository original = repositoryDao.findById(repository.getId());
			original.copy(repository);

			return repositoryDao.save(original);
		}
		else {
			final Repository newRepository = repositoryDao.newDomainObject();
			newRepository.copy(repository);

			return repositoryDao.save(newRepository);
		}
	}

	@Override
	public int findNumberOfRepositories(final RepositorySearchCommand sc) {
		return repositoryDao.findNumberOfRepositories(sc);
	}

	@Override
	public List<Repository> findRepositories(final RepositorySearchCommand sc) {
		return repositoryDao.findBySearchCommand(sc);
	}

	@Override
	public Repository findRepositoryById(final Long id) {
		return repositoryDao.findById(id);
	}

	/**
	 * @param repositoryDao
	 *            the repositoryDao to set
	 */
	@Required
	public void setRepositoryDao(final RepositoryDao<Repository> repositoryDao) {
		this.repositoryDao = repositoryDao;
	}

	/**
	 * @param repositoryValidator
	 *            the repositoryValidator to set
	 */
	@Required
	public void setRepositoryValidator(final Validator repositoryValidator) {
		this.repositoryValidator = repositoryValidator;
	}

}
