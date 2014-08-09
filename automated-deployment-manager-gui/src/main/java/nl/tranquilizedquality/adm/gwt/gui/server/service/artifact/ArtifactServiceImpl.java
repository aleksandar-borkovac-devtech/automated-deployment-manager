/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 24 sep. 2011 File: ArtifactServiceImpl.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.server.service.artifact
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
package nl.tranquilizedquality.adm.gwt.gui.server.service.artifact;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Deployer;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameter;
import nl.tranquilizedquality.adm.commons.business.domain.DeployerParameterTemplate;
import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.gwt.ext.server.service.AbstractService;
import nl.tranquilizedquality.adm.core.business.manager.MavenArtifactManager;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenArtifact;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifactSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModuleSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.artifact.ArtifactService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.artifact.ArtifactServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

/**
 * Service that provides artifact related services.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 24 sep. 2011
 */
public class ArtifactServiceImpl extends AbstractService implements ArtifactService {

    /** Logger for this class. */
    private static final Log LOGGER = LogFactory.getLog(ArtifactServiceImpl.class);

    /**
     * Factory that transforms client side beans into persistent beans and vice
     * versa.
     */
    private final static MavenArtifactFactory ARTIFACT_FACTORY = new MavenArtifactFactory();

    /**
     * Factory that transforms client side beans into persistent beans and vice
     * versa.
     */
    private final static MavenModuleFactory MAVEN_MODULE_FACTORY = new MavenModuleFactory();

    /** Manager that manages artifacts. */
    private MavenArtifactManager artifactManager;

    @Override
    public ClientMavenModule findMavenModuleById(final Long id) {
        final MavenModule module = artifactManager.findMavenModuleById(id);

        final UserGroup userGroup = module.getUserGroup();
        userGroup.setUsers(new ArrayList<User>());

        final List<Destination> destinations = module.getDestinations();
        for (final Destination destination : destinations) {
            destination.setDeployerParameters(new ArrayList<DeployerParameter>());
            final Environment environment = destination.getEnvironment();
            environment.setUsers(new ArrayList<User>());

            final Deployer deployer = destination.getDeployer();
            deployer.setParameters(new ArrayList<DeployerParameterTemplate>());
        }

        final List<MavenModule> deploymentDependencies = module.getDeploymentDependencies();
        for (final MavenModule mavenModule : deploymentDependencies) {
            /*
             * We are not interested in the dependencies of our children so we set it as empty for
             * the GUI.
             */
            mavenModule.setDeploymentDependencies(new ArrayList<MavenModule>());
        }

        final ClientMavenModule clientBean = MAVEN_MODULE_FACTORY.createClientBean(module);

        return clientBean;
    }

    @Override
    public List<ClientMavenModule> findMavenModules() {
        final List<MavenModule> modules = artifactManager.findAllMavenModules();

        for (final MavenModule mavenModule : modules) {
            final UserGroup userGroup = mavenModule.getUserGroup();
            userGroup.setUsers(new ArrayList<User>());
            mavenModule.setDeploymentDependencies(new ArrayList<MavenModule>());
            mavenModule.setDestinations(new ArrayList<Destination>());
        }

        final List<ClientMavenModule> clientBeans = MAVEN_MODULE_FACTORY.createClientBeans(modules);

        return clientBeans;
    }

    @Override
    public List<ClientMavenModule> findAvailableMavenModules(final Long excludeMavenModuleId) {
        final List<MavenModule> modules = artifactManager.findAvailableDependencies(excludeMavenModuleId);

        for (final MavenModule mavenModule : modules) {
            final UserGroup userGroup = mavenModule.getUserGroup();
            userGroup.setUsers(new ArrayList<User>());
            mavenModule.setDeploymentDependencies(new ArrayList<MavenModule>());
            mavenModule.setDestinations(new ArrayList<Destination>());
        }

        final List<ClientMavenModule> clientBeans = MAVEN_MODULE_FACTORY.createClientBeans(modules);

        return clientBeans;
    }

    @Override
    public PagingLoadResult<ClientMavenModule> findMavenModules(final PagingLoadConfig config, final ClientMavenModuleSearchCommand sc) {
        /*
         * Setup search command.
         */
        final SortDir sortDir = config.getSortDir();
        if (sortDir.equals(SortDir.ASC)) {
            sc.setAsc(true);
        } else {
            sc.setAsc(false);
        }

        final String sortField = config.getSortField();
        if (sortField != null) {
            sc.setOrderBy(sortField);
        }

        final int limit = config.getLimit();
        sc.setMaxResults(limit);

        final int offset = config.getOffset();
        sc.setStart(offset);

        /*
         * Search for the maven modules.
         */
        final List<MavenModule> mavenModules = artifactManager.findMavenModules(sc);

        for (final MavenModule mavenModule : mavenModules) {
            final UserGroup userGroup = mavenModule.getUserGroup();
            userGroup.setUsers(new ArrayList<User>());

            mavenModule.setDestinations(new ArrayList<Destination>());
            mavenModule.setDeploymentDependencies(new ArrayList<MavenModule>());
        }

        /*
         * Create client beans.
         */
        final List<ClientMavenModule> clientBeans = MAVEN_MODULE_FACTORY.createClientBeans(mavenModules);

        /*
         * Retrieve the total count.
         */
        final int count = artifactManager.findNumberOfMavenModules(sc);

        /*
         * Return the results for a grid.
         */
        return new BasePagingLoadResult<ClientMavenModule>(clientBeans, config.getOffset(), count);
    }

