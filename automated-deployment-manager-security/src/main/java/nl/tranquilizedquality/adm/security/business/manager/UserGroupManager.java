/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 23, 2012 File: fUserGroupManager.java
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
package nl.tranquilizedquality.adm.security.business.manager;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.UserGroupSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;

import org.springframework.validation.Errors;

/**
 * Manager that manages user groups.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 23, 2012
 */
public interface UserGroupManager {

    /**
     * Stores the specified user group.
     * 
     * @param userGroup
     *            The user group that will be stored.
     * @param errors
     *            {@link Errors} object that will be populated when something
     *            goes wrong during storage.
     * @return Returns the stored {@link UserGroup}.
     */
    UserGroup storeUserGroup(UserGroup userGroup, Errors errors);

    /**
     * Searches for user groups based on the specified search criteria.
     * 
     * @param sc
     *            The search criteria used to search for user groups.
     * @return Returns a {@link List} containing {@link UserGroup} objects or an
     *         empty one if none can be found.
     */
    List<UserGroup> findUserGroupsBySearchCommand(UserGroupSearchCommand sc);

    /**
     * Counts the number of user groups based on the specifies search criteria.
     * 
     * @param sc
     *            The search criteria used to count the number of user groups.
     * @return Returns an integer value
     */
    int findNumberOfUserGroups(UserGroupSearchCommand sc);

    /**
     * Retrieves the user group with the specified unique identifier.
     * 
     * @param id
     *            The unique identifier used to look for a user group.
     * @return Returns a {@link UserGroup} or null if none could be found.
     */
    UserGroup findUserGroupById(Long id);

    /**
     * Searches for all the user groups from the logged in user.
     * 
     * @return Returns a {@link List} containing user groups or an empty one if
     *         the user is not part of any user group.
     */
    List<UserGroup> findLoggedInUserUserGroups();

}
