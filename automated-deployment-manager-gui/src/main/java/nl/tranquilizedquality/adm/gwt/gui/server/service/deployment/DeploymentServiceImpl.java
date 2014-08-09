/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 6 okt. 2011 File: DeploymentServiceImpl.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.server.service.deployment
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
package nl.tranquilizedquality.adm.gwt.gui.server.service.deployment;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Deployer;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.gwt.ext.server.service.AbstractService;
import nl.tranquilizedquality.adm.core.business.manager.DeployProgress;
import nl.tranquilizedquality.adm.core.business.manager.DeployerManager;
import nl.tranquilizedquality.adm.core.business.manager.DeploymentManager;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateEnvironment;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientDeployer;
import nl.tranquilizedquality.adm.gwt.gui.client.model.environment.ClientEnvironment;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientProgress;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.service.deployment.DeploymentService;
import nl.tranquilizedquality.adm.gwt.gui.server.service.environment.EnvironmentFacotry;
import nl.tranquilizedquality.adm.gwt.gui.server.service.release.MavenArtifactFactory;
import nl.tranquilizedquality.adm.gwt.gui.server.service.release.ReleaseFactory;

import org.springframework.beans.factory.annotation.Required;

/**
 * Service that provides services to deploy a release or parts of a release.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 6 okt. 2011
 */
public class DeploymentServiceImpl extends AbstractService implements DeploymentService {

    /**
     * Factory that transforms client side beans into persistent beans and vice
     * versa.
     */
    private final static MavenArtifactFactory ARTIFACT_FACTORY = new MavenArtifactFactory();

    /**
     * Factory that transforms client side beans into persistent beans and vice
     * versa.
     */
    private static final ReleaseFactory RELEASE_FACTORY = new ReleaseFactory();

    /**
     * Factory that transforms client side beans into persistent beans and vice
     * versa.
     */
    private static final EnvironmentFacotry ENVIRONMENT_FACTORY = new EnvironmentFacotry();

    private static final DeployerFactory DEPLOYER_FACTORY = new DeployerFactory();

    /** Manager that performs a deployment of a release. */
    private DeploymentManager deploymentManager;

    /** Manager used to retrieve the progress of the deployment of a release. */
    private DeployProgress deployProgress;

    private DeployerManager deployerManager;

    @Override
    public ClientRelease deployRelease(final ClientRelease release, final ClientEnvironment environment) {

        /*
         * Create persistent beans.
         */
        final HibernateRelease persistentReleaseBean = RELEASE_FACTORY.createPersistentBean(release);
        final HibernateEnvironment persistentEnvironmentBean = ENVIRONMENT_FACTORY.createPersistentBean(environment);

        /*
         * Deploy release.
         */
        final Release deployedRelease =
                deploymentManager.deployArtifacts(persistentReleaseBean.getArtifacts(), persistentReleaseBean, persistentEnvironmentBean);

        initializeRelease(deployedRelease);

        /*
         * Create client bean.
         */
        final ClientRelease clientBean = RELEASE_FACTORY.createClientBean(deployedRelease);

        return clientBean;
    }

    @Override
    public ClientRelease deployArtifacts(final List<ClientMavenArtifact> artifacts, final ClientRelease release,
            final ClientEnvironment environment) {
        /*
         * Create persistent beans.
         */
        final HibernateRelease persistentReleaseBean = RELEASE_FACTORY.createPersistentBean(release);
        final HibernateEnvironment persistentEnvironmentBean = ENVIRONMENT_FACTORY.createPersistentBean(environment);
        final List<HibernateMavenArtifact> persistentArtifactBeans = ARTIFACT_FACTORY.createPersistentBeans(artifacts);

        /*
         * Copy to compatible collection.
         */
        final List<MavenArtifact> mavenArtifacts = new ArrayList<MavenArtifact>(persistentArtifactBeans);

        /*
         * Deploy release.
         */
        final Release deployedRelease = deploymentManager.deployArtifacts(mavenArtifacts, persistentReleaseBean, persistentEnvironmentBean);

        initializeRelease(deployedRelease);

        /*
         * Create client bean.
         */
        final ClientRelease clientBean = RELEASE_FACTORY.createClientBean(deployedRelease);

        return clientBean;
    }

    /**
     * @param deployedRelease
     */
    private void initializeRelease(final Release deployedRelease) {
        final UserGroup userGroup = deployedRelease.getUserGroup();
        userGroup.setUsers(new ArrayList<User>());

        final List<MavenArtifact> deployedArtifacts = deployedRelease.getArtifacts();
        for (final MavenArtifact mavenArtifact : deployedArtifacts) {
            final MavenModule parentModule = mavenArtifact.getParentModule();
            parentModule.setDestinations(new ArrayList<Destination>());
        }
    }

    @Override
    public ClientProgress getProgress() {
        final Integer progress = deployProgress.getProgress();
        final String activityDescription = deployProgress.getActivityDescription();

        final ClientProgress clientProgress = new ClientProgress();
        clientProgress.setProgress(progress);
        clientProgress.setDescription(activityDescription);
        return clientProgress;
    }

    @Override
    public List<ClientDeployer> findAvailableDeployers() {
        final List<Deployer> availableDeployers = deployerManager.findAvailableDeployers();

        final List<ClientDeployer> clientBeans = DEPLOYER_FACTORY.createClientBeans(availableDeployers);

        return clientBeans;
    }

    /**
     * @param deploymentManager
     *        the deploymentManager to set
     */
    @Required
    public void setDeploymentManager(final DeploymentManager deploymentManager) {
        this.deploymentManager = deploymentManager;
    }

    public void setDeployProgress(final DeployProgress deployProgress) {
        this.deployProgress = deployProgress;
    }

    public void setDeployerManager(final DeployerManager deployerManager) {
        this.deployerManager = deployerManager;
    }

}