    @Override
    public PagingLoadResult<ClientMavenArtifact> findMavenArtifacts(final PagingLoadConfig config, final ClientMavenArtifactSearchCommand sc) {
        /*
         * Setup search command.
         */
        final SortDir sortDir = config.getSortDir();
        if (sortDir.equals(SortDir.ASC)) {
            sc.setAsc(true);
        } else {
            sc.setAsc(false);
        }

        final String sortField = config.getSortField();
        if (sortField != null) {
            sc.setOrderBy(sortField);
        }

        final int limit = config.getLimit();
        sc.setMaxResults(limit);

        final int offset = config.getOffset();
        sc.setStart(offset);

        /*
         * Search for the destinations.
         */
        final List<MavenArtifact> mavenArtifacts = artifactManager.findArtifacts(sc);

        for (final MavenArtifact mavenArtifact : mavenArtifacts) {
            initializeMavenArtifact(mavenArtifact);
        }

        /*
         * Create client beans.
         */
        final List<ClientMavenArtifact> clientBeans = ARTIFACT_FACTORY.createClientBeans(mavenArtifacts);

        /*
         * Retrieve the total count.
         */
        final int count = artifactManager.findNumberOfArtifacts(sc);

        /*
         * Return the results for a grid.
         */
        return new BasePagingLoadResult<ClientMavenArtifact>(clientBeans, config.getOffset(), count);
    }

    @Override
    public ClientMavenArtifact saveMavenArtifact(final ClientMavenArtifact artifact) throws ArtifactServiceException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Creating persistent beans..");
        }

        final UserGroup userGroup = artifact.getUserGroup();
        userGroup.setUsers(new ArrayList<User>());

        final HibernateMavenArtifact persistentBean = ARTIFACT_FACTORY.createPersistentBean(artifact);

        final Errors errors = new BindException(persistentBean, persistentBean.getClass().getName());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Error bindings setup..");
        }

        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Storing artifact..");
            }

            final MavenArtifact storedArtifact = artifactManager.storeMavenArtifact(persistentBean, errors);

            initializeMavenArtifact(storedArtifact);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Artifact stored..");
            }

            final ClientMavenArtifact storedClientBean = ARTIFACT_FACTORY.createClientBean(storedArtifact);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Client beans created..");
            }

            return storedClientBean;
        } catch (final Exception e) {
            final List<String> errorList = createErrorList(errors);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Failed to store artifact!", e);
            }

            throw new ArtifactServiceException("Failed to save artifact!", e, errorList);
        }
    }

    @Override
    public void deleteMavenArtifact(final ClientMavenArtifact artifact) throws ArtifactServiceException {
        final HibernateMavenArtifact persistentBean = ARTIFACT_FACTORY.createPersistentBean(artifact);

        try {
            artifactManager.removeMavenArtifact(persistentBean);
        } catch (final Exception e) {
            throw new ArtifactServiceException("Failed to delete artifact!", e);
        }
    }

    @Override
    public ClientMavenModule saveMavenModule(final ClientMavenModule module) throws ArtifactServiceException {
        final HibernateMavenModule persistentBean = MAVEN_MODULE_FACTORY.createPersistentBean(module);

        final Errors errors = new BindException(persistentBean, persistentBean.getClass().getName());

        try {
            final MavenModule storedModule = artifactManager.storeMavenModule(persistentBean, errors);

            final ClientMavenModule storedClientBean = MAVEN_MODULE_FACTORY.createClientBean(storedModule);

            return storedClientBean;
        } catch (final Exception e) {
            final List<String> errorList = createErrorList(errors);

            throw new ArtifactServiceException("Failed to save module!", e, errorList);
        }
    }

    @Override
    public ClientMavenArtifact findMavenArtifactById(final Long id) {
        final MavenArtifact artifact = artifactManager.findArtifactById(id);

        initializeMavenArtifact(artifact);

        final ClientMavenArtifact clientBean = ARTIFACT_FACTORY.createClientBean(artifact);

        return clientBean;
    }

    /**
     * Initializes a {@link MavenArtifact} object so it doesn't contain data that isn't needed in
     * the GUI.
     * 
     * @param artifact
     *        The artifact that will be initialized.
     */
    private void initializeMavenArtifact(final MavenArtifact artifact) {
        final UserGroup userGroup = artifact.getUserGroup();
        userGroup.setUsers(new ArrayList<User>());

        final MavenModule parentModule = artifact.getParentModule();
        parentModule.setDeploymentDependencies(new ArrayList<MavenModule>());
        final List<Destination> destinations = parentModule.getDestinations();
        for (final Destination destination : destinations) {
            final Environment environment = destination.getEnvironment();
            environment.setUsers(new ArrayList<User>());

            final Deployer deployer = destination.getDeployer();
            deployer.setParameters(new ArrayList<DeployerParameterTemplate>());
            destination.setDeployerParameters(new ArrayList<DeployerParameter>());
        }
    }

    /**
     * @param artifactManager
     *        the artifactManager to set
     */
    @Required
    public void setArtifactManager(final MavenArtifactManager artifactManager) {
        this.artifactManager = artifactManager;
    }

}
