/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 7 okt. 2011 File: DeploymentServiceAsync.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.client.service.deployment
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
package nl.tranquilizedquality.adm.gwt.gui.client.service.deployment;

import java.util.List;

import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDeployer;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientProgress;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async counterpart of {@link DeploymentService}.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 7 okt. 2011
 */
public interface DeploymentServiceAsync {

    /**
     * Deploys the specified release to the specified environment.
     * 
     * @param release
     *        The release that will be deployed.
     * @param environment
     *        The environment where the release will be deployed to.
     * @return Returns the released release.
     */
    void deployRelease(ClientRelease release, ClientEnvironment environment, AsyncCallback<ClientRelease> callback);

    /**
     * Deploys the specified artifacts of the specified release to the specified
     * environment.
     * 
     * @param artifacts
     *        The artifacts that will be deployed.
     * @param release
     *        The release where the artifacts are part off.
     * @param environment
     *        The environment where the artifacts will be deployed to.
     * @return Returns the released release.
     */
    void deployArtifacts(List<ClientMavenArtifact> artifacts, ClientRelease release, ClientEnvironment environment,
            AsyncCallback<ClientRelease> callback);

    /**
     * @return
     */
    void getProgress(AsyncCallback<ClientProgress> callback);

    void findAvailableDeployers(AsyncCallback<List<ClientDeployer>> callback);

}
