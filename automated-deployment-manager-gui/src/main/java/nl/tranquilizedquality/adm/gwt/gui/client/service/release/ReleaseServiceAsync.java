/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 17 sep. 2011 File: ReleaseServiceAsync.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.service.release
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
package nl.tranquilizedquality.adm.gwt.gui.client.service.release;

import java.util.List;

import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecution;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecutionLog;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecutionSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseSearchCommand;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynch counter part of the {@link ReleaseService}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 */
public interface ReleaseServiceAsync {

    /**
     * Utility class for simplifying access to the instance of async service.
     */
    public static class Util {

        /** The async service. */
        private static ReleaseServiceAsync instance;

        /**
         * Retrieves an instance of the {@link ReleaseServiceAsync}.
         * 
         * @return Returns a {@link ReleaseServiceAsync}.
         */
        public static ReleaseServiceAsync getInstance() {
            if (instance == null) {
                instance = GWT.create(ReleaseService.class);
            }
            return instance;
        }
    }

    /**
     * Searches for a {@link ClientRelease} with the specified id.
     * 
     * @param id
     *            The unique identifier.
     * @param callback
     *            Returns {@link ClientRelease} or null if none could be found.
     */
    void findReleaseById(Long id, AsyncCallback<ClientRelease> callback);

    /**
     * Stores the specified release.
     * 
     * @param release
     *            The release that will be stored.
     * @param callback
     *            Returns the stored release.
     */
    void saveRelease(ClientRelease release, AsyncCallback<ClientRelease> callback);

    /**
     * Searches for releases based on the specified search criteria.
     * 
     * @param config
     *            The paging configuration.
     * @param sc
     *            The search criteria.
     * @param callback
     *            Returns a {@link PagingLoadResult} containing the results.
     */
    void findReleasesAndArtifacts(PagingLoadConfig config, ClientReleaseSearchCommand sc,
            AsyncCallback<PagingLoadResult<ClientMavenArtifact>> callback);

    /**
     * Finds the release history from the specified release.
     * 
     * @param config
     *            Paging configuration.
     * @param sc
     *            The search criteria to use for searching for
     *            {@link ClientReleaseExecution} objects.
     * @param callback
     *            Returns a {@link List} containing the release history or an
     *            empty one if there is no history.
     */
    void findReleaseHistory(PagingLoadConfig config, ClientReleaseExecutionSearchCommand sc,
            AsyncCallback<PagingLoadResult<ClientReleaseExecution>> callback);

    /**
     * Retrieves the {@link ClientReleaseExecution} with the specified unique
     * identifier.
     * 
     * @param id
     *            The unique identifier of the {@link ClientReleaseExecution}.
     * @param callback
     *            Callback function which returns the
     *            {@link ClientReleaseExecution}.
     */
    void findReleaseExecutionById(Long id, AsyncCallback<ClientReleaseExecution> callback);

    /**
     * Removes the specified release if it's not in use yet.
     * 
     * @param release
     *            The release that will be removed.
     * @param callback
     *            Callback function used to validate if removing went ok.
     */
    void removeRelease(ClientRelease release, AsyncCallback<Void> callback);

    /**
     * Retrieves the release execution log with the specified identifier.
     * 
     * @param id
     *            The unique identifier of the release execution log that needs
     *            to be retrieved.
     * @param callback
     *            Callback function which returns the
     *            {@link ClientReleaseExecutionLog}
     */
    void findReleaseExecutionLogById(Long id, AsyncCallback<ClientReleaseExecutionLog> callback);

    /**
     * Archives the specified release.
     * 
     * @param release
     *            The release that is archived.
     * @param callback
     *            Callback function used to validate if archiving went ok.
     */
    void archiveRelease(ClientRelease release, AsyncCallback<Void> callback);

    /**
     * Unarchives the specified release.
     * 
     * @param release
     *            The release that will be unarchived.
     * @param callback
     *            Callback function used to validate if archiving went ok.
     */
    void unArchiveRelease(ClientRelease release, AsyncCallback<Void> callback);

}
