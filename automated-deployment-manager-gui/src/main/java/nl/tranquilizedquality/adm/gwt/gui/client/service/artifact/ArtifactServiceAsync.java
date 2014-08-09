/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 24 sep. 2011 File: ArtifactServiceAsync.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.service.artifact
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
package nl.tranquilizedquality.adm.gwt.gui.client.service.artifact;

import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifactSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModuleSearchCommand;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous counterpart of the {@link ArtifactService}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 sep. 2011
 */
public interface ArtifactServiceAsync {

    /**
     * Retrieves the {@link ClientMavenModule} with the specified id.
     * 
     * @param id
     *        The unique identifier.
     * @param callback
     *        Returns a {@link ClientMavenModule} or null if none could be
     *        found.
     */
    void findMavenModuleById(Long id, AsyncCallback<ClientMavenModule> callback);

    /**
     * Retrieves all available {@link MavenModule} objects.
     * 
     * @param callback
     *        Returns a {@link List} of {@link ClientMavenModule} objects.
     */
    void findMavenModules(AsyncCallback<List<ClientMavenModule>> callback);

    /**
     * Retrieves {@link MavenModule} objects based on the specified search
     * criteria.
     * 
     * @param config
     *        The paging configuration.
     * @param sc
     *        The search criteria.
     * @param callback
     *        Returns a {@link PagingLoadResult} containing the results.
     */
    void findMavenModules(PagingLoadConfig config, ClientMavenModuleSearchCommand sc,
            AsyncCallback<PagingLoadResult<ClientMavenModule>> callback);

    /**
     * Retrieves {@link MavenArtifact} objects based on the specified search
     * criteria.
     * 
     * @param config
     *        The paging configuration.
     * @param sc
     *        The search criteria.
     * @param callback
     *        Returns a {@link PagingLoadResult} containing the results.
     */
    void findMavenArtifacts(PagingLoadConfig config, ClientMavenArtifactSearchCommand sc,
            AsyncCallback<PagingLoadResult<ClientMavenArtifact>> callback);

    /**
     * Saves the specified artifact.
     * 
     * @param artifact
     *        The artifact that will be stored.
     * @param callback
     *        Returns the stored artifact.
     */
    void saveMavenArtifact(ClientMavenArtifact artifact, AsyncCallback<ClientMavenArtifact> callback);

    /**
     * Saves the specified artifact.
     * 
     * @param module
     *        The module that will be stored.
     * @param callback
     *        Returns the stored artifact.
     */
    void saveMavenModule(ClientMavenModule module, AsyncCallback<ClientMavenModule> callback);

    /**
     * Retrieves the artifact with the specified id.
     * 
     * @param id
     *        The unique identifier of the artifact.
     * @param callback
     *        Returns a {@link ClientMavenArtifact} or null if none could be
     *        found.
     */
    void findMavenArtifactById(Long id, AsyncCallback<ClientMavenArtifact> callback);

    /**
     * Deletes the specified artifact.
     * 
     * @param artifact
     *        The artifact that will be deleted.
     * @param callback
     *        Call back method.
     */
    void deleteMavenArtifact(ClientMavenArtifact artifact, AsyncCallback<Void> callback);

    /**
     * Searches for all available maven modules excluding the one that has the specified unique
     * identifier since you cannot have a dependency on yourself.
     * 
     * @param excludeMavenModuleId
     *        The unique identifier of the maven module that should not be included.
     * @param callback
     *        Callback that contains the {@link MavenModule} objects or an empty collection if
     *        there are no dependencies available.
     */
    void findAvailableMavenModules(Long excludeMavenModuleId, AsyncCallback<List<ClientMavenModule>> callback);

}
