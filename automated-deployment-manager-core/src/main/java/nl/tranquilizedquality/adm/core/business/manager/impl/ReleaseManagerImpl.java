/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: 17 sep. 2011 File: ReleaseManagerImpl.java
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

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.ReleaseSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseStatus;
import nl.tranquilizedquality.adm.core.business.manager.ReleaseManager;
import nl.tranquilizedquality.adm.core.business.manager.exception.InvalidReleaseException;
import nl.tranquilizedquality.adm.core.business.manager.exception.ReleaseAlreadyInUseException;
import nl.tranquilizedquality.adm.core.persistence.dao.ReleaseDao;
import nl.tranquilizedquality.adm.security.business.manager.impl.UserGroupFilteringManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * Manager that manages releases.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 */
public class ReleaseManagerImpl extends UserGroupFilteringManager implements ReleaseManager {

	/** Logger for this class. */
	private static final Log LOGGER = LogFactory.getLog(ReleaseManagerImpl.class);

	/** DAO that manages {@link Release} objects. */
	private ReleaseDao<Release> releaseDao;

	/** Validator that validates {@link Release} objects. */
	private Validator releaseValidator;

	@Override
	public Release storeRelease(final Release release, final Errors errors) {
		if (release == null) {
			final String msg = "No release specified!";
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(msg);
			}

			throw new InvalidReleaseException(msg);
		}

		/*
		 * Validate release.
		 */
		releaseValidator.validate(release, errors);

		/*
		 * Check for errors.
		 */
		if (errors.hasErrors()) {
			final String msg = "Invalid release [" + release.getName() + "]";

			if (LOGGER.isDebugEnabled()) {
				final List<ObjectError> allErrors = errors.getAllErrors();
				for (final ObjectError objectError : allErrors) {
					LOGGER.debug(objectError.getDefaultMessage());
				}
			}

			throw new InvalidReleaseException(msg);
		}

		/*
		 * Check if we are doing an insert or an update.
		 */
		if (release.isPersistent()) {
			/*
			 * Create supported domain object
			 */
			final Release original = releaseDao.findById(release.getId());
			original.copy(release);

			final Release savedRelease = releaseDao.save(original);
			return savedRelease;
		}
		else {
			final Release savedRelease = releaseDao.save(release);
			return savedRelease;
		}

	}

	@Override
	public void removeRelease(final Release release) {
		if (release == null) {
			final String msg = "No release specified!";
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(msg);
			}

			throw new InvalidReleaseException(msg);
		}

		final Long releaseId = release.getId();
		final Release foundRelease = releaseDao.findById(releaseId);
		final ReleaseStatus releaseStatus = foundRelease.getStatus();
		final Integer releaseCount = foundRelease.getReleaseCount();

		switch (releaseStatus) {
			case DRAFT:
				if (releaseCount > 0) {
					final String msg = "Release cannot be deleted since it's already in use.";
					if (LOGGER.isErrorEnabled()) {
						LOGGER.error(msg);
					}
					throw new ReleaseAlreadyInUseException(msg);
				}
				break;

			default:
				final String msg = "Release cannot be deleted since it's already in use.";
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error(msg);
				}
				throw new ReleaseAlreadyInUseException(msg);

		}

		releaseDao.delete(release);
	}

	@Override
	public List<Release> findReleases(final ReleaseSearchCommand sc) {
		addUserGroupsFromLoggedInUser(sc);

		final List<Release> releases = releaseDao.findBySearchCommand(sc);

		for (final Release release : releases) {
			final List<MavenArtifact> artifacts = release.getArtifacts();
			for (final MavenArtifact mavenArtifact : artifacts) {
				mavenArtifact.getId();

				final MavenModule parentModule = mavenArtifact.getParentModule();
				parentModule.setDestinations(new ArrayList<Destination>());
			}
		}

		return releases;
	}

	@Override
	public int findNumberOfReleases(final ReleaseSearchCommand sc) {
		addUserGroupsFromLoggedInUser(sc);
		return releaseDao.findNumberOfReleases(sc);
	}

	@Override
	public Release findReleaseById(final Long id) {
		final Release release = releaseDao.findById(id);

		final List<MavenArtifact> artifacts = release.getArtifacts();
		for (final MavenArtifact mavenArtifact : artifacts) {
			final MavenModule parentModule = mavenArtifact.getParentModule();
			parentModule.setDestinations(new ArrayList<Destination>());
		}

		return release;
	}

	/**
	 * @param releaseDao
	 *            the releaseDao to set
	 */
	@Required
	public void setReleaseDao(final ReleaseDao<Release> releaseDao) {
		this.releaseDao = releaseDao;
	}

	/**
	 * @param releaseValidator
	 *            the releaseValidator to set
	 */
	@Required
	public void setReleaseValidator(final Validator releaseValidator) {
		this.releaseValidator = releaseValidator;
	}

}
