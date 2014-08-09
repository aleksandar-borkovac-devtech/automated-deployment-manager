/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 30, 2012 File: fAbstractUserGroupFilteringManager.java
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

import nl.tranquilizedquality.adm.commons.business.command.AbstractPagingUserGroupSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.security.business.manager.SecurityContextManager;
import nl.tranquilizedquality.adm.security.persistence.db.dao.UserGroupDao;

import org.springframework.beans.factory.annotation.Required;

/**
 * Base class for managers that need to filter their data based on user groups.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 30, 2012
 * 
 */
public class UserGroupFilteringManager {

	/** DAO that manages user groups. */
	protected UserGroupDao<UserGroup> userGroupDao;

	/** Manager used to retrieve the logged in user so data can be filtered out. */
	protected SecurityContextManager securityContextManager;

	/**
	 * @param sc
	 */
	protected void addUserGroupsFromLoggedInUser(final AbstractPagingUserGroupSearchCommand sc) {
		final User loggedInUser = securityContextManager.findLoggedInUser();
		final List<UserGroup> userGroups = userGroupDao.findUserGroupsByUser(loggedInUser);
		sc.setUserGroups(userGroups);
	}

	@Required
	public void setSecurityContextManager(final SecurityContextManager securityContextManager) {
		this.securityContextManager = securityContextManager;
	}

	@Required
	public void setUserGroupDao(final UserGroupDao<UserGroup> userGroupDao) {
		this.userGroupDao = userGroupDao;
	}

}
