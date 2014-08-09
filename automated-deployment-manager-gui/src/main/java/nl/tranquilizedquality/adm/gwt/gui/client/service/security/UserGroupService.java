/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 24, 2012 File: fUserGroupService.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.server.service.security
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
package nl.tranquilizedquality.adm.gwt.gui.client.service.security;

import java.util.List;

import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUser;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroup;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserGroupSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.security.ClientUserSearchCommand;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service that provides services to manage user groups.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 24, 2012
 */
@RemoteServiceRelativePath("UserGroupService.rpc")
public interface UserGroupService extends RemoteService {

    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util {

        /** The async service. */
        private static UserGroupServiceAsync instance;

        /**
         * Retrieves an instance of the {@link UserGroupServiceAsync}.
         * 
         * @return Returns a {@link UserGroupServiceAsync}.
         */
        public static UserGroupServiceAsync getInstance() {
            if (instance == null) {
                instance = GWT.create(UserGroupService.class);
            }
            return instance;
        }
    }

    /**
     * Searches for a user group with the specified unique identifier.
     * 
     * @param id
     *        The unique identifier of the user.
     * @return Returns a {@link ClientUserGroup} or null if none could be found.
     */
    ClientUserGroup findUserGroupById(Long id);

    /**
     * Stores the specified user group.
     * 
     * @param userGroup
     *        The user group that will be stored.
     * @return Returns the saved user group.
     * @throws UserGroupServiceException
     *         Is thrown when something goes wrong during storage.
     */
    ClientUserGroup saveUserGroup(ClientUserGroup userGroup) throws UserGroupServiceException;

    /**
     * Searches for user groups based on the search criteria.
     * 
     * @param config
     *        The paging load configuration.
     * @param sc
     *        The search criteria that will be used.
     * @return Returns a {@link PagingLoadResult} containing the user groups or
     *         an empty one if none could be found.
     */
    PagingLoadResult<ClientUserGroup> findUserGroups(PagingLoadConfig config, ClientUserGroupSearchCommand sc);

    /**
     * Searches for users based on the passed in search criteria.
     * 
     * @param loadConfig
     *        The paging load configuration.
     * @param sc
     *        The search criteria that will be used.
     * @return Returns the search results.
     */
    PagingLoadResult<ClientUser> findUsers(PagingLoadConfig loadConfig, ClientUserSearchCommand sc);

    /**
     * Retrieves all availalbe user groups.
     * 
     * @return Returns a {@link List} containing user groups.
     */
    List<ClientUserGroup> findUserGroups();

}
