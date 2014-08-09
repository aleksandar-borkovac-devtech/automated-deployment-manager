/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 23, 2012 File: fUserGroupManagerImpl.java
 * Package: nl.Tranquilized Quality.adm.core.business.manager.impl
 * 
 * Copyright (c) 2012 Tranquilized Quality www.Tranquilized Quality.nl All rights
 * reserved.
 * 
 * This software is the confidential and proprietary information of Tranquilized Quality
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with Tranquilized Quality.
 * </pre>
 */
package nl.tranquilizedquality.adm.security.business.manager.impl;

import java.util.List;
import java.util.Set;

import nl.tranquilizedquality.adm.commons.business.command.UserGroupSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.business.domain.UserRole;
import nl.tranquilizedquality.adm.security.business.exception.InvalidUserGroupException;
import nl.tranquilizedquality.adm.security.business.manager.UserGroupManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * Manager that manages user groups.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 23, 2012
 */
public class UserGroupManagerImpl extends UserGroupFilteringManager implements UserGroupManager {

	/** Logger for this class. */
	private static final Log LOGGER = LogFactory.getLog(UserGroupManagerImpl.class);

	/** Validator that validates user groups. */
	private Validator userGroupValidator;

	@Override
	public UserGroup storeUserGroup(final UserGroup userGroup, final Errors errors) {
		if (userGroup == null) {
			final String msg = "No user group specified!";
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(msg);
			}

			throw new InvalidUserGroupException(msg);
		}

		/*
		 * Validate the repository.
		 */
		userGroupValidator.validate(userGroup, errors);

		/*
		 * Check for errors.
		 */
		if (errors.hasErrors()) {
			final String msg = "Invalid user group.. " + userGroup.getName();

			if (LOGGER.isDebugEnabled()) {
				final List<ObjectError> allErrors = errors.getAllErrors();
				for (final ObjectError objectError : allErrors) {
					LOGGER.debug(objectError.getDefaultMessage());
				}
			}

			throw new InvalidUserGroupException(msg);
		}

		/*
		 * Check if we are doing an insert or an update.
		 */
		if (userGroup.isPersistent()) {
			/*
			 * Create supported domain object
			 */
			final UserGroup original = userGroupDao.findById(userGroup.getId());
			original.copy(userGroup);

			final UserGroup savedUserGroup = userGroupDao.save(original);
			initialize(savedUserGroup);

			return savedUserGroup;
		}
		else {
			final UserGroup newUserGroup = userGroupDao.newDomainObject();
			newUserGroup.copy(userGroup);

			final UserGroup savedUserGroup = userGroupDao.save(newUserGroup);
			initialize(savedUserGroup);

			return savedUserGroup;
		}
	}

	/**
	 * @param userGroup
	 */
	private void initialize(final UserGroup userGroup) {
		final List<User> users = userGroup.getUsers();
		for (final User user : users) {
			final Set<UserRole> userRoles = user.getUserRoles();
			for (final UserRole userRole : userRoles) {
				userRole.getId();
			}
		}
	}

	@Override
	public List<UserGroup> findUserGroupsBySearchCommand(final UserGroupSearchCommand sc) {
		final List<UserGroup> userGroups = userGroupDao.findUserGroupsBySearchCommand(sc);

		for (final UserGroup userGroup : userGroups) {
			initialize(userGroup);
		}

		return userGroups;
	}

	public List<UserGroup> findLoggedInUserUserGroups() {
		final User loggedInUser = securityContextManager.findLoggedInUser();
		final List<UserGroup> userGroups = userGroupDao.findUserGroupsByUser(loggedInUser);
		for (final UserGroup userGroup : userGroups) {
			initialize(userGroup);
		}

		return userGroups;
	}

	@Override
	public int findNumberOfUserGroups(final UserGroupSearchCommand sc) {
		return userGroupDao.findNumberUserGroups(sc);
	}

	@Override
	public UserGroup findUserGroupById(final Long id) {
		final UserGroup userGroup = userGroupDao.findById(id);
		initialize(userGroup);

		return userGroup;
	}

	@Required
	public void setUserGroupValidator(final Validator userGroupValidator) {
		this.userGroupValidator = userGroupValidator;
	}

}
