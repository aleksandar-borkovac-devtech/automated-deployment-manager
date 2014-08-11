/**
 * <pre>
 * Project: automated-deployment-manager-core Created on: Aug 23, 2012 File: fUserGroupDao.java
 * Package: nl.Tranquilized Quality.adm.core.persistence.db.hibernate.dao
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
package nl.tranquilizedquality.adm.security.persistence.db.dao;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.command.UserGroupSearchCommand;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.hibernate.dao.BaseDao;

/**
 * DAO that manages {@link UserGroup} objects.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 23, 2012
 */
public interface UserGroupDao<T extends UserGroup> extends BaseDao<T, Long> {

    /**
     * Searches for {@link UserGroup} objects based on the passed in search
     * criteria.
     * 
     * @param sc
     *            The search criteria to search on.
     * @return Returns a {@link List} containing {@link UserGroup} objects or an
     *         empty one if none could be found.
     */
    List<UserGroup> findUserGroupsBySearchCommand(UserGroupSearchCommand sc);

    /**
     * Counts the number of user groups based on the passed in search criteria.
     * 
     * @param sc
     *            The search criteria to search on.
     * @return Returns an integer value of 0 or greater.
     */
    int findNumberUserGroups(UserGroupSearchCommand sc);

    /**
     * Searches for user groups that the user is part of.
     * 
     * @param user
     *            The user where the user groups will be looked up for.
     * @return Returns a {@link List} containing all the groups the user is part
     *         of or an empty one if the user is not part of a group.
     */
    List<UserGroup> findUserGroupsByUser(User user);

}
