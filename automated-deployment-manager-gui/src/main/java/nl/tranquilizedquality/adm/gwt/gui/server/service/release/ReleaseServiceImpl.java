/**
 * <pre>
 * Project: automated-deployment-manager-gui Created on: 17 sep. 2011 File: ReleaseServiceImpl.java
 * Package: nl.Tranquilized Quality.adm.gwt.gui.server.service.release
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
package nl.tranquilizedquality.adm.gwt.gui.server.service.release;

import java.util.ArrayList;
import java.util.List;

import nl.tranquilizedquality.adm.commons.business.domain.Destination;
import nl.tranquilizedquality.adm.commons.business.domain.Environment;
import nl.tranquilizedquality.adm.commons.business.domain.MavenArtifact;
import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.business.domain.Release;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecution;
import nl.tranquilizedquality.adm.commons.business.domain.ReleaseExecutionLog;
import nl.tranquilizedquality.adm.commons.business.domain.User;
import nl.tranquilizedquality.adm.commons.business.domain.UserGroup;
import nl.tranquilizedquality.adm.commons.gwt.ext.server.service.AbstractService;
import nl.tranquilizedquality.adm.core.business.manager.ReleaseHistoryManager;
import nl.tranquilizedquality.adm.core.business.manager.ReleaseManager;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenArtifact;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientRelease;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecution;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecutionLog;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseExecutionSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.model.release.ClientReleaseSearchCommand;
import nl.tranquilizedquality.adm.gwt.gui.client.service.release.ReleaseService;
import nl.tranquilizedquality.adm.gwt.gui.client.service.release.ReleaseServiceException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

/**
 * Service that provides release releated services.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since 17 sep. 2011
 */
public class ReleaseServiceImpl extends AbstractService implements ReleaseService {

    /** Factory that is used to transform client beans to persistent beans etc. */
    private static final ReleaseFactory RELEASE_FACTORY = new ReleaseFactory();

    /** Factory that is used to transform client beans to persistent beans etc. */
    private static final ReleaseExecutionFactory RELEASE_EXECUTION_FACTORY = new ReleaseExecutionFactory();

    /** Factory that is used to transform client beans to persistent beans etc. */
    private static final ReleaseExecutionLogFactory RELEASE_EXECUTION_LOG_FACTORY = new ReleaseExecutionLogFactory();

    /** Manager that manages releases. */
    private ReleaseManager releaseManager;

    /** Manager used to retrieve the release history. */
    private ReleaseHistoryManager releaseHistoryManager;

    @Override
    public ClientRelease findReleaseById(final Long id) {
        final Release release = releaseManager.findReleaseById(id);

        final UserGroup userGroup = release.getUserGroup();
        initializeUserGroup(userGroup);
        final List<MavenArtifact> artifacts = release.getArtifacts();
        for (final MavenArtifact mavenArtifact : artifacts) {
            final MavenModule parentModule = mavenArtifact.getParentModule();
            parentModule.setDeploymentDependencies(new ArrayList<MavenModule>());
            parentModule.setDestinations(new ArrayList<Destination>());
        }

        final ClientRelease clientBean = RELEASE_FACTORY.createClientBean(release);

        return clientBean;
    }

    @Override
    public ClientRelease saveRelease(final ClientRelease release) throws ReleaseServiceException {
        final HibernateRelease hibernateRelease = RELEASE_FACTORY.createPersistentBean(release);

        final Errors errors = new BindException(hibernateRelease, hibernateRelease.getClass().getName());

        try {
            final Release storedRelease = releaseManager.storeRelease(hibernateRelease, errors);

            final UserGroup userGroup = storedRelease.getUserGroup();
            initializeUserGroup(userGroup);

            final ClientRelease clientRelease = RELEASE_FACTORY.createClientBean(storedRelease);

            return clientRelease;
        } catch (final Exception e) {
            final List<String> errorList = createErrorList(errors);

            throw new ReleaseServiceException("Failed to save release!", e, errorList);
        }
    }

    @Override
    public PagingLoadResult<ClientMavenArtifact> findReleasesAndArtifacts(final PagingLoadConfig config, final ClientReleaseSearchCommand sc) {
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
        } else {
            sc.setOrderBy("releaseDate");
        }

        final int limit = config.getLimit();
        sc.setMaxResults(limit);

        final int offset = config.getOffset();
        sc.setStart(offset);

        /*
         * Search for the offices.
         */
        final List<Release> releases = releaseManager.findReleases(sc);

        for (final Release release : releases) {
            final UserGroup userGroup = release.getUserGroup();
            userGroup.setUsers(new ArrayList<User>());

            final List<MavenArtifact> artifacts = release.getArtifacts();
            for (final MavenArtifact mavenArtifact : artifacts) {
                final MavenModule parentModule = mavenArtifact.getParentModule();
                parentModule.setDeploymentDependencies(new ArrayList<MavenModule>());
                parentModule.setDestinations(new ArrayList<Destination>());
            }
        }

        /*
         * Create client beans.
         */
        final List<ClientRelease> clientBeans = RELEASE_FACTORY.createClientBeans(releases);

        /*
         * Convert releases to maven artifact objects.
         */
        final List<ClientMavenArtifact> clientArtifacts = new ArrayList<ClientMavenArtifact>();
        for (final ClientRelease clientRelease : clientBeans) {
            final List<MavenArtifact> artifacts = clientRelease.getArtifacts();

            if (!artifacts.isEmpty()) {
                for (final MavenArtifact mavenArtifact : artifacts) {
                    clientArtifacts.add((ClientMavenArtifact) mavenArtifact);
                }
            } else {
                final ClientMavenArtifact newArtifact = new ClientMavenArtifact();
                newArtifact.setRelease(clientRelease);

                clientArtifacts.add(newArtifact);
            }
        }

