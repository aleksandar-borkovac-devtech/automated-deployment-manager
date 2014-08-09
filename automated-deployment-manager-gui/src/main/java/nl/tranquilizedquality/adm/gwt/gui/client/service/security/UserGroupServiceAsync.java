/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: Aug 24, 2012 File: fUserGroupServiceAsync.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.service.security
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
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async counter part of the {@link UserGroupService} that provides services to
 * manage user groups.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 24, 2012
 */
public interface UserGroupServiceAsync {

    /**
     * Searches for a user group with the specified unique identifier.
     * 
     * @param id
     *        The unique identifier of the user.
     * @param callback
     *        Returns a {@link ClientUserGroup} or null if none could be
     *        found.
     */
    void findUserGroupById(Long id, AsyncCallback<ClientUserGroup> callback);

    /**
     * Stores the specified user group.
     * 
     * @param userGroup
     *        The user group that will be stored.
     * @param callback
     *        Returns the saved user group.
     */
    void saveUserGroup(ClientUserGroup userGroup, AsyncCallback<ClientUserGroup> callback);

    /**
     * Searches for user groups based on the search criteria.
     * 
     * @param config
     *        The paging load configuration.
     * @param sc
     *        The search criteria that will be used.
     * @param callback
     *        Returns a {@link PagingLoadResult} containing the user groups
     *        or an empty one if none could be found.
     */
    void findUserGroups(PagingLoadConfig config, ClientUserGroupSearchCommand sc, AsyncCallback<PagingLoadResult<ClientUserGroup>> callback);

    /**
     * Retrieves all availalbe user groups.
     * 
     * @param callback
     *        Returns a {@link List} containing user groups.
     */
    void findUserGroups(AsyncCallback<List<ClientUserGroup>> callback);

    /**
     * Searches for users based on the passed in search criteria.
     * 
     * @param loadConfig
     *        The paging load configuration.
     * @param sc
     *        The search criteria that will be used.
     * @param callback
     *        Callback used to return the search results.
     */
    void findUsers(PagingLoadConfig loadConfig, ClientUserSearchCommand sc, AsyncCallback<PagingLoadResult<ClientUser>> callback);

}