        /*
         * Retrieve the total count.
         */
        final int count = releaseManager.findNumberOfReleases(sc);

        /*
         * Return the results for a grid.
         */
        return new BasePagingLoadResult<ClientMavenArtifact>(clientArtifacts, config.getOffset(), count);
    }

    @Override
    public PagingLoadResult<ClientReleaseExecution> findReleaseHistory(final PagingLoadConfig config,
            final ClientReleaseExecutionSearchCommand sc) {
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
        if (StringUtils.isNotBlank(sortField)) {
            sc.setOrderBy(sortField);
        } else {
            sc.setOrderBy("releaseDate");
        }

        final int limit = config.getLimit();
        sc.setMaxResults(limit);

        final int offset = config.getOffset();
        sc.setStart(offset);

        /*
         * Transform to persitent bean.
         */
        final Release release = sc.getRelease();

        if (release != null && release.isPersistent()) {

            final HibernateRelease persitentBean = RELEASE_FACTORY.createPersistentBean(release);
            sc.setRelease(persitentBean);

            /*
             * Retrieve the release history.
             */
            final List<ReleaseExecution> releaseHistory = releaseHistoryManager.findReleaseHistory(sc);

            for (final ReleaseExecution releaseExecution : releaseHistory) {
                final Release releaseExecutionRelease = releaseExecution.getRelease();
                final UserGroup userGroup = releaseExecutionRelease.getUserGroup();
                userGroup.setUsers(new ArrayList<User>());

                final Environment environment = releaseExecution.getEnvironment();
                environment.setUsers(new ArrayList<User>());
            }

            /*
             * Transform the results into client beans.
             */
            final List<ClientReleaseExecution> clientBeans = RELEASE_EXECUTION_FACTORY.createClientBeans(releaseHistory);

            /*
             * Count the number of total release executions.
             */
            final int count = releaseHistoryManager.findNumberOfReleaseExecutions(sc);

            return new BasePagingLoadResult<ClientReleaseExecution>(clientBeans, config.getOffset(), count);
        } else {
            /*
             * If there is no release specified we shouldn't return anything
             * otherwise we will return all release history of all the releases.
             */
            return new BasePagingLoadResult<ClientReleaseExecution>(new ArrayList<ClientReleaseExecution>(), config.getOffset(), 0);
        }
    }

    @Override
    public ClientReleaseExecution findReleaseExecutionById(final Long id) {

        final ReleaseExecution history = releaseHistoryManager.findReleaseHistoryById(id);

        final Release release = history.getRelease();
        final UserGroup userGroup = release.getUserGroup();
        initializeUserGroup(userGroup);

        final Environment environment = history.getEnvironment();
        environment.setUsers(new ArrayList<User>());

        final ClientReleaseExecution clientBean = RELEASE_EXECUTION_FACTORY.createClientBean(history);

        return clientBean;
    }

    @Override
    public void removeRelease(final ClientRelease release) throws ReleaseServiceException {
        final HibernateRelease persistentBean = RELEASE_FACTORY.createPersistentBean(release);

        try {
            releaseManager.removeRelease(persistentBean);
        } catch (final Exception e) {
            throw new ReleaseServiceException("Failed to remove release!", e);
        }
    }

    @Override
    public void archiveRelease(final ClientRelease release) throws ReleaseServiceException {
        final HibernateRelease persistentBean = RELEASE_FACTORY.createPersistentBean(release);
        persistentBean.setArchived(true);

        try {
            final Errors errors = new BindException(persistentBean, persistentBean.getClass().getName());
            releaseManager.storeRelease(persistentBean, errors);
        } catch (final Exception e) {
            throw new ReleaseServiceException("Failed to archive release!", e);
        }

    }

    @Override
    public void unArchiveRelease(final ClientRelease release) throws ReleaseServiceException {
        final HibernateRelease persistentBean = RELEASE_FACTORY.createPersistentBean(release);
        persistentBean.setArchived(false);

        try {
            final Errors errors = new BindException(persistentBean, persistentBean.getClass().getName());
            releaseManager.storeRelease(persistentBean, errors);
        } catch (final Exception e) {
            throw new ReleaseServiceException("Failed to unarchive release!", e);
        }
    }

    @Override
    public ClientReleaseExecutionLog findReleaseExecutionLogById(final Long id) {

        final ReleaseExecutionLog releaseExecutionLog = releaseHistoryManager.findReleaseExecutionLogById(id);

        final ReleaseExecution releaseExecution = releaseExecutionLog.getReleaseExecution();
        final Environment environment = releaseExecution.getEnvironment();
        environment.setUsers(new ArrayList<User>());
        final ClientReleaseExecutionLog clientBean = RELEASE_EXECUTION_LOG_FACTORY.createClientBean(releaseExecutionLog);

        return clientBean;
    }

    /**
     * Initializes the user group to set the users to be empty since in some
     * contexts this is not used so there is no need to pass it to the GUI. This
     * needs to be done on the persistent bean since otherwise there will be an
     * infinite loop trygin to copy the persisten bean values to a client bean.
     * 
     * @param userGroup
     *        The user group users list will be set to an empty array.
     */
    private void initializeUserGroup(final UserGroup userGroup) {
        userGroup.setUsers(new ArrayList<User>());
    }

    /**
     * @param releaseManager
     *        the releaseManager to set
     */
    @Required
    public void setReleaseManager(final ReleaseManager releaseManager) {
        this.releaseManager = releaseManager;
    }

    /**
     * @param releaseHistoryManager
     *        the releaseHistoryManager to set
     */
    @Required
    public void setReleaseHistoryManager(final ReleaseHistoryManager releaseHistoryManager) {
        this.releaseHistoryManager = releaseHistoryManager;
    }

}
